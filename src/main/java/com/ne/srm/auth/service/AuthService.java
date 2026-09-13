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
                .refreshToken(refreshTokenStr)
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

    public LoginResponse refresh(String refreshTokenStr, HttpServletRequest request){
        // 锚定业务时间
        Instant now = Instant.now();

        String oldHash = CryptoUtil.sha256(refreshTokenStr);

        RefreshToken refreshToken = refreshTokenService.findByHash(oldHash);

        if(refreshToken == null){
            throw new RuntimeException("refresh_token 数据异常");
        }

        if(!refreshToken.isUsable()){
            refreshToken.setStatus(RefreshTokenStatus.COMPROMISED);
            refreshTokenService.update(refreshToken, now);
            log.warn("refreshToken : {}", refreshTokenStr);
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
        refreshTokenStr = UuidCreator.getRandomBased().toString();
        refreshToken = refreshTokenService.refresh(oldHash, refreshTokenStr, now);


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

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshTokenStr)
                .tokenType(securityProperties.getTokenMode())
                .expiresIn(securityProperties.getJwt().getAccessExpire())
                .refreshExpiresIn(securityProperties.getJwt().getRefreshExpire())
                .userInfo(userInfo)
                .permissions(permissions).build();
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
}
