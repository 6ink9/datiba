package com.datiba.domain.dto;
import lombok.Data;

import java.util.List;

/**
 * Description: 编题考试参数
 * Author:WuXiaotong
 * Date: 2024/2/19
 */
@Data
public class ExamSetDto {

    Integer paperId;

    String paperName;
    /**
     * 时间限制
     */
    String dateRange;
    /**
     * 倒计时 min
     */
    Long countDownMinutes;

}
