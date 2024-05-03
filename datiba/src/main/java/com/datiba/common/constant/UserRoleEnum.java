package com.datiba.common.constant;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/15
 */
public enum UserRoleEnum {

    TEACHER("老师",1),

    STUDENT("学生",0);

    private String msg;
    private int code;
    UserRoleEnum(String msg, int code) {
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
