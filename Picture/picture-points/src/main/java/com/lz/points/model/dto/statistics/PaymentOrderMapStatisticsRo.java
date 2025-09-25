package com.lz.points.model.dto.statistics;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户充值返回ro-地图
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-23  22:18
 * @Version: 1.0
 */
@Data
public class PaymentOrderMapStatisticsRo {
    private Long value;
    private BigDecimal amount;
    private String ipAddress;
    private String date;
}
