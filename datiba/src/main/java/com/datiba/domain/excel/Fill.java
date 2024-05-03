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
public class Fill {

    public static final String SHEET_NAME = "填空题";

    public static final String[] HEADER_LIST = {"题目（必填）", "答案1（必填）", "答案2", "答案3", "答案4", "答案5","解析"};

    @ExcelProperty("题目（必填）")
    private String question;

    @ExcelProperty("答案1（必填）")
    private String answer1;

    @ExcelProperty("答案2")
    private String answer2;

    @ExcelProperty("答案3")
    private String answer3;

    @ExcelProperty("答案4")
    private String answer4;

    @ExcelProperty("答案5")
    private String answer5;
    
    @ExcelProperty("解析")
    private String analysis;

}
