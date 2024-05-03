package com.datiba.util;

import com.datiba.domain.dto.ScoreConfig;

import java.util.Map;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/9
 */
public class ScoreUtil {

    public static int getFullScore(ScoreConfig scoreConfig, Map<Integer, Integer> questionNum ){
        int totalScore = 0;
        for (Integer type : questionNum.keySet()) {
            // 该题型数量
            int num = questionNum.get(type);
            // 总分增加 数量*分数
            switch (type) {
                case 1:
                    totalScore += num * scoreConfig.getSingle();
                    break;
                case 2:
                    totalScore += num * scoreConfig.getMultiple();
                    break;
                case 3:
                    totalScore += num * scoreConfig.getJudge();
                    break;
                case 4:
                    totalScore += num * scoreConfig.getFill();
            }
        }
        return totalScore;
    }
}
