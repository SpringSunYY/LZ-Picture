package com.lz.points.model.dto.statistics;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 订单支付地图统计
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-18  15:54
 * @Version: 1.0
 */
@Data
public class PaymentOrderMapStatisticsRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "开始时间不能为空")
    private String startDate;
    @NotBlank(message = "结束时间不能为空")
    private String endDate;

    private String ipAddress;

    private String orderStatus;

    private String isDelete;
}
