package com.lz.points.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.points.model.dto.accountInfo.AccountAuthDto;
import com.lz.points.model.dto.accountInfo.AccountPasswordUploadRequest;
import com.lz.points.service.IAccountInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户账户
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-22  08:55
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user/points/accountInfo")
public class UserAccountInfoController extends BaseUserInfoController {

    @Resource
    private IAccountInfoService accountInfoService;

    /**
     * 更新用户基本信息
     */
    @PreAuthorize("@uss.hasLogin()")
    @PutMapping(value = "/password")
    public AjaxResult updatePassword(@RequestBody AccountPasswordUploadRequest accountPasswordUploadRequest) {
        String userId = getUserId();
        if (!userId.equals(accountPasswordUploadRequest.getUserId())) {
            return error("无权限访问");
        }
        return AjaxResult.success(accountInfoService.userUpdateAccountInfoPassword(accountPasswordUploadRequest));
    }

    /**
     * 获取用户账户最近是否校验密码
     *
     * @return
     */
    @PreAuthorize("@uss.hasLogin()")
    @GetMapping(value = "/verifyPassword")
    public AjaxResult getAuth() {
        String userId = getUserId();
        return AjaxResult.success(accountInfoService.auth(userId));
    }

    /**
     * 校验用户账户密码
     *
     * @param accountAuthDto
     * @return
     */
    @PreAuthorize("@uss.hasLogin()")
    @PostMapping(value = "/verifyPassword")
    public AjaxResult verifyPassword(@RequestBody AccountAuthDto accountAuthDto) {
        String userId = getUserId();
        String password = accountAuthDto.getPassword();
        return AjaxResult.success(accountInfoService.verifyPassword(userId, password));
    }
}
