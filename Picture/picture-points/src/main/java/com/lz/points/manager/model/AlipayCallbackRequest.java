package com.lz.points.manager.model;

import com.alipay.api.internal.mapping.ApiField;
import lombok.Data;

/**
 * 阿里支付回调请求参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-16  13:50
 * @Version: 1.0
 */
@Data
public class AlipayCallbackRequest {
    /**
     * 编码
     */
    @ApiField("charset")
    private String charset;

    /**
     * 签名
     */
    @ApiField("sign")
    private String sign;

    /**
     * 签名类型
     */
    @ApiField("sign_type")
    private String signType;

    /**
     * 时间搓
     */
    @ApiField("timestamp")
    private String timestamp;

    /**
     * 商户订单号 商户自定义
     */
    @ApiField("out_trade_no")
    private String outTradeNo;

    /**
     * 方法
     */
    @ApiField("method")
    private String method;

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

    /**
     * 收款支付宝账号对应的支付宝唯一用户号。
     * 以2088开头的纯16位数字
     */
    @ApiField("seller_id")
    private String sellerId;

    /**
     * 校验appid
     */
    @ApiField("auth_app_id")
    private String authAppId;

    /**
     * APPID
     */
    @ApiField("app_id")
    private String appId;

    /**
     * 版本
     */
    @ApiField("version")
    private String version;
}
