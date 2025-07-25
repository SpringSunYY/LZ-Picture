package com.lz.picture.controller.user;

import com.alibaba.fastjson.JSON;
import com.lz.common.constant.Constants;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.manager.file.PictureDownloadManager;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.manager.file.model.FileResponse;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.file.FileUtils;
import com.lz.config.model.enmus.CFileLogOssTypeEnum;
import com.lz.config.model.enmus.CFileLogTypeEnum;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureFileLogAsyncFactory;
import com.lz.picture.model.dto.file.UrlUploadRequest;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoRequest;
import com.lz.picture.model.enums.PDownloadTypeEnum;
import com.lz.picture.model.vo.pictureInfo.PictureDownloadVo;
import com.lz.picture.model.vo.pictureInfo.PictureInfoDto;
import com.lz.picture.service.IPictureInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Project: Picture
 * Package: com.lz.picture.controller.user
 * Author: YY
 * CreateTime: 2025-04-17  00:27
 * Description: UserFileController
 * Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/file")
@Slf4j
public class UserFileController extends BaseUserInfoController {
    @Resource
    private PictureDownloadManager pictureDownloadManager;

    @Resource
    private PictureUploadManager pictureUploadManager;

    @Resource
    private IPictureInfoService pictureInfoService;


    /**
     * 上传图片
     *
     * @param multipartFile
     * @return
     */
    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PostMapping("/upload")
    public AjaxResult uploadPicture(@RequestPart("file") MultipartFile multipartFile) {
        // 执行业务上传
        FileResponse picture = pictureUploadManager.uploadPicture(multipartFile, "picture", getLoginUser());
        //异步执行存入文件日志
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.recordFileLog(picture,
                getLoginUser().getUserId(),
                CFileLogOssTypeEnum.OSS_TYPE_0.getValue(),
                CFileLogTypeEnum.LOG_TYPE_0.getValue()
        ));
        return success(picture);
    }

    /**
     * 上传封面图
     *
     * @param multipartFile
     * @return
     */
    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PostMapping("/upload/cover")
    public AjaxResult uploadCover(@RequestPart("file") MultipartFile multipartFile,
                                  @RequestParam(name = "type", required = false) String type,
                                  @RequestParam(name = "fileDir", required = false) String fileDir) {
        if (StringUtils.isEmpty(type)) {
            type = CFileLogTypeEnum.LOG_TYPE_1.getValue();
        }
        if (StringUtils.isEmpty(fileDir)) {
            fileDir = "cover";
        }
        // 执行业务上传
        FileResponse fileResponse = pictureUploadManager.uploadCover(multipartFile, fileDir, getLoginUser());
        //防止线程变量共享
        FileResponse target = new FileResponse();
        BeanUtils.copyProperties(fileResponse, target);
        //异步执行存入文件日志
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.recordFileLog(target,
                getLoginUser().getUserId(),
                CFileLogOssTypeEnum.OSS_TYPE_0.getValue(),
                type
        ));
        fileResponse.setUrl(null);
        return success(fileResponse);
    }

    /**
     * 上传封文件
     *
     * @param multipartFile
     * @return
     */
    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PostMapping("/upload/file")
    public AjaxResult uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                 @RequestParam(name = "type", required = false) String type,
                                 @RequestParam(name = "fileDir", required = false) String fileDir) {
        if (StringUtils.isEmpty(fileDir)) {
            fileDir = "file";
        }
        // 执行业务上传
        FileResponse fileResponse = pictureUploadManager.uploadFile(multipartFile, fileDir, getLoginUser());
        //防止线程变量共享
        FileResponse target = new FileResponse();
        BeanUtils.copyProperties(fileResponse, target);
        //异步执行存入文件日志
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.recordFileLog(target,
                getLoginUser().getUserId(),
                CFileLogOssTypeEnum.OSS_TYPE_0.getValue(),
                CFileLogTypeEnum.LOG_TYPE_3.getValue()
        ));
        return success(fileResponse);
    }

    /**
     * url上传图片
     *
     * @param urlUploadRequest
     * @return
     */
    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PostMapping("/upload/url")
    public AjaxResult uploadUrl(@RequestBody UrlUploadRequest urlUploadRequest) {
        // 执行业务上传
        FileResponse fileResponse = pictureUploadManager.uploadUrl(urlUploadRequest.getUrl(), "picture", getLoginUser());
        //异步执行存入文件日志
        PictureAsyncManager.me().execute(
                PictureFileLogAsyncFactory.recordFileLog(fileResponse,
                        getLoginUser().getUserId(),
                        CFileLogOssTypeEnum.OSS_TYPE_0.getValue(),
                        CFileLogTypeEnum.LOG_TYPE_0.getValue()
                ));
        return success(fileResponse);
    }

    /**
     * 用户下载图片
     *
     * @param pictureId 图片id
     * @param response  响应
     */
    @PreAuthorize("@uss.hasPermi('picture:download')")
    @GetMapping("/download/{pictureId}")
    public void downloadPicture(@PathVariable("pictureId") String pictureId, HttpServletResponse response) {
        try {
            // 校验图片
            PictureInfoDto pictureInfo = pictureInfoService.verifyPictureInfo(pictureId, getUserId(), PDownloadTypeEnum.DOWNLOAD_TYPE_1.getValue());
            String url = pictureDownloadManager.generateDownloadUrl(pictureInfo.getPictureUrl(), 5L);

            response.reset();
            response.setContentType("application/octet-stream");
            String fileName = FileUtils.getName(pictureInfo.getPictureUrl());
            response.setCharacterEncoding(Constants.UTF8);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

            // 打开 URL 作为输入流
            try (InputStream inputStream = new URL(url).openStream();
                 OutputStream out = response.getOutputStream()) {

                byte[] buffer = new byte[4096];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }
        } catch (Exception e) {
            try {
                response.reset(); // 清除之前的响应头和内容
//                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 设置状态码
                response.setContentType("application/json"); // 设置响应类型为 JSON
                response.setCharacterEncoding("UTF-8");
                AjaxResult error = error(e.getMessage());
                response.getWriter().write(JSON.toJSONString(error)); // 写入错误信息
            } catch (IOException ioEx) {
                log.error("写入错误响应失败", ioEx);
            }
        }
    }

    /**
     * 用户下载图片
     *
     * @param downloadId 图片下载记录编号
     * @param response   响应
     */
    @GetMapping("/download/log/{downloadId}")
    public void downloadPictureByLog(@PathVariable("downloadId") String downloadId, HttpServletResponse response) {
        try {
            PictureDownloadLogInfoRequest request = new PictureDownloadLogInfoRequest();
            request.setDownloadId(downloadId);
            request.setUserId(getUserId());
            PictureInfoDto pictureInfo = pictureInfoService.verifyPictureInfoByLog(request);
            String url = pictureDownloadManager.generateDownloadUrl(pictureInfo.getPictureUrl(), 5L);

            // 设置响应头
            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");

            String fileName = pictureInfo.getName() + "." + pictureInfo.getPicFormat();
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 流式传输文件
            try (InputStream inputStream = new URL(url).openStream()) {
                OutputStream out = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }

        } catch (Exception e) {
            try {
                log.error("下载图片失败", e);
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                AjaxResult error = AjaxResult.error(e.getMessage());
                ServletOutputStream out = response.getOutputStream();
                out.write(JSON.toJSONString(error).getBytes(StandardCharsets.UTF_8));
                out.flush();
            } catch (IOException ioEx) {
                log.error("写入错误响应失败", ioEx);
            }
        }
    }

    /**
     * 用户查看原图，获取原图链接
     */
    @GetMapping("/original/{pictureId}")
    public AjaxResult getOriginalPicture(@PathVariable("pictureId") String pictureId) {
        // 校验图片
        PictureInfoDto pictureInfo = pictureInfoService.verifyPictureInfo(pictureId, getUserId(), PDownloadTypeEnum.DOWNLOAD_TYPE_0.getValue());
        String url = pictureDownloadManager.generateDownloadUrl(pictureInfo.getPictureUrl(), 5L);
        return success(new PictureDownloadVo(pictureId, url, pictureInfo.getPictureMoreInfo().getPointsNeed(), pictureInfo.getPictureMoreInfo().getPriceNeed()));
    }

    /**
     * 用户查看原图，获取原图链接
     */
    @GetMapping("/log/original/{downloadId}")
    public AjaxResult getLogOriginalPicture(@PathVariable("downloadId") String downloadId) {
        // 校验图片
        PictureDownloadLogInfoRequest request = new PictureDownloadLogInfoRequest();
        request.setDownloadId(downloadId);
        request.setUserId(getUserId());
        PictureInfoDto pictureInfo = pictureInfoService.verifyPictureInfoByLog(request);
        String url = pictureDownloadManager.generateDownloadUrl(pictureInfo.getPictureUrl(), 5L);
        return success(new PictureDownloadVo(pictureInfo.getPictureId(), url, pictureInfo.getPictureMoreInfo().getPointsNeed(), pictureInfo.getPictureMoreInfo().getPriceNeed()));
    }
}
