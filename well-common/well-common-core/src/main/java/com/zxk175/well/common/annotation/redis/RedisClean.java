package com.zxk175.well.common.annotation.redis;

import java.lang.annotation.*;

/**
 * @author zxk175
 * @since 2019/04/13 18:02
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisClean {
    
    // 操作描述
    String value() default "";
}

