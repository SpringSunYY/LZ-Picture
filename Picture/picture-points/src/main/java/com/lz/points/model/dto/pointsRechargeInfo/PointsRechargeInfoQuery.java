package com.lz.points.model.dto.pointsRechargeInfo;

import java.io.Serial;
import java.util.Map;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.core.page.PageDomain;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PointsRechargeInfo;

/**
 * 积分充值记录Query对象 po_points_recharge_info
 *
 * @author YY
 * @date 2025-05-17
 */
@Data
public class PointsRechargeInfoQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 充值记录编号
     */
    private String rechargeId;

    /**
     * 套餐编号
     */
    private String packageId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 总数
     */
    private Long totalCount;

    /**
     * 充值积分数量
     */
    private Long pointsCount;

    /**
     * 赠送数量
     */
    private Long bonusCount;

    /**
     * 充值金额
     */
    private BigDecimal priceCount;

    /**
     * 实付金额
     */
    private BigDecimal buyerPayAmount;

    /**
     * 数量
     */
    private Long rechargeCount;

    /**
     * 支付方式
     */
    private String paymentType;

    /**
     * 第三方支付平台
     */
    private String thirdParty;

    /**
     * 第三方支付平台订单号
     */
    private String thirdPartyOrder;

    /**
     * 充值状态
     */
    private String rechargeStatus;

    /**
     * 充值时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 到账时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date arrivalTime;

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
     * @param pointsRechargeInfoQuery 查询对象
     * @return PointsRechargeInfo
     */
    public static PointsRechargeInfo queryToObj(PointsRechargeInfoQuery pointsRechargeInfoQuery) {
        if (pointsRechargeInfoQuery == null) {
            return null;
        }
        PointsRechargeInfo pointsRechargeInfo = new PointsRechargeInfo();
        BeanUtils.copyProperties(pointsRechargeInfoQuery, pointsRechargeInfo);
        return pointsRechargeInfo;
    }
}
