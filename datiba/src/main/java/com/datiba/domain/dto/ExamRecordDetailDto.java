package com.datiba.domain.dto;

import com.datiba.domain.Exam;
import com.datiba.domain.ExamRecord;
import lombok.Builder;
import lombok.Data;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/22
 */
@Data
@Builder
public class ExamRecordDetailDto {
    Exam exam;
    ExamRecord examRecord;
}
