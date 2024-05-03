package com.datiba.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.datiba.domain.answer.UserChoiceAnswer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/8
 */
class ExamServiceTest {

    public boolean checkChoice(String str) {
        boolean res = true;
        List<UserChoiceAnswer> choiceAnswer = JSON.parseObject(str, new TypeReference<List<UserChoiceAnswer>>() {
        });
        for (UserChoiceAnswer answer :choiceAnswer){
            // 该答案是正确答案
            boolean choice = answer.getIsAnswer() == 1;
            // 正确则 choice & isChecked为真
            res =  (choice == answer.isChecked()) & res;
            if (!res){
                break;
            }
        }
        return res;
    }
    @Test
    void checkChoice1() {
        String str = "[{\"content\":\"a\",\"isAnswer\":0,\"isChecked\":false},{\"content\":\"b\",\"isAnswer\":1,\"isChecked\":true}]";
        boolean res = checkChoice(str);
        assert res;
    }

    @Test
    void checkChoice2(){
        String str = "[{\"content\":\"A\",\"isAnswer\":1,\"isChecked\":true},{\"content\":\"B\",\"isAnswer\":1,\"isChecked\":true},{\"content\":\"C\",\"isAnswer\":0,\"isChecked\":true},{\"content\":\"D\",\"isAnswer\":0,\"isChecked\":true}]";
        boolean res = checkChoice(str);
        assert !res;
    }

    public boolean checkFill(String str, List<String> userFill) {
        List<List<String>> rightFill = JSON.parseObject(str, new TypeReference<List<List<String>>>() {
        });
        boolean res = true;
        for (int i = 0; i < userFill.size(); i++) {
            if (!rightFill.get(i).contains(userFill.get(i))){
                res = false;
                break;
            }
        }

        return res;
    }

    @Test
    void checkFill1(){
        String str = "[[\"1.1\",\"1.2\"],[\"2.1\",\"2.2\"]]";
        List<String> mList = Arrays.asList("1.1","2.1");
        boolean res = checkFill(str,mList);
        assert res;
    }

    @Test
    void checkFill2(){
        String str = "[[\"1.1\",\"1.2\"],[\"2.1\",\"2.2\"]]";
        List<String> mList = Arrays.asList("1.1","1.1");
        boolean res = checkFill(str,mList);
        assert !res;
    }

}