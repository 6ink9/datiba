package com.datiba.common.constant;


/**
 * Description: 问题类型枚举类
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/16
 */
public enum JudgeAnswerEnum {

    TRUE("正确",1),

    FALSE("错误",2);

    private String msg;
    private int code;
    JudgeAnswerEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
