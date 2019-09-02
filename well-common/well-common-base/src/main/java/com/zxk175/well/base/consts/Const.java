package com.zxk175.well.base.consts;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.zxk175.well.base.util.json.serializer.MyLocalDateFormatSerializer;
import com.zxk175.well.base.util.json.serializer.MyLocalDateTimeFormatSerializer;
import com.zxk175.well.base.util.json.serializer.MyToStringSerializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author zxk175
 * @since 2019-08-11 00:43
 */
public class Const {

    public static final String UTF_8 = StandardCharsets.UTF_8.name();
    public static final Charset UTF_8_OBJ = StandardCharsets.UTF_8;

    public static final String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_CN = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String DATE1_FORMAT_DEFAULT = "yyyy-MM-dd";
    public static final String DATE2_FORMAT_DEFAULT = "yyyyMMdd";

    public static final String FORMAT6 = "===> ";
    public static final String LOG_PREFIX = FORMAT6;

    public static final int OK_CODE = 0;
    public static final int FAIL_CODE = -1;
    public static final String OK_TXT = "请求成功";
    public static final String FAIL_TXT = "请求失败";

    public static final String DB_ID = "id";
    public static final String DB_DELETED = "deleted";
    public static final String DB_ENABLED = "enabled";

    public static final Integer ZERO = 0;
    public static final Integer ONE = 1;
    public static final Integer TWO = 2;
    public static final Integer THREE = 3;
    public static final Integer FOUR = 4;
    public static final Integer FIVE = 5;
    public static final Integer ELEVEN = 11;

    public static final String SCAN_CORE = "com.zxk175.well.core";
    public static final String SCAN_PROVIDER = "com.zxk175.well.provider";


    public static SerializerFeature[] serializerFeatures() {
        return new SerializerFeature[]{
                // 输出key时是否使用双引号,默认为true
                SerializerFeature.QuoteFieldNames,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect,
                // 是否输出值为null的字段
                SerializerFeature.WriteMapNullValue,
                // 字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteNullStringAsEmpty,
                // list字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,
                // boolean字段如果为null,输出为false,而非null
                SerializerFeature.WriteNullBooleanAsFalse,
                // 设置使用文本方式输出日期，fastJson默认是long
                SerializerFeature.WriteDateUseDateFormat,
                // 将key不是String类型转为String
                SerializerFeature.WriteNonStringKeyAsString,
                // 忽略那些抛异常的get方法
                SerializerFeature.IgnoreErrorGetter,
                // 启用对'<'和'>'的处理方式
                SerializerFeature.BrowserSecure
        };
    }

    public static SerializeConfig serializeConfig() {
        SerializeConfig config = new SerializeConfig();
        config.put(Long.TYPE, MyToStringSerializer.INSTANCE);
        config.put(Long.class, MyToStringSerializer.INSTANCE);
        config.put(java.sql.Date.class, new SimpleDateFormatSerializer(Const.DATE_TIME_FORMAT_DEFAULT));
        config.put(java.util.Date.class, new SimpleDateFormatSerializer(Const.DATE_TIME_FORMAT_DEFAULT));
        config.put(java.time.LocalDate.class, new MyLocalDateFormatSerializer(Const.DATE1_FORMAT_DEFAULT));
        config.put(java.time.LocalDateTime.class, new MyLocalDateTimeFormatSerializer(Const.DATE_TIME_FORMAT_DEFAULT));

        return config;
    }
}
