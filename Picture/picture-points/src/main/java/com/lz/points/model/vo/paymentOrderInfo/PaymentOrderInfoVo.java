package com.lz.points.model.vo.paymentOrderInfo;

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
 * @date 2025-03-25
 */
@Data
public class PaymentOrderInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 订单类型 */
    @Excel(name = "订单类型")
    private String orderType;

    /** 订单状态（0待支付 1成功 2失败 3退款） */
    @Excel(name = "订单状态", readConverterExp = "0=待支付,1=成功,2=失败,3=退款")
    private String orderStatus;

    /** 订单总金额 */
    @Excel(name = "订单总金额")
    private BigDecimal amount;

    /** 支付方式编号 */
    @Excel(name = "支付方式编号")
    private String paymentId;

    /** 第三方支付平台 */
    @Excel(name = "第三方支付平台")
    private String thirdParty;

    /** 第三方支付平台订单号 */
    @Excel(name = "第三方支付平台订单号")
    private String thirdPartyOrder;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    /** 支付状态（0待支付 1成功 2失败 3取消） */
    @Excel(name = "支付状态", readConverterExp = "0=待支付,1=成功,2=失败,3=取消")
    private String paymentStatus;

    /** 支付手续费 */
    @Excel(name = "支付手续费")
    private BigDecimal transactionFee;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 支付失败原因 */
    @Excel(name = "支付失败原因")
    private String failReason;

    /** 设备唯一标识 */
    @Excel(name = "设备唯一标识")
    private String deviceId;

    /** 浏览器类型 */
    @Excel(name = "浏览器类型")
    private String browser;

    /** 操作系统 */
    @Excel(name = "操作系统")
    private String os;

    /** 平台 */
    @Excel(name = "平台")
    private String platform;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddr;

    /** 删除（0否 1是） */
    @Excel(name = "删除", readConverterExp = "0=否,1=是")
    private String isDelete;

    /** 备注 */
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
