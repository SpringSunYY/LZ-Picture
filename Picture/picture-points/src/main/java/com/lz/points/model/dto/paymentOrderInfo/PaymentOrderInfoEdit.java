package com.lz.points.model.dto.paymentOrderInfo;

import java.io.Serial;
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
 * @date 2025-05-19
 */
@Data
public class PaymentOrderInfoEdit implements Serializable {
    @Serial
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
     * 订单类型
     */
    private String orderType;

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
     * 备注
     */
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
