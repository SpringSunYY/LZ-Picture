package com.lz.points.model.dto.pointsRechargeInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.PointsRechargeInfo;

/**
 * 积分充值记录Vo对象 po_points_recharge_info
 *
 * @author YY
 * @date 2025-05-17
 */
@Data
public class PointsRechargeInfoEdit implements Serializable {
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
     * 数量
     */
    private Long rechargeCount;

    /**
     * 支付方式
     */
    private String paymentType;

    /**
     * 充值状态
     */
    private String rechargeStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除
     */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param pointsRechargeInfoEdit 编辑对象
     * @return PointsRechargeInfo
     */
    public static PointsRechargeInfo editToObj(PointsRechargeInfoEdit pointsRechargeInfoEdit) {
        if (pointsRechargeInfoEdit == null) {
            return null;
        }
        PointsRechargeInfo pointsRechargeInfo = new PointsRechargeInfo();
        BeanUtils.copyProperties(pointsRechargeInfoEdit, pointsRechargeInfo);
        return pointsRechargeInfo;
    }
}
