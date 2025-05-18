package com.lz.points.model.vo.paymentOrderInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.points.model.domain.PaymentOrderInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单Vo对象 po_payment_order_info
 *
 * @author YY
 * @date 2025-05-17
 */
@Data
public class UserPaymentOrderInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String orderId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 订单状态
     */
    @Excel(name = "订单状态")
    private String orderStatus;

    /**
     * 实付金额
     */
    @Excel(name = "实付金额")
    private BigDecimal buyerPayAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;


    /**
     * 对象转封装类
     *
     * @param paymentOrderInfo PaymentOrderInfo实体对象
     * @return PaymentOrderInfoVo
     */
    public static UserPaymentOrderInfoVo objToVo(PaymentOrderInfo paymentOrderInfo) {
        if (paymentOrderInfo == null) {
            return null;
        }
        UserPaymentOrderInfoVo paymentOrderInfoVo = new UserPaymentOrderInfoVo();
        BeanUtils.copyProperties(paymentOrderInfo, paymentOrderInfoVo);
        return paymentOrderInfoVo;
    }
}
