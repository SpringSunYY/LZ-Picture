package com.lz.web.controller.user;

import com.lz.common.constant.Constants;
import com.lz.common.constant.redis.ConfigRedisConstants;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.core.domain.model.LoginUser;
import com.lz.config.service.IConfigInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import com.lz.userauth.model.domain.AuthUserInfo;
import com.lz.userauth.model.domain.LoginUserInfo;
import com.lz.userauth.model.domain.UserInfoLoginBody;
import com.lz.framework.web.service.UserInfoLoginService;
import com.lz.userauth.model.sms.SmsLoginBody;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult customerLogin(@RequestBody UserInfoLoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.userInfoLogin(loginBody.getUsername(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

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

    @GetMapping("/getSmsLoginCode")
    public AjaxResult getSmsLoginCode(SmsLoginBody smsLoginBody) {
        String configInfoCache = configInfoService.getConfigInfoCache(ConfigRedisConstants.USER_LOGIN_CAPTCHA_ENABLED);
        boolean captchaEnabled = "true".equals(configInfoCache);
        smsLoginBody.setCaptchaEnabled(captchaEnabled);
        String smsLoginCode = loginService.getSmsLoginCode(smsLoginBody);
        System.err.println(smsLoginCode);
        return AjaxResult.success("验证码发送成功");
    }

    @PostMapping("/smsLogin")
    public AjaxResult smsLogin(@RequestBody SmsLoginBody smsLoginBody) {
        AjaxResult ajax = AjaxResult.success();
        System.out.println("ajax = " + smsLoginBody);
        // 生成令牌
        String token = loginService.smsLogin(smsLoginBody.getPhone(), smsLoginBody.getCountryCode(), smsLoginBody.getSmsCode());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }
}
