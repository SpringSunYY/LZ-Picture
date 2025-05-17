package com.lz.points.model.dto.paymentOrderInfo;

import java.util.Map;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PaymentOrderInfo;

/**
 * 支付订单Query对象 po_payment_order_info
 *
 * @author YY
 * @date 2025-05-17
 */
@Data
public class PaymentOrderInfoQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 支付方式
     */
    private String paymentType;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    private BigDecimal buyerPayAmount;

    /**
     * 实收金额
     */
    private BigDecimal receiptAmount;

    /**
     * 平台优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 第三方支付平台
     */
    private String thirdParty;

    /**
     * 第三方用户编号
     */
    private String thirdUserId;

    /**
     * 第三方支付平台订单号
     */
    private String thirdPartyOrder;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentTime;

    /**
     * 支付状态
     */
    private String paymentStatus;

    /**
     * 支付返回Code
     */
    private String paymentCode;

    /**
     * 支付返回Msg
     */
    private String paymentMsg;

    /**
     * 支付返回额外信息
     */
    private String paymentExtend;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 设备唯一标识
     */
    private String deviceId;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 平台
     */
    private String platform;

    /**
     * IP地址
     */
    private String ipAddr;

    /**
     * IP属地
     */
    private String ipAddress;

    /**
     * 删除
     */
    private String isDelete;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param paymentOrderInfoQuery 查询对象
     * @return PaymentOrderInfo
     */
    public static PaymentOrderInfo queryToObj(PaymentOrderInfoQuery paymentOrderInfoQuery) {
        if (paymentOrderInfoQuery == null) {
            return null;
        }
        PaymentOrderInfo paymentOrderInfo = new PaymentOrderInfo();
        BeanUtils.copyProperties(paymentOrderInfoQuery, paymentOrderInfo);
        return paymentOrderInfo;
    }
}
