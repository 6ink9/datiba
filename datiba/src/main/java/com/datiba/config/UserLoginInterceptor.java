package com.datiba.config;

import com.datiba.exception.UnauthorizedTokenException;
import com.datiba.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/13
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if(handler instanceof HandlerMethod ){
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            //判断如果请求的类是swagger的控制器，直接通行。
//            if (handlerMethod.getBean().getClass().getName().equals("springfox.documentation.swagger.web.ApiResourceController")) {
//                return true;
//            }
//        }
//
//        //预检直接放行
//        if("OPTIONS".equals(request.getMethod())){
//            return true;
//        }
//
//        // 从 request 的 header 中获得 token 值
//        String token = request.getHeader("Authorization");
//        if (token == null || token.equals("")) {
//            log.error("token缺失");
//            throw new UnauthorizedTokenException("token缺失");
//        }
//        // 验证 token, JwtUtil 是自己定义的类，里面有个方法验证 token
//        String sub = JwtUtil.validateToken(token);
//        if (sub == null || sub.equals("")) {
//            log.error("token无效");
//            throw new UnauthorizedTokenException("token无效");
//        }
//        // 更新 token 有效时间
//        if (JwtUtil.isNeedUpdate(token)) {
//            // 过期就创建新的 token 给前端
//            String newToken = JwtUtil.createToken(sub);
//            response.setHeader(JwtUtil.USER_LOGIN_TOKEN, newToken);
//        }

        return true;
    }
}

