package com.lz.points.model.dto.pointsUsageLog;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.PointsUsageLog;
/**
 * 积分使用记录Vo对象 po_points_usage_log
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PointsUsageLogInsert implements Serializable
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

    /** 使用前积分 */
    private Long pointsBefore;

    /** 消费积分 */
    private Long pointsUsed;

    /** 使用后积分 */
    private Long pointsAfter;

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

    /** 备注 */
    private String remark;

    /** 删除（0否 1是） */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param pointsUsageLogInsert 插入对象
     * @return PointsUsageLogInsert
     */
    public static PointsUsageLog insertToObj(PointsUsageLogInsert pointsUsageLogInsert) {
        if (pointsUsageLogInsert == null) {
            return null;
        }
        PointsUsageLog pointsUsageLog = new PointsUsageLog();
        BeanUtils.copyProperties(pointsUsageLogInsert, pointsUsageLog);
        return pointsUsageLog;
    }
}
