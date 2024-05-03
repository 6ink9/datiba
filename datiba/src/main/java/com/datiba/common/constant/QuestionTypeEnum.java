package com.datiba.common.constant;


import com.datiba.exception.BusinessException;

/**
 * Description: 问题类型枚举类
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/16
 */
public enum QuestionTypeEnum {

    SINGLE("单选题",1),

    MULTIPLE("多选题",2),

    JUDGE("判断题",3),

    FILL("填空题",4);

    private String msg;
    private int type;
    // 构造方法
    private QuestionTypeEnum(String msg, int type) {
        this.msg = msg;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    // 你可以添加一个静态方法，通过code找到对应的ErrorCode
    public static int fromStr(String typeStr) {
        for (QuestionTypeEnum type : QuestionTypeEnum.values()) {
            if (typeStr.toUpperCase().equals(type.name())) {
                return type.getType();
            }
        }
        throw new BusinessException("该类型不存在");
    }
}
