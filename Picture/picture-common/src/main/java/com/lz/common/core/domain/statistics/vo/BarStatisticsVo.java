package com.lz.common.core.domain.statistics.vo;

import lombok.Data;

import java.util.List;

/**
* 统计柱形图VO
 */
@Data
public class BarStatisticsVo {
   private List<String> names;

    private List<Long> totals;
}
