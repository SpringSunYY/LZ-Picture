package com.lz.picture;

import com.lz.picture.utils.PictureStatisticsUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 图片模块测试类
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-18  00:10
 * @Version: 1.0
 */
@SpringBootTest
public class StatisticsTest {
    @Resource
    private PictureStatisticsUtil pictureStatisticsUtil;

    @Test
    public void testRedisZSet() {
        for (int i = 0; i < 100000; i++) {
            int i1 = i % 10;
            pictureStatisticsUtil.pictureHotStatisticsIncrementScore(String.valueOf(i1), 1.0);
        }
    }

    @Test
    public void testAutoStatisticsPictureByDay() {
        pictureStatisticsUtil.autoStatisticsPictureByDay();
    }
}
