package com.lz.web.controller.user;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.lz.common.constant.Constants;
import com.lz.common.constant.redis.ConfigRedisConstants;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import com.lz.userauth.model.domain.AuthUserInfo;
import com.lz.userauth.model.domain.LoginUserInfo;
import com.lz.userauth.model.domain.UserInfoLoginBody;
import com.lz.framework.web.service.UserInfoLoginService;
import com.lz.userauth.model.register.RegisterLoginBody;
import com.lz.userauth.model.sms.SmsLoginBody;
import com.lz.userauth.service.IAuthUserInfoService;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
    public AjaxResult customerLogin(@RequestBody UserInfoLoginBody loginBody) {
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
     * @param smsLoginBody
     * @return
     */
    @GetMapping("/getSmsLoginCode")
    public AjaxResult getSmsLoginCode(SmsLoginBody smsLoginBody) {
        String configInfoCache = configInfoService.getConfigInfoCache(ConfigRedisConstants.USER_LOGIN_CAPTCHA_ENABLED);
        boolean captchaEnabled = "true".equals(configInfoCache);
        smsLoginBody.setCaptchaEnabled(captchaEnabled);
        String smsLoginCode = loginService.getSmsCode(smsLoginBody.getPhone(), smsLoginBody.getCountryCode(), smsLoginBody.getCode(), smsLoginBody.isCaptchaEnabled(), smsLoginBody.getUuid());
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
    public AjaxResult smsLogin(@RequestBody SmsLoginBody smsLoginBody) {
        AjaxResult ajax = AjaxResult.success();
        System.out.println("ajax = " + smsLoginBody);
        // 生成令牌
        String token = loginService.smsLogin(smsLoginBody.getPhone(), smsLoginBody.getCountryCode(), smsLoginBody.getSmsCode());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取注册验证码
     */
    @GetMapping("/getRegisterCode")
    public AjaxResult getRegisterCode(RegisterLoginBody registerLoginBody) {
        String configInfoCache = configInfoService.getConfigInfoCache(ConfigRedisConstants.USER_LOGIN_CAPTCHA_ENABLED);
        boolean captchaEnabled = "true".equals(configInfoCache);
        String registerCode = loginService.getRegisterCode(registerLoginBody.getPhone(), registerLoginBody.getCountryCode(), registerLoginBody.getCode(), captchaEnabled, registerLoginBody.getUuid());
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
    public AjaxResult register(@RequestBody RegisterLoginBody registerLoginBody) {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isEmpty(registerLoginBody.getSmsCode()) || StringUtils.isEmpty(registerLoginBody.getPhone()) || StringUtils.isEmpty(registerLoginBody.getPassword()) || StringUtils.isEmpty(registerLoginBody.getConfirmPassword())) {
            return AjaxResult.error("请输入手机号和密码");
        }
        //校验两次密码是否正确
        if (!registerLoginBody.getPassword().equals(registerLoginBody.getConfirmPassword())) {
            return AjaxResult.error("两次密码不一致");
        }
        //校验验证码
        loginService.checkRegisterCode(registerLoginBody.getPhone(), registerLoginBody.getCountryCode(), registerLoginBody.getSmsCode());
        //查询此用户是否存在
        AuthUserInfo authUserInfo = authUserInfoService.selectUserInfoByPhone(registerLoginBody.getPhone(), registerLoginBody.getCountryCode());
        if (StringUtils.isNotNull(authUserInfo)) {
            AjaxResult.error("用户已存在");
        }
        AuthUserInfo userInfo = authUserInfoService.register(registerLoginBody);
        // 生成令牌
        String token = loginService.userInfoLogin(userInfo.getUserName(), registerLoginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
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
