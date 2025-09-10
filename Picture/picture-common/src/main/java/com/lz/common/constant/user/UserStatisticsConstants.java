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
}
