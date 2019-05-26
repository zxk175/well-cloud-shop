package com.zxk175.well.config.http;

import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * @author zxk175
 * @since 2018/5/31 20:24
 */
@ControllerAdvice
public class MyRequestBodyAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(@Nullable MethodParameter methodParameter, @Nullable Type targetType, @Nullable Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
}
