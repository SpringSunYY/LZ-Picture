package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.PointsUsageLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 积分使用记录Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface PointsUsageLogMapper extends BaseMapper<PointsUsageLog>
{
    /**
     * 查询积分使用记录
     *
     * @param logId 积分使用记录主键
     * @return 积分使用记录
     */
    public PointsUsageLog selectPointsUsageLogByLogId(String logId);

    /**
     * 查询积分使用记录列表
     *
     * @param pointsUsageLog 积分使用记录
     * @return 积分使用记录集合
     */
    public List<PointsUsageLog> selectPointsUsageLogList(PointsUsageLog pointsUsageLog);

    /**
     * 新增积分使用记录
     *
     * @param pointsUsageLog 积分使用记录
     * @return 结果
     */
    public int insertPointsUsageLog(PointsUsageLog pointsUsageLog);

    /**
     * 修改积分使用记录
     *
     * @param pointsUsageLog 积分使用记录
     * @return 结果
     */
    public int updatePointsUsageLog(PointsUsageLog pointsUsageLog);

    /**
     * 删除积分使用记录
     *
     * @param logId 积分使用记录主键
     * @return 结果
     */
    public int deletePointsUsageLogByLogId(String logId);

    /**
     * 批量删除积分使用记录
     *
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePointsUsageLogByLogIds(String[] logIds);
}
