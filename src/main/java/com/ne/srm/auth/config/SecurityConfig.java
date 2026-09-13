package com.ne.srm.auth.config;

import com.ne.srm.auth.config.handler.ForbiddenHandler;
import com.ne.srm.auth.config.handler.UnauthorizedHandler;
import com.ne.srm.auth.config.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtFilter;

    private final UnauthorizedHandler unauthorizedHandler;

    private final ForbiddenHandler forbiddenHandler;

    private final SecurityProperties properties;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /*
         * 根据token模式处理CSRF
         */
        if ("cookie".equalsIgnoreCase(properties.getTokenMode())) {
            http.csrf(csrf -> csrf
                    .ignoringRequestMatchers(
                            "/auth/login",
                            "/auth/logout"
                    )
            );

        } else {
            http.csrf(AbstractHttpConfigurer::disable);
        }

        http
            /*
             * JWT无状态认证
            */
            .sessionManagement(
                    session -> session.sessionCreationPolicy(STATELESS)
            ).exceptionHandling(exception -> {
                exception.authenticationEntryPoint(authenticationEntryPoint);
                })
                /*
                 * 请求权限规则
                 */
            .authorizeHttpRequests(
                    auth -> auth
                            //登录相关api
                            .requestMatchers(
                                    "/",
                                    "/auth/login",
                                    "/auth/register",
                                    "/auth/refresh"
                            ).permitAll()
                            //静态页面
                            .requestMatchers(
                                    "/login",
                                    "/register"
                            ).permitAll()
                            //静态资源
                            .requestMatchers(
                                    "/css/**",
                                    "/js/**",
                                    "/images/**"
                            ).permitAll()
                            //除此之外全部认证
                            .anyRequest()
                            .authenticated()
                )
                .addFilterBefore(
                    //JWT过滤器放在账号密码过滤器之前
                    jwtFilter,
                    UsernamePasswordAuthenticationFilter.class
                ).exceptionHandling(
                    //异常处理
                    e -> e
                            .authenticationEntryPoint(unauthorizedHandler)
                            .accessDeniedHandler(forbiddenHandler)
            );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {

        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return builder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
