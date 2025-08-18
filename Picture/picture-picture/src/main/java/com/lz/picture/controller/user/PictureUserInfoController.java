package com.lz.picture.controller.user;

import com.lz.common.constant.HttpStatus;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.picture.service.IPictureUserInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图片模块查询用户信息
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-18  17:23
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/userinfo")
public class PictureUserInfoController extends BaseUserInfoController {
    @Resource
    private IPictureUserInfoService pictureUserInfoService;

    @RequestMapping("/{username}")
    public AjaxResult getUserInfo(@PathVariable("username") String username) {
        ThrowUtils.throwIf(StringUtils.isEmpty(username), HttpStatus.BAD_REQUEST, "请输入用户名");
        return success(pictureUserInfoService.getPictureUserInfo(username));
    }
}
