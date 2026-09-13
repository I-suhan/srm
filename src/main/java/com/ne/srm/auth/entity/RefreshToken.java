package com.ne.srm.auth.entity;

import com.ne.srm.auth.enums.RefreshTokenStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name="sys_refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String tokenHash;

    private String sessionId;

    /**
     * 所属用户
     */
    private Long userId;

    @Enumerated(EnumType.STRING)
    private RefreshTokenStatus status;

    /**
     * 过期时间
     */
    private Instant expiredTime;

    private Instant createdTime;

    private Instant revokedTime;

    public boolean isUsable(){
        return status.isUsable();
    }


}
