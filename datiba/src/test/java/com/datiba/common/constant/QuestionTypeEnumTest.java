package com.datiba.common.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/6
 */
class QuestionTypeEnumTest {

    @Test
    void fromStr() {
        String singleStr  ="single";
        int single = QuestionTypeEnum.fromStr(singleStr);
        String multipleStr  ="multiple";
        int multiple = QuestionTypeEnum.fromStr(multipleStr);
        assert single == 1;
        assert multiple == 2;
    }
}