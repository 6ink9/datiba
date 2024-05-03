package com.datiba.domain.dto;

import com.datiba.domain.Question;
import lombok.Data;

import java.util.List;

/**
 * Description: 编题考试参数
 * Author:WuXiaotong
 * Date: 2024/2/19
 */
@Data
public class PaperSetDto {
    /**
     * 手动编题
     */
    List<Question> questionList;
    /**
     * 题库选题
     */
    List<Integer> bankQuestionId;

    // 试卷相关信息
    /**
     * 试卷名称
     */
    String name;
    /**
     * 试卷描述
     */
    String description;
    /**
     * 分值信息
     */
    String eachScore;

    /**
     * 题库
     */
    Integer bankId;

    /**
     * 是否私密
     */
    Integer isPrivate;

}
