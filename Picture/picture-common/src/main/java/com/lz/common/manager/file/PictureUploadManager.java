package com.lz.common.manager.file;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import com.lz.common.config.OssConfig;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.file.FileTypeUtils;
import com.lz.common.utils.uuid.IdUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Project: Picture
 * Package: com.lz.common.manage.file
 * Author: YY
 * CreateTime: 2025-03-27  10:48
 * Description: PictureUploadManager
 * 图片上传
 * Version: 1.0
 */
@Component
public class PictureUploadManager {
    @Resource
    private OssConfig ossConfig;


    /**
     * description: 初始化oss客户端
     * author: YY
     * method: initOssClient
     * date: 2025/3/27 11:02
     * param:
     * return: void
     **/
    @PostConstruct
    public OSS initOssClient() {
        return new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
    }

    /**
     * description: 上传图片
     * author: YY
     * method: uploadPicture
     * date: 2025/3/27 11:03
     * param:
     * param: fileName
     * param: fileBytes
     * return: java.lang.String
     **/
    public String uploadPicture(String fileName, File file) {
        //获取文件信息 文件为图片
        if (StringUtils.isNull(file)) {
            return "";
        }
        String[] split = fileName.split("\\.");
        fileName = split[0] + "-" + IdUtils.snowflakeId() + "." + split[1];
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        String filePath = ossConfig.getDir() + "/" + fileName;
        PutObjectResult putObjectResult = ossClient.putObject(ossConfig.getBucket(), filePath, file);
        System.out.println("putObjectResult = " + JSONObject.toJSONString(putObjectResult));
        try {
            EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
            // 获取图片信息。
            String style = "image/info";
            // 指定签名URL过期时间为10分钟。
            Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(ossConfig.getBucket(), fileName, HttpMethod.GET);
            req.setExpiration(expiration);
            req.setProcess(style);
            URL signedUrl = ossClient.generatePresignedUrl(req);
            System.out.println(signedUrl);
            System.out.println("req = " + JSONObject.toJSONString(req));
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
