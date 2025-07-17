package com.lz.picture.utils;

import com.lz.common.core.redis.RedisCache;
import com.lz.picture.service.IStatisticsInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;

/**
 * 图片统计工具类
 * 云想衣裳花想容，春风拂槛露华浓。代码写好看一点啦
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-17  23:53
 * @Version: 1.0
 */
@Component
public class PictureStatisticsUtil {
    @Resource
    private IStatisticsInfoService statisticsInfoService;

    @Resource
    private RedisCache redisCache;

    private static final String PICTURE_STATISTICS_HOT_KEY = "picture:statistics:hot:minute";
    private static final Integer PICTURE_STATISTICS_HOT_EXPIRE_TIME = 60 * 60 * 24;

    private static long startTime = System.currentTimeMillis();
    //五分钟统计一次
    private static final long millis = 1000 * 60 * 5L;
    private static long endTime = startTime + millis;
    //当前key,需要存入缓存的
    private static String currentKey = PICTURE_STATISTICS_HOT_KEY + COMMON_SEPARATOR_CACHE + startTime + COMMON_SEPARATOR_CACHE + endTime;
    private static String oldKey = "";

    public void pictureHotStatisticsIncrementScore(String pictureId, Double score) {
        //获取当前毫秒值
        long now = System.currentTimeMillis();
        if (now >= endTime) {
            endTime = now + millis;
            startTime = now;
            oldKey = currentKey;
            currentKey = PICTURE_STATISTICS_HOT_KEY + COMMON_SEPARATOR_CACHE + startTime + COMMON_SEPARATOR_CACHE + endTime;
            System.out.println("oldKey = " + oldKey);
        }
        Double aDouble = redisCache.zSetIncrementScore(currentKey, pictureId, score, PICTURE_STATISTICS_HOT_EXPIRE_TIME);
    }
}
