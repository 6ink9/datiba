package com.datiba.domain.dto;

import lombok.Data;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/25
 */

@Data
public class ExamReleaseDto {

    /**
     * 选择的试卷id
     */
    Integer paperId;

    /**
     * 试卷名称
     */
    String paperName;
    /**
     * 班级Id
     */
    Integer clazzId;

    /**
     * 时间限制
     */
    String dateRange;

    /**
     * 倒计时 min
     */
    Long countDownMinutes;
}
