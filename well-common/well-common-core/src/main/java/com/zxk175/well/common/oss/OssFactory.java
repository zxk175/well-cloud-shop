package com.zxk175.well.common.oss;

/**
 * @author zxk175
 * @since 2019/05/06 10:30
 */
public class OssFactory {

    public static OssService build() {
        return new AliYunOssService();
    }
}
