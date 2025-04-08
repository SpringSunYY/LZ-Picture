package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoInsert;
import com.lz.picture.model.dto.pictureInfo.UserPictureInfoAdd;
import com.lz.picture.service.IPictureInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @Resource
    private IPictureInfoService pictureInfoService;

//    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PostMapping("/upload")
    public AjaxResult uploadPicture(@RequestPart("file") MultipartFile multipartFile) {
        // 执行业务上传
        return success(pictureUploadManager.uploadPicture(multipartFile));
    }

    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PostMapping()
    public AjaxResult add(@RequestBody @Validated UserPictureInfoAdd userPictureInfoAdd) {
        PictureInfo pictureInfo = UserPictureInfoAdd.addToObj(userPictureInfoAdd);
        pictureInfo.setUserId(getUserId());
        return success(pictureInfoService.userInsertPictureInfo(pictureInfo));
    }
}
