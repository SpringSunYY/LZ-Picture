package com.lz.config.utils;

import com.lz.common.core.redis.RedisCache;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.StringUtils;
import com.lz.config.model.domain.ConfigInfo;
import com.lz.config.service.IConfigInfoService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.redis.UserConfigRedisConstants.CONFIG_CONFIG_INFO_KEY;

/**
 * 配置工具类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-14  18:20
 * @Version: 1.0
 */
@Component
@Slf4j
public class ConfigInfoUtils {

    @Resource
    private IConfigInfoService configInfoService;

    @Resource
    private RedisCache redisCache;

    //region 初始化缓存
    @PostConstruct
    public void init() {
        //初始化缓存
        initConfigInfoCache();
        startRefreshTask();
    }

    public int initConfigInfoCache() {
        List<ConfigInfo> configInfoList = configInfoService.selectConfigInfoList(null);
        for (ConfigInfo configInfo : configInfoList) {
            redisCache.deleteObject(CONFIG_CONFIG_INFO_KEY + configInfo.getConfigIsIn() + COMMON_SEPARATOR_CACHE + configInfo.getConfigKey());
            redisCache.setCacheObject(CONFIG_CONFIG_INFO_KEY + configInfo.getConfigIsIn() + COMMON_SEPARATOR_CACHE + configInfo.getConfigKey(), configInfo.getConfigValue());
        }
        refreshConfig();
        return 1;
    }
    //endregion
    //region 获取配置信息
    /**
     * 是否需要验证码
     */
    public static final String USER_LOGIN_CAPTCHA_ENABLED_KEY = "user:login:captchaEnabled";
    public static Boolean USER_LOGIN_CAPTCHA_ENABLED_VALUE = true;
    /**
     * 空间最大文件数
     */
    public static final String PICTURE_SPACE_MAX_COUNT_KEY = "picture:space:maxCount";
    public static Long PICTURE_SPACE_MAX_COUNT_VALUE = 300L;

    /**
     * 空间最大容量
     */
    public static final String PICTURE_SPACE_MAX_SIZE_KEY = "picture:space:maxSize";
    public static Long PICTURE_SPACE_MAX_SIZE_VALUE = 1L;

    /**
     * 用户最大团队空间数
     */
    public static final String PICTURE_SPACE_MAX_1_KEY = "picture:space:max:1";
    public static Integer PICTURE_SPACE_MAX_1_VALUE = 5;

    /**
     * 用户最大个人空间数
     */
    public static final String PICTURE_SPACE_MAX_2_KEY = "picture:space:max:2";
    public static Integer PICTURE_SPACE_MAX_2_VALUE = 5;

    /**
     * 封面图片压缩倍率百分比
     */
    public static final String PICTURE_SPACE_AVATAR_P_KEY = "picture:space:avatar:p";
    public static Integer PICTURE_SPACE_AVATAR_P_VALUE = 50;

    /**
     * 首页固定高度
     */
    public static final String PICTURE_INDEX_HEIGHT_KEY = "picture:index:height";
    public static Integer PICTURE_INDEX_HEIGHT_VALUE = 500;

    /**
     * 首页图片压缩倍率百分比
     */
    public static final String PICTURE_INDEX_P_KEY = "picture:index:p";
    public static Integer PICTURE_INDEX_P_VALUE = 30;

    /**
     * 图片所需积分最大值
     */
    public static final String PICTURE_POINTS_MAX_KEY = "picture:points:max";
    public static Integer PICTURE_POINTS_MAX_VALUE = 1000;

    /**
     * 图片所需积分最小值
     */
    public static final String PICTURE_POINTS_MIN_KEY = "picture:points:min";
    public static Integer PICTURE_POINTS_MIN_VALUE = 0;

    /**
     * 用户查看自己原图密钥时间
     */
    public static final String PICTURE_LOOK_ORIGINAL_TIMEOUT_KEY = "picture:look:original:timeout";
    public static Long PICTURE_LOOK_ORIGINAL_TIMEOUT_VALUE = 60 * 5L;

    /**
     * 用户自己浏览记录、行为记录等表格封面压缩倍率百分比
     */
    public static final String PICTURE_COVER_P_KEY = "picture:cover:p";
    public static Integer PICTURE_COVER_P_VALUE = 20;

