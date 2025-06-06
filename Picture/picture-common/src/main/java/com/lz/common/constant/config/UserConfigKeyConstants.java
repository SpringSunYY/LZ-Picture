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

    /**
     * 图片推荐分类分数占比
     */
    public static final String PICTURE_RECOMMEND_VIEW_CATEGORY = "picture:recommend:view:category";

    /**
     * 图片推荐标签分数占比
     */
    public static final String PICTURE_RECOMMEND_VIEW_TAG = "picture:recommend:view:tag";

    /**
     * 图片推荐时间衰减
     */
    public static final String PICTURE_RECOMMEND_VIEW_TIME_DECAY = "picture:recommend:view:time:decay";

    /**
     * 图片推荐下载分类分数占比
     */
    public static final String PICTURE_RECOMMEND_DOWNLOAD_CATEGORY = "picture:recommend:download:category";

    /**
     * 图片推荐下载标签分数占比
     */
    public static final String PICTURE_RECOMMEND_DOWNLOAD_TAG = "picture:recommend:download:tag";

    /**
     * 图片推荐下载时间衰减
     */
    public static final String PICTURE_RECOMMEND_TIME_DOWNLOAD_DECAY = "picture:recommend:download:time:decay";

    /**
     * 图片推荐行为分类分数占比
     */
    public static final String PICTURE_RECOMMEND_BEHAVIOR_CATEGORY = "picture:recommend:behavior:category";

    /**
     * 图片推荐行为标签分数占比
     */
    public static final String PICTURE_RECOMMEND_BEHAVIOR_TAG = "picture:recommend:behavior:tag";

    /**
     * 图片推荐行为时间衰减
     */
    public static final String PICTURE_RECOMMEND_TIME_BEHAVIOR_DECAY = "picture:recommend:behavior:time:decay";

    /**
     * 图片推荐浏览查询数量
     */
    public static final String PICTURE_RECOMMEND_VIEW_LIMIT = "picture:recommend:view:limit";

    /**
     * 图片推荐行为查询数量
     */
    public static final String PICTURE_RECOMMEND_BEHAVIOR_LIMIT = "picture:recommend:behavior:limit";

    /**
     * 图片推荐下载查询数量
     */
    public static final String PICTURE_RECOMMEND_DOWNLOAD_LIMIT = "picture:recommend:download:limit";

    /**
     * 图片推荐缓存超时时间
     */
    public static final String PICTURE_RECOMMEND_TIMEOUT = "picture:recommend:timeout";

    /**
     * 图片推荐不可重复超时时间
     */
    public static final String PICTURE_RECOMMEND_NOT_REPEAT_TIMEOUT = "picture:recommend:not:repeat:timeout";

}
