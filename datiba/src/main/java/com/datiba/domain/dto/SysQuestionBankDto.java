package com.datiba.domain.dto;

import lombok.Data;

/**
 * Description:
 * Param:
 * return:
 * @author:WuXiaotong Date: 2024/2/17
 */
@Data
public class SysQuestionBankDto {
    /**
     * 题库Id
     */
    Integer id;

    /**
     * 题库名称
     */
    String name;

    Integer parentId;
}
