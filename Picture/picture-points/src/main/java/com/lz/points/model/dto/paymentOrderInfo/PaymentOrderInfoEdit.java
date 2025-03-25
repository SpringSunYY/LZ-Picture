package com.lz.points.model.dto.paymentOrderInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.PaymentOrderInfo;
/**
 * 支付订单Vo对象 po_payment_order_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PaymentOrderInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 订单编号 */
    private String orderId;

    /** 用户编号 */
    private String userId;

    /** 订单类型 */
    private String orderType;

    /** 订单状态（0待支付 1成功 2失败 3退款） */
    private String orderStatus;

    /** 订单总金额 */
    private BigDecimal amount;

    /** 支付方式编号 */
    private String paymentId;

    /** 第三方支付平台 */
    private String thirdParty;

    /** 第三方支付平台订单号 */
    private String thirdPartyOrder;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentTime;

    /** 支付状态（0待支付 1成功 2失败 3取消） */
    private String paymentStatus;

    /** 支付手续费 */
    private BigDecimal transactionFee;

    /** 支付失败原因 */
    private String failReason;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台 */
    private String platform;

    /** IP地址 */
    private String ipAddr;

    /** 删除（0否 1是） */
    private String isDelete;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param paymentOrderInfoEdit 编辑对象
     * @return PaymentOrderInfo
     */
    public static PaymentOrderInfo editToObj(PaymentOrderInfoEdit paymentOrderInfoEdit) {
        if (paymentOrderInfoEdit == null) {
            return null;
        }
        PaymentOrderInfo paymentOrderInfo = new PaymentOrderInfo();
        BeanUtils.copyProperties(paymentOrderInfoEdit, paymentOrderInfo);
        return paymentOrderInfo;
    }
}
