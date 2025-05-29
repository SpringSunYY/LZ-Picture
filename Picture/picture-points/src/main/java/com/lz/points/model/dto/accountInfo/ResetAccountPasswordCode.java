package com.lz.points.model.dto.accountInfo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Project: Picture
 * Package: com.lz.points.model.sms
 * Author: YY
 * CreateTime: 2025-03-19  08:53
 * Description: 用户重置密码
 * Version: 1.0
 */
@Data
public class ResetAccountPasswordCode implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @NotEmpty(message = "用户编号不能为空")
    private String userId;

    /**
     * 国家代码
     */
    @NotEmpty(message = "国家代码不能为空")
    private String countryCode;

    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空")
    private String phone;
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
}
