package com.ne.srm.auth.repositoty;

import com.ne.srm.auth.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    @Query("""
        select us from UserSession us
        where
        us.userId =:userId
        and us.expiredTime >:time
        and us.status ='ACTIVE'
    """)
    List<UserSession> findActiveSessionsByUserId(@Param("userId") Long userId, @Param("time") Instant time);

    @Query("""
        select us from UserSession us
        where us.sessionId =:sessionId
        and us.status ='ACTIVE'
    """)
    Optional<UserSession> findBySessionId(@Param("sessionId") String sessionId);

    @Modifying
    @Query("""
        update UserSession us
        set us.revokedTime = :now,
            us.revokedReason = :reason
        where us.userId = :userId
        and us.expiredTime > :now
        and us.status = 'ACTIVE'
    """)
    void revokeAll(@Param("userId") Long userId, @Param("now") Instant now, @Param("reason") String reason);

    @Modifying
    @Query("""
        update UserSession us
        set us.revokedTime = :now,
            us.revokedReason = :reason
        where us.sessionId =:sid
    """)
    void revoke(@Param("sid") String sid,@Param("now") Instant now,@Param("reason") String reason);

    @Query("""
        select us from UserSession us
        where us.sessionId =:sid
        and us.expiredTime >:now
        and us.status ='ACTIVE'
    """)
    Optional<UserSession> findActiveBySessionId(@Param("sid") String sid,@Param("now") Instant now);

}
