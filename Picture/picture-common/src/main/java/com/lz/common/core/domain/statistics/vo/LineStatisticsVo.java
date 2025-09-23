package com.lz.common.core.domain.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 主线图返回VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineStatisticsVo {
   private List<String> names;

    private List<Long> totals;
}
