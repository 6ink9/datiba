package com.datiba.util;

import com.datiba.exception.UnauthorizedTokenException;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/14
 */

public class AuthUtil {

    public static Integer getUserId(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String sub = null;
        try {
            sub = JwtUtil.validateToken(token);
        } catch (Exception e) {
            throw new UnauthorizedTokenException("token异常");
        }
        return Integer.parseInt(sub);
    }
}
