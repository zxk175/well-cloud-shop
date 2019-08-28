package com.zxk175.well.core.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author zxk175
 * @since 2018/8/21 18:27
 */
public class MyStrUtil {

    public static final String UNDERLINE = "_";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String SLASH = "/";
    public static final String EMPTY = "";
    public static final String DOT = ".";


    public static boolean isBlank(CharSequence str) {
        if (StrUtil.isBlank(str)) {
            return true;
        }

        final String nullStr = "null";
        final String undefinedStr = "undefined";
        return MyStrUtil.eqIgnoreCase(nullStr, str) || MyStrUtil.eqIgnoreCase(undefinedStr, str);
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static boolean eq(CharSequence one, CharSequence two) {
        return StrUtil.equals(one, two);
    }

    public static boolean ne(CharSequence one, CharSequence two) {
        return !eq(one, two);
    }

    public static boolean neIgnoreCase(CharSequence one, CharSequence two) {
        return !eqIgnoreCase(one, two);
    }

    public static boolean eqIgnoreCase(CharSequence one, CharSequence two) {
        return StrUtil.equals(one, two, true);
    }

    private static boolean eqEndWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return StrUtil.endWithIgnoreCase(str, suffix);
    }

    public static boolean neEndWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return !eqEndWithIgnoreCase(str, suffix);
    }

    public static String format(CharSequence template, Object... params) {
        return StrUtil.format(template, params);
    }
}
