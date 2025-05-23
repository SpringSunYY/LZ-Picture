package com.lz.points.model.dto.pointsUsageLogInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.points.model.domain.PointsUsageLogInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 积分使用记录Query对象 po_points_usage_log_info
 * 用户查询
 *
 * @author YY
 * @date 2025-05-23
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserPointsUsageLogInfoQuery extends PageDomain implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 日志类型（0充值 1消费 2提成 3提现）
     */
    private String logType;

    /**
     * 使用类型（0下载图片 1AI服务）
     */
    private String usageType;

    /**
     * 创建时间
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
     * @param pointsUsageLogInfoQuery 查询对象
     * @return PointsUsageLogInfo
     */
    public static PointsUsageLogInfo queryToObj(UserPointsUsageLogInfoQuery pointsUsageLogInfoQuery) {
        if (pointsUsageLogInfoQuery == null) {
            return null;
        }
        PointsUsageLogInfo pointsUsageLogInfo = new PointsUsageLogInfo();
        BeanUtils.copyProperties(pointsUsageLogInfoQuery, pointsUsageLogInfo);
        return pointsUsageLogInfo;
    }
}
