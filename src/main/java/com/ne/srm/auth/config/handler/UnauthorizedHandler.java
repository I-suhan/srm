package com.ne.srm.auth.config.handler;

import jakarta.servlet.http.*;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String uri = request.getRequestURI();

        /*
         * API返回JSON
         */
        if(uri.startsWith("/api/") || uri.startsWith("/auth/")){

            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                            """
                            {
                              "code":401,
                              "message":"登录已失效"
                            }
                            """
                    );

            return;

        }

        /*
         * 页面跳登录
         */
        response.sendRedirect(request.getContextPath() + "/login");

    }

}
