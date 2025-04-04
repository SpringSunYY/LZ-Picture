package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Project: Picture
 * Package: com.lz.picture.controller.user
 * Author: YY
 * CreateTime: 2025-03-29  21:35
 * Description: UserPictureInfoController
 * Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/pictureInfo")
public class UserPictureInfoController extends BaseUserInfoController {
    @Resource
    private PictureUploadManager pictureUploadManager;

    @PostMapping("/upload")
    public AjaxResult uploadPicture(@RequestPart("file") MultipartFile multipartFile) {
        // 执行业务上传
        return success(pictureUploadManager.uploadPicture(multipartFile));
    }
}
