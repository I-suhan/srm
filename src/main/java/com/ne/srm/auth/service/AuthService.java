package com.ne.srm.auth.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ne.srm.auth.config.SecurityProperties;
import com.ne.srm.auth.config.resolver.TokenResolver;
import com.ne.srm.auth.dto.*;
import com.ne.srm.auth.entity.*;
import com.ne.srm.auth.enums.RefreshTokenStatus;
import com.ne.srm.auth.enums.SessionStatus;
import com.ne.srm.auth.repositoty.RoleRepository;
import com.ne.srm.auth.repositoty.UserRepository;
import com.ne.srm.auth.repositoty.UserRoleRepository;
import com.ne.srm.auth.util.IpUtils;
import com.ne.srm.auth.vo.UserInfoVO;
import com.ne.srm.auth.exception.AuthException;
import com.ne.srm.core.dto.RedisLockToken;
import com.ne.srm.core.service.RedisService;
import com.ne.srm.core.utils.CryptoUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    private final PasswordEncoder passwordEncoder;
    private final TokenResolver tokenResolver;
    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionService permissionService;
    private final AuthenticationManager authenticationManager;
    private final UserSessionsService userSessionsService;
    private final SecurityProperties securityProperties;
    private final UserService userService;
    private final RedisService redisService;

    @Transactional
    public UserInfoResponse me(){

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        return new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream().map(Role::getName).toList(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());

    }

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request, HttpServletRequest servletRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityUser user =  (SecurityUser) authentication.getPrincipal();

        ClientIpInfo ipInfo = IpUtils.resolve(servletRequest);

        //todo 记录历史ip

        String refreshTokenStr = UUID.randomUUID().toString();

        Instant now = Instant.now();
        UserSession userSession = userSessionsService.create(user.getId(),  ipInfo, request, now, servletRequest);

        RefreshToken refreshToken = refreshTokenService.create(user.getId(), now, refreshTokenStr, userSession);

        /* 生成access_token*/
        String accessTokenStr = jwtService.createAccessToken(
                user.getId(),
                jwtService.generateJti(now),
                userSession.getSessionId(),
                user.getTokenVersion(),
                now
                );

        redisService.set("user:token:version:" + user.getId(), user.getTokenVersion().toString(),
                Duration.ofMillis(securityProperties.getJwt().getRefreshExpire()).plusDays(1));
        redisService.set("session:alive:" +userSession.getSessionId(), "1");
        redisService.setObject("user:check:" + user.getId(), user);

        return LoginResponse.builder()
                .accessToken(accessTokenStr)
                .refreshToken(refreshToken.getTokenHash())
                .tokenType(securityProperties.getTokenMode())
                .expiresIn(securityProperties.getJwt().getAccessExpire())
                .refreshExpiresIn(securityProperties.getJwt().getRefreshExpire())
                .userInfo(user.convertVO())
                .permissions(permissionService.getPermissions(user)).build();
    }

    public void logout(HttpServletRequest request){
        String accessToken = tokenResolver.resolve(request);
        String sid = jwtService.getSessionId(accessToken);
        Instant now = Instant.now();
        UserSession session = userSessionsService.revoke(sid, now, "LOGOUT");
        refreshTokenService.revoke(session.getSessionId(), now);
        // redis-session黑名单
        redisService.delete(sid);

    }

    public void register(RegisterRequest request){
        if(userRepository.existsByUsername(request.username())){
            throw new AuthException(400, "用户名已存在");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setNickname(request.username());
        user.setStatus(1);
        user.setDisabled(false);
        Instant now = Instant.now();
        user.setCreatedTime(now);
        user.setUpdatedTime(now);

        userRepository.save(user);

        //绑定默认角色USER

        Role role = roleRepository.findByCode("USER").orElseThrow();

        UserRole userRole = new UserRole();

        UserRoleId userRoleId = new UserRoleId();
        userRoleId.setUserId(user.getId());
        userRoleId.setRoleId(role.getId());

        userRole.setId(userRoleId);

        userRoleRepository.save(userRole);

    }

    public void updateProfile(ProfileRequest request){
        SecurityUser current = (SecurityUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findById(current.getId()).orElseThrow();

        if( user.getUsername().equals(request.username()) ){
            return;
        }
        if( userRepository.existsByUsername(request.username()) ){
            throw new AuthException(400, "用户名已存在");
        }
        user.setUsername(request.username());
        user.setUpdatedTime(Instant.now());
        userRepository.save(user);
    }

    public LoginResponse refresh(String oldRefreshTokenStr){

        String oldHash = CryptoUtil.sha256(oldRefreshTokenStr);

        String lockKey = "refresh:lock:" + oldHash;
        String rotationKey = "refresh:rotation:" + oldHash;

        //防抖
        LoginResponse cached = redisService.getObject(rotationKey, LoginResponse.class);
        if (cached != null) {
            return cached;
        }

        //抢锁
        RedisLockToken locked = redisService.tryLock(
                lockKey,
                Duration.ofSeconds(5)
        );

        if (locked == null) {

            LoginResponse rotationResult = waitForRotationResult(
                    rotationKey,
                    Duration.ofMillis(500)
            );

            if (rotationResult != null) {
                return rotationResult;
            }
            throw new RuntimeException("刷新繁忙，请稍后重试");
        }

        try {
            /*
             * ④ 再检查一次 Grace Window
             *
             * 防止：
             *
             * A 抢锁
             * B 没抢到锁
             * A 刚好完成 rotation
             * B 此时才进来
             */
            LoginResponse cachedAgain =
                    redisService.getObject(
                            rotationKey,
                            LoginResponse.class
                    );

            if (cachedAgain != null) {
                return cachedAgain;
            }

            RefreshToken refreshToken = refreshTokenService.findByHash(oldHash);

            if(refreshToken == null){
                throw new RuntimeException("refresh_token 数据异常");
            }

            // 锚定业务时间
            Instant now = Instant.now();

            if(!refreshToken.isUsable()){
                refreshToken.setStatus(RefreshTokenStatus.COMPROMISED);
                refreshTokenService.update(refreshToken, now);
                log.warn("refreshToken : {}", oldRefreshTokenStr);
                log.warn("refresh token replay detected, sessionId={}", refreshToken.getSessionId());
                throw new RuntimeException("重放攻击，令牌泄露！");
            }

            UserSession session = userSessionsService.findBySessionId(refreshToken.getSessionId());
            if(session == null || !session.isValid()){
                throw new RuntimeException("会话已失效");
            }
            if(session.isExpired(now)
                    || now.isAfter(session.getLastActiveTime().plusMillis(securityProperties.getJwt().getSessionTimeout()))){
                session.setStatus(SessionStatus.EXPIRED);
                userSessionsService.update(session);
                throw new RuntimeException("登录已过期");
            }

            User user = userService.findById(session.getUserId());
            if(user.getStatus() != 1) {
                throw new RuntimeException("用户已被冻结");
            }

            //刷新会话
            session.setRefreshedTime(now);
            session.setLastActiveTime(now);
            session = userSessionsService.update(session);

            //更新refresh_token
            String newRefreshTokenStr = UuidCreator.getRandomBased().toString();
            refreshToken = refreshTokenService.refresh(oldHash, newRefreshTokenStr, now);


            /* 生成新的 access_token*/
            String newAccessToken = jwtService.createAccessToken(
                    user.getId(),
                    jwtService.generateJti(now),
                    session.getSessionId(),
                    user.getTokenVersion(),
                    now
            );

            UserInfoVO userInfo = user.convertVO();
            Collection<String> permissions = permissionService.getPermissions(user);

            LoginResponse build = LoginResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshTokenStr)
                    .tokenType(securityProperties.getTokenMode())
                    .expiresIn(securityProperties.getJwt().getAccessExpire())
                    .refreshExpiresIn(securityProperties.getJwt().getRefreshExpire())
                    .userInfo(userInfo)
                    .permissions(permissions).build();
            redisService.setObject(rotationKey, build, Duration.ofSeconds(2));
            return build;

        }finally {
            redisService.unlock(locked);
        }
    }

    public void changePassword(PasswordRequest request){
        log.info("changePwd - old pwd :  {}", request.oldPassword());
        log.info("changePwd - new pwd :  {}", request.newPassword());
        SecurityUser current = (SecurityUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findById(current.getId()).orElseThrow(()->new AuthException(401, "用户不存在"));

        // 校验旧密码
        if(!passwordEncoder.matches(request.oldPassword(), user.getPassword())){
            throw new AuthException(401, "旧密码错误");
        }

        Instant now = Instant.now();
        // 更新密码
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setTokenVersion(user.getTokenVersion() + 1);
        user.setUpdatedTime(now);
        userRepository.save(user);

        List<UserSession> sessions = userSessionsService.revoke(user.getId(), now, "CHANGE PASSWORD");
        List<String> refreshTokens = sessions.stream().map(UserSession::getSessionId).distinct().toList();
        refreshTokenService.revoke(refreshTokens, now);

        redisService.set("user:token:version:" + user.getId(),
                user.getTokenVersion().toString(),
                Duration.ofMillis(securityProperties.getJwt().getRefreshExpire()).plusDays(1));

    }

    private LoginResponse waitForRotationResult(
            String rotationKey,
            Duration timeout
    ) {

        long deadline =
                System.currentTimeMillis()
                        + timeout.toMillis();

        while (System.currentTimeMillis() < deadline) {

            LoginResponse result =
                    redisService.getObject(
                            rotationKey,
                            LoginResponse.class
                    );

            if (result != null) {
                return result;
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        return null;
    }
}
