package com.lz.points.model.vo.paymentOrderInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PaymentOrderInfo;

/**
 * 支付订单Vo对象 po_payment_order_info
 *
 * @author YY
 * @date 2025-05-19
 */
@Data
public class PaymentOrderInfoVo implements Serializable {
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
     * 订单类型
     */
    @Excel(name = "订单类型")
    private String orderType;

    /**
     * 订单状态
     */
    @Excel(name = "订单状态")
    private String orderStatus;

    /**
     * 支付方式
     */
    @Excel(name = "支付方式")
    private String paymentType;

    /**
     * 订单总金额
     */
    @Excel(name = "订单总金额")
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    @Excel(name = "实付金额")
    private BigDecimal buyerPayAmount;

    /**
     * 实收金额
     */
    @Excel(name = "实收金额")
    private BigDecimal receiptAmount;

    /**
     * 平台优惠金额
     */
    @Excel(name = "平台优惠金额")
    private BigDecimal discountAmount;

    /**
     * 第三方支付平台
     */
    @Excel(name = "第三方支付平台")
    private String thirdParty;

    /**
     * 第三方用户编号
     */
    @Excel(name = "第三方用户编号")
    private String thirdUserId;

    /**
     * 第三方支付平台订单号
     */
    @Excel(name = "第三方支付平台订单号")
    private String thirdPartyOrder;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    /**
     * 支付状态
     */
    @Excel(name = "支付状态")
    private String paymentStatus;

    /**
     * 支付返回Code
     */
    @Excel(name = "支付返回Code")
    private String paymentCode;

    /**
     * 支付返回Msg
     */
    @Excel(name = "支付返回Msg")
    private String paymentMsg;

    /**
     * 支付返回额外信息
     */
    @Excel(name = "支付返回额外信息")
    private String paymentExtend;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 设备唯一标识
     */
    @Excel(name = "设备唯一标识")
    private String deviceId;

    /**
     * 浏览器类型
     */
    @Excel(name = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @Excel(name = "操作系统")
    private String os;

    /**
     * 平台
     */
    @Excel(name = "平台")
    private String platform;

    /**
     * IP地址
     */
    @Excel(name = "IP地址")
    private String ipAddr;

    /**
     * IP属地
     */
    @Excel(name = "IP属地")
    private String ipAddress;

    /**
     * 删除
     */
    @Excel(name = "删除")
    private String isDelete;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;


    /**
     * 对象转封装类
     *
     * @param paymentOrderInfo PaymentOrderInfo实体对象
     * @return PaymentOrderInfoVo
     */
    public static PaymentOrderInfoVo objToVo(PaymentOrderInfo paymentOrderInfo) {
        if (paymentOrderInfo == null) {
            return null;
        }
        PaymentOrderInfoVo paymentOrderInfoVo = new PaymentOrderInfoVo();
        BeanUtils.copyProperties(paymentOrderInfo, paymentOrderInfoVo);
        return paymentOrderInfoVo;
    }
}
