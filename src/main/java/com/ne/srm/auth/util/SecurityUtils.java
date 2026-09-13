package com.ne.srm.auth.util;
import com.ne.srm.auth.entity.SecurityUser;
import com.ne.srm.auth.exception.AuthException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public final class SecurityUtils {


    private SecurityUtils(){

    }


    //获取当前user信息
    public static SecurityUser getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if(authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken){
            throw new AuthException(401, "未认证");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof SecurityUser) {
            return (SecurityUser) principal;
        }

        // 支持其他认证方式
        if (principal instanceof UserDetails) {
            return convertToSecurityUser((UserDetails) principal);
        }

        throw new AuthException(401, "非法用户");

    }

    private static SecurityUser convertToSecurityUser(UserDetails principal) {
        return null;
    }

    //获取当前用户id
    public static Long getUserId(){

        return getCurrentUser().getId();

    }


}