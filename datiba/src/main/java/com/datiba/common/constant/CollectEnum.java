package com.datiba.common.constant;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/10
 */
public enum CollectEnum {

    DIS_COLLECT("取消收藏",0),
    COLLECT("收藏",1),

    CHECK("检查收藏状态",2);

    private String msg;

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

    private int code;
    // 构造方法
    private CollectEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

}
