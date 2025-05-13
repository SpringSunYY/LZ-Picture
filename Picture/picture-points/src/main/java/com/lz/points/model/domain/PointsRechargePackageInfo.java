package com.lz.points.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 充值积分套餐对象 po_points_recharge_package_info
 *
 * @author YY
 * @date 2025-03-25
 */
@TableName("po_points_recharge_package_info")
@Data
public class PointsRechargePackageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 套餐编号
     */
    @Excel(name = "套餐编号")
    @TableId(value = "package_id", type = IdType.ASSIGN_ID)
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
     * 排序权重
     */
    @Excel(name = "排序权重")
    private Long sortOrder;


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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
