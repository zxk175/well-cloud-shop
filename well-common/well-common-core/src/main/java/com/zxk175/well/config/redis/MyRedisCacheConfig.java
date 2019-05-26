package com.zxk175.well.config.redis;

import com.zxk175.well.common.util.serializer.FstRedisSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author zxk175
 * @since 2019/04/08 12:09
 */
@Configuration
@EnableCaching
public class MyRedisCacheConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        // 超时时间 单位：秒
        redisCacheConfiguration.entryTtl(Duration.ofSeconds(7200));

        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }


    @Bean
    public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory connectionFactory, FstRedisSerializer fstRedisSerializer) {
        // 关闭共享链接
        connectionFactory.setShareNativeConnection(false);

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        // key序列化方式,但是如果方法上有long等非string类型的话,会报类型转换错误
        // long类型会出现异常信息
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);

        redisTemplate.setValueSerializer(fstRedisSerializer);

        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    FstRedisSerializer fstRedisSerializer() {
        return new FstRedisSerializer();
    }

    /**
     * 自定义key.
     * 此方法将会根据类名+方法名+所有参数的值生成唯一的key,
     * 即使@Cacheable中的value属性一样，key也会不一样。
     *
     * @return ignore
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            String str = target.toString();
            sb.append(str, 0, str.lastIndexOf('@')).append(".");
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }
}
