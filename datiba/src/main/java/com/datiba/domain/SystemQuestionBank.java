package com.datiba.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName system_question_bank
 */
@TableName(value ="system_question_bank")
@Data
public class SystemQuestionBank implements Serializable {
    /**
     * 主键Id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 题库名称
     */
    private String name;

    /**
     * 
     */
    private Date createdTime;

    /**
     * 
     */
    private Date updatedTime;

    /**
     * 上级分类 0->无上级分类（menu）
     */
    private Integer parentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}