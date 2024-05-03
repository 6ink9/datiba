package com.datiba.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/10
 */
@Data
@TableName("class")
@Builder
public class Clazz {

    /**
     * 课程ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("课程id")
    private int id;
    /**
     * 课程名称
     */
    @ApiModelProperty("课程名称")
    @TableField("name")
    private String name;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField("created_by")
    private int createdBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("created_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /**
     * 修改时间
     */
    @ApiModelProperty("updated_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /**
     * 课程码
     */
    @ApiModelProperty("课程码")
    @TableField("code")
    private String code;
}