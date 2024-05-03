package com.datiba.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/8
 */

@Data
@TableName("paper")
@Builder
public class Paper {

    /**
     * id
     */
    @ApiModelProperty("id")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 试卷名称
     */
    @ApiModelProperty("试卷名称")
    private String name;
    /**
     *
     */
    @ApiModelProperty("")
    private String description;
    /**
     * 分值
     */
    @ApiModelProperty("分值")
    private String eachScore;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Integer createdBy;

    private Date createdTime;

    private Date updatedTime;
    /**
     * 0-公开 1-私有
     */
    @ApiModelProperty("0-公开 1-私有")
    private Integer isPrivate;

    /**
     * 分类id
     */
    @ApiModelProperty("分类Id")
    private Integer bankId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String bankName;
}