package com.lz.points.model.dto.pointsRechargeInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.PointsRechargeInfo;
/**
 * 积分充值记录Vo对象 po_points_recharge_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PointsRechargeInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 充值记录编号 */
    private String rechargeId;

    /** 套餐编号 */
    private String packageId;

    /** 用户编号 */
    private String userId;

    /** 订单编号 */
    private String orderId;

    /** 总数 */
    private Long totalCount;

    /** 充值积分数量 */
    private Long pointsCount;

    /** 赠送数量 */
    private Long bonusCount;

    /** 充值金额 */
    private BigDecimal priceCount;

    /** 数量 */
    private Long rechargeCount;

    /** 第三方支付平台 */
    private String thirdParty;

    /** 第三方支付平台订单号 */
    private String thirdPartyOrder;

    /** 充值状态（0成功 1失败 2取消 3超时） */
    private String rechargeStatus;

    /** 充值失败原因 */
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

    /** 备注 */
    private String remark;

    /** 删除（0否 1是） */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param pointsRechargeInfoInsert 插入对象
     * @return PointsRechargeInfoInsert
     */
    public static PointsRechargeInfo insertToObj(PointsRechargeInfoInsert pointsRechargeInfoInsert) {
        if (pointsRechargeInfoInsert == null) {
            return null;
        }
        PointsRechargeInfo pointsRechargeInfo = new PointsRechargeInfo();
        BeanUtils.copyProperties(pointsRechargeInfoInsert, pointsRechargeInfo);
        return pointsRechargeInfo;
    }
}
