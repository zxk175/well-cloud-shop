package com.zxk175.well.core.util;

import com.zxk175.well.core.consts.Const;

import java.util.regex.Pattern;

/**
 * @author zxk175
 * @since 2019/03/23 20:59
 */
public class RegexUtil {

    /**
     * 手机号码的有效性验证
     * <p>
     * 中国移动：134,135,136,137,138,139,147,150,151,152,157,158,158,178,182,183,184,187,188,198
     * 中国联通：130,131,132,145,155,156,166,175,176,185,186
     * 中国电信：133,134,149,153,173,177,180,181,189,199
     * 虚拟运营商：170,171
     *
     * @param mobile 手机号
     * @return true or false
     */
    public static boolean isMobile(String mobile) {
        if (MyStrUtil.isBlank(mobile)) {
            return false;
        }

        if (mobile.length() == Const.ELEVEN) {
            String regex = "^(\\+?\\d{2}-?)?1[3456789]\\d{9}$";
            return regexMatch(mobile, regex);
        }

        return false;
    }

    private static boolean regexMatch(String input, String regex) {
        return Pattern.compile(regex).matcher(input).matches();
    }
}
