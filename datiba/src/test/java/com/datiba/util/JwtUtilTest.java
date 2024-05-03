package com.datiba.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/13
 */
@SpringBootTest
class JwtUtilTest {

    @Test
    void validateToken() throws Exception {
        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzIiwiZXhwIjoxNzExMDk5OTEzfQ.yGqRCh8qivky0FoDcLq5znVJWxjJYeOHnpmoCGlLVZMicj2p8g8sjcJCQ4S3gYP8kr1Ac22SVONuXD9drZDa_g";
        String id = JwtUtil.validateToken(token);
        System.out.println(id);
    }
}