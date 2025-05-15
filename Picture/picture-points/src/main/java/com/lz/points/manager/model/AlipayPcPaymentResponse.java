package com.lz.points.manager.model;

import com.alipay.api.internal.mapping.ApiField;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

import java.util.Map;

/**
 * PC阿里支付返回
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-14  22:04
 * @Version: 1.0
 */
@Data
public class AlipayPcPaymentResponse {
    private String pageRedirectionData;
    private String html;


    /**
     * 是否成功
     */
    private boolean success;

    @ApiField("code")
    private String code;

    @ApiField("msg")
    private String msg;

    @ApiField("sub_code")
    private String subCode;

    @ApiField("sub_msg")
    private String subMsg;

    private Map<String, String> params;

    /**
     * 商户原始订单号，最大长度限制32位
     */
    @ApiField("merchant_order_no")
    private String merchantOrderNo;

    /**
     * 商户订单号 商户自定义
     */
    @ApiField("out_trade_no")
    private String outTradeNo;

    /**
     * 收款支付宝账号对应的支付宝唯一用户号。
     * 以2088开头的纯16位数字
     */
    @ApiField("seller_id")
    private String sellerId;

    /**
     * 交易金额
     */
    @ApiField("total_amount")
    private String totalAmount;

    /**
     * 支付宝交易号
     */
    @ApiField("trade_no")
    private String tradeNo;

}
