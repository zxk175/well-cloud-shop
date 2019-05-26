package com.zxk175.well.common.annotation.request;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zxk175
 * @since 2019/04/13 18:02
 */
@Component
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestDelay {

    // 单位：毫秒 默认2秒
    int time() default 2000;
}
