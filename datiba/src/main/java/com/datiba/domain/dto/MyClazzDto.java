package com.datiba.domain.dto;

import lombok.Data;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/10
 */
@Data
public class MyClazzDto {

    /**
     * 课程ID
     */
    private Integer clazzId;

    /**
     * 记录Id
     */
    private Integer recordId;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程码
     */
    private String code;

}
