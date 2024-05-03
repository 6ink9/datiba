package com.datiba.domain.dto;

import com.datiba.domain.ExamRecord;
import com.datiba.domain.Paper;
import io.swagger.models.auth.In;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/26
 */
@Data
@Builder
public class ExamListDto {

    String paperName;

    Integer recordId;

    /**
     *
     */
    private String score;

    /**
     *
     */
    private String correctCount;

    /**
     * 试卷状态
     */
    private Integer status;

    private Date startTime;


}
