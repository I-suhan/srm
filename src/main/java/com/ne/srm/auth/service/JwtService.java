package com.ne.srm.auth.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ne.srm.auth.config.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
public class JwtService {
    private final SecurityProperties securityProperties;

    private final SecretKey key;

    public JwtService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        key = Keys.hmacShaKeyFor(securityProperties.getJwt().getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String generateJti(Instant time){
        return UuidCreator.getTimeOrderedEpoch(time).toString();
    }

    public String getType(Claims claims) {
        return claims.get("type", String.class);
    }

    public String getSessionId(Claims claims) {
        return claims.get("session_id", String.class);
    }

    public String getSessionId(String token) {
        return parse(token).get("session_id", String.class);
    }

    public Long getVersion(String token) {
        return parse(token).get("version", Long.class);
    }

    public Long getVersion(Claims claims) {
        return claims.get("version", Number.class).longValue();
    }

    public String getJti(String token){
        return parse(token).get("jti",String.class);
    }

    public String getJti(Claims claims){
        return claims.get("jti",String.class);
    }

    public Instant getExpiration(String token){
        return parse(token).getExpiration().toInstant();
    }

    public Instant getExpiration(Claims claims){
        return claims.getExpiration().toInstant();
    }


    public String createAccessToken(
            Long userId,
            String jti,
            String sessionId,
            Long tokenVersion,
            Instant time){
        return Jwts.builder()
                .id(jti)
                .subject(userId.toString())
                .claim("type", "access")
                .claim("session_id", sessionId)
                .claim("version", tokenVersion)
                .issuedAt(Date.from(time))
                .expiration(Date.from(time.plusMillis(securityProperties.getJwt().getAccessExpire())))
                .issuer("srm-su")
                .audience().add("api-name")
                .and()
                .signWith(key)
                .compact();
    }

    //解析
    private Claims parse(String token){
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer("srm-su")
                .requireAudience("api-name")
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //验证
    public Claims parseForValidate(String token) {

            return parse(token);

    }

    public Long getUserId(String token){
        return Long.valueOf(parse(token).getSubject());
    }

    public Long getUserId(Claims claims){
        return Long.valueOf(claims.getSubject());
    }

}