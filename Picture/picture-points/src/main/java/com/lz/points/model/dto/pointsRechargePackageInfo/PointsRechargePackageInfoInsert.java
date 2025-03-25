package com.lz.points.model.dto.pointsRechargePackageInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.PointsRechargePackageInfo;
/**
 * 充值积分套餐Vo对象 po_points_recharge_package_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PointsRechargePackageInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 套餐编号 */
    private String packageId;

    /** 套餐名称 */
    private String packageName;

    /** 套餐价格 */
    private BigDecimal price;

    /** 套餐积分数量 */
    private Long points;

    /** 套餐赠送积分 */
    private Long pointsBonus;

    /** 套餐描述 */
    private String description;

    /** 是否长期（0是 1否） */
    private String isLongTerm;

    /** 套餐生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 套餐结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 套餐状态（0正常 1失效） */
    private String packageStatus;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param pointsRechargePackageInfoInsert 插入对象
     * @return PointsRechargePackageInfoInsert
     */
    public static PointsRechargePackageInfo insertToObj(PointsRechargePackageInfoInsert pointsRechargePackageInfoInsert) {
        if (pointsRechargePackageInfoInsert == null) {
            return null;
        }
        PointsRechargePackageInfo pointsRechargePackageInfo = new PointsRechargePackageInfo();
        BeanUtils.copyProperties(pointsRechargePackageInfoInsert, pointsRechargePackageInfo);
        return pointsRechargePackageInfo;
    }
}
