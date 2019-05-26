package com.zxk175.well.common.util;

import cn.hutool.core.date.DateTime;
import com.zxk175.well.common.consts.Const;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zxk175
 * @since 17/10/19 16:46
 */
public class DateUtil {

    private static final String FORMAT_DEFAULT = Const.DEFAULT_DATE_FORMAT;


    public static String getNow(String format) {
        if (MyStrUtil.isBlank(format)) {
            format = FORMAT_DEFAULT;
        }
        return new DateTime().toString(format);
    }

    public static Date str2Date(String date, String format) throws Exception {
        if (MyStrUtil.isBlank(format)) {
            format = FORMAT_DEFAULT;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    public static String formatDate(Date date, String format) {
        if (MyStrUtil.isBlank(format)) {
            format = FORMAT_DEFAULT;
        }
        return new DateTime(date).toString(format);
    }
}
