package com.zxk175.well.base.util.json.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author zxk175
 * @since 2019-03-23 15:27
 */
public class MyLocalDateFormatSerializer implements ObjectSerializer {

    private final String pattern;

    public MyLocalDateFormatSerializer(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        if (object == null) {
            serializer.out.writeNull();
            return;
        }

        LocalDate localDate = (LocalDate) object;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        String text = dateTimeFormatter.format(localDate);

        serializer.write(text);
    }
}
