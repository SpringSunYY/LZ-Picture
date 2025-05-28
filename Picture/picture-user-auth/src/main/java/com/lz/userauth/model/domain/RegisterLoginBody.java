package com.lz.userauth.model.domain;

import lombok.Data;

/**
 * Project: Picture
 * Package: com.lz.userauth.model.sms
 * Author: YY
 * CreateTime: 2025-03-19  08:53
 * Description: SmsLoginBody
 * Version: 1.0
 */
@Data
public class RegisterLoginBody {
    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * smsCode
     */
    private String smsCode;
    /**
     * 验证码
     */
    private String code;

    /**
     * 是否需要验证码
     */
    private boolean captchaEnabled;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String confirmPassword;

    /**
     * 偏好语言
     */
    private String preferredLanguageLocale;
}
