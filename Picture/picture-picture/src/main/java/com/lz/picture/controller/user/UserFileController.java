package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.manager.file.model.PictureFileResponse;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.file.FileUtils;
import com.lz.config.model.enmus.CFileLogOssTypeEnum;
import com.lz.config.model.enmus.CFileLogTypeEnum;
import com.lz.config.service.IFileLogInfoService;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureAsyncFactory;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.file.UrlUploadRequest;
import com.lz.picture.service.IPictureInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.TimerTask;

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
public class UserFileController extends BaseUserInfoController {
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
        PictureFileResponse picture = pictureUploadManager.uploadPicture(multipartFile, "picture", getLoginUser());
        //异步执行存入文件日志
        PictureAsyncManager.me().execute(PictureAsyncFactory.recordFileLog(picture,
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
    public AjaxResult uploadCover(@RequestPart("file") MultipartFile multipartFile) {
        // 执行业务上传
        PictureFileResponse pictureFileResponse = pictureUploadManager.uploadCover(multipartFile, "cover", getLoginUser());
        //防止线程变量共享
        PictureFileResponse target = new PictureFileResponse();
        BeanUtils.copyProperties(pictureFileResponse, target);
        //异步执行存入文件日志
        PictureAsyncManager.me().execute(PictureAsyncFactory.recordFileLog(target,
                getLoginUser().getUserId(),
                CFileLogOssTypeEnum.OSS_TYPE_0.getValue(),
                CFileLogTypeEnum.LOG_TYPE_1.getValue()
        ));
        pictureFileResponse.setPictureUrl(null);
        return success(pictureFileResponse);
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
        PictureFileResponse pictureFileResponse = pictureUploadManager.uploadUrl(urlUploadRequest.getUrl(), "picture", getLoginUser());
        //异步执行存入文件日志
        PictureAsyncManager.me().execute(PictureAsyncFactory.recordFileLog(pictureFileResponse,
                getLoginUser().getUserId(),
                CFileLogOssTypeEnum.OSS_TYPE_0.getValue(),
                CFileLogTypeEnum.LOG_TYPE_0.getValue()
        ));
        return success(pictureFileResponse);
    }

    @PreAuthorize("@uss.hasPermi('picture:download')")
    @GetMapping("/download/{pictureId}")
    public void downloadPicture(@PathVariable("pictureId") String pictureId, HttpServletResponse response) throws IOException {
        //TODO 图片校验
        PictureInfo pictureInfo = pictureInfoService.selectPictureInfoByPictureId(pictureId);
        String url = pictureUploadManager.generateDownloadUrl(pictureInfo.getPictureUrl(), 5L);

        response.reset();
        response.setContentType("application/octet-stream");
        String fileName = FileUtils.getName(pictureInfo.getPictureUrl());
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
    }
}
