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
public class MultipleChoice {

    public static final String SHEET_NAME = "多选题";

    public static final String[] HEADER_LIST = {"题目（必填）","选项A（必填）","选项B（必填）",
            "选项C（必填）", "选项D（必填）","选项E","选项F","选项G","正确选项（必填，用/分隔）","解析"};

    @ExcelProperty("题目（必填）")
    private String question;

    @ExcelProperty("选项A（必填）")
    private String choiceA;

    @ExcelProperty("选项B（必填）")
    private String choiceB;

    @ExcelProperty("选项C（必填）")
    private String choiceC;

    @ExcelProperty("选项D（必填）")
    private String choiceD;

    @ExcelProperty("选项E")
    private String choiceE;

    @ExcelProperty("选项F")
    private String choiceF;

    @ExcelProperty("选项G")
    private String choiceG;

    @ExcelProperty("正确选项（必填，用/分隔）")
    private String rightChoice;
    
    @ExcelProperty("解析")
    private String analysis;

}
