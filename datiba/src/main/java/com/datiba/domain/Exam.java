package com.datiba.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.models.auth.In;
import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName exam
 */
@TableName(value ="exam")
@Data
@Builder
public class Exam implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 考试对应的试卷id
     */
    private Integer paperId;

    /**
     * 考试码
     */
    private String code;

    /**
     * 答题日期限制
     */
    private String dateRange;

    /**
     * 倒计时
     */
    private Long countdownMinutes;

    /**
     * name 与试卷名称相等
     */
    private String name;
    /**
     * 
     */
    private Integer createdBy;

    /**
     * 
     */
    private Date createdTime;

    /**
     * 
     */
    private Date updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}