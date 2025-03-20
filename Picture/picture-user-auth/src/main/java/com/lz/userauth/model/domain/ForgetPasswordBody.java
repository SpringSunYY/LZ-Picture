package com.lz.userauth.model.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Project: Picture
 * Package: com.lz.userauth.model.sms
 * Author: YY
 * CreateTime: 2025-03-19  08:53
 * Description: ForgetPasswordBody
 * 忘记密码实体
 * Version: 1.0
 */
@Data
public class ForgetPasswordBody {
    /**
     * 国家代码
     */
    @NotNull(message ="国家代码不能为空")
    private String countryCode;

    /**
     * 手机号码
     */
    @NotNull(message ="手机号码不能为空")
    private String phone;

    /**
     * smsCode
     */
    @NotNull(message ="短信验证码不能为空")
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
    @NotNull(message ="密码不能为空")
    private String password;

    /**
     * 确认密码
     */
    @NotNull(message ="确认密码不能为空")
    private String confirmPassword;
}
