package com.lz.common.manager.file;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.lz.common.config.OssConfig;
import com.lz.common.manager.file.model.Exif;
import com.lz.common.manager.file.model.PictureResponse;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.file.FileUtils;
import com.lz.common.utils.http.HttpUtils;
import com.lz.common.utils.uuid.IdUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
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
        if (file == null || StringUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("文件或文件名不能为空");
        }

        // 生成唯一文件名
        String nameNotSuffix = FileUtils.getNameNotSuffix(file.getName());
        String suffix = FileUtil.getSuffix(fileName);
        Long snowflaked = IdUtils.snowflakeId();
        String newFileName = nameNotSuffix + "-" + snowflaked + "." + suffix;
        String filePath = ossConfig.getDir() + "/" + newFileName;
        String compressedSuffix = "-compressed.webp";
        String compressedFileName = nameNotSuffix + "-" + snowflaked + compressedSuffix;
        String compressedFilePath = ossConfig.getDir() + "/" + compressedFileName;

        OSS ossClient = null;
        InputStream inputStream = null;
        try {
            // 初始化OSS客户端
            ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret()
            );

            // 上传原始文件
            PutObjectResult putObjectResult = ossClient.putObject(ossConfig.getBucket(), filePath, file);
            System.out.println("原图上传成功：" + filePath);
            System.out.println("putObjectResult = " + JSONObject.toJSONString(putObjectResult));

            // 生成获取图片信息的预签名URL（包含目录路径）
            String style = "image/info";
            Date expiration = new Date(System.currentTimeMillis() + 600_000); // 10分钟有效期
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(
                    ossConfig.getBucket(),
                    filePath,  // 使用完整路径
                    HttpMethod.GET
            );
            req.setExpiration(expiration);
            req.setProcess(style);
            URL signedUrl = ossClient.generatePresignedUrl(req);
            System.out.println("图片信息URL：" + signedUrl);

            // 发送HTTP请求获取图片信息
            String imageInfo = HttpUtils.sendGet(signedUrl.toString());
            System.out.println("图片元数据：" + imageInfo);
            Exif exif = JSONObject.parseObject(imageInfo, Exif.class);
            System.out.println("exif = " + exif);
            //设置图片信息
            PictureResponse pictureResponse = new PictureResponse();
            String pictureUrl = signedUrl.toString().split("\\?")[0];
            pictureResponse.setPictureUrl(pictureUrl);
            pictureResponse.setName(nameNotSuffix);
            pictureResponse.setPicSize(Long.parseLong(exif.getFileSize().getValue()));
            long picWidth = Long.parseLong(exif.getImageWidth().getValue());
            pictureResponse.setPicWidth(picWidth);
            pictureResponse.setPicHeight(Long.parseLong(exif.getImageHeight().getValue()));
            pictureResponse.setPicScale(picWidth / Long.parseLong(exif.getImageHeight().getValue()));
            pictureResponse.setPicFormat(suffix);
            int limit = pictureUrl.lastIndexOf(".");
            pictureResponse.setThumbnailUrl(pictureUrl.substring(0, limit) + compressedSuffix);
            System.out.println("limit = " + pictureResponse);

            // 设置图片处理参数（转换为 WebP 格式）并携带水印
            long fontSize = picWidth / 30;
            String encodeToString = Base64.getEncoder().encodeToString("荔枝云图库".getBytes());
            String process = "image/format,webp/watermark,text_" + encodeToString
                    + ",size_" + fontSize
                    + ",shadow_50"
                    + ",color_FFFFFF,y_10,type_ZHJvaWRzYW5zZmFsbGJhY2s,g_south";
            // 创建获取压缩后图片的预签名URL
            req.setExpiration(expiration);
            System.out.println("图片信息URL：" + signedUrl);
            req.setProcess(process);
            URL compressedUrl = ossClient.generatePresignedUrl(req);
            System.out.println("压缩图URL：" + compressedUrl);
            // 获取压缩后的图片输入流
            HttpURLConnection connection = (HttpURLConnection) compressedUrl.openConnection();
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();
            // 将压缩图上传到OSS
            PutObjectResult compressedPutObjectResult = ossClient.putObject(ossConfig.getBucket(), compressedFilePath, inputStream);
            System.out.println("压缩图上传成功：" + compressedFilePath);
            System.out.println("compressedPutObjectResult = " + JSONObject.toJSONString(compressedPutObjectResult));
            // 返回文件访问路径或URL
            return compressedUrl.toString().split("\\?")[0];

        } catch (Exception e) {
            // 记录详细日志
            System.err.println("上传失败：" + e.getMessage());
            throw new RuntimeException("文件上传异常", e);
        } finally {
            // 确保关闭OSSClient
            if (ossClient != null) {
                ossClient.shutdown();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
