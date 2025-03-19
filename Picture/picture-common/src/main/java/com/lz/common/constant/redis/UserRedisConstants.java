package com.lz.common.constant.redis;

/**
 * Project: Picture
 * Package: com.lz.common.constant
 * Author: YY
 * CreateTime: 2025-03-19  09:11
 * Description: UserRedisConstants
 * Version: 1.0
 */
public class UserRedisConstants {
    /**
     * 登录验证码
     */
    public static final String USER_SMS_LOGIN_CODE = "user:sms:login:code:";

    /**
     * 登录验证码超时时间
     */
    public static final Integer USER_SMS_LOGIN_CODE_EXPIRE_TIME = 5 * 60;
}
