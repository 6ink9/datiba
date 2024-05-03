package com.datiba.config;

import com.datiba.exception.BusinessException;
import com.datiba.exception.UnauthorizedTokenException;
import com.datiba.util.Result;
import com.datiba.util.ResultGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/13
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedTokenException.class)
    @ResponseBody
    public Result handleUnauthorizedTokenException(UnauthorizedTokenException ex) {
        return ResultGenerator.genTokenErrorResult();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result handleBusinessException(BusinessException ex) {
        return ResultGenerator.genFailResult(ex.getMsg());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        return ResultGenerator.genFailResult(ex.getMessage());
    }

}