package com.lz.points.manager.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 阿里云PC端支付请求参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-14  22:05
 * @Version: 1.0
 */
@Data
public class AlipayPcPaymentRequest {
    /**
     * 总价格
     */
    private BigDecimal totalAmount;
    /**
     * 订单号
     */
    private String outTradeNo;
    /**
     * 订单标题
     */
    private String subject;
    /**
     * 买家编号
     */
    private String buyerId;

    /**
     * 超时时间
     */
    private String timeoutExpress;

    /**
     * 支付宝产品码
     */
    private String productCode;

}
