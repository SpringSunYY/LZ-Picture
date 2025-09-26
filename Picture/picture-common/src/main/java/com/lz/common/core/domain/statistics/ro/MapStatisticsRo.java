package com.lz.common.core.domain.statistics.ro;

import lombok.Data;

/**
 * 地图返回RO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-15  17:23
 * @Version: 1.0
 */
@Data
public class MapStatisticsRo {
    private String location;
    private Long value;
    private String date;
}
