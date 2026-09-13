package com.ne.srm.auth.service;

import com.ne.srm.auth.config.SecurityProperties;
import com.ne.srm.auth.entity.UserSession;
import com.ne.srm.auth.enums.RefreshTokenStatus;
import com.ne.srm.auth.dto.LoginRequest;
import com.ne.srm.auth.entity.RefreshToken;
import com.ne.srm.auth.entity.User;
import com.ne.srm.auth.repositoty.RefreshTokenRepository;
import com.ne.srm.core.utils.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.time.Instant;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;


    private final SecurityProperties securityProperties;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    /**
     * accessToken认证时调用
     */
    public boolean isValid(String tokenHash){
        if(tokenHash==null){
            throw new RuntimeException("无认证信息");
        }

        RefreshToken refreshToken = repository.findByTokenHash(tokenHash).orElse(null);
        return isValid(refreshToken);
    }

    public boolean isValid(RefreshToken refreshToken){
        if(refreshToken == null) {
            throw new RuntimeException("非法认证");
        }

        if (!refreshToken.isUsable()) {
            throw new RuntimeException("认证无效");
        }

        if(refreshToken.getExpiredTime().isBefore(Instant.now())){
            throw new RuntimeException("认证过期");
        }

        return true;
    }

    /**
     * 创建RefreshToken
     */
    public RefreshToken create(Long userId, Instant time, String refreshToken, UserSession session){
        String hash = CryptoUtil.sha256(refreshToken);
        RefreshToken token = new RefreshToken();
        token.setTokenHash(hash);
        token.setSessionId(session.getSessionId());
        token.setUserId(userId);
        token.setStatus(RefreshTokenStatus.ACTIVE);
        token.setExpiredTime(time.plus(Duration.ofMillis(securityProperties.getJwt().getRefreshExpire())));
        token.setCreatedTime(time);

        return repository.save(token);
    }

    public RefreshToken refresh(String oldHash, String newToken, Instant now){
        RefreshToken oldToken = repository.findByTokenHash(oldHash).orElse(null);
        if(oldToken == null || !isValid(oldToken)){
            throw new RuntimeException("异常认证");
        }
        oldToken.setStatus(RefreshTokenStatus.ROTATED);
        repository.save(oldToken);

        //新token
        String newHash = CryptoUtil.sha256(newToken);
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setTokenHash(newHash);
        newRefreshToken.setUserId(oldToken.getUserId());
        newRefreshToken.setStatus(RefreshTokenStatus.ACTIVE);
        newRefreshToken.setExpiredTime(now.plus(Duration.ofMillis(securityProperties.getJwt().getRefreshExpire())));
        newRefreshToken.setCreatedTime(now);
        return repository.save(newRefreshToken);
    }

    public void revoke(String sessionId, Instant now) {
        repository.revokeBySessionId(sessionId, now);
    }

    public RefreshToken findByHash(String refreshTokenHash) {
        return repository.findByTokenHash(refreshTokenHash).orElseThrow(()->new RuntimeException("找不到权限令牌，服务器错误"));
    }

    public void revoke(List<String> sessions, Instant now) {
        repository.revokeBySessionIds(sessions, now);
    }

    public void update(RefreshToken refreshToken, Instant now) {

    }
}