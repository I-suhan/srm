package com.ne.srm.auth.config.filter;

import com.ne.srm.auth.config.JwtAuthenticationEntryPoint;
import com.ne.srm.auth.config.SecurityProperties;
import com.ne.srm.auth.config.resolver.TokenResolver;
import com.ne.srm.auth.entity.SecurityUser;
import com.ne.srm.auth.service.*;
import com.ne.srm.core.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenResolver tokenResolver;
    private final JwtService jwtService;
    private final RedisService redisService;

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;



    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {

        if(SecurityContextHolder.getContext().getAuthentication() != null){
            log.info("空上下文");
            filterChain.doFilter(request,response);
            return;
        }

        String accessToken = tokenResolver.resolve(request);

        log.info("access--TOKEN: {}", accessToken);

            /*
             * 验证JWT
             */
        if(accessToken != null && !accessToken.isEmpty()) {

                try{
                    Claims claims = jwtService.parseForValidate(accessToken);

                    if(claims == null){
                        throw new BadCredentialsException("Token无效");
                    }

                    if(!"access".equalsIgnoreCase(jwtService.getType(claims))){
                        log.debug("JWT validation failed");
                        throw new BadCredentialsException(
                                "Token 类型错误"
                        );
                    }

                    Long userId = jwtService.getUserId(claims);

                    //查询黑名单
                    if(redisService.exists("user:block:" + userId)){
                        throw new LockedException("用户已被锁定");
                    }

                    // 查询会话存活名单
                    if(!redisService.exists("session:alive:" + jwtService.getSessionId(claims))){
                        tokenResolver.remove(response);
                        throw new CredentialsExpiredException("登录状态失效");
                    }

                    //token版本检查
                    Long tokenVersion = jwtService.getVersion(claims);
                    String realVersion = redisService.get("user:token:version:" + userId);
                    if(tokenVersion == null || realVersion == null ||!Objects.equals(
                            tokenVersion, Long.valueOf(realVersion)
                    )) {
                        throw new CredentialsExpiredException("令牌已过时，请重新登录");
                    }

                    SecurityUser user = (SecurityUser)userDetailsService.loadUserByUserId(userId);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                            (user, null, user.getAuthorities());
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }catch (ExpiredJwtException e) {
                    log.debug("JWT expired");
                    tokenResolver.remove(response);
                    SecurityContextHolder.clearContext();
                    throw new CredentialsExpiredException("Token 已过期", e);

                } catch (JwtException e) {
                    log.warn("JWT validation failed: {}", e.getMessage());
                    tokenResolver.remove(response);
                    SecurityContextHolder.clearContext();
                    throw new BadCredentialsException("Token 无效", e);
                } catch(AuthenticationException e){

                    authenticationEntryPoint.commence(request, response, e);
                    SecurityContextHolder.clearContext();

                    return;

                } catch(Exception e){
                    log.warn("JWT 认证失败, uri={}", request.getRequestURI(), e);
                    tokenResolver.remove(response);
                    SecurityContextHolder.clearContext();
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            e.getMessage()
                    );
                    return;
                }

        }
        filterChain.doFilter(request, response);
    }

}
