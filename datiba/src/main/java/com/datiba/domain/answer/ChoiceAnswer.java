package com.datiba.domain.answer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description: 用于构造前端显示单选题/多选题答案的JSON数据
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/16
 */
@Data
@AllArgsConstructor
public class ChoiceAnswer {
    /**
     * 选项内容
     */
    String content;

    /**
     * 是否正确
     */
    int isAnswer;
}
