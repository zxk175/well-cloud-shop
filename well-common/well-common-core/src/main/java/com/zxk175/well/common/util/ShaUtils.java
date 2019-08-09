package com.zxk175.well.common.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.github.sd4324530.jtuple.Tuple2;
import com.github.sd4324530.jtuple.Tuples;
import com.zxk175.well.common.consts.Const;

/**
 * @author zxk175
 * @since 2019/04/13 16:04
 */
public class ShaUtils {

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
