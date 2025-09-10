package com.lz.common.core.domain.statistics.vo;

import lombok.Data;

import java.util.List;

/**
* 主线图返回VO
 */
@Data
public class LineStatisticsVo {
   private List<String> names;

    private List<Long> totals;
}
