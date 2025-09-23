package com.lz.points.model.dto.statistics;

import lombok.Data;

/**
 * 用户充值返回ro
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-23  22:18
 * @Version: 1.0
 */
@Data
public class PaymentOrderStatisticsRo {
    private Long value;
    private String name;
    private String data;
}
