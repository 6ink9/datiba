package com.datiba.config;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/28
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS全局配置类
 */
@Configuration
public class ProviderMvcConfig implements WebMvcConfigurer {

    @SuppressWarnings("AlibabaAvoidCommentBehindStatement")
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  //设置项目中允许跨域访问的接口，*表示项目中的所有接口都支持跨域
                .allowedOriginPatterns("*") // 支持域
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(6000)
                .allowedHeaders("*"); //允许所有的请求header访问，可以自定义设置任意请求头信息

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 registration 拦截器
        InterceptorRegistration registration = registry.addInterceptor(new UserLoginInterceptor());

        // 拦截所有的路径
        registration.addPathPatterns("/**");

        // 添加不拦截路径 /api/user/login 是登录的请求, /api/user/register 注册的请求
        registration.excludePathPatterns(
                "/user/login",
                "/user/sign",
                "/doc.html/**",
                "/v3/**"
        );
    }
}

