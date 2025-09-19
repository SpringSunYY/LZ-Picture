package com.lz.common.core.domain.statistics.vo;

import lombok.Data;

import java.util.List;

/**
 * 折线图-多条返回VO
 */
@Data
public class LineManyStatisticsVo {
    private List<String> names;

    private List<Data> values;

    @lombok.Data
    public static class Data {
        private String name;
        private List<Long> value;
    }
}
