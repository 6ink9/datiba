package com.datiba.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperQuestionPair {
    Integer paperId;
    Integer questionId;
}
