package com.datiba.util;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/15
 */
class TimeUtilTest {

    @Test
    void strToDate() {
        String str = "2024-03-15 00:00,2024-04-15 23:59";
        Date startTime = TimeUtil.strToDate(str.split(",")[0]);
        System.out.println(startTime);
    }
}