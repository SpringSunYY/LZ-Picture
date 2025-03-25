package com.lz.points.model.dto.pointsRechargeInfo;

import java.util.Map;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @date 2025-03-25
 */
@Data
public class PointsRechargeInfoQuery implements Serializable
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

    /** 第三方支付平台 */
    private String thirdParty;

    /** 第三方支付平台订单号 */
    private String thirdPartyOrder;

    /** 充值状态（0成功 1失败 2取消 3超时） */
    private String rechargeStatus;

    /** 充值时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

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

    /** 删除（0否 1是） */
    private String isDelete;

    /** 请求参数 */
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
