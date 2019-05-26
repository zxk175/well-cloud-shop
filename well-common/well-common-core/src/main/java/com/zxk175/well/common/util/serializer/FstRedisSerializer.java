package com.zxk175.well.common.util.serializer;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author zxk175
 * @since 2019/03/23 15:55
 */
public class FstRedisSerializer implements RedisSerializer<Object> {

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (ObjectUtil.isNull(t)) {
            return new byte[0];
        }
        return SerializationUtil.serialize(t);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        return SerializationUtil.deserialize(bytes);
    }
}
