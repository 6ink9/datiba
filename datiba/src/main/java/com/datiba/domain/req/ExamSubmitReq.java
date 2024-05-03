package com.datiba.domain.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.datiba.domain.answer.UserAnswer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/8
 */
@Data
public class ExamSubmitReq {
    Integer recordId;

    List<UserAnswer> answerList;
}