    /**
     * 用户账户密码免校验时间
     */
    public static final String POINTS_ACCOUNT_VERIFY_PASSWORD_TIMEOUT_KEY = "points:account:verify:password:timeout";
    public static Integer POINTS_ACCOUNT_VERIFY_PASSWORD_TIMEOUT_VALUE = 60 * 60;

    /**
     * 图片下载官方分成比例
     */
    public static final String PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_KEY = "picture:download:official:proportion";
    public static Double PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_VALUE = 0.3;
    /**
     * 图片下载空间分成比例
     */
    public static final String PICTURE_DOWNLOAD_SPACE_PROPORTION_KEY = "picture:download:space:proportion";
    public static Double PICTURE_DOWNLOAD_SPACE_PROPORTION_VALUE = 0.2;

    /**
     * 图片推荐分类分数占比
     */
    public static final String PICTURE_RECOMMEND_VIEW_CATEGORY_KEY = "picture:recommend:view:category";
    public static Integer PICTURE_RECOMMEND_VIEW_CATEGORY_VALUE = 1;

    /**
     * 图片推荐标签分数占比
     */
    public static final String PICTURE_RECOMMEND_VIEW_TAG_KEY = "picture:recommend:view:tag";
    public static Integer PICTURE_RECOMMEND_VIEW_TAG_VALUE = 2;

    /**
     * 图片推荐时间衰减
     */
    public static final String PICTURE_RECOMMEND_VIEW_TIME_DECAY_KEY = "picture:recommend:view:time:decay";
    public static Double PICTURE_RECOMMEND_VIEW_TIME_DECAY_VALUE = 0.8;

    /**
     * 图片推荐下载分类分数占比
     */
    public static final String PICTURE_RECOMMEND_DOWNLOAD_CATEGORY_KEY = "picture:recommend:download:category";
    public static Integer PICTURE_RECOMMEND_DOWNLOAD_CATEGORY_VALUE = 2;

    /**
     * 图片推荐下载标签分数占比
     */
    public static final String PICTURE_RECOMMEND_DOWNLOAD_TAG_KEY = "picture:recommend:download:tag";
    public static Integer PICTURE_RECOMMEND_DOWNLOAD_TAG_VALUE = 1;

    /**
     * 图片推荐下载时间衰减
     */
    public static final String PICTURE_RECOMMEND_TIME_DOWNLOAD_DECAY_KEY = "picture:recommend:download:time:decay";
    public static Double PICTURE_RECOMMEND_TIME_DOWNLOAD_DECAY_VALUE = 0.8;

    /**
     * 图片推荐行为分类分数占比
     */
    public static final String PICTURE_RECOMMEND_BEHAVIOR_CATEGORY_KEY = "picture:recommend:behavior:category";
    public static Integer PICTURE_RECOMMEND_BEHAVIOR_CATEGORY_VALUE = 1;

    /**
     * 图片推荐行为标签分数占比
     */
    public static final String PICTURE_RECOMMEND_BEHAVIOR_TAG_KEY = "picture:recommend:behavior:tag";
    public static Integer PICTURE_RECOMMEND_BEHAVIOR_TAG_VALUE = 2;

    /**
     * 图片推荐行为时间衰减
     */
    public static final String PICTURE_RECOMMEND_TIME_BEHAVIOR_DECAY_KEY = "picture:recommend:behavior:time:decay";
    public static Double PICTURE_RECOMMEND_TIME_BEHAVIOR_DECAY_VALUE = 0.8;

    /**
     * 图片推荐浏览查询数量
     */
    public static final String PICTURE_RECOMMEND_VIEW_LIMIT_KEY = "picture:recommend:view:limit";
    public static Integer PICTURE_RECOMMEND_VIEW_LIMIT_VALUE = 500;

    /**
     * 图片推荐行为查询数量
     */
    public static final String PICTURE_RECOMMEND_BEHAVIOR_LIMIT_KEY = "picture:recommend:behavior:limit";
    public static Integer PICTURE_RECOMMEND_BEHAVIOR_LIMIT_VALUE = 200;

    /**
     * 图片推荐下载查询数量
     */
    public static final String PICTURE_RECOMMEND_DOWNLOAD_LIMIT_KEY = "picture:recommend:download:limit";
    public static Integer PICTURE_RECOMMEND_DOWNLOAD_LIMIT_VALUE = 100;

    /**
     * 图片推荐缓存超时时间
     */
    public static final String PICTURE_RECOMMEND_TIMEOUT_KEY = "picture:recommend:timeout";
    public static Integer PICTURE_RECOMMEND_TIMEOUT_VALUE = 60 * 30;

