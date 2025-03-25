package com.lz.points.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * 用户提现记录对象 po_withdrawal_order_info
 *
 * @author YY
 * @date 2025-03-25
 */
@TableName("po_withdrawal_order_info")
@Data
public class WithdrawalOrderInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 提现订单编号 */
        @Excel(name = "提现订单编号")
    @TableId(value = "withdrawal_id", type = IdType.ASSIGN_ID)
    private String withdrawalId;

    /** 用户编号 */
        @Excel(name = "用户编号")
    private String userId;

    /** 提现积分 */
        @Excel(name = "提现积分")
    private Long pointsWithdrawal;

    /** 提现金额 */
        @Excel(name = "提现金额")
    private BigDecimal amountWithdrawal;

    /** 平台抽成金额 */
        @Excel(name = "平台抽成金额")
    private BigDecimal platformFee;

    /** 用户实际收到金额 */
        @Excel(name = "用户实际收到金额")
    private BigDecimal userReceivedAmount;

    /** 提现方式（0支付宝 1微信） */
        @Excel(name = "提现方式", readConverterExp = "0=支付宝,1=微信")
    private String withdrawalMethod;

    /** 提现账户 */
        @Excel(name = "提现账户")
    private String withdrawalAccount;

    /** 提现状态（0待处理 1完成 2失败 3超时） */
        @Excel(name = "提现状态", readConverterExp = "0=待处理,1=完成,2=失败,3=超时")
    private String withdrawalStatus;

    /** 提现平台订单号 */
        @Excel(name = "提现平台订单号")
    private String withdrawalPlatformOrder;

    /** 交易编号 */
        @Excel(name = "交易编号")
    private String transactionId;

    /** 审核状态（0待审核 1同意 2拒绝） */
        @Excel(name = "审核状态", readConverterExp = "0=待审核,1=同意,2=拒绝")
    private String reviewStatus;

    /** 审核时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 审核人编号 */
        @Excel(name = "审核人编号")
    private Long reviewUserId;

    /** 审核建议 */
        @Excel(name = "审核建议")
    private String reviewRemark;

    /** 完成时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date accomplishTime;

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

    /** 创建时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 提现失败原因 */
        @Excel(name = "提现失败原因")
    private String failReason;

    /** 删除（0否 1是） */
        @Excel(name = "删除", readConverterExp = "0=否,1=是")
    private String isDelete;

    /** 备注 */
        @Excel(name = "备注")
    private String remark;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
