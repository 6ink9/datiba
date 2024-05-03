package com.datiba.domain.res;

import com.datiba.domain.ExamRecord;
import com.datiba.domain.Paper;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/21
 */
@Data
@Builder
public class PaperDetailRes {
    Paper paper;

    List<ExamRecord> examRecordList;

    Integer questionNum;

    Integer totalScore;

}
