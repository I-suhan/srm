package com.ne.srm.auth.util;

import com.ne.srm.auth.config.SecurityProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieUtil {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private final SecurityProperties properties;

    public CookieUtil(SecurityProperties properties) {
        this.properties = properties;
    }

    /**
     * 添加access_token
     */
    public void addAccessToken(HttpServletResponse response, String token){

        long maxAge = properties.getJwt().getAccessExpire();

        ResponseCookie cookie =
                ResponseCookie
                        .from(ACCESS_TOKEN, token)
                        .httpOnly(true)
                        .secure(properties.getCookie().isSecure())
                        .path("/")
                        .maxAge(Duration.ofSeconds(maxAge))
                        .sameSite(properties.getCookie().getSameSite())
                        .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * 删除access_token
     */
    public void removeAccessToken(HttpServletResponse response){

        ResponseCookie cookie =
                ResponseCookie
                        .from(ACCESS_TOKEN, "")
                        .httpOnly(true)
                        .secure(properties.getCookie().isSecure())
                        .path("/")
                        .maxAge(Duration.ZERO)
                        .sameSite(properties.getCookie().getSameSite())
                        .build();

        response.addHeader("Set-Cookie", cookie.toString());

    }

    /**
     * 保存RefreshToken
     */
    public void addRefreshToken(HttpServletResponse response, String refreshToken){
        ResponseCookie cookie =
                ResponseCookie
                        .from(REFRESH_TOKEN, refreshToken)
                        .httpOnly(true)
                        .secure(properties.getCookie().isSecure())
                        .path("/")
                        .maxAge(properties.getJwt().getRefreshExpire())
                        .sameSite(properties.getCookie().getSameSite())
                        .build();

        response.addHeader("Set-Cookie", cookie.toString());


    }

    /*
     * 读取access token
     * */
    public static String getAccessToken(HttpServletRequest request){
        return getCookie(request, ACCESS_TOKEN);

    }

    /*
    * 读取refresh token
    * */
    public String getRefreshToken(HttpServletRequest request){
        return getCookie(request, REFRESH_TOKEN);
    }

    /*
    * 删除access token
    * */
    public static void clearAccessToken(HttpServletResponse response){
        clear(response,ACCESS_TOKEN);
    }

    /*
     * 删除refresh token
     * */
    public static void clearRefreshToken(HttpServletResponse response){
        clear(response,REFRESH_TOKEN);
    }

    private static void addCookie(HttpServletResponse response,
            String name, String value, int maxAge
    ){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private static String getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return null;
        }

        for(Cookie cookie:cookies){
            if(name.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }

    private static void clear(HttpServletResponse response, String name){
        Cookie cookie = new Cookie(name, "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
