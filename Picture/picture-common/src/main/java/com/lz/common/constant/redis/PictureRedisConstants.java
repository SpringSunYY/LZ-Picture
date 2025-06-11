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
    public static final String PICTURE_RECOMMEND_DETAIL = "picture:recommend:detail";

    /**
     * 图片详情推荐缓存时间
     */
    public static final Integer PICTURE_RECOMMEND_DETAIL_EXPIRE_TIME = 10 * 60;

    /**
     * 图片热门推荐缓存
     */
    public static final String PICTURE_RECOMMEND_HOT = "picture:recommend:hot:";

    /**
     * 图片热门推荐缓存时间
     */
    public static final Integer PICTURE_RECOMMEND_HOT_EXPIRE_TIME = 10 * 60;

    /**
     * 图片推荐缓存分类-标签
     */
    public static final String PICTURE_RECOMMEND_CATEGORY_TAG = "picture:recommend:categoryTags";

    /**
     * 图片推荐缓存分类-标签缓存时间
     */
    public static final Integer PICTURE_RECOMMEND_CATEGORY_TAG_EXPIRE_TIME = 60 * 60 * 12;

    /**
     * 图片推荐缓存用户模型
     */
    public static final String PICTURE_RECOMMEND_PICTURE_MODEL = "picture:recommend:picture:model:";

    /**
     * 图片推荐缓存用户模型缓存时间
     */
    public static final Integer PICTURE_RECOMMEND_PICTURE_MODEL_EXPIRE_TIME = 60 * 60;

    /**
     * 图片推荐所有信息缓存
     */
    public static final String PICTURE_RECOMMEND_USER = "picture:recommend:user:";

    /**
     * 图片推荐所有信息缓存时间
     */
    public static final Integer PICTURE_RECOMMEND_USER_EXPIRE_TIME = 60 * 60;

}
