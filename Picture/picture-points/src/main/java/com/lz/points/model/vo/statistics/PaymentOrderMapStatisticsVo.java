package com.lz.points.model.vo.statistics;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * data map
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-25  17:47
 * @Version: 1.0
 */
@Data
public class PaymentOrderMapStatisticsVo {
    private String date;
    private List<Data> datas;

    @lombok.Data
    public static class Data {
        private String ipAddress;
        private Long value;
        private BigDecimal amount;
    }
}