    /**
     * 图片推荐不可重复超时时间
     */
    public static final String PICTURE_RECOMMEND_NOT_REPEAT_TIMEOUT_KEY = "picture:recommend:not:repeat:timeout";
    public static Integer PICTURE_RECOMMEND_NOT_REPEAT_TIMEOUT_VALUE = 60 * 30;

    /**
     * 图片推荐分类返回最大值
     */
    public static final String PICTURE_RECOMMEND_CATEGORY_MAX_KEY = "picture:recommend:category:max";
    public static Integer PICTURE_RECOMMEND_CATEGORY_MAX_VALUE = 5000;

    /**
     * 图片推荐标签返回最大值
     */
    public static final String PICTURE_RECOMMEND_TAG_MAX_KEY = "picture:recommend:tag:max";
    public static Integer PICTURE_RECOMMEND_TAG_MAX_VALUE = 5000;

    /**
     * 图片推荐分类返回权重
     */
    public static final String PICTURE_RECOMMEND_CATEGORY_WEIGHT_KEY = "picture:recommend:category:weight";
    public static Double PICTURE_RECOMMEND_CATEGORY_WEIGHT_VALUE = 0.3;

    /**
     * 图片推荐标签返回权重
     */
    public static final String PICTURE_RECOMMEND_TAG_WEIGHT_KEY = "picture:recommend:tag:weight";
    public static Double PICTURE_RECOMMEND_TAG_WEIGHT_VALUE = 0.7;

    /**
     * 图片推荐行为缓存时间
     */
    public static final String PICTURE_RECOMMEND_BEHAVIOR_CACHE_TIMEOUT_KEY = "picture:recommend:behavior:cache:timeout";
    public static Integer PICTURE_RECOMMEND_BEHAVIOR_CACHE_TIMEOUT_VALUE = 60 * 20;
    /**
     * 图片推荐行为缓存数阈值
     */
    public static final String PICTURE_RECOMMEND_BEHAVIOR_CACHE_THRESHOLD_KEY = "picture:recommend:behavior:cache:threshold";
    public static Integer PICTURE_RECOMMEND_BEHAVIOR_CACHE_THRESHOLD_VALUE = 10;

