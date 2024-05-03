package com.datiba.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamVisualAnalysis {
    /**
     * 考试Id
     */
    int paperId;

    /**
     * 考试名称
     */
    String paperName;

    /**
     * 班级考试情况
     */
    List<ClassScore> classScoresList;
}
