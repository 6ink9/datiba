package com.datiba.common.constant;

/**
 * Description: 考试状态枚举类
 * Author:WuXiaotong
 * Date: 2024/2/18
 */
public enum ExamRecordStatusEnum {
    /**
     * UNSTARTED
     */
    UNSTARTED("unstarted",1),

    /**
     * 正在进行
     */
    ONGOING("ongoing",2),

    /**
     * 已完成
     */
    FINISH("finish",3);


    private String msg;
    private int code;
    ExamRecordStatusEnum(String msg, int code) {
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
