package com.lz.points.controller.user;

import com.lz.common.constant.config.UserConfigKeyConstants;
import com.lz.common.constant.redis.UserRedisConstants;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.model.AuthUserInfo;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.sign.RsaUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.dto.accountInfo.AccountAuthDto;
import com.lz.points.model.dto.accountInfo.AccountPasswordUploadRequest;
import com.lz.points.model.dto.accountInfo.ResetAccountPasswordBody;
import com.lz.points.model.dto.accountInfo.ResetAccountPasswordCode;
import com.lz.points.model.vo.accountInfo.UserAccountInfoVo;
import com.lz.points.service.IAccountInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import com.lz.userauth.model.domain.ForgetPasswordBody;
import com.lz.userauth.model.domain.ForgetPasswordCode;
import com.lz.userauth.utils.PasswordUtils;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.lz.common.constant.redis.PointsRedisConstants.POINTS_ACCOUNT_RESET_PASSWORD_CODE;

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

    @Resource
    private IConfigInfoService configInfoService;

    /**
     * 更新用户账户密码
     */
    @PreAuthorize("@uss.hasLogin()")
    @PutMapping(value = "/password")
    public AjaxResult updatePassword(@RequestBody AccountPasswordUploadRequest accountPasswordUploadRequest) throws Exception {
        String userId = getUserId();
        if (!userId.equals(accountPasswordUploadRequest.getUserId())) {
            return error("无权限访问");
        }
        accountPasswordUploadRequest.setPassword(RsaUtils.decryptUserByPrivateKey(accountPasswordUploadRequest.getPassword()));
        accountPasswordUploadRequest.setOldPassword(RsaUtils.decryptUserByPrivateKey(accountPasswordUploadRequest.getOldPassword()));
        accountPasswordUploadRequest.setConfirmPassword(RsaUtils.decryptUserByPrivateKey(accountPasswordUploadRequest.getConfirmPassword()));
        return AjaxResult.success(accountInfoService.userUpdateAccountInfoPassword(accountPasswordUploadRequest));
    }

    /**
     * 获取用户账户最近是否校验密码
     *
     * @return
     */
    @PreAuthorize("@uss.hasLogin()")
    @GetMapping(value = "/verifyPassword")
    public AjaxResult getVerifyPassword() {
        String userId = getUserId();
        return AjaxResult.success(accountInfoService.getVerifyPassword(userId));
    }

    /**
     * 校验用户账户密码
     *
     * @param accountAuthDto
     * @return
     */
    @PreAuthorize("@uss.hasLogin()")
    @PostMapping(value = "/verifyPassword")
    public AjaxResult verifyPassword(@RequestBody AccountAuthDto accountAuthDto) throws Exception {
        String userId = getUserId();
        String password = RsaUtils.decryptUserByPrivateKey(accountAuthDto.getPassword());
        return AjaxResult.success(accountInfoService.verifyPassword(userId, password));
    }

    @PreAuthorize("@uss.hasLogin()")
    @GetMapping()
    public AjaxResult getAccountInfo() {
        String userId = getUserId();
        //查询用户账户
        AccountInfo accountInfo = accountInfoService.selectUserAccountInfoByUserId(userId);
        return AjaxResult.success(UserAccountInfoVo.objToVo(accountInfo));
    }

    @PreAuthorize("@uss.hasLogin()")
    @GetMapping("/password/code")
    public AjaxResult getAccountPasswordCode(@Validated ResetAccountPasswordCode resetAccountPasswordCode) throws Exception {
        resetAccountPasswordCode.setUserId(RsaUtils.decryptUserByPrivateKey(resetAccountPasswordCode.getUserId()));
        //校验用户
        if (!getUserId().equals(resetAccountPasswordCode.getUserId())) {
            return error("无权限访问");
        }
        resetAccountPasswordCode.setPhone(RsaUtils.decryptUserByPrivateKey(resetAccountPasswordCode.getPhone()));
        resetAccountPasswordCode.setCountryCode(RsaUtils.decryptUserByPrivateKey(resetAccountPasswordCode.getCountryCode()));
        String configInfoCache = configInfoService.getConfigInfoInCache(UserConfigKeyConstants.USER_LOGIN_CAPTCHA_ENABLED);
        boolean captchaEnabled = "true".equals(configInfoCache);
        String registerCode = accountInfoService.getAccountPasswordCode(resetAccountPasswordCode.getPhone(), resetAccountPasswordCode.getCountryCode(), resetAccountPasswordCode.getCode(), captchaEnabled, resetAccountPasswordCode.getUuid());
        System.err.println(registerCode);
        return AjaxResult.success("验证码发送成功");
    }

    @PostMapping("/password/reset")
    public AjaxResult resetAccountPassword(@RequestBody @Validated ResetAccountPasswordBody resetAccountPasswordBody) throws Exception {
        resetAccountPasswordBody.setUserId(RsaUtils.decryptUserByPrivateKey(resetAccountPasswordBody.getUserId()));
        //校验用户
        if (!getUserId().equals(resetAccountPasswordBody.getUserId())) {
            return error("无权限访问");
        }
        resetAccountPasswordBody.setConfirmPassword(RsaUtils.decryptUserByPrivateKey(resetAccountPasswordBody.getConfirmPassword()));
        resetAccountPasswordBody.setPassword(RsaUtils.decryptUserByPrivateKey(resetAccountPasswordBody.getPassword()));
        resetAccountPasswordBody.setSmsCode(RsaUtils.decryptUserByPrivateKey(resetAccountPasswordBody.getSmsCode()));
        resetAccountPasswordBody.setPhone(RsaUtils.decryptUserByPrivateKey(resetAccountPasswordBody.getPhone()));
        resetAccountPasswordBody.setCountryCode(RsaUtils.decryptUserByPrivateKey(resetAccountPasswordBody.getCountryCode()));
        PasswordUtils.checkPasswordFormate(resetAccountPasswordBody.getPassword(), resetAccountPasswordBody.getConfirmPassword(), 6, 10);
        //校验验证码
        accountInfoService.checkSmsCode(POINTS_ACCOUNT_RESET_PASSWORD_CODE, resetAccountPasswordBody.getCountryCode(), resetAccountPasswordBody.getPhone(), resetAccountPasswordBody.getSmsCode());
        AccountInfo accountInfo = accountInfoService.resetAccountPassword(resetAccountPasswordBody);
        return AjaxResult.success("修改成功");
    }
}
