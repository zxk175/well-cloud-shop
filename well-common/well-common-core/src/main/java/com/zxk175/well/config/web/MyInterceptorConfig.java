package com.zxk175.well.config.web;

import com.zxk175.well.config.interceptor.CorsAllowInterceptor;
import com.zxk175.well.config.interceptor.RequestDelayInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zxk175
 * @since 2019/03/28 14:46
 */
@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 跨域拦截器
        registry.addInterceptor(new CorsAllowInterceptor()).addPathPatterns("/**");

        // 限制请求频率拦截器
        registry.addInterceptor(new RequestDelayInterceptor()).addPathPatterns("/**");
    }
}
