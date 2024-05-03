package com.datiba.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 班级成绩情况
 * Author:WuXiaotong
 * Date: 2024/3/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassScore {

    /**
     * 班级Id
     */
    int clazzId;

    /**
     * 成绩分布 0-20，20-40，...,80-100 共五个区间
     */
    List<Long> scoreRange;

    String clazzName;

    /**
     * 平均成绩 保留两位小数
     */
    double averageScore;
}
