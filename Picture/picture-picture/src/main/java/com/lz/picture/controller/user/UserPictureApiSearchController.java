package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.picture.model.dto.pictureApiSearch.PictureApiSearchRequest;
import com.lz.picture.service.IUserPictureApiSearchService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户图片API搜索
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-12  23:18
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/api/search")
public class UserPictureApiSearchController extends BaseUserInfoController {
    @Resource
    private IUserPictureApiSearchService userPictureApiSearchService;

    @GetMapping("/keyword")
    public AjaxResult keyword(@Validated PictureApiSearchRequest pictureApiSearchRequest) {
        pictureApiSearchRequest.setUserId(getUserId());
        return success(userPictureApiSearchService.keyword(pictureApiSearchRequest));
    }
}
