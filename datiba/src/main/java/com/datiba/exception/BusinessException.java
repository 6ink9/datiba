package com.datiba.exception;

import com.datiba.util.ResultGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    Integer code;
    String msg;

    public BusinessException(String msg){
        this.code = ResultGenerator.RESULT_CODE_SERVER_ERROR;
        this.msg = msg;
    }

}
