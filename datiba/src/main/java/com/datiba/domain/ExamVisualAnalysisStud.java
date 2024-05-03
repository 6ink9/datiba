package com.datiba.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/21
 */

@Data
@Builder
public class ExamVisualAnalysisStud {

    /**
     * 考试名称
     */
    List<String> examName;

    /**
     * 正确率
     */
    List<Double> accuracy;
}
