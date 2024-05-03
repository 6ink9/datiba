package com.datiba.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datiba.domain.Question;
import com.datiba.exception.BusinessException;
import com.datiba.mapper.QuestionMapper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/16
 */
@Service
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {
    public List<Question> getRandomQuestion(Integer number, Integer bankId) {
        List<Question> questionList = this.list(new QueryWrapper<Question>().eq("bank_id",bankId));
        List<Question> randomQuestions = new ArrayList<>();
        int length = questionList.size();
        if (length<number){
            throw new BusinessException("抱歉，题库数量不足");
        }

        Random random= new Random();
        Set<Integer> questionIndex = new HashSet<>();
        for (int i = 0; i < number; i++) {
            int index = random.nextInt(length);
            while (questionIndex.contains(index)){
                index = random.nextInt(length);
            }
            randomQuestions.add(questionList.get(index));
        }
        return randomQuestions;
    }
}
