package com.lz.common.constant.config;

/**
 * Project: Picture
 * Package: com.lz.common.constant.redis
 * Author: YY
 * CreateTime: 2025-02-28  21:34
 * Description: UserConfigKeyConstants
 * Version: 1.0
 */
public class UserConfigKeyConstants {
    /**
     * 是否需要验证码
     */
    public static final String USER_LOGIN_CAPTCHA_ENABLED = "user:login:captchaEnabled";
    /**
     * 空间最大文件数
     */
    public static final String PICTURE_SPACE_MAX_COUNT = "picture:space:maxCount";

    /**
     * 空间最大容量
     */
    public static final String PICTURE_SPACE_MAX_SIZE = "picture:space:maxSize";

    /**
     * 用户最大团队空间数
     */
    public static final String PICTURE_SPACE_MAX_1 = "picture:space:max:1";

    /**
     * 用户最大个人空间数
     */
    public static final String PICTURE_SPACE_MAX_2 = "picture:space:max:2";

    /**
     * 封面图片压缩倍率百分比
     */
    public static final String PICTURE_SPACE_AVATAR_P = "picture:space:avatar:p";

    /**
     * 首页固定高度
     */
    public static final String PICTURE_INDEX_HEIGHT = "picture:index:height";

    /**
     * 首页图片压缩倍率百分比
     */
    public static final String PICTURE_INDEX_P = "picture:index:p";
    /**
     * 图片所需积分最大值
     */
    public static final String PICTURE_POINTS_MAX = "picture:points:max";

    /**
     * 图片所需积分最小值
     */
    public static final String PICTURE_POINTS_MIN = "picture:points:min";

    /**
     * 用户查看自己原图密钥时间
     */
    public static final String PICTURE_LOOK_ORIGINAL_TIMEOUT = "picture:look:original:timeout";

    /**
     * 用户自己浏览记录、行为记录封面压缩倍率百分比
     */
    public static final String PICTURE_COVER_P = "picture:cover:p";

    /**
     * 用户账户密码免校验时间
     */
    public static final String POINTS_ACCOUNT_VERIFY_PASSWORD_TIMEOUT = "points:account:verify:password:timeout";

    /**
     * 图片下载官方分成比例
     */
    public static final String PICTURE_DOWNLOAD_OFFICIAL_PROPORTION = "picture:download:official:proportion";
    /**
     * 图片下载空间分成比例
     */
    public static final String PICTURE_DOWNLOAD_SPACE_PROPORTION = "picture:download:space:proportion";
}
