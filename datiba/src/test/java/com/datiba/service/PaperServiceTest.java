package com.datiba.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.datiba.domain.dto.ScoreConfig;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/5
 */
class PaperServiceTest {

    @Test
    void getPaperDetail() {
        String eachScore = "{\"single\":\"10\",\"multiple\":\"10\",\"judge\":\"10\",\"fill\":\"10\"}";
        ScoreConfig scoreConfig = JSON.parseObject(eachScore, ScoreConfig.class);
        System.out.println(scoreConfig.getMultiple());

    }
    @Test
    void transfer(){
        List<String> answerList = new ArrayList<>();
        answerList.add("答案一");
        answerList.add("答案二");
        String s = answerList.toString();
        System.out.println(s);
    }
}