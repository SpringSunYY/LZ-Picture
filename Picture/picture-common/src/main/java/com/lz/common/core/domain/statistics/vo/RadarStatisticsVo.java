package com.lz.common.core.domain.statistics.vo;

import lombok.Data;

import java.util.List;

/**
 * 雷达图统计数据
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-11  18:25
 * @Version: 1.0
 */
@Data
public class RadarStatisticsVo {

    private List<Indicator> indicators;
    private List<Data> datas;

    @lombok.Data
    public static class Data {
        private String name;
        private List<Long> values;
    }

    @lombok.Data
    public static class Indicator {
        private String text;
        private Long max;
    }
}
