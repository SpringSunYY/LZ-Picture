package com.lz.points.model.dto.pointsRechargeInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.points.model.domain.PointsRechargeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * 用户积分充值记录查询
 *
 * @author YY
 * @date 2025-05-17
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserPointsRechargeInfoQuery extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 充值状态
     */
    private String rechargeStatus;


    /**
     * 支付方式
     */
    private String paymentType;


    /**
     * 充值时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

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
    public static PointsRechargeInfo queryToObj(UserPointsRechargeInfoQuery pointsRechargeInfoQuery) {
        if (pointsRechargeInfoQuery == null) {
            return null;
        }
        PointsRechargeInfo pointsRechargeInfo = new PointsRechargeInfo();
        BeanUtils.copyProperties(pointsRechargeInfoQuery, pointsRechargeInfo);
        return pointsRechargeInfo;
    }
}
