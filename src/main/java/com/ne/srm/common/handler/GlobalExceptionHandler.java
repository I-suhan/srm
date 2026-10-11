package com.ne.srm.common.handler;

import com.ne.srm.auth.exception.AuthException;
import com.ne.srm.common.module.Result;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Result> notFound(EntityNotFoundException e) {
        return ResponseEntity.status(404).body(Result.error(404, "NOT_FOUND", e.getMessage()));
    }

    /**
     * 认证业务异常
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Result> auth(AuthException e){
        return ResponseEntity
                .status(e.getCode())
                .body(
                        Result.error(
                                e.getCode(),
                                "",
                                e.getMessage()
                        )
                );

    }

    /**
     * 参数错误
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> param(Exception e){

        return ResponseEntity
                .badRequest()
                .body(
                        Result.error(
                                400,
                                "",
                                e.getMessage()
                        )
                );

    }

    /**
     * 未处理异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> error(Exception e){

        return ResponseEntity
                .status(500)
                .body(
                        Result.error(
                                500, "", "服务器异常"
                        )
                );

    }


}
