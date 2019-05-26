package com.zxk175.well.common.util;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author zxk175
 */
public class Md5Util {

    public static String md5(InputStream in) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        int len;
        byte[] buffer = new byte[8192];
        while ((len = in.read(buffer)) != -1) {
            md5.update(buffer, 0, len);
        }
        BigInteger bi = new BigInteger(1, md5.digest());
        return bi.toString(16);
    }
}