package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.PointsUsageLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 积分使用记录Mapper接口
 *
 * @author YY
 * @date 2025-05-23
 */
public interface PointsUsageLogInfoMapper extends BaseMapper<PointsUsageLogInfo>
{
    /**
     * 查询积分使用记录
     *
     * @param logId 积分使用记录主键
     * @return 积分使用记录
     */
    public PointsUsageLogInfo selectPointsUsageLogInfoByLogId(String logId);

    /**
     * 查询积分使用记录列表
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 积分使用记录集合
     */
    public List<PointsUsageLogInfo> selectPointsUsageLogInfoList(PointsUsageLogInfo pointsUsageLogInfo);

    /**
     * 新增积分使用记录
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 结果
     */
    public int insertPointsUsageLogInfo(PointsUsageLogInfo pointsUsageLogInfo);

    /**
     * 修改积分使用记录
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 结果
     */
    public int updatePointsUsageLogInfo(PointsUsageLogInfo pointsUsageLogInfo);

    /**
     * 删除积分使用记录
     *
     * @param logId 积分使用记录主键
     * @return 结果
     */
    public int deletePointsUsageLogInfoByLogId(String logId);

    /**
     * 批量删除积分使用记录
     *
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePointsUsageLogInfoByLogIds(String[] logIds);
}
