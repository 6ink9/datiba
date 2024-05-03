package com.datiba.domain.res;

import com.datiba.domain.ExamRecord;
import com.datiba.domain.Question;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Description: 考试分析响应
 * Author:WuXiaotong
 * Date: 2024/3/10
 */
@Data
@Builder
public class ExamAnalysisRes {

    List<Question> questionList;

    String analysis;

    String eachScore;
}
