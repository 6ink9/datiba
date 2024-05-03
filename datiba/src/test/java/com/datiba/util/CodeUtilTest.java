package com.datiba.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/19
 */
class CodeUtilTest {

    @Test
    void generateShortUuid() {
        Set<String> set = new HashSet<>(10000);
        for (int i = 0 ; i < 10000 ; i++){
            String s = CodeUtil.generateShortUuid();
            set.add(s);
        }
        assert (set.size()==10000);
    }
}