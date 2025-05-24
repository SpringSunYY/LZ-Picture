package com.lz.web.controller.user;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lz.common.constant.Constants;
import com.lz.common.constant.config.UserConfigKeyConstants;
import com.lz.common.constant.redis.UserRedisConstants;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.model.AuthUserInfo;
import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.common.utils.sign.RsaUtils;
import com.lz.userauth.utils.PasswordUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.framework.web.service.UserInfoLoginService;
import com.lz.userauth.controller.BaseUserInfoController;
import com.lz.userauth.model.domain.*;
import com.lz.userauth.service.IAuthUserInfoService;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;

/**
 * 用户信息Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/user")
public class AuthUserInfoController extends BaseUserInfoController {
    @Resource
    private UserInfoLoginService loginService;

    @Resource
    private IConfigInfoService configInfoService;

    @Resource
    private IAuthUserInfoService authUserInfoService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult customerLogin(@Validated @RequestBody UserInfoLoginBody loginBody) throws Exception {
        loginBody.setPassword(RsaUtils.decryptUserByPrivateKey(loginBody.getPassword()));
        loginBody.setUsername(RsaUtils.decryptUserByPrivateKey(loginBody.getUsername()));
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.userInfoLogin(loginBody.getUsername(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        LoginUserInfo loginUser = getLoginUser();
        Set<String> permissions = loginUser.getPermissions();
        AuthUserInfo user = loginUser.getUser();
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("user", user);
        ajaxResult.put("permissions", permissions);
        return ajaxResult;
    }

    /**
     * 获取短信登录验证码
     *
     * @param loginCode 请求参数
     * @return
     */
    @GetMapping("/getSmsLoginCode")
    public AjaxResult getSmsLoginCode(SmsLoginCode loginCode) throws Exception {
        loginCode.setPhone(RsaUtils.decryptUserByPrivateKey(loginCode.getPhone()));
        loginCode.setCountryCode(RsaUtils.decryptUserByPrivateKey(loginCode.getCountryCode()));
        String configInfoCache = configInfoService.getConfigInfoInCache(UserConfigKeyConstants.USER_LOGIN_CAPTCHA_ENABLED);
        boolean captchaEnabled = "true".equals(configInfoCache);
        loginCode.setCaptchaEnabled(captchaEnabled);
        String smsLoginCode = loginService.getSmsCode(loginCode.getPhone(), loginCode.getCountryCode(), loginCode.getCode(), loginCode.isCaptchaEnabled(), loginCode.getUuid());
        System.err.println(smsLoginCode);
        return AjaxResult.success("验证码发送成功");
    }

    /**
     * 短信登录
     *
     * @param smsLoginBody
     * @return
     */
    @PostMapping("/smsLogin")
    public AjaxResult smsLogin(@RequestBody SmsLoginBody smsLoginBody) throws Exception {
        AjaxResult ajax = AjaxResult.success();
//        System.out.println("ajax = " + smsLoginBody);
        smsLoginBody.setSmsCode(RsaUtils.decryptUserByPrivateKey(smsLoginBody.getSmsCode()));
        smsLoginBody.setPhone(RsaUtils.decryptUserByPrivateKey(smsLoginBody.getPhone()));
        smsLoginBody.setCountryCode(RsaUtils.decryptUserByPrivateKey(smsLoginBody.getCountryCode()));
        // 生成令牌
        String token = loginService.smsLogin(smsLoginBody.getCountryCode(), smsLoginBody.getPhone(), smsLoginBody.getSmsCode());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取注册验证码
     */
    @GetMapping("/getRegisterCode")
    public AjaxResult getRegisterCode(RegisterLoginCode registerLoginCode) throws Exception {
        registerLoginCode.setPhone(RsaUtils.decryptUserByPrivateKey(registerLoginCode.getPhone()));
        registerLoginCode.setCountryCode(RsaUtils.decryptUserByPrivateKey(registerLoginCode.getCountryCode()));
        String configInfoCache = configInfoService.getConfigInfoInCache(UserConfigKeyConstants.USER_LOGIN_CAPTCHA_ENABLED);
        boolean captchaEnabled = "true".equals(configInfoCache);
        String registerCode = loginService.getRegisterCode(registerLoginCode.getPhone(), registerLoginCode.getCountryCode(), registerLoginCode.getCode(), captchaEnabled, registerLoginCode.getUuid());
        System.err.println(registerCode);
        return AjaxResult.success("验证码发送成功");
    }

    /**
     * 注册
     *
     * @param registerLoginBody
     * @return
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterLoginBody registerLoginBody) throws Exception {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isEmpty(registerLoginBody.getSmsCode()) || StringUtils.isEmpty(registerLoginBody.getPhone()) || StringUtils.isEmpty(registerLoginBody.getPassword()) || StringUtils.isEmpty(registerLoginBody.getConfirmPassword())) {
            return AjaxResult.error("请输入手机号和密码");
        }
        registerLoginBody.setPhone(RsaUtils.decryptUserByPrivateKey(registerLoginBody.getPhone()));
        registerLoginBody.setCountryCode(RsaUtils.decryptUserByPrivateKey(registerLoginBody.getCountryCode()));
        registerLoginBody.setSmsCode(RsaUtils.decryptUserByPrivateKey(registerLoginBody.getSmsCode()));
        registerLoginBody.setPassword(RsaUtils.decryptUserByPrivateKey(registerLoginBody.getPassword()));
        registerLoginBody.setConfirmPassword(RsaUtils.decryptUserByPrivateKey(registerLoginBody.getConfirmPassword()));
        PasswordUtils.checkPasswordFormate(registerLoginBody.getPassword(), registerLoginBody.getConfirmPassword(), 8, 20);
        //校验验证码
        loginService.checkSmsCode(UserRedisConstants.USER_SMS_REGISTER_CODE, registerLoginBody.getCountryCode(), registerLoginBody.getPhone(), registerLoginBody.getSmsCode());
        //查询此用户是否存在
        AuthUserInfo authUserInfo = authUserInfoService.selectUserInfoByPhone(registerLoginBody.getPhone(), registerLoginBody.getCountryCode());
        if (StringUtils.isNotNull(authUserInfo)) {
            throw new RuntimeException("该手机已经注册");
        }
        AuthUserInfo userInfo = authUserInfoService.register(registerLoginBody);
        // 生成令牌
        String token = loginService.userInfoLogin(userInfo.getUserName(), registerLoginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @GetMapping("/getForgetPasswordCode")
    public AjaxResult getForgetPasswordCode(@Validated ForgetPasswordCode forgetPasswordCode) throws Exception {
        forgetPasswordCode.setPhone(RsaUtils.decryptUserByPrivateKey(forgetPasswordCode.getPhone()));
        forgetPasswordCode.setCountryCode(RsaUtils.decryptUserByPrivateKey(forgetPasswordCode.getCountryCode()));
        String configInfoCache = configInfoService.getConfigInfoInCache(UserConfigKeyConstants.USER_LOGIN_CAPTCHA_ENABLED);
        boolean captchaEnabled = "true".equals(configInfoCache);
        String registerCode = loginService.getForgetPasswordCode(forgetPasswordCode.getPhone(), forgetPasswordCode.getCountryCode(), forgetPasswordCode.getCode(), captchaEnabled, forgetPasswordCode.getUuid());
        System.err.println(registerCode);
        return AjaxResult.success("验证码发送成功");
    }

    @PostMapping("/forgetPassword")
    public AjaxResult forgetPassword(@RequestBody @Validated ForgetPasswordBody forgetPasswordBody) throws Exception {
        forgetPasswordBody.setConfirmPassword(RsaUtils.decryptUserByPrivateKey(forgetPasswordBody.getConfirmPassword()));
        forgetPasswordBody.setPassword(RsaUtils.decryptUserByPrivateKey(forgetPasswordBody.getPassword()));
        forgetPasswordBody.setSmsCode(RsaUtils.decryptUserByPrivateKey(forgetPasswordBody.getSmsCode()));
        forgetPasswordBody.setPhone(RsaUtils.decryptUserByPrivateKey(forgetPasswordBody.getPhone()));
        forgetPasswordBody.setCountryCode(RsaUtils.decryptUserByPrivateKey(forgetPasswordBody.getCountryCode()));
        PasswordUtils.checkPasswordFormate(forgetPasswordBody.getPassword(), forgetPasswordBody.getConfirmPassword(), 8, 20);
        //校验验证码
        loginService.checkSmsCode(UserRedisConstants.USER_SMS_FORGET_PASSWORD_CODE, forgetPasswordBody.getCountryCode(), forgetPasswordBody.getPhone(), forgetPasswordBody.getSmsCode());
        AuthUserInfo authUserInfo = authUserInfoService.forgetPassword(forgetPasswordBody);
        return AjaxResult.success("修改成功");
    }


    @PostMapping("/logout")
    public AjaxResult logout(HttpServletRequest request) {
        LoginUserInfo loginUser = UserInfoSecurityUtils.getLoginUser();
        LambdaUpdateWrapper<AuthUserInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(AuthUserInfo::getLastLoginTime, new Date())
                .set(AuthUserInfo::getLastLoginIp, IpUtils.getIpAddr(request))
                .eq(AuthUserInfo::getUserId, loginUser.getUser().getUserId());
        boolean update = authUserInfoService.update(null, updateWrapper);// 传入wrapper
        if (!update) {
            return AjaxResult.error("退出失败");
        }
        loginService.logout(request);
        return AjaxResult.success();
    }
}
