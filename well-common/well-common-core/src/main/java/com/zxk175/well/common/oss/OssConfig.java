package com.zxk175.well.common.oss;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zxk175
 * @since 2019/05/06 09:29
 */
@Data
class OssConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    // 阿里云绑定的域名
    private String aliYunDomain;

    // 阿里云路径前缀
    private String aliYunPrefix;

    // 阿里云内网EndPoint
    private String aliYunInEndPoint;

    // 阿里云外网EndPoint
    private String aliYunOutEndPoint;

    // 阿里云AccessKeyId
    private String aliYunAccessKeyId;

    // 阿里云AccessKeySecret
    private String aliYunAccessKeySecret;

    // 阿里云BucketName
    private String aliYunBucketName;
}
