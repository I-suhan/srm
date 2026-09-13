package com.ne.srm.common.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Integer code;

    private String msg;

    private Object data;

    private String errorCode;

    private String errorMsg;

    public static Result error(Integer code, String errorCode, String message){
        return new Result(code, null, null, errorCode, message);
    }

    public static Result ok(){
        return new Result(200, "success", null, null, null);

    }

    public static Result ok(String msg){
        return new Result(200, msg, null, null, null);
    }

    public static Result ok(Object data){
        return new Result(200, null, data, null, null);
    }

}
