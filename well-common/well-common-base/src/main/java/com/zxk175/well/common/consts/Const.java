package com.zxk175.well.common.consts;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.zxk175.well.common.util.json.MyLocalDateFormatSerializer;
import com.zxk175.well.common.util.json.MyToStringSerializer;

import java.nio.charset.Charset;

/**
 * @author zxk175
 * @since 2019/03/14 10:26
 */
public class Const {

    public static final String UTF_8 = "UTF-8";
    public static final Charset UTF_8_OBJ = Charset.forName(UTF_8);
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    public static final String DEFAULT_UID = "865545599049039872";
    public static final String DEFAULT_MOBILE = "18820216402";
    public static final String TOKEN_DESC_KEY = "GQ0A0k1DdsJ1XcJiDPg5M235vjiODPHcJ1XT0uXOYHjiXHj10NHwDJ1iwDvViwDrk";
    public static final String TOKEN_KEY = "token";
    public static final String BASE_URL = "";

    public static final String DEV = "dev";
    public static final String TEST = "test";

    public static final String WE_CHAT_MSG_URL = "https://well.zxk175.com/notify/send/v1";
    public static final String MSG_KEY = "1129650133014663170";

    public static final long TOKEN_TTL = 7200L * 1000;

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_NO_TIME = "yyyyMMdd";
    public static final String DATE_FORMAT_CN = "yyyy年MM月dd日 HH时mm分ss秒";

    public static final String FORMAT1 = "\n\n### ";
    public static final String FORMAT2 = "\n\n- ";
    public static final String FORMAT3 = "\n\n";
    public static final String FORMAT4 = "\n```\n";
    public static final String FORMAT5 = "\n```";

    public static final int OK_CODE = 0;
    public static final int FAIL_CODE = -1;
    public static final String OK_TXT = "请求成功";
    public static final String FAIL_TXT = "请求失败";

    public static final String DB_USER_ID = "user_id";
    public static final String DB_STATE = "state";
    public static final String DB_SORT = "sort";
    public static final String OK = "OK";
    public static final int DB_BATCH_SIZE = 10;

    public static final int LIMIT_45 = 45;
    public static final int LIMIT_30 = 30;
    public static final int LIMIT_20 = 20;
    public static final int LIMIT_10 = 10;
    public static final int LIMIT_5 = 5;
    public static final int LIMIT_1 = 1;

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int ELEVEN = 11;

    public static final String ZERO_STR = "0";

    public static final String MY_HOST = "MyHost";

    public static final String IPV6_LOCAL = "0:0:0:0:0:0:0:1";

    public static SerializerFeature[] serializerFeatures() {
        return new SerializerFeature[]{
                // 输出key时是否使用双引号,默认为true
                SerializerFeature.QuoteFieldNames,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect,
                // 是否输出值为null的字段
                SerializerFeature.WriteMapNullValue,
                // 数值字段如果为null,输出为0,而非null
                SerializerFeature.WriteNullNumberAsZero,
                // 字符类型字段如果为null,输出为"",而非null
                SerializerFeature.WriteNullStringAsEmpty,
                // list字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,
                // boolean字段如果为null,输出为false,而非null
                SerializerFeature.WriteNullBooleanAsFalse,
                // 设置使用文本方式输出日期，fastJson默认是long
                SerializerFeature.WriteDateUseDateFormat,
                // 兼容IE6 即将中文转为Unicode编码
                //SerializerFeature.BrowserCompatible,
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
        config.put(java.util.Date.class, new SimpleDateFormatSerializer(Const.DEFAULT_DATE_FORMAT));
        config.put(java.sql.Date.class, new SimpleDateFormatSerializer(Const.DEFAULT_DATE_FORMAT));
        config.put(java.time.LocalDateTime.class, new MyLocalDateFormatSerializer(Const.DEFAULT_DATE_FORMAT));
        config.put(Long.class, MyToStringSerializer.INSTANCE);
        config.put(Long.TYPE, MyToStringSerializer.INSTANCE);

        return config;
    }
}
