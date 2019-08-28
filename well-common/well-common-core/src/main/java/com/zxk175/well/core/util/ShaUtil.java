package com.zxk175.well.core.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.zxk175.well.base.consts.Const;
import com.zxk175.well.base.util.tuple.Tuple2;
import com.zxk175.well.base.util.tuple.Tuples;

/**
 * @author zxk175
 * @since 2019-08-28 11:47
 */
public class ShaUtil {

    public static Tuple2<String, String> enc(String data) {
        String salt = RandomUtil.randomString(20);
        return enc(data, salt);
    }

    public static Tuple2<String, String> enc(String data, String salt) {
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        sha256.setSalt(salt.getBytes(Const.UTF_8_OBJ));

        return Tuples.tuple(sha256.digestHex(data), salt);
    }
}
