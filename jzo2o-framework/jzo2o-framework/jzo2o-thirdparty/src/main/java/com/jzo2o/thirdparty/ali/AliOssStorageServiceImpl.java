package com.jzo2o.thirdparty.ali;

import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.thirdparty.core.storage.StorageService;
import com.jzo2o.thirdparty.ali.properties.AliOssProperties;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author itcast
 */
@Slf4j
@Service
@ConditionalOnBean(AliOssProperties.class)
public class AliOssStorageServiceImpl implements StorageService {
    @Autowired
    private AliOssProperties aliOssProperties;

    /**
     * 文件上传
     *
     * @param extension   文件拓展名
     * @param inputStream 文件流
     * @return 文件访问路径
     */
    @Override
    public String upload(String extension, InputStream inputStream) {
        String fileName = UUID.randomUUID() + extension;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret());
        StringBuilder stringBuilder = new StringBuilder("https://");

        try {
            // 创建PutObject请求。
            ossClient.putObject(aliOssProperties.getBucketName(), fileName, inputStream);

            //文件访问路径规则 https://BucketName.Endpoint/ObjectName
            stringBuilder.append(aliOssProperties.getBucketName())
                    .append(".")
                    .append(aliOssProperties.getEndpoint())
                    .append("/")
                    .append(fileName);
        } catch (OSSException oe) {
            log.error("阿里OSS异常，Error Message:{}", oe.getErrorMessage());
        } catch (ClientException ce) {
            log.error("阿里OSS Client异常，Error Message:{}", ce.getMessage());
        } finally {
            if (ObjectUtil.isNotEmpty(ossClient)) {
                ossClient.shutdown();
            }
        }

        return stringBuilder.toString();
    }
}
