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
}
