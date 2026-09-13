package com.ne.srm.auth.exception;

import lombok.Getter;

@Getter
public class JwtException extends Exception {

    private final Integer code;

    public JwtException(Integer code, String message){
        super(message);
        this.code = code;
    }
}
