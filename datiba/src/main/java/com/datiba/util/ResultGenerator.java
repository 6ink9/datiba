package com.datiba.util;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public class ResultGenerator {
    public static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    public static final String DEFAULT_FAIL_MESSAGE = "FAIL";

    public static final String DEFAULT_TOKEN_ERROR_MESSAGE = "Token失效";
    public static final int RESULT_CODE_SUCCESS = 200;
    public static final int RESULT_CODE_SERVER_ERROR = 500;

    public static final int RESULT_CODE_TOKEN_ERROR = 401;
    public static Result genSuccessResult() {
        Result result = new Result();
        result.setCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;
    }

    public static Result genSuccessResult(String message) {
        Result result = new Result();
        result.setCode(RESULT_CODE_SUCCESS);
        result.setMessage(message);
        return result;
    }

    public static Result genSuccessResult(Object data) {
        Result result = new Result();
        result.setCode(RESULT_CODE_SUCCESS);
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    public static Result genSuccessResult(String message,Object data) {
        Result result = new Result();
        result.setCode(RESULT_CODE_SUCCESS);
        result.setData(data);
        result.setMessage(message);
        return result;
    }


    public static Result genFailResult(String message) {
        Result result = new Result();
        result.setCode(RESULT_CODE_SERVER_ERROR);
        if (StringUtils.isBlank(message)) {
            result.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            result.setMessage(message);
        }
        return result;
    }

    public static Result genErrorResult(int code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static Result genTokenErrorResult(){
        Result result = new Result();
        result.setCode(RESULT_CODE_TOKEN_ERROR);
        result.setMessage(DEFAULT_TOKEN_ERROR_MESSAGE);
        return result;
    }
}
