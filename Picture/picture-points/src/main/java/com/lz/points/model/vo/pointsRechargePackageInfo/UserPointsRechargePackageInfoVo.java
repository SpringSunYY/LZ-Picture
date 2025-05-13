package com.lz.points.model.vo.pointsRechargePackageInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.common.utils.StringUtils;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 充值积分套餐Vo对象 po_points_recharge_package_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class UserPointsRechargePackageInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 套餐编号
     */
    @Excel(name = "套餐编号")
    private String packageId;

    /**
     * 套餐名称
     */
    @Excel(name = "套餐名称")
    private String packageName;

    /**
     * 套餐价格
     */
    @Excel(name = "套餐价格")
    private BigDecimal price;

    /**
     * 套餐积分数量
     */
    @Excel(name = "套餐积分数量")
    private Long points;

    /**
     * 套餐赠送积分
     */
    @Excel(name = "套餐赠送积分")
    private Long pointsBonus;

    /**
     * 套餐描述
     */
    @Excel(name = "套餐描述")
    private String description;

    /**
     * 是否长期（0是 1否）
     */
    @Excel(name = "是否长期", readConverterExp = "0=是,1=否")
    private String isLongTerm;

    /**
     * 套餐生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "套餐生效时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 套餐结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "套餐结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 套餐状态（0正常 1失效）
     */
    @Excel(name = "套餐状态", readConverterExp = "0=正常,1=失效")
    private String packageStatus;


    /**
     * 对象转封装类
     *
     * @param pointsRechargePackageInfo PointsRechargePackageInfo实体对象
     * @return PointsRechargePackageInfoVo
     */
    public static UserPointsRechargePackageInfoVo objToVo(PointsRechargePackageInfo pointsRechargePackageInfo) {
        if (pointsRechargePackageInfo == null) {
            return null;
        }
        UserPointsRechargePackageInfoVo pointsRechargePackageInfoVo = new UserPointsRechargePackageInfoVo();
        BeanUtils.copyProperties(pointsRechargePackageInfo, pointsRechargePackageInfoVo);
        return pointsRechargePackageInfoVo;
    }

    /**
     * 对象列表转封装类
     *
     * @param pointsRechargePackageInfoList
     * @return UserPointsRechargePackageInfoVo
     * @author: YY
     * @method: objToVo
     * @date: 2025/5/13 10:02
     **/
    public static List<UserPointsRechargePackageInfoVo> objToVo(List<PointsRechargePackageInfo> pointsRechargePackageInfoList) {
        if (StringUtils.isEmpty(pointsRechargePackageInfoList)) {
            return new ArrayList<>();
        }
        return pointsRechargePackageInfoList.stream().map(UserPointsRechargePackageInfoVo::objToVo).toList();
    }
}
