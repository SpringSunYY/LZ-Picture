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
 * @date 2025-05-17
 */
@Data
public class PaymentOrderInfoEdit implements Serializable {
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
     * 删除
     */
    private String isDelete;

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
