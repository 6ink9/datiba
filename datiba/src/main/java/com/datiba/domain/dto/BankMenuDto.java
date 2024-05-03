package com.datiba.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * Description: 题库菜单
 * Param:
 * return:
 * @author:WuXiaotong Date: 2024/2/17
 */
@Data
public class BankMenuDto {
    /**
     * 题库Id
     */
    Integer id;

    /**
     * 大类名称
     */
    String name;

    /**
     * 子类
     */
    List<SysQuestionBankDto> childs;

}
