package com.lz.points.model.dto.statistics;

import lombok.Data;

/**
 * 积分统计查询返回结果
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-23  16:41
 * @Version: 1.0
 */
@Data
public class PointsUsageLogStatisticsRo {
    private String date;
    private Long value;
    private String usageType;
}
