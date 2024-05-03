package com.datiba.domain.answer;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/8
 */
@Data
public class UserAnswer {

    private String question;

    private int type;

    private String analysis;

    private String answerSingle;

    private String answerMultiple;

    private Integer answerJudge;

    private String answerFill;
    private String mFillAnswer;

    private Integer mJudgeAnswer;
    public String getmFillAnswer() {
        return mFillAnswer;
    }

    public void setmFillAnswer(String mFillAnswer) {
        this.mFillAnswer = mFillAnswer;
    }

    public Integer getmJudgeAnswer() {
        return mJudgeAnswer;
    }

    public void setmJudgeAnswer(Integer mJudgeAnswer) {
        this.mJudgeAnswer = mJudgeAnswer;
    }
}
