package com.lz.points.model.vo.pointsRechargeInfo;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PointsRechargeInfo;
/**
 * 积分充值记录Vo对象 po_points_recharge_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PointsRechargeInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 充值记录编号 */
    @Excel(name = "充值记录编号")
    private String rechargeId;

    /** 套餐编号 */
    @Excel(name = "套餐编号")
    private String packageId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderId;

    /** 总数 */
    @Excel(name = "总数")
    private Long totalCount;

    /** 充值积分数量 */
    @Excel(name = "充值积分数量")
    private Long pointsCount;

    /** 赠送数量 */
    @Excel(name = "赠送数量")
    private Long bonusCount;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal priceCount;

    /** 数量 */
    @Excel(name = "数量")
    private Long rechargeCount;

    /** 第三方支付平台 */
    @Excel(name = "第三方支付平台")
    private String thirdParty;

    /** 第三方支付平台订单号 */
    @Excel(name = "第三方支付平台订单号")
    private String thirdPartyOrder;

    /** 充值状态（0成功 1失败 2取消 3超时） */
    @Excel(name = "充值状态", readConverterExp = "0=成功,1=失败,2=取消,3=超时")
    private String rechargeStatus;

    /** 充值时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "充值时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 充值失败原因 */
    @Excel(name = "充值失败原因")
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

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 删除（0否 1是） */
    @Excel(name = "删除", readConverterExp = "0=否,1=是")
    private String isDelete;


     /**
     * 对象转封装类
     *
     * @param pointsRechargeInfo PointsRechargeInfo实体对象
     * @return PointsRechargeInfoVo
     */
    public static PointsRechargeInfoVo objToVo(PointsRechargeInfo pointsRechargeInfo) {
        if (pointsRechargeInfo == null) {
            return null;
        }
        PointsRechargeInfoVo pointsRechargeInfoVo = new PointsRechargeInfoVo();
        BeanUtils.copyProperties(pointsRechargeInfo, pointsRechargeInfoVo);
        return pointsRechargeInfoVo;
    }
}
