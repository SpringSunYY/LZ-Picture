package com.lz.common.constant.redis;

/**
 * Project: Picture
 * Package: com.lz.common.constant.redis
 * Author: YY
 * CreateTime: 2025-04-11  17:03
 * Description: PictureRedisConstants
 * Version: 1.0
 */
public class PictureRedisConstants {
    /**
     * 图片详细信息
     */
    public static final String PICTURE_PICTURE_DETAIL = "picture:picture:detail:";

    /**
     * 用户行为日志缓存
     */
    public static final String PICTURE_USER_BEHAVIOR = "picture:user:behavior:";

    /**
     * 图片搜索建议缓存
     */
    public static final String PICTURE_SEARCH_SUGGESTION = "picture:search:suggestion";

    /**
     * 图片搜索建议缓存时间
     */
    public static final Integer PICTURE_SEARCH_SUGGESTION_EXPIRE_TIME = 5 * 60;

    /**
     * 图片搜索推荐
     */
    public static final String PICTURE_SEARCH_RECOMMEND = "picture:search:recommend";

    /**
     * 图片搜索推荐缓存时间
     */
    public static final Integer PICTURE_SEARCH_RECOMMEND_EXPIRE_TIME = 5 * 60;

    /**
     * 图片详情推荐缓存
     */
    public static final String PICTURE_DETAIL_RECOMMEND = "picture:detail:recommend:";

    /**
     * 图片详情推荐缓存时间
     */
    public static final Integer PICTURE_DETAIL_RECOMMEND_EXPIRE_TIME = 10 * 60;
}
