package com.lz.common.manager.file;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.lz.common.config.OssConfig;
import com.lz.common.manager.file.model.BatchDownloadFileDto;
import com.lz.common.utils.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * 图片下载
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-25  16:00
 * @Version: 1.0
 */
@Component
@Slf4j
public class PictureDownloadManager {
    @Resource
    private OssConfig ossConfig;

    @PostConstruct
    public OSS initOssClient() {
        return new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
    }

    /**
     * 生成图片下载url
     *
     * @param filePath
     * @return
     */
    public String generateDownloadUrl(String filePath, Long expireTime) {
        // 1. 使用你的 CNAME 域名作为 endpoint，如 cdn.example.com
        String cnameEndpoint = OssConfig.getDnsUrl(); // 你配置的 CNAME，比如 https://cdn.xxx.com

        // 2. 启用 CNAME 支持
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setSupportCname(true); // 关键配置

        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(
                    cnameEndpoint, // 这里传的是 CNAME 域名，不是原始 endpoint
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret(),
                    conf
            );

            String objectKey = extractKeyFromPath(filePath);
            Date expiration = new Date(System.currentTimeMillis() + expireTime * 60 * 1000);

            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(ossConfig.getBucket(), objectKey, HttpMethod.GET);
            req.setExpiration(expiration);

            URL url = ossClient.generatePresignedUrl(req);
            return url.toString();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 格式化路径，将 URL 转换为 OSS 的 key
     *
     * @param path
     * @return
     */
    private String extractKeyFromPath(String path) {
        try {
            if (path.startsWith("http")) {
                // 是 URL，比如 https://bucket-name.oss-cn-region.aliyuncs.com/picture/xxx.jpg
                URL url = new URL(path);
                return url.getPath().substring(1); // 去掉前面的 "/"
            }

            if (path.startsWith(ossConfig.getBucket())) {
                // 是以桶开头的路径，比如 litchi-picture/picture/xxx.jpg
                return path.substring(ossConfig.getBucket().length() + 1); // 去掉桶名和斜杠
            }
            if (path.startsWith("/")) {
                //去掉"/"
                return path.substring(1);
            }
            // 否则就是正常的 key，比如 picture/xxx.jpg
            return path;
        } catch (Exception e) {
            log.warn("路径解析失败，默认按原始 key 处理：{}", path);
            return path;
        }
    }

    /**
     * 批量下载文件
     *
     * @param files
     */
    public void downloadFile(List<BatchDownloadFileDto> files) {
        // 使用默认凭证提供者
        DefaultCredentialProvider credentialsProvider = new DefaultCredentialProvider(
                ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());

        // 创建OSSClient实例
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(ossConfig.getEndpoint())
                .credentialsProvider(credentialsProvider)
                .region(ossConfig.getRegion())
                .build();

        try {
            // 下载Object到本地文件，并保存到指定的本地路径中。如果指定的本地文件存在会覆盖，不存在则新建。
            // 如果未指定本地路径，则下载后的文件默认保存到示例程序所属项目对应本地路径中。
            for (BatchDownloadFileDto file : files) {
                ossDownloadFile(ossClient, ossConfig.getBucket(), file.getOssFilePath(), file.getLocalPath());
            }
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            throw new RuntimeException("文件下载异常,请检查OSS是否配置正确");
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 下载文件
     *
     * @param ossClient  OSSClient
     * @param bucketName 存储空间名称
     * @param objectName OSS对象名称
     * @param pathName   本地路径
     */
    private static void ossDownloadFile(OSS ossClient, String bucketName, String objectName, String pathName) {
        try {
            // 确保本地文件的父目录存在
            File localFile = new File(pathName);
            File parentDir = localFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            //去除OSS对象名称开头的斜杠
            if (StringUtils.isNotEmpty(objectName) && objectName.startsWith("/")) {
                objectName = objectName.substring(1);
            }
            ossClient.getObject(new GetObjectRequest(bucketName, objectName), localFile);
        } catch (Exception e) {
            log.error("文件下载失败: bucket={}, object={}, path={}", bucketName, objectName, pathName, e);
            throw e;
        }
    }
}
