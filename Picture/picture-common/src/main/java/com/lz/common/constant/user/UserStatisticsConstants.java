package com.lz.common.constant.user;

/**
 * 用户统计常量
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-10  16:11
 * @Version: 1.0
 */
public class UserStatisticsConstants {
    /**
     * 用户注册
     */
    public static final String USER_STATISTICS_REGISTER_DAY = "user:statistics:register:day";
    /**
     * 用户注册
     */
    public static final String USER_STATISTICS_REGISTER_DAY_NAME = "用户注册-每日";

    /**
     * 用户注册缓存时间
     */
    public static final int USER_STATISTICS_REGISTER_DAY_EXPIRE_TIME = 60 * 5;

    /**
     * 用户性别
     */
    public static final String USER_STATISTICS_SEX = "user:statistics:sex";
    /**
     * 用户性别
     */
    public static final String USER_STATISTICS_SEX_NAME = "用户性别";
    /**
     * 用户性别缓存时间
     */
    public static final int USER_STATISTICS_SEX_EXPIRE_TIME = 60 * 5;
    /**
     * 用户年龄比例
     */
    public static final String USER_STATISTICS_AGE = "user:statistics:age";
    /**
     * 用户年龄比例
     */
    public static final String USER_STATISTICS_AGE_NAME = "用户年龄比例";
    /**
     * 用户年龄比例缓存时间
     */
    public static final int USER_STATISTICS_AGE_EXPIRE_TIME = 60 * 5;

    /**
     * 用户登录-每日
     */
    public static final String USER_STATISTICS_LOGIN_DAY = "user:statistics:login:day";

    public static final String USER_STATISTICS_LOGIN_DAY_NAME = "用户登录-每日";

    public static final int USER_STATISTICS_LOGIN_DAY_EXPIRE_TIME = 60 * 5;

    /**
     * 用户消息发送-每日
     */
    public static final String USER_STATISTICS_INFORM_DAY = "user:statistics:inform:day";
    public static final String USER_STATISTICS_INFORM_DAY_NAME = "用户消息发送-每日";
    public static final int USER_STATISTICS_INFORM_DAY_EXPIRE_TIME = 60 * 5;

    /**
     * 用户总数
     */
    public static final String USER_STATISTICS_COUNT = "user:statistics:count";
    public static final String USER_STATISTICS_COUNT_NAME = "用户总数-每日";
    public static final int USER_STATISTICS_COUNT_EXPIRE_TIME = 60 * 5;

    /**
     * 用户在线总数
     */
    public static final String USER_STATISTICS_ONLINE_COUNT = "user:statistics:online:count";
    public static final String USER_STATISTICS_ONLINE_COUNT_NAME = "用户在线总数-每日";
    public static final int USER_STATISTICS_ONLINE_COUNT_EXPIRE_TIME = 60 * 5;

    /**
     * 用户地理位置
     */
    public static final String USER_STATISTICS_LOCATION = "user:statistics:location";
    public static final String USER_STATISTICS_LOCATION_NAME = "用户地理位置-每日";
    public static final int USER_STATISTICS_LOCATION_EXPIRE_TIME = 60 * 5;

    /**
     * 用户登录地理位置
     */
    public static final String USER_STATISTICS_LOGIN_LOCATION = "user:statistics:login:location";
    public static final String USER_STATISTICS_LOGIN_LOCATION_NAME = "用户登录地理位置-每日";
    public static final int USER_STATISTICS_LOGIN_LOCATION_EXPIRE_TIME = 60 * 5;
}
