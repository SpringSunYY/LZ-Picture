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

    /**
     * 用户未读信息数量
     */
    public static final String USER_INFORM_UNREAD_COUNT = "user:inform:unread:count:";

    /**
     * 用户未读信息数量超时时间
     */
    public static final Integer USER_INFORM_UNREAD_COUNT_EXPIRE_TIME = 60 * 60 * 24;

    /**
     * 用户通知信息缓存保存信息
     */
    public static final String USER_INFORM_DETAIL = "user:inform:detail:";
    /**
     * 用户通知信息缓存超时时间
     */
    public static final Integer USER_INFORM_DETAIL_EXPIRE_TIME = 5 * 60;
}
