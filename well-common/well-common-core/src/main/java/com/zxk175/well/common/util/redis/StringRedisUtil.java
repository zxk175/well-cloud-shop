package com.zxk175.well.common.util.redis;

import cn.hutool.core.convert.Convert;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.MyStrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zxk175
 * @since 2018/8/21 18:27
 */
@Component
public class StringRedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public boolean ping() {
        final Object execute = stringRedisTemplate.execute(RedisConnectionCommands::ping);

        return MyStrUtil.eqIgnoreCase("pong", Convert.toStr(execute));
    }

    public boolean flushRedis() {
        final Object execute = stringRedisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushAll();
            return Const.OK;
        });

        return Const.OK.equals(Convert.toStr(execute));
    }

    /**
     * 写入缓存
     *
     * @param key    ignore
     * @param value  ignore
     * @param expire 过期时间 单位：秒
     */
    public void set(final String key, final String value, final Long expire) {
        stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 读取缓存
     *
     * @param key ignore
     * @return ignore
     */
    public String get(final String key) {
        return stringRedisTemplate.boundValueOps(key).get();
    }

    /**
     * 递增操作
     *
     * @param key ignore
     * @param val ignore
     * @return ignore
     */
    public Long increment(final String key, final Long val) throws Exception {
        return stringRedisTemplate.boundValueOps(key).increment(val);
    }

    /**
     * 递减操作
     *
     * @param key ignore
     * @param val ignore
     * @return ignore
     */
    public Long decrement(final String key, final Long val) throws Exception {
        return stringRedisTemplate.boundValueOps(key).increment(-val);
    }

    /**
     * 设置key过期时间
     *
     * @param key      ignore
     * @param time     ignore
     * @param timeUnit ignore
     * @return ignore
     */
    public Boolean expire(final String key, final Long time, final TimeUnit timeUnit) {
        return stringRedisTemplate.expire(key, time, timeUnit);
    }
}
