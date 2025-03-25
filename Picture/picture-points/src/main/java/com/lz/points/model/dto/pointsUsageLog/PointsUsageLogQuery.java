package com.lz.points.model.dto.pointsUsageLog;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PointsUsageLog;
/**
 * 积分使用记录Query对象 po_points_usage_log
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PointsUsageLogQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
    private String logId;

    /** 用户编号 */
    private String userId;

    /** 使用类型（0下载图片 1AI服务） */
    private String usageType;

    /** 目标编号 */
    private String targetId;

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

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 删除（0否 1是） */
    private String isDelete;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pointsUsageLogQuery 查询对象
     * @return PointsUsageLog
     */
    public static PointsUsageLog queryToObj(PointsUsageLogQuery pointsUsageLogQuery) {
        if (pointsUsageLogQuery == null) {
            return null;
        }
        PointsUsageLog pointsUsageLog = new PointsUsageLog();
        BeanUtils.copyProperties(pointsUsageLogQuery, pointsUsageLog);
        return pointsUsageLog;
    }
}
