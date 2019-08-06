package com.zxk175.well.common.oss;

import cn.hutool.core.convert.Convert;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.github.sd4324530.jtuple.Tuple2;
import com.github.sd4324530.jtuple.Tuples;
import com.zxk175.well.common.consts.AliConst;
import com.zxk175.well.common.consts.Const;
import com.zxk175.well.common.util.MyStrUtil;
import com.zxk175.well.common.util.spring.SpringActiveUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author zxk175
 * @since 2019/05/06 09:29
 */
@Slf4j
public class AliYunOssService extends OssService {

    private OSSClient ossClient;


    AliYunOssService() {
        ClientConfiguration configuration = new ClientConfiguration();
        // 设置失败请求重试次数，默认为3次
        configuration.setMaxErrorRetry(5);
        // 设置ossClient允许打开的最大HTTP连接数，默认为1024个
        configuration.setMaxConnections(600);
        // 设置socket层传输数据的超时时间，默认为50000毫秒
        configuration.setSocketTimeout(AliConst.TIMEOUT);
        // 设置建立连接的超时时间，默认为50000毫秒
        configuration.setConnectionTimeout(AliConst.TIMEOUT);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时
        configuration.setConnectionRequestTimeout(AliConst.TIMEOUT);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒
        configuration.setIdleConnectionTime(AliConst.TIMEOUT);

        this.isTest = SpringActiveUtil.isDebug();

        Tuple2<String, String> tuple = getBucketNameAndBaseUrl();
        this.bucketName = tuple.first;
        this.baseUrl = tuple.second;

        DefaultCredentialProvider credentialProvider = new DefaultCredentialProvider(AliConst.ALI_KEY, AliConst.ALI_SECRET);
        ossClient = new OSSClient(isTest ? AliConst.OUT_END_POINT : AliConst.IN_END_POINT, credentialProvider, configuration);
    }

    @Override
    public Tuple2<Boolean, String> upload(InputStream inputStream, String dir, String ext, String newName, String oldName) throws Exception {
        ObjectMetadata meta = new ObjectMetadata();
        long fileSize = inputStream.available();
        meta.setContentLength(fileSize);
        meta.setCacheControl("no-cache");
        meta.setHeader("Pragma", "no-cache");
        meta.setContentEncoding(Const.UTF_8);
        meta.setContentDisposition("fileName/fileSize=" + newName + MyStrUtil.SLASH + Convert.toStr(fileSize) + "Byte");

        // 上传Object
        String fullPath = dir + newName + MyStrUtil.DOT + ext;
        ossClient.putObject(bucketName, fullPath, inputStream, meta);

        // 删除旧文件
        if (MyStrUtil.isNotBlank(oldName) && MyStrUtil.ne(newName, oldName)) {
            removeFile(bucketName, dir, oldName + MyStrUtil.DOT + ext);
        }

        return Tuples.tuple(true, baseUrl + fullPath);
    }

    @Override
    public void removeFile(String bucketName, String diskName, String key) {
        String filePath = diskName + key;
        ossClient.deleteObject(bucketName, filePath);

        log.info("删除【" + bucketName + "】下的文件【" + filePath + "】成功");
    }

    @Override
    public void removeBatch(String dir) {
        ObjectListing objectListing = ossClient.listObjects(bucketName, dir);

        objectListing.getObjectSummaries().forEach(ossObjectSummary -> {
            // 删除操作
            ossClient.deleteObject(bucketName, ossObjectSummary.getKey());
        });

        log.info("删除【" + bucketName + "】下的文件夹【" + dir + "】成功");
    }

    @Override
    public boolean objectExist(String fullPath) {
        // Object是否存在
        boolean found;
        Tuple2<String, String> tuple = getBucketNameAndBaseUrl();
        String bucketName = tuple.first;

        try {
            found = ossClient.doesObjectExist(bucketName, fullPath);
        } catch (ClientException | OSSException e) {
            e.printStackTrace();
            found = false;
        }

        log.info("【" + bucketName + "】中【" + fullPath + (found ? "】已存在" : "】不存在"));
        return found;
    }

    private Tuple2<String, String> getBucketNameAndBaseUrl() {
        return Tuples.tuple(AliConst.BUCKET_NAME, AliConst.BASE_URL);
    }
}
