package com.lz.common.core.domain.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地图返回VO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-15  17:23
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapStatisticsVo {
    private String location;
    private Long value;
}
