package com.zxk175.well.common.util;

import cn.hutool.core.convert.Convert;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zxk175
 * @since 2019/04/17 17:11
 * <p>
 * https://github.com/jooyu/invitation-code
 * https://blog.csdn.net/coolybq/article/details/49512047
 * https://blog.csdn.net/blogliang/article/details/53898226
 */
public class ShareCodeUtil {

    // code最小长度
    private static final int minLen = 6;
    // 自定义进制(0,1没有加入,容易与o,l混淆)
    private static char[] charArr = new char[]{'Q', 'w', 'E', '8', 'a', 'S', '2', 'd', 'Z', 'x', '9', 'c', '7', 'p', 'O', '5', 'i', 'K', '3', 'm', 'j', 'U', 'f', 'r', '4', 'V', 'y', 'L', 't', 'N', '6', 'b', 'g', 'H'};
    // 进制长度
    private static int binLen = charArr.length;
    // 自动补全组(不能与自定义进制有重复)
    private static char[] completeArr = new char[]{'q', 'W', 'e', 'A', 's', 'D', 'z', 'X', 'C', 'P', 'o', 'I', 'k', 'M', 'J', 'u', 'F', 'R', 'v', 'Y', 'T', 'n', 'B', 'G', 'h'};


    public static String code(String userId) {
        Long userIdL = Convert.toLong(userId);
        return code(userIdL);
    }

    public static String code(Long userId) {
        char[] buf = new char[32];
        int charPos = 32;

        while ((userId / binLen) > 0) {
            int ind = (int) (userId % binLen);
            buf[--charPos] = charArr[ind];
            userId /= binLen;
        }

        buf[--charPos] = charArr[(int) (userId % binLen)];
        String str = new String(buf, charPos, (32 - charPos));

        // 不够长度的自动随机补全
        if (str.length() < minLen) {
            StringBuilder sb = new StringBuilder();
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int length = minLen - str.length();
            for (int i = 1; i < length; i++) {
                sb.append(completeArr[random.nextInt(binLen)]);
            }
            str += sb.toString();
        }
        return str;
    }

    public static String id2Str(String code) {
        return Convert.toStr(id2Long(code));
    }

    public static long id2Long(String code) {
        long result = 0L;
        char[] charArray = code.toCharArray();
        int length = charArray.length;

        for (int i = 0; i < length; i++) {
            int index = 0;
            for (int j = 0; j < binLen; j++) {
                if (charArray[i] == charArr[j]) {
                    index = j;
                    break;
                }
            }

            if (charArray[i] == completeArr[i]) {
                break;
            }

            if (i > 0) {
                result = result * binLen + index;
            } else {
                result = index;
            }
        }

        return result;
    }
}