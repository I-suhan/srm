package com.ne.srm.auth.entity;

import com.ne.srm.auth.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "sys_user_session")
public class UserSession {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private Long userId;

    private String sessionId;

    private String deviceId;

    private String deviceName;

    private String os;

    private String ipv4;

    private String ipv6;

    private String rawIp;

    /**
     * 浏览器信息
     */
    private String userAgent;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    private Instant createdTime;

    //会话死亡时间
    private Instant expiredTime;

    private Instant lastActiveTime;

    //最后刷新令牌时间
    private Instant refreshedTime;

    private Instant revokedTime;

    private String revokedReason;

    public boolean isValid() {
        return status == SessionStatus.ACTIVE
                && revokedTime == null;
    }

    public boolean isExpired(Instant time) {
        return expiredTime.isBefore(time);
    }
}