    /**
     * 用户推荐下载缓存时间
     */
    public static final String PICTURE_RECOMMEND_DOWNLOAD_CACHE_TIMEOUT_KEY = "picture:recommend:download:cache:timeout";
    public static Integer PICTURE_RECOMMEND_DOWNLOAD_CACHE_TIMEOUT_VALUE = 60 * 10;
    /**
     * 用户推荐下载缓存数阈值
     */
    public static final String PICTURE_RECOMMEND_DOWNLOAD_CACHE_THRESHOLD_KEY = "picture:recommend:download:cache:threshold";
    public static Integer PICTURE_RECOMMEND_DOWNLOAD_CACHE_THRESHOLD_VALUE = 5;
    /**
     * 用户推荐浏览缓存时间
     */
    public static final String PICTURE_RECOMMEND_VIEW_CACHE_TIMEOUT_KEY = "picture:recommend:view:cache:timeout";
    public static Integer PICTURE_RECOMMEND_VIEW_CACHE_TIMEOUT_VALUE = 60 * 30;
    /**
     * 用户推荐浏览缓存数阈值
     */
    public static final String PICTURE_RECOMMEND_VIEW_CACHE_THRESHOLD_KEY = "picture:recommend:view:cache:threshold";
    public static Integer PICTURE_RECOMMEND_VIEW_CACHE_THRESHOLD_VALUE = 20;
    /**
     * 空间扩容-数量
     */
    public static final String PICTURE_SPACE_DILATATION_COUNT_KEY = "picture:space:dilatation:count";
    public static Long PICTURE_SPACE_DILATATION_COUNT_VALUE = 1L;
    /**
     * 空间扩容-容量
     */
    public static final String PICTURE_SPACE_DILATATION_SIZE_KEY = "picture:space:dilatation:size";
    public static Long PICTURE_SPACE_DILATATION_SIZE_VALUE = 1000L;
    /**
     * 空间扩容-人数
     */
    public static final String PICTURE_SPACE_DILATATION_MEMBER_KEY = "picture:space:dilatation:member";
    public static Long PICTURE_SPACE_DILATATION_MEMBER_VALUE = 100L;
    /**
     * 图片热门统计用户行为分数-浏览
     */
    public static final String PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_VIEW_KEY = "picture:statistics:hot:behavior:score:view";
    public static double PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_VIEW_VALUE = 1.0;
    /**
     * 图片热门统计用户行为分数-下载
     */
    public static final String PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_DOWNLOAD_KEY = "picture:statistics:hot:behavior:score:download";
    public static Double PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_DOWNLOAD_VALUE = 20.0;
    /**
     * 图片热门统计用户行为分数-点赞
     */
    public static final String PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_LIKE_KEY = "picture:statistics:hot:behavior:score:like";
    public static Double PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_LIKE_VALUE = 3.0;
    /**
     * 图片热门统计用户行为分数-收藏
     */
    public static final String PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_COLLECT_KEY = "picture:statistics:hot:behavior:score:collect";
    public static Double PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_COLLECT_VALUE = 6.0;
    /**
     * 图片热门统计用户行为分数-转发
     */
    public static final String PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_SHARE_KEY = "picture:statistics:hot:behavior:score:share";
    public static Double PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_SHARE_VALUE = 12.0;
    /**
     * 图片热门统计日排行
     */
    public static final String PICTURE_STATISTICS_HOT_DAY_RANK_KEY = "picture:statistics:hot:day:rank";
    public static Integer PICTURE_STATISTICS_HOT_DAY_RANK_VALUE = 5000;
    /**
     * 图片热门统计周排行
     */
    public static final String PICTURE_STATISTICS_HOT_WEEK_RANK_KEY = "picture:statistics:hot:week:rank";
    public static Integer PICTURE_STATISTICS_HOT_WEEK_RANK_VALUE = 5000;
    /**
     * 图片热门统计月排行
     */
    public static final String PICTURE_STATISTICS_HOT_MONTH_RANK_KEY = "picture:statistics:hot:month:rank";
    public static Integer PICTURE_STATISTICS_HOT_MONTH_RANK_VALUE = 5000;
    /**
     * 图片热门统计年排行
     */
    public static final String PICTURE_STATISTICS_HOT_YEAR_RANK_KEY = "picture:statistics:hot:year:rank";
    public static Integer PICTURE_STATISTICS_HOT_YEAR_RANK_VALUE = 5000;
    /**
     * 图片热门统计总排行
     */
    public static final String PICTURE_STATISTICS_HOT_TOTAL_RANK_KEY = "picture:statistics:hot:total:rank";
    public static Integer PICTURE_STATISTICS_HOT_TOTAL_RANK_VALUE = 10000;

    /**
     * 图片分类AI ID
     */
    public static final String PICTURE_CATEGORY_AI_ID_KEY = "picture:category:ai:id";
    public static String PICTURE_CATEGORY_AI_ID_VALUE = "C_AI";


    private static final long lastCacheRefreshTime = 0;
    private static final long CACHE_REFRESH_INTERVAL = 3600 * 1000; // 1小时刷新一次
    private ScheduledExecutorService scheduler = null;

