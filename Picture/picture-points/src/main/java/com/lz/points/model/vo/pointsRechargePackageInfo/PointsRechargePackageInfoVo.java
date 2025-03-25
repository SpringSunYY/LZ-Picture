package com.lz.points.model.vo.pointsRechargePackageInfo;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PointsRechargePackageInfo;
/**
 * 充值积分套餐Vo对象 po_points_recharge_package_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PointsRechargePackageInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 套餐编号 */
    @Excel(name = "套餐编号")
    private String packageId;

    /** 套餐名称 */
    @Excel(name = "套餐名称")
    private String packageName;

    /** 套餐价格 */
    @Excel(name = "套餐价格")
    private BigDecimal price;

    /** 套餐积分数量 */
    @Excel(name = "套餐积分数量")
    private Long points;

    /** 套餐赠送积分 */
    @Excel(name = "套餐赠送积分")
    private Long pointsBonus;

    /** 套餐描述 */
    @Excel(name = "套餐描述")
    private String description;

    /** 是否长期（0是 1否） */
    @Excel(name = "是否长期", readConverterExp = "0=是,1=否")
    private String isLongTerm;

    /** 套餐生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "套餐生效时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 套餐结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "套餐结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 套餐状态（0正常 1失效） */
    @Excel(name = "套餐状态", readConverterExp = "0=正常,1=失效")
    private String packageStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


     /**
     * 对象转封装类
     *
     * @param pointsRechargePackageInfo PointsRechargePackageInfo实体对象
     * @return PointsRechargePackageInfoVo
     */
    public static PointsRechargePackageInfoVo objToVo(PointsRechargePackageInfo pointsRechargePackageInfo) {
        if (pointsRechargePackageInfo == null) {
            return null;
        }
        PointsRechargePackageInfoVo pointsRechargePackageInfoVo = new PointsRechargePackageInfoVo();
        BeanUtils.copyProperties(pointsRechargePackageInfo, pointsRechargePackageInfoVo);
        return pointsRechargePackageInfoVo;
    }
}
