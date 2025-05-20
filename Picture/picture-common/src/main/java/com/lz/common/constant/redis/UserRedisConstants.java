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
     * 登录短信验证码
     */
    public static final String USER_SMS_LOGIN_CODE = "user:sms:login:code:";
    /**
     * 登录短信验证码超时时间
     */
    public static final Integer USER_SMS_LOGIN_CODE_EXPIRE_TIME = 5 * 60;
    /**
     * 注册短信验证码
     */
    public static final String USER_SMS_REGISTER_CODE = "user:sms:register:code:";

    /**
     * 注册验证码超时时间
     */
    public static final Integer USER_SMS_REGISTER_CODE_EXPIRE_TIME = 5 * 60;
    /**
     * 注册验证码
     */
    public static final String USER_SMS_FORGET_PASSWORD_CODE = "user:sms:forgetPassword:code:";

    /**
     * 忘记密码验证码超时时间
     */
    public static final Integer USER_SMS_FORGET_PASSWORD_CODE_EXPIRE_TIME = 5 * 60;

    /**
     * 用户自己的信息缓存保存信息
     */
    public static final String USER_INFO = "user:info:";
}
