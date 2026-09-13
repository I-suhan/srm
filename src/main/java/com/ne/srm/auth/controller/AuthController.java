package com.ne.srm.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ne.srm.auth.config.SecurityProperties;
import com.ne.srm.auth.config.resolver.TokenResolver;
import com.ne.srm.auth.dto.*;
import com.ne.srm.auth.service.AuthService;
import com.ne.srm.auth.util.CookieUtil;
import com.ne.srm.common.module.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieUtil cookieUtil;
    private final SecurityProperties properties;
    private final TokenResolver tokenResolver;

    public AuthController(AuthService authService, CookieUtil cookieUtil, SecurityProperties properties, TokenResolver tokenResolver) {
        this.authService = authService;
        this.cookieUtil = cookieUtil;
        this.properties = properties;
        this.tokenResolver = tokenResolver;
    }

    @GetMapping("/me")
    public UserInfoResponse me(){

        UserInfoResponse me = authService.me();
        return me;

    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest req, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws JsonProcessingException {
        LoginResponse result = authService.login(req, servletRequest);

        log.info("refresh-token-Login: {}", result.getRefreshToken());
        log.info("Access-Token-Login: {}", result.getAccessToken());

        if("cookie".equalsIgnoreCase(properties.getTokenMode())){
            cookieUtil.addAccessToken(servletResponse, result.getAccessToken());
            return Result.ok();
        }

        return Result.ok(result);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        authService.logout(request);
        SecurityContextHolder.clearContext();
        if("cookie".equalsIgnoreCase(properties.getTokenMode())){
            tokenResolver.remove(response);
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterRequest request){
        authService.register(request);
        return Result.ok();
    }

    @PutMapping("/password")
    public Result password(@RequestBody PasswordRequest request, HttpServletResponse response){

        authService.changePassword(request);
        CookieUtil.clearAccessToken(response);
        return Result.ok();
    }

    @PutMapping("/profile")
    public Result profile(@RequestBody ProfileRequest request){
        authService.updateProfile(request);
        return Result.ok();
    }

    @PostMapping("/refresh")
    public Result refresh(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        LoginResponse loginResponse = authService.refresh(cookieUtil.getRefreshToken(request), request);

        /*
         * Cookie模式
         */
        if("cookie".equalsIgnoreCase(properties.getTokenMode())){
            cookieUtil.addAccessToken(response, loginResponse.getAccessToken());
            cookieUtil.addRefreshToken(response, loginResponse.getRefreshToken());
            return Result.ok(loginResponse);

        }
        //todo 存入cookie
        return Result.ok(loginResponse);
    }

}
