package com.lz.user.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.sign.RsaUtils;
import com.lz.config.model.domain.MenuInfo;
import com.lz.config.model.vo.menuInfo.MenuInfoUserVo;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.dto.userInfo.UserInfoUpdate;
import com.lz.user.model.dto.userInfo.UserPasswordUploadRequest;
import com.lz.user.model.enums.UUserSexEnum;
import com.lz.user.service.IUserInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Project: Picture
 * Package: com.lz.user.controller.user
 * Author: YY
 * CreateTime: 2025-03-31  22:11
 * Description: UserInfoController
 * Version: 1.0
 */
@RestController
@RequestMapping("/user/user/userInfo")
public class UserUserInfoController extends BaseUserInfoController {
    @Resource
    private IUserInfoService userInfoService;

    /**
     * 获取用户菜单
     */
    @PreAuthorize("@uss.hasLogin()")
    @GetMapping(value = "/menu")
    public AjaxResult getMenu() {
        Set<String> permissions = getLoginUser().getPermissions();
        List<MenuInfo> menu = userInfoService.getMenu(permissions);
        //转换为Vo
        List<MenuInfoUserVo> menuInfoList = menu.stream().map(MenuInfoUserVo::objToVo).toList();
        return AjaxResult.success(menuInfoList);
    }

    /**
     * 获取当前用户信息
     */
    @PreAuthorize("@uss.hasLogin()")
    @GetMapping(value = "/my/{userName}")
    public AjaxResult getMyUserInfoByUserName(@PathVariable("userName") String userName) {
        if (StringUtils.isEmpty(userName)) {
            return error("无权限访问");
        }
        String username = getUsername();
        if (!username.equals(userName)) {
            return error("无权限访问");
        }
        return AjaxResult.success(userInfoService.getMyUserInfoByUserName(username));
    }

    /**
     * 更新用户基本信息
     */
    @PreAuthorize("@uss.hasLogin()")
    @PutMapping(value = "/update")
    public AjaxResult updateUserInfo(@RequestBody UserInfoUpdate userInfoUpdate) {
        String userId = getUserId();
        if (!userId.equals(userInfoUpdate.getUserId())) {
            return error("无权限访问");
        }
        Optional<UUserSexEnum> enumByValue = UUserSexEnum.getEnumByValue(userInfoUpdate.getSex());
        if (enumByValue.isEmpty()) {
            return error("性别参数错误");
        }
        UserInfo userInfo = UserInfoUpdate.editToObj(userInfoUpdate);
        return AjaxResult.success(userInfoService.userUpdateUserInfo(userInfo));
    }

    /**
     * 更新用户基本信息
     */
    @PreAuthorize("@uss.hasLogin()")
    @PutMapping(value = "/password")
    public AjaxResult updatePassword(@RequestBody UserPasswordUploadRequest userPasswordUploadRequest) throws Exception {
        String userId = getUserId();
        if (!userId.equals(userPasswordUploadRequest.getUserId())) {
            return error("无权限访问");
        }
        userPasswordUploadRequest.setPassword(RsaUtils.decryptUserByPrivateKey(userPasswordUploadRequest.getPassword()));
        userPasswordUploadRequest.setOldPassword(RsaUtils.decryptUserByPrivateKey(userPasswordUploadRequest.getOldPassword()));
        userPasswordUploadRequest.setConfirmPassword(RsaUtils.decryptUserByPrivateKey(userPasswordUploadRequest.getConfirmPassword()));
        return AjaxResult.success(userInfoService.userUpdateUserInfoPassword(userPasswordUploadRequest));
    }
}
