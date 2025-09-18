package com.lz.picture;

import com.lz.common.core.domain.statistics.vo.KeywordStatisticsVo;
import com.lz.picture.model.dto.statistics.KeywordStatisticsRequest;
import com.lz.picture.service.IStatisticsInfoService;
import com.lz.picture.utils.PictureStatisticsUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    @Resource
    private IStatisticsInfoService statisticsInfoService;

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

    @Test
    public void testKeywordSearchStatistics() {
        KeywordStatisticsRequest request = new KeywordStatisticsRequest();
        request.setStartDate("2025-07-18");
        request.setEndDate("2025-09-18");
        request.setSize(100L);
        List<KeywordStatisticsVo> keywordStatisticsVos = statisticsInfoService.keywordSearchStatistics(request);
        keywordStatisticsVos.forEach(System.err::println);
    }
}
