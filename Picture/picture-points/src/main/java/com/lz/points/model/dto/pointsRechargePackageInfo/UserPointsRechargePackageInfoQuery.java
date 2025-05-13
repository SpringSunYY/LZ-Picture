package com.lz.points.model.dto.pointsRechargePackageInfo;

import com.lz.points.model.domain.PointsRechargePackageInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 充值积分套餐Query对象 po_points_recharge_package_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class UserPointsRechargePackageInfoQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 套餐编号
     */
    private String packageId;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 是否长期（0是 1否）
     */
    private String isLongTerm;


    /**
     * 对象转封装类
     *
     * @param pointsRechargePackageInfoQuery 查询对象
     * @return PointsRechargePackageInfo
     */
    public static PointsRechargePackageInfo queryToObj(UserPointsRechargePackageInfoQuery pointsRechargePackageInfoQuery) {
        if (pointsRechargePackageInfoQuery == null) {
            return null;
        }
        PointsRechargePackageInfo pointsRechargePackageInfo = new PointsRechargePackageInfo();
        BeanUtils.copyProperties(pointsRechargePackageInfoQuery, pointsRechargePackageInfo);
        return pointsRechargePackageInfo;
    }
}
