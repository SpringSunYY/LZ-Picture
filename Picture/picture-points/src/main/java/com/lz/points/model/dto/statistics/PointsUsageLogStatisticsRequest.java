package com.lz.points.model.dto.statistics;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 积分使用统计请求参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-23  16:39
 * @Version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PointsUsageLogStatisticsRequest extends BasePointsStatisticsRequest implements Serializable {
    private String logType;
}
