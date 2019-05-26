package com.zxk175.well.common.aspect.redis;

import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.http.Response;
import com.zxk175.well.common.util.redis.StringRedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author zxk175
 * @since 2019/05/18 09:49
 */
@Aspect
@Component
@Order(9996)
public class RedisCleanAop {

    @Autowired
    private StringRedisUtil stringRedisUtil;


    @Around(value = "@annotation(com.zxk175.well.common.annotation.redis.RedisClean)")
    public Object after(ProceedingJoinPoint joinPoint) throws Throwable {
        Response response = (Response) joinPoint.proceed();
        if (response.getSuccess()) {
            boolean ok = stringRedisUtil.flushRedis();

            if (ok) {
                return Response.ok(Const.OK_CODE, "Redis缓存清除成功");
            }

            return Response.ok(Const.FAIL_CODE, "Redis缓存清除失败");
        }

        return response;
    }
}