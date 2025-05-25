package com.lz.points.model.vo.pointsUsageLogInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.points.model.domain.PointsUsageLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 积分使用记录Vo对象 用户积分使用记录列表
 *
 * @author YY
 * @date 2025-05-23
 */
@Data
public class UserPointsUsageLogInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String logId;

    /**
     * 日志类型（0充值 1消费 2提成 3提现） 字典类型po_points_usage_log_type
     */
    @Excel(name = "日志类型", readConverterExp = "0=充值,1=消费,2=提成,3=提现")
    private String logType;

    /**
     * 使用类型（0下载图片 1AI服务） 字典类型po_points_usage_type
     */
    @Excel(name = "使用类型", readConverterExp = "0=下载图片,1=AI服务")
    private String usageType;

    /**
     * 目标编号
     */
    @Excel(name = "目标编号")
    private String targetId;

    /**
     * 使用前积分
     */
    @Excel(name = "使用前积分")
    private Long pointsBefore;

    /**
     * 消费积分
     */
    @Excel(name = "消费积分")
    private Long pointsUsed;

    /**
     * 使用后积分
     */
    @Excel(name = "使用后积分")
    private Long pointsAfter;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 对象转封装类
     *
     * @param pointsUsageLogInfo PointsUsageLogInfo实体对象
     * @return PointsUsageLogInfoVo
     */
    public static UserPointsUsageLogInfoVo objToVo(PointsUsageLogInfo pointsUsageLogInfo) {
        if (pointsUsageLogInfo == null) {
            return null;
        }
        UserPointsUsageLogInfoVo pointsUsageLogInfoVo = new UserPointsUsageLogInfoVo();
        BeanUtils.copyProperties(pointsUsageLogInfo, pointsUsageLogInfoVo);
        return pointsUsageLogInfoVo;
    }

    /**
     * 对象列表封装类
     * @author: YY
     * @method: objToVo
     * @date: 2025/5/24 00:23
     * @param pointsUsageLogInfoList 对象列表
     * @return List<UserPointsUsageLogInfoVo>
     **/
    public static List<UserPointsUsageLogInfoVo> objToVo(List<PointsUsageLogInfo> pointsUsageLogInfoList) {
        if (pointsUsageLogInfoList == null) {
            return null;
        }
        return pointsUsageLogInfoList.stream().map(UserPointsUsageLogInfoVo::objToVo).toList();
    }
}
