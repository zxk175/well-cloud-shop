package com.zxk175.well.base.util.json.serializer;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.lang.reflect.Type;

/**
 * @author zxk175
 * @since 2019-03-23 15:32
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
        String page = "page";
        String size = "size";
        String total = "total";
        String pageTotal = "pageTotal";
        if (page.equals(fieldName) || size.equals(fieldName) || total.equals(fieldName) || pageTotal.equals(fieldName)) {
            out.writeLong(Convert.toLong(object));
            return;
        }

        out.writeString(object.toString());
    }
}
