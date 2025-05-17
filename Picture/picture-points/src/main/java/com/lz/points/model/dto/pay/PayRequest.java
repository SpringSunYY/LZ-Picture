package com.lz.points.model.dto.pay;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付请求
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-15  09:43
 * @Version: 1.0
 */
@Data
public class PayRequest {
    private String packageId;
    //自定义订单编号
    private String outTradeNo;
    private String userId;
    private BigDecimal totalAmount;
}
