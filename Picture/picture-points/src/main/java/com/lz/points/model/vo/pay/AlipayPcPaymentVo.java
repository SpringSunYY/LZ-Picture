package com.lz.points.model.vo.pay;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Data;

import java.util.Map;

/**
 * 支付宝支付PC端返回
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-15  10:35
 * @Version: 1.0
 */
@Data
public class AlipayPcPaymentVo {
    /**
     * 返回HTML
     */
    private String html;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 商户订单号 商户自定义
     */
    @ApiField("out_trade_no")
    private String outTradeNo;
}
