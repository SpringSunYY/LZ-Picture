package com.lz.points.model.domain;

import java.io.Serial;
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
 * 积分充值记录对象 po_points_recharge_info
 *
 * @author YY
 * @date 2025-05-17
 */
@TableName("po_points_recharge_info")
@Data
public class PointsRechargeInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 充值记录编号
     */
    @Excel(name = "充值记录编号")
    @TableId(value = "recharge_id", type = IdType.ASSIGN_ID)
    private String rechargeId;

    /**
     * 套餐编号
     */
    @Excel(name = "套餐编号")
    private String packageId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 订单编号
     */
    @Excel(name = "订单编号")
    private String orderId;

    /**
     * 总数
     */
    @Excel(name = "总数")
    private Long totalCount;

    /**
     * 充值积分数量
     */
    @Excel(name = "充值积分数量")
    private Long pointsCount;

    /**
     * 赠送数量
     */
    @Excel(name = "赠送数量")
    private Long bonusCount;

    /**
     * 充值金额
     */
    @Excel(name = "充值金额")
    private BigDecimal priceCount;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Long rechargeCount;

    /**
     * 第三方支付平台
     */
    @Excel(name = "第三方支付平台")
    private String thirdParty;

    /**
     * 第三方支付平台订单号
     */
    @Excel(name = "第三方支付平台订单号")
    private String thirdPartyOrder;

    /**
     * 充值状态
     */
    @Excel(name = "充值状态")
    private String rechargeStatus;

    /**
     * 充值时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "充值时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 到账时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "到账时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date arrivalTime;

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
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 删除
     */
    @Excel(name = "删除")
    private String isDelete;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
