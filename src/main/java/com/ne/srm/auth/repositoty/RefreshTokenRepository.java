package com.ne.srm.auth.repositoty;

import com.ne.srm.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {


    @Modifying(clearAutomatically = true)
    @Query("""
        update RefreshToken r
        set r.status='REVOKED'
        where r.userId=:userId
    """)
    void revokeByUserId(Long userId);

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying(clearAutomatically = true)
    @Query("""
        update RefreshToken rt
        set rt.status='REVOKED' ,
        rt.revokedTime=:now
        where rt.sessionId=:sessionId and rt.status='ACTIVE'
    """)
    void revokeBySessionId(String sessionId, Instant now);

    @Modifying(clearAutomatically = true)
    @Query("""
        update RefreshToken r
        set r.status='REVOKED'
        where r.sessionId in :sessions
        and r.status='ACTIVE'
        and r.expiredTime > :time
    """)
    void revokeBySessionIds(@Param("tokens") List<String> sessions, Instant time);
}
