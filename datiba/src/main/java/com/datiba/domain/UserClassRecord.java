package com.datiba.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 用户-班级记录
 * @TableName user_class_record
 */
@TableName(value ="user_class_record")
@Data
@Builder
public class UserClassRecord implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer userId;

    /**
     *
     */
    private Integer classId;
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