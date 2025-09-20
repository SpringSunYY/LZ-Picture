package com.lz.common.constant.picture;

/**
 * 图片模块统计常量
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-18  16:05
 * @Version: 1.0
 */
public class PictureStatisticsConstants {
    public static final String PICTURE_STATISTICS_PICTURE_HOT_KEY = "picture:statistics:picture:hot:minute";
    public static final Integer PICTURE_STATISTICS_PICTURE_HOT_EXPIRE_TIME = 60 * 60 * 24;
    //图片热门统计缓存key，日
    public static final String PICTURE_STATISTICS_PICTURE_HOT = "picture:statistics:picture:hot";
    public static final String PICTURE_STATISTICS_PICTURE_HOT_DAY_NAME = "图片热门统计日排行";
    //图片热门统计缓存key，周
    public static final String PICTURE_STATISTICS_PICTURE_HOT_WEEK_NAME = "图片热门统计周排行";
    //图片热门统计缓存key，月
    public static final String PICTURE_STATISTICS_PICTURE_HOT_MONTH_NAME = "图片热门统计月排行";
    //图片热门统计缓存key，年
    public static final String PICTURE_STATISTICS_PICTURE_HOT_YEAR_NAME = "图片热门统计年排行";
    //图片热门统计缓存key，总
    public static final String PICTURE_STATISTICS_PICTURE_HOT_TOTAL_KEY = "picture:statistics:picture:hot:total";
    public static final String PICTURE_STATISTICS_PICTURE_HOT_TOTAL_NAME = "图片热门统计总排行";
    /**
     * 搜索关键词
     */
    public static final String PICTURE_STATISTICS_SEARCHER_KEYWORD = "picture:statistics:searcher:keyword";
    public static final String PICTURE_STATISTICS_SEARCHER_KEYWORD_NAME = "搜索关键词-每日";
    public static final int PICTURE_STATISTICS_SEARCHER_KEYWORD_EXPIRE_TIME = 60 * 5;

    /**
     * 标签关键词
     */
    public static final String PICTURE_STATISTICS_TAG_KEYWORD = "picture:statistics:tag:keyword";
    public static final String PICTURE_STATISTICS_TAG_KEYWORD_NAME = "标签关键词-每日";
    public static final int PICTURE_STATISTICS_TAG_KEYWORD_EXPIRE_TIME = 60 * 5;
    /**
     * 图片状态
     */
    public static final String PICTURE_STATISTICS_PICTURE_STATUS = "picture:statistics:picture:status";
    public static final String PICTURE_STATISTICS_PICTURE_STATUS_NAME = "图片状态-每日";
    public static final int PICTURE_STATISTICS_PICTURE_STATUS_EXPIRE_TIME = 60 * 5;

    /**
     * 图片上传类型
     */
    public static final String PICTURE_STATISTICS_PICTURE_UPLOAD_TYPE = "picture:statistics:picture:upload:type";
    public static final String PICTURE_STATISTICS_PICTURE_UPLOAD_TYPE_NAME = "图片上传类型-每日";
    public static final int PICTURE_STATISTICS_PICTURE_UPLOAD_TYPE_EXPIRE_TIME = 60 * 5;

    /**
     * 空间文件总数
     */
    public static final String PICTURE_STATISTICS_SPACE_FILE_TOTAL = "picture:statistics:space:file:total";
    public static final String PICTURE_STATISTICS_SPACE_FILE_TOTAL_NAME = "空间文件总数-每日";
    public static final int PICTURE_STATISTICS_SPACE_FILE_TOTAL_EXPIRE_TIME = 60 * 5;

    /**
     * 空间文件大小
     */
    public static final String PICTURE_STATISTICS_SPACE_FILE_SIZE = "picture:statistics:space:file:size";
    public static final String PICTURE_STATISTICS_SPACE_FILE_SIZE_NAME = "空间文件大小-每日";
    public static final int PICTURE_STATISTICS_SPACE_FILE_SIZE_EXPIRE_TIME = 60 * 5;

    /**
     * 用户行为
     */
    public static final String PICTURE_STATISTICS_USER_BEHAVIOR = "picture:statistics:user:behavior";
    public static final String PICTURE_STATISTICS_USER_BEHAVIOR_NAME = "用户行为-每日";
    public static final int PICTURE_STATISTICS_USER_BEHAVIOR_EXPIRE_TIME = 60 * 5;

    /**
     * 图片下载
     */
    public static final String PICTURE_STATISTICS_PICTURE_DOWNLOAD = "picture:statistics:picture:download";
    public static final String PICTURE_STATISTICS_PICTURE_DOWNLOAD_NAME = "图片下载-每日";
    public static final int PICTURE_STATISTICS_PICTURE_DOWNLOAD_EXPIRE_TIME = 60 * 5;

    /**
     * 空间
     */
    public static final String PICTURE_STATISTICS_SPACE_ADD = "picture:statistics:space:add";
    public static final String PICTURE_STATISTICS_SPACE_ADD_NAME = "空间新增-每日";
    public static final int PICTURE_STATISTICS_SPACE_ADD_EXPIRE_TIME = 60 * 5;

    /**
     *  图片
     */
    public static final String PICTURE_STATISTICS_PICTURE_ADD = "picture:statistics:picture:add";
    public static final String PICTURE_STATISTICS_PICTURE_ADD_NAME = "图片新增-每日";
    public static final int PICTURE_STATISTICS_PICTURE_ADD_EXPIRE_TIME = 60 * 5;

    /**
     * 图片分类
     */
    public static final String PICTURE_STATISTICS_PICTURE_CATEGORY = "picture:statistics:picture:category";
    public static final String PICTURE_STATISTICS_PICTURE_CATEGORY_NAME = "图片分类-每日";
    public static final int PICTURE_STATISTICS_PICTURE_CATEGORY_EXPIRE_TIME = 60 * 5;
}
