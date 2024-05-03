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
@TableName("user")
@Data
@Builder
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;

    @TableField("phone")
    private String phone;

    @TableField("nickName")
    private String nickName;

    @TableField("avatar")
    private String avatar;

    @TableField("description")
    private String description;

    @TableField("role")
    private Integer role;

    @TableField("password")
    private String password;

    @TableField("created_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @TableField("updated_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

}
