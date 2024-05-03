package com.datiba.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/4
 */
@TableName("question")
@Data
@Builder
public class Question {

    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField("question")
    private String question;

    /**
     * 题库Id
     */
    @TableField("bank_id")
    private int bankId;

    /**
     * 问题类型 1-单选 2-多选 3-判断 4-填空
     */
    @TableField("type")
    private int type;



    /**
     * 单选题答案
     */
    @TableField("answer_single")
    private String answerSingle;

    /**
     * 多选题答案
     */
    @TableField("answer_multiple")
    private String answerMultiple;

    /**
     * 判断题答案 类型为Integer,默认为null
     * 如果使用int 则会有默认值0
     */
    @TableField("answer_judge")
    private Integer answerJudge;

    /**
     * 填空题答案
     */
    @TableField("answer_fill")

    private String answerFill;


    /**
     * 解析
     */
    @TableField("analysis")
    private String analysis;

    @TableField("created_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @TableField("updated_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    @TableField("show_code")
    private int showCode;
}