    private void startRefreshTask() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::refreshConfig, 1, 1, TimeUnit.HOURS);
    }

    private void refreshConfig() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheRefreshTime < CACHE_REFRESH_INTERVAL) {
            return;
        }
        USER_LOGIN_CAPTCHA_ENABLED_VALUE = getBool(USER_LOGIN_CAPTCHA_ENABLED_KEY);
        PICTURE_SPACE_MAX_COUNT_VALUE = getLong(PICTURE_SPACE_MAX_COUNT_KEY);
        PICTURE_SPACE_MAX_SIZE_VALUE = getLong(PICTURE_SPACE_MAX_SIZE_KEY);
        PICTURE_SPACE_MAX_1_VALUE = getInt(PICTURE_SPACE_MAX_1_KEY);
        PICTURE_SPACE_MAX_2_VALUE = getInt(PICTURE_SPACE_MAX_2_KEY);
        PICTURE_SPACE_AVATAR_P_VALUE = getInt(PICTURE_SPACE_AVATAR_P_KEY);
        PICTURE_INDEX_HEIGHT_VALUE = getInt(PICTURE_INDEX_HEIGHT_KEY);
        PICTURE_INDEX_P_VALUE = getInt(PICTURE_INDEX_P_KEY);
        PICTURE_POINTS_MAX_VALUE = getInt(PICTURE_POINTS_MAX_KEY);
        PICTURE_POINTS_MIN_VALUE = getInt(PICTURE_POINTS_MIN_KEY);
        PICTURE_LOOK_ORIGINAL_TIMEOUT_VALUE = getLong(PICTURE_LOOK_ORIGINAL_TIMEOUT_KEY);
        PICTURE_COVER_P_VALUE = getInt(PICTURE_COVER_P_KEY);
        POINTS_ACCOUNT_VERIFY_PASSWORD_TIMEOUT_VALUE = getInt(POINTS_ACCOUNT_VERIFY_PASSWORD_TIMEOUT_KEY);
        PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_VALUE = getDouble(PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_KEY);
        PICTURE_DOWNLOAD_SPACE_PROPORTION_VALUE = getDouble(PICTURE_DOWNLOAD_SPACE_PROPORTION_KEY);
        PICTURE_RECOMMEND_VIEW_CATEGORY_VALUE = getInt(PICTURE_RECOMMEND_VIEW_CATEGORY_KEY);
        PICTURE_RECOMMEND_VIEW_TAG_VALUE = getInt(PICTURE_RECOMMEND_VIEW_TAG_KEY);
        PICTURE_RECOMMEND_VIEW_TIME_DECAY_VALUE = getDouble(PICTURE_RECOMMEND_VIEW_TIME_DECAY_KEY);
        PICTURE_RECOMMEND_DOWNLOAD_CATEGORY_VALUE = getInt(PICTURE_RECOMMEND_DOWNLOAD_CATEGORY_KEY);
        PICTURE_RECOMMEND_DOWNLOAD_TAG_VALUE = getInt(PICTURE_RECOMMEND_DOWNLOAD_TAG_KEY);
        PICTURE_RECOMMEND_TIME_DOWNLOAD_DECAY_VALUE = getDouble(PICTURE_RECOMMEND_TIME_DOWNLOAD_DECAY_KEY);
        PICTURE_RECOMMEND_BEHAVIOR_CATEGORY_VALUE = getInt(PICTURE_RECOMMEND_BEHAVIOR_CATEGORY_KEY);
        PICTURE_RECOMMEND_BEHAVIOR_TAG_VALUE = getInt(PICTURE_RECOMMEND_BEHAVIOR_TAG_KEY);
        PICTURE_RECOMMEND_TIME_BEHAVIOR_DECAY_VALUE = getDouble(PICTURE_RECOMMEND_TIME_BEHAVIOR_DECAY_KEY);
        PICTURE_RECOMMEND_VIEW_LIMIT_VALUE = getInt(PICTURE_RECOMMEND_VIEW_LIMIT_KEY);
        PICTURE_RECOMMEND_BEHAVIOR_LIMIT_VALUE = getInt(PICTURE_RECOMMEND_BEHAVIOR_LIMIT_KEY);
        PICTURE_RECOMMEND_DOWNLOAD_LIMIT_VALUE = getInt(PICTURE_RECOMMEND_DOWNLOAD_LIMIT_KEY);
        PICTURE_RECOMMEND_TIMEOUT_VALUE = getInt(PICTURE_RECOMMEND_TIMEOUT_KEY);
        PICTURE_RECOMMEND_NOT_REPEAT_TIMEOUT_VALUE = getInt(PICTURE_RECOMMEND_NOT_REPEAT_TIMEOUT_KEY);
        PICTURE_RECOMMEND_CATEGORY_MAX_VALUE = getInt(PICTURE_RECOMMEND_CATEGORY_MAX_KEY);
        PICTURE_RECOMMEND_TAG_MAX_VALUE = getInt(PICTURE_RECOMMEND_TAG_MAX_KEY);
        PICTURE_RECOMMEND_CATEGORY_WEIGHT_VALUE = getDouble(PICTURE_RECOMMEND_CATEGORY_WEIGHT_KEY);
        PICTURE_RECOMMEND_TAG_WEIGHT_VALUE = getDouble(PICTURE_RECOMMEND_TAG_WEIGHT_KEY);
        PICTURE_RECOMMEND_BEHAVIOR_CACHE_TIMEOUT_VALUE = getInt(PICTURE_RECOMMEND_BEHAVIOR_CACHE_TIMEOUT_KEY);
        PICTURE_RECOMMEND_BEHAVIOR_CACHE_THRESHOLD_VALUE = getInt(PICTURE_RECOMMEND_BEHAVIOR_CACHE_THRESHOLD_KEY);
        PICTURE_RECOMMEND_DOWNLOAD_CACHE_TIMEOUT_VALUE = getInt(PICTURE_RECOMMEND_DOWNLOAD_CACHE_TIMEOUT_KEY);
        PICTURE_RECOMMEND_DOWNLOAD_CACHE_THRESHOLD_VALUE = getInt(PICTURE_RECOMMEND_DOWNLOAD_CACHE_THRESHOLD_KEY);
        PICTURE_RECOMMEND_VIEW_CACHE_TIMEOUT_VALUE = getInt(PICTURE_RECOMMEND_VIEW_CACHE_TIMEOUT_KEY);
        PICTURE_RECOMMEND_VIEW_CACHE_THRESHOLD_VALUE = getInt(PICTURE_RECOMMEND_VIEW_CACHE_THRESHOLD_KEY);
        PICTURE_SPACE_DILATATION_COUNT_VALUE = getLong(PICTURE_SPACE_DILATATION_COUNT_KEY);
        PICTURE_SPACE_DILATATION_SIZE_VALUE = getLong(PICTURE_SPACE_DILATATION_SIZE_KEY);
        PICTURE_SPACE_DILATATION_MEMBER_VALUE = getLong(PICTURE_SPACE_DILATATION_MEMBER_KEY);
        PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_VIEW_VALUE = getDouble(PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_VIEW_KEY);
        PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_DOWNLOAD_VALUE = getDouble(PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_DOWNLOAD_KEY);
        PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_LIKE_VALUE = getDouble(PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_LIKE_KEY);
        PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_COLLECT_VALUE = getDouble(PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_COLLECT_KEY);
        PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_SHARE_VALUE = getDouble(PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_SHARE_KEY);
        PICTURE_STATISTICS_HOT_DAY_RANK_VALUE = getInt(PICTURE_STATISTICS_HOT_DAY_RANK_KEY);
        PICTURE_STATISTICS_HOT_WEEK_RANK_VALUE = getInt(PICTURE_STATISTICS_HOT_WEEK_RANK_KEY);
        PICTURE_STATISTICS_HOT_MONTH_RANK_VALUE = getInt(PICTURE_STATISTICS_HOT_MONTH_RANK_KEY);
        PICTURE_STATISTICS_HOT_YEAR_RANK_VALUE = getInt(PICTURE_STATISTICS_HOT_YEAR_RANK_KEY);
        PICTURE_STATISTICS_HOT_TOTAL_RANK_VALUE = getInt(PICTURE_STATISTICS_HOT_TOTAL_RANK_KEY);
        PICTURE_CATEGORY_AI_ID_VALUE = getString(PICTURE_CATEGORY_AI_ID_KEY);
    }


    public Integer getInt(String key) {
        try {
            String value = configInfoService.getConfigInfoInCache(key);
            return value != null ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            log.error("NumberFormatException: {},key:{}", e.getMessage(), key);
            throw new ServiceException(StringUtils.format("类型转换错误: {},key:{}", e.getMessage(), key));
        }
    }

    public Boolean getBool(String key) {
        try {
            String value = configInfoService.getConfigInfoInCache(key);
            return value != null ? Boolean.parseBoolean(value) : null;
        } catch (Exception e) {
            log.error("NumberFormatException: {},key:{}", e.getMessage(), key);
            throw new ServiceException(StringUtils.format("类型转换错误: {},key:{}", e.getMessage(), key));
        }
    }

    public Long getLong(String key) {
        try {
            String value = configInfoService.getConfigInfoInCache(key);
            return value != null ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            log.error("NumberFormatException: {},key:{}", e.getMessage(), key);
            throw new ServiceException(StringUtils.format("类型转换错误: {},key:{}", e.getMessage(), key));
        }
    }

    public Double getDouble(String key) {
        try {
            String value = configInfoService.getConfigInfoInCache(key);
            return value != null ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            log.error("NumberFormatException: {},key:{}", e.getMessage(), key);
            throw new ServiceException(StringUtils.format("类型转换错误: {},key:{}", e.getMessage(), key));
        }
    }

    public String getString(String key) {
        try {
            return configInfoService.getConfigInfoInCache(key);
        } catch (Exception e) {
            log.error("NumberFormatException: {},key:{}", e.getMessage(), key);
            throw new ServiceException(StringUtils.format("类型转换错误: {},key:{}", e.getMessage(), key));
        }
    }
    //endregion
}
