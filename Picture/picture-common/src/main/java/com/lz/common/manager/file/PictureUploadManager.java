package com.lz.common.manager.file;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.lz.common.config.OssConfig;
import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.manager.file.model.Exif;
import com.lz.common.manager.file.model.PictureResponse;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.file.FileUtils;
import com.lz.common.utils.http.HttpUtils;
import com.lz.common.utils.uuid.IdUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import static com.lz.common.constant.ConfigConstants.*;

/**
 * Project: Picture
 * Package: com.lz.common.manage.file
 * Author: YY
 * CreateTime: 2025-03-27  10:48
 * Description: PictureUploadManager
 * 图片上传
 * Version: 1.0
 */
@Slf4j
@Component
public class PictureUploadManager {
    @Resource
    private OssConfig ossConfig;

    @Resource
    private RedisCache redisCache;

    /**
     * 默认50m
     */
    public static Long DEFAULT_FONT_SIZE = 1024L * 1024L * 50;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png", "webp"
    };

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
    public PictureResponse uploadPicture(MultipartFile multipartFile, LoginUserInfo loginUser) {
        //创建临时文件
        File file = null;
        // 生成唯一文件名
        String nameNotSuffix = FileUtils.getNameNotSuffix(multipartFile.getOriginalFilename());
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        Long snowflaked = IdUtils.snowflakeId();
        String newFileName = nameNotSuffix + "-" + snowflaked + "." + suffix;
        String dir = ossConfig.getDir();
        String filePath = dir + "/" + newFileName;
        //压缩文件信息
        String compressedSuffix = "-compressed.webp";
        String compressedFileName = nameNotSuffix + "-" + snowflaked + compressedSuffix;
        String compressedFilePath = dir + "/" + compressedFileName;
        try {
            file = File.createTempFile(newFileName, suffix);
            multipartFile.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //校验文件
        validateFile(file);

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
//            System.out.println("原图上传成功：" + filePath);
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
//            System.out.println("图片信息URL：" + signedUrl);

            // 发送HTTP请求获取图片信息
            String imageInfo = HttpUtils.sendGet(signedUrl.toString());
//            System.out.println("图片元数据：" + imageInfo);
            Exif exif = JSONObject.parseObject(imageInfo, Exif.class);
//            System.out.println("exif = " + exif);
            //设置图片信息
            PictureResponse pictureResponse = new PictureResponse();
            String pictureUrl = signedUrl.toString().split("\\?")[0];
            pictureResponse.setPictureUrl(pictureUrl);
            pictureResponse.setName(nameNotSuffix);
            pictureResponse.setPicSize(Long.parseLong(exif.getFileSize().getValue()));
            long picWidth = Long.parseLong(exif.getImageWidth().getValue());
            pictureResponse.setPicWidth(picWidth);
            long picHeight = Long.parseLong(exif.getImageHeight().getValue());
            pictureResponse.setPicHeight(picHeight);
            pictureResponse.setPicScale(((double) picWidth / (double) picHeight));
            pictureResponse.setPicFormat(suffix);
            int limit = pictureUrl.lastIndexOf(".");
            pictureResponse.setThumbnailUrl(pictureUrl.substring(0, limit) + compressedSuffix);
//            System.out.println("limit = " + pictureResponse);

            // 设置图片处理参数（转换为 WebP 格式）并携带水印
            String process = getWatermark(loginUser, picWidth, picHeight);
            // 创建获取压缩后图片的预签名URL
            req.setExpiration(expiration);
//            System.out.println("图片信息URL：" + signedUrl);
            req.setProcess(process);
            URL compressedUrl = ossClient.generatePresignedUrl(req);
//            System.out.println("压缩图URL：" + compressedUrl);
            // 获取压缩后的图片输入流
            HttpURLConnection connection = (HttpURLConnection) compressedUrl.openConnection();
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();
            // 将压缩图上传到OSS
            PutObjectResult compressedPutObjectResult = ossClient.putObject(ossConfig.getBucket(), compressedFilePath, inputStream);
//            System.out.println("压缩图上传成功：" + compressedFilePath);
//            System.out.println("compressedPutObjectResult = " + JSONObject.toJSONString(compressedPutObjectResult));
            // 返回文件访问路径或URL
            return pictureResponse;
        } catch (Exception e) {
            // 记录详细日志
            System.err.println("上传失败：" + e.getMessage());
            throw new RuntimeException("文件上传异常");
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
            if (file != null && file.exists()) {
                boolean delete = file.delete();
            }
        }
    }

    /**
     * 获取水印基本信息
     *
     * @param loginUser
     * @param picWidth
     * @param picHeight
     * @return
     */
    private String getWatermark(LoginUserInfo loginUser, long picWidth, long picHeight) {
        long fontSize = picWidth / 30;
        long userFontSize = picHeight / 30;

        //获取水印基本信息
        String text = redisCache.getCacheObject(PICTURE_WATERMARK_TEXT);
        if (StringUtils.isEmpty(text)) {
            text = "www.springsun.online";
        }
        String encodeToString = Base64.getEncoder().encodeToString(text.getBytes());
        String userEncodeToString = Base64.getEncoder().encodeToString(loginUser.getUsername().getBytes());

        //获取水印字体大小
        try {
            Long pSize = redisCache.getCacheObject(PICTURE_WATERMARK_TEXT_SP);
            if (StringUtils.isNull(pSize)) {
                pSize = 50L;
            }
            Long uSize = redisCache.getCacheObject(PICTURE_WATERMARK_TEXT_SU);
            if (StringUtils.isNull(uSize)) {
                uSize = 30L;
            }
            fontSize = picWidth / pSize;
            userFontSize = picHeight / uSize;
        } catch (Exception e) {
            log.error("获取水印基本信息失败：{}", e.getMessage());
        }
        //获取水印透明度
        String pAlpha = "50";
        String uAlpha = "70";
        String pAlphaStr = redisCache.getCacheObject(PICTURE_WATERMARK_TEXT_PP);
        if (StringUtils.isNotEmpty(pAlphaStr)) {
            pAlpha = pAlphaStr;
        }
        String uAlphaStr = redisCache.getCacheObject(PICTURE_WATERMARK_TEXT_PU);
        if (StringUtils.isNotEmpty(uAlphaStr)) {
            uAlpha = uAlphaStr;
        }


        return "image/format,webp/watermark,text_" + encodeToString
                + ",size_" + fontSize
                + ",t_" + pAlpha
                + ",color_FFFFFF,y_10,type_ZHJvaWRzYW5zZmFsbGJhY2s,g_south"
                + "/watermark,text_" + userEncodeToString
                + ",size_" + userFontSize
                + ",t_" + uAlpha
                + ",color_FFFFFF"
                + ",g_east,x_50,type_ZHJvaWRzYW5zZmFsbGJhY2s";
    }

    private void validateFile(File file) {
        if (StringUtils.isNull(file) || StringUtils.isEmpty(file.getName())) {
            throw new IllegalArgumentException("文件或文件名不能为空");
        }
        //校验文件大小
        if (file.length() > DEFAULT_FONT_SIZE) {
            throw new RuntimeException("文件大小不能超过" + DEFAULT_FONT_SIZE / 1024 / 1024 + "MB");
        }
        //文件名长度
        if (file.getName().length() > DEFAULT_FILE_NAME_LENGTH) {
            throw new RuntimeException("文件名长度不能超过" + DEFAULT_FILE_NAME_LENGTH + "个字符");
        }
        //校验文件类型
        String fileType = FileUtil.getType(file);
        if (!Arrays.asList(DEFAULT_ALLOWED_EXTENSION).contains(fileType)) {
            throw new RuntimeException("文件类型不支持");
        }
    }

}
