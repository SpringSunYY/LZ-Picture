package com.lz.points.model.dto.pointsUsageLogInfo;

import java.util.Map;
import java.io.Serializable;

import com.lz.common.annotation.Excel;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PointsUsageLogInfo;
/**
 * 积分使用记录Query对象 po_points_usage_log_info
 *
 * @author YY
 * @date 2025-05-23
 */
@Data
public class PointsUsageLogInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
    private String logId;

    /** 用户编号 */
    private String userId;

    /** 给予用户编号 */
    private String giveUserId;

    /** 日志类型（0充值 1消费 2提成 3提现） */
    private String logType;

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

    /**
     * IP属地
     */
    private String ipAddress;

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
     * @param pointsUsageLogInfoQuery 查询对象
     * @return PointsUsageLogInfo
     */
    public static PointsUsageLogInfo queryToObj(PointsUsageLogInfoQuery pointsUsageLogInfoQuery) {
        if (pointsUsageLogInfoQuery == null) {
            return null;
        }
        PointsUsageLogInfo pointsUsageLogInfo = new PointsUsageLogInfo();
        BeanUtils.copyProperties(pointsUsageLogInfoQuery, pointsUsageLogInfo);
        return pointsUsageLogInfo;
    }
}
