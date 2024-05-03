package com.datiba.domain.answer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description: 交卷单选题/多选题返回的JSON数据 ischecked表示是否选中
 * Author:WuXiaotong
 * Date: 2024/3/8
 */
@Data
@AllArgsConstructor
public class UserChoiceAnswer {
    /**
     * 选项内容
     */
    String content;

    /**
     * 是否正确
     */
    int isAnswer;
    /**
     * 是否选中
     */
    boolean isChecked;
}
