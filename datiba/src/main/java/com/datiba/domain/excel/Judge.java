package com.datiba.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/16
 */
@Data
public class Judge {

    public static final String SHEET_NAME = "判断题";

    public static final String[] HEADER_LIST = {"题目（必填）","答案（正确/错误）","解析"};

    @ExcelProperty("题目（必填）")
    private String question;

    @ExcelProperty("答案（正确/错误）")
    private String answer;
    
    @ExcelProperty("解析")
    private String analysis;

}
