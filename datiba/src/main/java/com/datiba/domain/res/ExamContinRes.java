package com.datiba.domain.res;

import com.datiba.domain.Question;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/7
 */
@Data
@Builder
public class ExamContinRes {

    List<Question> questionList;

    /**
     * 考试开始时间
     */
    Date startTime;

    /**
     * 答题时间范围限制
     */
    Date ddl;

    /**
     * 倒计时
     */
    Long countDownMinutes;
    /**
     * 分数
     */
    String eachScore;

}
