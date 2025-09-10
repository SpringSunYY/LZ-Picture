package com.lz.common.core.domain.statistics.vo;

import lombok.Data;

import java.util.List;

/**
 * 饼图统计VO
 */
@Data
public class PieStatisticsVo {
    private List<String> names;

    private List<Data> datas;

    @lombok.Data
    public static class Data {
        private String name;
        private Long value;
    }
}
