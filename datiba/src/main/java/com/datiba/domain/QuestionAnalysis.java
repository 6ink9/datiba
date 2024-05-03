package com.datiba.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnalysis {

    /**
     * 用户答案
     */
    String answer;
    /**
     * 是否正确
     */
    Boolean correct;

}
