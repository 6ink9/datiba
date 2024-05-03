package com.datiba.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datiba.domain.Paper;
import com.datiba.domain.Question;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/16
 */
public interface QuestionMapper extends BaseMapper<Question> {
    List<Question> listQuestion(Integer bankId, Integer type, String keyword);
}
