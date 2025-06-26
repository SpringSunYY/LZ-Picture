package com.lz.common.manager.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.lz.common.config.OssConfig;
import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.manager.file.model.Exif;
import com.lz.common.manager.file.model.FileInfo;
import com.lz.common.manager.file.model.FileResponse;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
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
import java.net.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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
    public static Long DEFAULT_PICTURE_SIZE = 1024L * 1024L * 50;
    public static Long URL_PICTURE_SIZE = 1024L * 1024L * 30;
    /**
     * 默认文件大小
     */
    public static final Long DEFAULT_FILE_SIZE = 1024L * 1024L * 50;
    /**
     * 默认的文件名最大长度 100
     */
    public static final Long DEFAULT_FILE_NAME_LENGTH = 100L;

    public static final List<String> DEFAULT_PICTURE_ALLOWED_EXTENSION = Arrays.asList("bmp", "gif", "jpg", "jpeg", "png", "webp");
    public static final List<String> DEFAULT_FILE_ALLOWED_EXTENSION = Arrays.asList("pdf", "doc", "docx", "ppt", "pptx", "txt", "rar", "zip", "gz");

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
     * 生成图片下载url
     *
     * @param filePath
     * @return
     */
    public String generateDownloadUrl(String filePath, Long expireTime) {
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret()
            );
            String objectKey = extractKeyFromPath(filePath);
            // 设置过期时间，比如 5 分钟
            Date expiration = new Date(System.currentTimeMillis() + expireTime * 60 * 1000);
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(
                    ossConfig.getBucket(), objectKey, HttpMethod.GET
            );
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
     * description: 上传图片
     * author: YY
     * method: uploadPicture
     * date: 2025/3/27 11:03
     * param:
     * param: fileName
     * param: fileBytes
     * return: java.lang.String
     **/
    public FileResponse uploadPicture(MultipartFile multipartFile, String fileDir, LoginUserInfo loginUser) {
        //创建临时文件
        File file = null;
        // 生成唯一文件名
        FileInfo fileInfo = getPictureFileInfo(multipartFile.getOriginalFilename(), fileDir);

        try {
            file = File.createTempFile(fileInfo.getNewFileName(), fileInfo.getFileSuffix());
            multipartFile.transferTo(file);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        }
        //校验文件
        validateFile(file, DEFAULT_PICTURE_SIZE, DEFAULT_FILE_NAME_LENGTH, DEFAULT_PICTURE_ALLOWED_EXTENSION);

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
            ossClient.putObject(ossConfig.getBucket(), fileInfo.getFilePath(), file);

            // 生成获取图片信息的预签名URL（包含目录路径）
            String style = "image/info";
            Date expiration = new Date(System.currentTimeMillis() + 600_000); // 10分钟有效期
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(
                    ossConfig.getBucket(),
                    fileInfo.getFilePath(),  // 使用完整路径
                    HttpMethod.GET
            );
            req.setExpiration(expiration);
            req.setProcess(style);
            URL signedUrl = ossClient.generatePresignedUrl(req);

            // 发送HTTP请求获取图片信息
            String imageInfo = HttpUtils.sendGet(signedUrl.toString());
            Exif exif = JSONObject.parseObject(imageInfo, Exif.class);
            long picWidth = Long.parseLong(exif.getImageWidth().getValue());
            long picHeight = Long.parseLong(exif.getImageHeight().getValue());

            // 设置图片处理参数（转换为 WebP 格式）并携带水印
            String process = getWatermark(loginUser, picWidth, picHeight);
            req.setExpiration(expiration);
            req.setProcess(process);
            URL compressedUrl = ossClient.generatePresignedUrl(req);
            // 获取压缩后的图片输入流
            HttpURLConnection connection = (HttpURLConnection) compressedUrl.openConnection();
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();
            // 将压缩图上传到OSS
            ossClient.putObject(ossConfig.getBucket(), fileInfo.getCompressedFilePath(), inputStream);

            // 返回文件访问路径或URL
            return buildPictureResponse(ossConfig.getEndpoint(), fileInfo, Long.parseLong(exif.getFileSize().getValue()), picWidth, picHeight);
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
                    log.error("文件关闭失败", e);
                }
            }
            if (file.exists()) {
                boolean delete = file.delete();
            }
        }
    }

    /**
     * 返回图片信息
     *
     * @param endpoint  环境dns
     * @param fileInfo  图片文件信息
     * @param picSize   图片大小
     * @param picWidth  图片宽度
     * @param picHeight 图片高度
     * @return com.lz.common.manager.file.model.PictureFileResponse
     * @author YY
     * @method buildPictureResponse
     * @date 2025/4/24 20:20
     **/
    private static FileResponse buildPictureResponse(String endpoint, FileInfo fileInfo, Long picSize, long picWidth, long picHeight) {
        //设置图片信息
        FileResponse fileResponse = new FileResponse();
        if (StringUtils.isNotEmpty(fileInfo.getFilePath())) {
            fileResponse.setUrl("/" + fileInfo.getFilePath());
        }
        fileResponse.setName(fileInfo.getFileNameNotSuffix());
        fileResponse.setPicSize(picSize);
        fileResponse.setPicWidth(picWidth);
        fileResponse.setPicHeight(picHeight);
        if (picWidth > 0 && picHeight > 0) {
            fileResponse.setPicScale(((double) picWidth / (double) picHeight));
        } else {
            fileResponse.setPicScale(0.0);
        }
        fileResponse.setPicFormat(fileInfo.getFileSuffix());
        if (StringUtils.isNotEmpty(fileInfo.getCompressedFilePath())) {
            fileResponse.setThumbnailUrl("/" + fileInfo.getCompressedFilePath());
        }
        fileResponse.setDnsUrl(endpoint);
        return fileResponse;
    }

    /**
     * 获取文件基本信息
     *
     * @param fileName 文件名
     * @param fileDir  文件目录
     * @return com.lz.common.manager.file.model.PictureFileInfo
     * @author YY
     * @method getPictureFileInfo
     * @date 2025/4/24 20:14
     **/
    public FileInfo getPictureFileInfo(String fileName, String fileDir) {
        String nameNotSuffix = FileUtils.getNameNotSuffix(fileName);
        String fileSuffix = FileUtil.getSuffix(fileName);
//        System.err.println("nameNotSuffix = " + nameNotSuffix);
//        System.out.println("fileSuffix = " + fileSuffix);
        //如果文件格式不是图片，因为之前校验通过，但是有些图片链接没有图片格式，所以要自己替换
        if (!DEFAULT_PICTURE_ALLOWED_EXTENSION.contains(fileSuffix)) {
            fileSuffix = "jpg";
        }
        String newFileName = nameNotSuffix + "-" + IdUtils.snowflakeId() + "." + fileSuffix;
        String dir = ossConfig.getDir();
        //生成文件路径 包括时间年/月/日
        String parseDateToStr = DateUtils.parseDateToStr(DateUtils.yyyy_mm_dd, new Date());
        String filePath = dir + "/" + fileDir + "/" + parseDateToStr + "/" + newFileName;
        //压缩文件信息
        String compressedSuffix = "-compressed.webp";
        String compressedFileName = nameNotSuffix + "-" + IdUtils.snowflakeId() + compressedSuffix;
        String compressedFilePath = dir + "/" + fileDir + "/" + parseDateToStr + "/" + compressedFileName;
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileNameNotSuffix(nameNotSuffix);
        fileInfo.setFileName(fileName);
        fileInfo.setFileSuffix(fileSuffix);
        fileInfo.setFilePath(filePath);
        fileInfo.setNewFileName(newFileName);
        fileInfo.setCompressedSuffix(compressedSuffix);
        fileInfo.setCompressedFileName(compressedFileName);
        fileInfo.setCompressedFilePath(compressedFilePath);
        return fileInfo;
    }

    /**
     * 获取水印基本信息
     *
     * @param loginUser 当前登录用户信息
     * @param picWidth  图片宽度
     * @param picHeight 图片高度
     * @return
     */
    public String getWatermark(LoginUserInfo loginUser, long picWidth, long picHeight) {
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

    /**
     * description: 上传封面图操作
     * author: YY
     * method: uploadCover
     * date: 2025/4/16 23:59
     * param:
     * param: multipartFile
     * param: fileDir
     * param: loginUser
     * return: String
     **/
    public FileResponse uploadCover(MultipartFile multipartFile, String fileDir, LoginUserInfo loginUser) {
        //创建临时文件
        File file = null;
        // 生成唯一文件名
        FileInfo fileInfo = getPictureFileInfo(multipartFile.getOriginalFilename(), fileDir);
        try {
            file = File.createTempFile(fileInfo.getNewFileName(), fileInfo.getFileSuffix());
            multipartFile.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //校验文件
        validateFile(file, DEFAULT_PICTURE_SIZE, DEFAULT_FILE_NAME_LENGTH, DEFAULT_PICTURE_ALLOWED_EXTENSION);

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
            ossClient.putObject(ossConfig.getBucket(), fileInfo.getFilePath(), file);
            Date expiration = new Date(System.currentTimeMillis() + 600_000); // 10分钟有效期
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(
                    ossConfig.getBucket(),
                    fileInfo.getFilePath(),  // 使用完整路径
                    HttpMethod.GET
            );
            req.setExpiration(expiration);

            // 发送HTTP请求获取图片信息
            URL compressedUrl = ossClient.generatePresignedUrl(req);
            // 获取压缩后的图片输入流
            HttpURLConnection connection = (HttpURLConnection) compressedUrl.openConnection();
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();
            // 将压缩图上传到OSS
            ossClient.putObject(ossConfig.getBucket(), fileInfo.getCompressedFilePath(), inputStream);
            // 返回文件访问路径或1URL
            return buildPictureResponse(ossConfig.getEndpoint(), fileInfo, 0L, 0, 0);
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
     * 根据url上传图片
     * 上传原始文件和压缩后的图片
     *
     * @param url       路径
     * @param fileDir   文件目录
     * @param loginUser 登录用户
     * @return com.lz.common.manager.file.model.PictureFileResponse
     * @author YY
     * @method uploadUrl
     * @date 2025/4/24 23:10
     **/
    public FileResponse uploadUrl(String url, String fileDir, LoginUserInfo loginUser) {
        //校验文件路径
        validPicture(url);
        //获取文件名
        String fileName = getValidFilename(url);
        //生成唯一文件名
        FileInfo fileInfo = getPictureFileInfo(fileName, fileDir);
        //获取文件
        File file = null;
        InputStream inputStream = null;
        OSS ossClient = null;
        try {
            file = File.createTempFile(fileInfo.getNewFileName(), fileInfo.getFileSuffix());
            processFile(url, file);
            // 初始化OSS客户端
            ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret()
            );

            // 上传原始文件
            ossClient.putObject(ossConfig.getBucket(), fileInfo.getFilePath(), file);
            // 生成获取图片信息的预签名URL（包含目录路径）
            String style = "image/info";
            Date expiration = new Date(System.currentTimeMillis() + 600_000); // 10分钟有效期
            GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(
                    ossConfig.getBucket(),
                    fileInfo.getFilePath(),  // 使用完整路径
                    HttpMethod.GET
            );
            req.setExpiration(expiration);
            req.setProcess(style);
            URL signedUrl = ossClient.generatePresignedUrl(req);

            // 发送HTTP请求获取图片信息
            String imageInfo = HttpUtils.sendGet(signedUrl.toString());
            Exif exif = JSONObject.parseObject(imageInfo, Exif.class);
            long picWidth = Long.parseLong(exif.getImageWidth().getValue());
            long picHeight = Long.parseLong(exif.getImageHeight().getValue());

            // 设置图片处理参数（转换为 WebP 格式）并携带水印
            String process = getWatermark(loginUser, picWidth, picHeight);
            // 创建获取压缩后图片的预签名URL
            req.setExpiration(expiration);
            req.setProcess(process);
            URL compressedUrl = ossClient.generatePresignedUrl(req);
            // 获取压缩后的图片输入流
            HttpURLConnection connection = (HttpURLConnection) compressedUrl.openConnection();
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();
            // 将压缩图上传到OSS
            ossClient.putObject(ossConfig.getBucket(), fileInfo.getCompressedFilePath(), inputStream);
            // 返回文件访问路径或URL
            return buildPictureResponse(ossConfig.getEndpoint(), fileInfo, Long.parseLong(exif.getFileSize().getValue()), picWidth, picHeight);
        } catch (Exception e) {
            // 记录详细日志
//            System.err.println("上传失败：" + e.getMessage());
            throw new RuntimeException("文件上传异常,获取是图片链接有误,请使用可访问的地址");
        } finally {
            // 确保关闭OSSClient
            if (ossClient != null) {
                ossClient.shutdown();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("文件关闭失败", e);
                }
            }
            if (StringUtils.isNotNull(file) && file.exists()) {
                boolean delete = file.delete();
            }
        }

    }

    /**
     * 校验url
     *
     * @param fileUrl 路径
     * @return void
     * @author YY
     * @method validPicture
     * @date 2025/4/24 22:46
     **/
    private void validPicture(String fileUrl) {
        ThrowUtils.throwIf(StringUtils.isEmpty(fileUrl), "文件地址不能为空");
        try {
            // 1. 验证 URL 格式（使用URI代替弃用的URL构造方式）
            new URI(fileUrl).toURL(); // 验证是否是合法的 URL
        } catch (URISyntaxException | IllegalArgumentException | MalformedURLException e) {
            throw new ServiceException("文件地址格式不正确");
        }

        // 2. 校验 URL 协议
        ThrowUtils.throwIf(!(fileUrl.startsWith("http://") || fileUrl.startsWith("https://")),
                "仅支持 HTTP 或 HTTPS 协议的文件地址");

        // 3. 发送 HEAD 请求以验证文件是否存在
        HttpResponse response = null;
        try {
            response = HttpUtil.createRequest(Method.HEAD, fileUrl).execute();
            // 未正常返回，无需执行其他判断
            if (response.getStatus() != HttpStatus.HTTP_OK) {
                return;
            }
            // 4. 校验文件类型
            String contentType = response.header("Content-Type");
            if (StrUtil.isNotBlank(contentType)) {
                // 允许的图片类型
                final List<String> ALLOW_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/webp");
                ThrowUtils.throwIf(!ALLOW_CONTENT_TYPES.contains(contentType.toLowerCase()),
                        "文件类型错误");
            }
            // 5. 校验文件大小
            String contentLengthStr = response.header("Content-Length");
            if (StrUtil.isNotBlank(contentLengthStr)) {
                try {
                    long contentLength = Long.parseLong(contentLengthStr);
                    ThrowUtils.throwIf(contentLength > URL_PICTURE_SIZE, "文件大小不能超过 2M");
                } catch (NumberFormatException e) {
                    throw new ServiceException("文件大小格式错误");
                }
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private String getValidFilename(String originFilename) {
        // 去掉 URL 中的查询参数部分，保留文件名
        int queryIndex = originFilename.indexOf("?");
        if (queryIndex != -1) {
            // 如果存在查询参数，截取文件名部分
            originFilename = originFilename.substring(0, queryIndex);
        }
        return FileUtils.getName(originFilename);
    }

    protected void processFile(Object inputSource, File file) throws Exception {
        String fileUrl = (String) inputSource;
        HttpUtil.downloadFile(fileUrl, file);
    }


    public void deleteFile(List<String> keys) {
        // 使用最原始、兼容性最强的方法创建 OSS 客户端
        OSS ossClient = new OSSClientBuilder().build(
                ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
        );

        try {
            // 删除文件。
            DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(ossConfig.getBucket()).withKeys(keys).withEncodingType("url"));
//            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
//            try {
//                for (String obj : deletedObjects) {
//                    String deleteObj = URLDecoder.decode(obj, "UTF-8");
//                    System.out.println(deleteObj);
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            log.error("文件删除失败", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 上传文件
     *
     * @param multipartFile
     * @param fileDir
     * @param loginUser
     * @return PictureFileResponse
     * @author: YY
     * @method: uploadFile
     * @date: 2025/6/25 22:19
     **/
    public FileResponse uploadFile(MultipartFile multipartFile, String fileDir, LoginUserInfo loginUser) {
        //创建临时文件
        File file = null;
        // 生成唯一文件名
        FileInfo fileInfo = getFileInfo(multipartFile.getOriginalFilename(), fileDir);
        try {
            file = File.createTempFile(fileInfo.getNewFileName(), fileInfo.getFileSuffix());
            multipartFile.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //校验文件
        validateFile(file, DEFAULT_FILE_SIZE, DEFAULT_FILE_NAME_LENGTH, DEFAULT_FILE_ALLOWED_EXTENSION);

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
            ossClient.putObject(ossConfig.getBucket(), fileInfo.getFilePath(), file);
            new GeneratePresignedUrlRequest(
                    ossConfig.getBucket(),
                    fileInfo.getFilePath(),  // 使用完整路径
                    HttpMethod.GET
            );

            // 返回文件访问路径或1URL
            return buildPictureResponse(ossConfig.getEndpoint(), fileInfo, 0L, 0, 0);
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

    private FileInfo getFileInfo(String fileName, String fileDir) {
        String nameNotSuffix = FileUtils.getNameNotSuffix(fileName);
        String fileSuffix = FileUtil.getSuffix(fileName);
        String newFileName = nameNotSuffix + "-" + IdUtils.snowflakeId() + "." + fileSuffix;
        String dir = ossConfig.getDir();
        //生成文件路径 包括时间年/月/日
        String parseDateToStr = DateUtils.parseDateToStr(DateUtils.yyyy_mm_dd, new Date());
        String filePath = dir + "/" + fileDir + "/" + parseDateToStr + "/" + newFileName;
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileNameNotSuffix(nameNotSuffix);
        fileInfo.setFileName(fileName);
        fileInfo.setFileSuffix(fileSuffix);
        fileInfo.setFilePath(filePath);
        fileInfo.setNewFileName(newFileName);
        return fileInfo;
    }

    /**
     * 校验文件
     *
     * @param file
     * @return void
     * @author: YY
     * @method: validatePictureFile
     * @date: 2025/6/25 22:26
     **/
    private void validateFile(File file, Long fileSize, Long fileNameSize, List<String> allowedExtensions) {
        if (StringUtils.isNull(file) || StringUtils.isEmpty(file.getName())) {
            throw new IllegalArgumentException("文件或文件名不能为空");
        }
        //校验文件大小
        if (file.length() > fileSize) {
            throw new RuntimeException("文件大小不能超过" + fileSize / 1024 / 1024 + "MB");
        }
        //文件名长度
        if (file.getName().length() > fileNameSize) {
            throw new RuntimeException("文件名长度不能超过" + fileNameSize + "个字符");
        }
        //校验文件类型
        String fileType = FileUtil.getType(file);
        System.out.println("fileType = " + fileType);
        if (!allowedExtensions.contains(fileType)) {
            throw new RuntimeException("文件类型不支持");
        }
    }

}
