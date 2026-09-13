package com.ne.srm.auth.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final Integer code;

    public AuthException(Integer code, String message){
        super(message);
        this.code = code;
    }

}
