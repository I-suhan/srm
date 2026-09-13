package com.ne.srm.auth.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ne.srm.auth.config.SecurityProperties;
import com.ne.srm.auth.dto.ClientIpInfo;
import com.ne.srm.auth.dto.DeviceResponse;
import com.ne.srm.auth.dto.LoginRequest;
import com.ne.srm.auth.entity.RefreshToken;
import com.ne.srm.auth.entity.SecurityUser;
import com.ne.srm.auth.entity.User;
import com.ne.srm.auth.entity.UserSession;
import com.ne.srm.auth.enums.SessionStatus;
import com.ne.srm.auth.repositoty.UserSessionRepository;
import com.ne.srm.core.utils.CryptoUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSessionsService {

    private final UserSessionRepository repository;

    private final RefreshTokenService refreshTokenService;

    private final SecurityProperties securityProperties;

    public UserSession create(Long userId, ClientIpInfo ipInfo, LoginRequest loginRequest, Instant now, HttpServletRequest servletRequest) {
        UserSession session = new UserSession();

        session.setUserId(userId);
        session.setSessionId(UuidCreator.getTimeOrderedEpoch(now).toString());
        session.setDeviceId(loginRequest.deviceId());
        session.setDeviceName(loginRequest.deviceName());
        session.setOs(loginRequest.os());
        session.setIpv4(ipInfo.getIpv4());
        session.setIpv6(ipInfo.getIpv6());
        session.setRawIp(ipInfo.getRaw());
        session.setUserAgent(servletRequest.getHeader("User-Agent"));
        session.setStatus(SessionStatus.ACTIVE);
        session.setCreatedTime(now);
        session.setExpiredTime(now.minusMillis(securityProperties.getJwt().getSessionExpire()));
        session.setLastActiveTime(now);
        session.setRefreshedTime(now);

        return repository.save(session);
    }

    public List<UserSession> revoke(Long userId, Instant now, String reason) {
        List<UserSession> activeSessions = repository.findActiveSessionsByUserId(userId, now);
        for (UserSession activeSession : activeSessions) {
            activeSession.setStatus(SessionStatus.REVOKED);
            activeSession.setRevokedTime(now);
            activeSession.setRevokedReason(reason);
        }
        return repository.saveAll(activeSessions);
    }

    public UserSession findBySessionId(String sessionId) {
        return repository.findBySessionId(sessionId).orElse(null);
    }

    public UserSession revoke(String sid, Instant now, String reason) {
        UserSession userSession = repository.findActiveBySessionId(sid, now).orElse(null);
        if(userSession == null) {
            throw new RuntimeException("无法查询到当前会话");
        }
        userSession.setStatus(SessionStatus.REVOKED);
        userSession.setRevokedTime(now);
        userSession.setRevokedReason(reason);
        return repository.save(userSession);
    }

    public UserSession update(UserSession session) {
        return repository.save(session);
    }

    public List<DeviceResponse> devices() {
        SecurityUser user = (SecurityUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();


        List<UserSession> tokenList = repository.findActiveSessionsByUserId(user.getId(), Instant.now());

        return tokenList.stream()
                .map(s -> new DeviceResponse(
                        s.getId(),
                        s.getDeviceId(),
                        s.getDeviceName(),
                        s.getOs(),
                        s.getIpv4(),
                        s.getIpv6(),
                        s.getUserAgent(),
                        s.getLastActiveTime()))
                .toList();
    }

    public void refresh(UserSession oldSession) {
        oldSession.setStatus(SessionStatus.EXPIRED);
        repository.save(oldSession);
    }
}
