package com.zxk175.well.common.util.serializer;

import org.nustaq.serialization.FSTConfiguration;

/**
 * @author zxk175
 * @since 2019/03/23 15:55
 */
public class SerializationUtil {

    private static FSTConfiguration configuration = FSTConfiguration.createDefaultConfiguration();


    public static byte[] serialize(Object object) {
        return configuration.asByteArray(object);
    }

    public static Object deserialize(byte[] bytes) {
        return configuration.asObject(bytes);
    }
}
