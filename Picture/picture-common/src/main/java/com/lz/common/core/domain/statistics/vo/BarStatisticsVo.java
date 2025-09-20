package com.lz.common.core.domain.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 统计柱形图VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarStatisticsVo {
   private List<String> names;

    private List<Long> totals;
}
