package com.lz.framework.web.domain;

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
    private String password;


    /**
     * 唯一标识
     */
    private String uuid;

}
