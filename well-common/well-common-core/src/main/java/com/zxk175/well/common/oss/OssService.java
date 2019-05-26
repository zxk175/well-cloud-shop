package com.zxk175.well.common.oss;

import com.github.sd4324530.jtuple.Tuple2;

import java.io.InputStream;

/**
 * @author zxk175
 * @since 2019/05/06 09:29
 */
public abstract class OssService {

    String bucketName;
    String baseUrl;
    boolean isTest;

    public abstract Tuple2<Boolean, String> upload(InputStream inputStream, String dir, String ext, String newName, String oldName) throws Exception;

    public abstract void removeFile(String bucketName, String diskName, String key);

    public abstract void removeBatch(String dir);

    public abstract boolean objectExist(String fullPath);

    public String getBaseUrl() {
        return baseUrl;
    }
}
