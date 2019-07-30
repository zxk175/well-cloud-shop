package com.zxk175.well.common.util.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.lang.reflect.Type;

/**
 * @author zxk175
 * @since 2019/03/23 15:32
 */
public class MyToStringSerializer implements ObjectSerializer {

    public static final MyToStringSerializer INSTANCE = new MyToStringSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeNull();
            return;
        }

        // 分页返回total 不转换为String
        if ("page".equals(fieldName) || "size".equals(fieldName) || "total".equals(fieldName) || "pageTotal".equals(fieldName)) {
            out.writeLong((Long) object);
            return;
        }

        String strVal = object.toString();
        out.writeString(strVal);
    }
}
