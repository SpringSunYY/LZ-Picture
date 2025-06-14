package com.lz.userauth.model.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 用户登录对象
 *
 * @author YY
 */
@Data
public class UserInfoLoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    @NotEmpty(message = "用户密码不能为空")
    private String password;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 国家编码
     */
    private String countryCode;


    /**
     * 唯一标识
     */
    private String uuid;

}
