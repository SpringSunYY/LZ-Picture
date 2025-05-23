package com.lz.points.model.dto.pointsUsageLogInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.PointsUsageLogInfo;
/**
 * 积分使用记录Vo对象 po_points_usage_log_info
 *
 * @author YY
 * @date 2025-05-23
 */
@Data
public class PointsUsageLogInfoInsert implements Serializable
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

    /** 使用前积分 */
    private Long pointsBefore;

    /** 消费积分 */
    private Long pointsUsed;

    /** 使用后积分 */
    private Long pointsAfter;

    /** 备注 */
    private String remark;

    /** 删除（0否 1是） */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param pointsUsageLogInfoInsert 插入对象
     * @return PointsUsageLogInfoInsert
     */
    public static PointsUsageLogInfo insertToObj(PointsUsageLogInfoInsert pointsUsageLogInfoInsert) {
        if (pointsUsageLogInfoInsert == null) {
            return null;
        }
        PointsUsageLogInfo pointsUsageLogInfo = new PointsUsageLogInfo();
        BeanUtils.copyProperties(pointsUsageLogInfoInsert, pointsUsageLogInfo);
        return pointsUsageLogInfo;
    }
}
