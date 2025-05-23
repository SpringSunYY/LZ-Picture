package com.lz.points.model.vo.pointsRechargeInfo;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.PointsRechargeInfo;

/**
 * 积分充值记录Vo对象 po_points_recharge_info
 *
 * @author YY
 * @date 2025-05-17
 */
@Data
public class UserPointsRechargeInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 充值记录编号
     */
    private String rechargeId;

    /**
     * 套餐编号
     */
    private String packageId;
    private String packageName;

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
     * 充值状态
     */
    private String rechargeStatus;

    /**
     * 充值时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 到账时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivalTime;


    /**
     * 对象转封装类
     *
     * @param pointsRechargeInfo PointsRechargeInfo实体对象
     * @return PointsRechargeInfoVo
     */
    public static UserPointsRechargeInfoVo objToVo(PointsRechargeInfo pointsRechargeInfo) {
        if (pointsRechargeInfo == null) {
            return null;
        }
        UserPointsRechargeInfoVo userPointsRechargeInfoVo = new UserPointsRechargeInfoVo();
        BeanUtils.copyProperties(pointsRechargeInfo, userPointsRechargeInfoVo);
        return userPointsRechargeInfoVo;
    }
}
