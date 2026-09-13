package com.ne.srm.auth.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {


    private final PasswordEncoder encoder;


    public PasswordUtils(PasswordEncoder encoder){
        this.encoder = encoder;
    }



    /**
     * 密码加密
     */
    public String encode(String password){
        return encoder.encode(password);
    }

    /**
     * 密码验证
     */
    public boolean matches(String rawPassword, String encodedPassword){
        return encoder.matches(
                rawPassword,
                encodedPassword
        );

    }

}
