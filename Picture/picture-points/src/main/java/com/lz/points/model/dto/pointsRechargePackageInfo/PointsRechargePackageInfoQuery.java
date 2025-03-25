package com.lz.points.model.dto.pointsRechargePackageInfo;

import java.util.Map;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PointsRechargePackageInfo;
/**
 * 充值积分套餐Query对象 po_points_recharge_package_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PointsRechargePackageInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 套餐编号 */
    private String packageId;

    /** 套餐名称 */
    private String packageName;

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

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pointsRechargePackageInfoQuery 查询对象
     * @return PointsRechargePackageInfo
     */
    public static PointsRechargePackageInfo queryToObj(PointsRechargePackageInfoQuery pointsRechargePackageInfoQuery) {
        if (pointsRechargePackageInfoQuery == null) {
            return null;
        }
        PointsRechargePackageInfo pointsRechargePackageInfo = new PointsRechargePackageInfo();
        BeanUtils.copyProperties(pointsRechargePackageInfoQuery, pointsRechargePackageInfo);
        return pointsRechargePackageInfo;
    }
}
