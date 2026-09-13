package com.ne.srm.auth.config.resolver;

import com.ne.srm.auth.config.SecurityProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConditionalOnProperty(
        name="security.token-mode",
        havingValue="cookie",
        matchIfMissing=true
)
public class CookieTokenResolver implements TokenResolver {
    private final SecurityProperties properties;
    public CookieTokenResolver(SecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public String resolve(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();

        if(cookies==null){
            return null;
        }

        for(Cookie cookie:cookies){
            if( "access_token".equals(cookie.getName())){
                return cookie.getValue();

            }
        }
        return null;
    }

    @Override
    public void remove(HttpServletResponse response) {
        ResponseCookie cookie =
                ResponseCookie
                        .from("access_token", "")
                        .httpOnly(true)
                        .secure(properties.getCookie().isSecure())
                        .path("/")
                        .maxAge(Duration.ZERO)
                        .sameSite(properties.getCookie().getSameSite())
                        .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

}
