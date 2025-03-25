package com.lz.points.service;

import java.util.List;
import com.lz.points.model.domain.PointsUsageLog;
import com.lz.points.model.vo.pointsUsageLog.PointsUsageLogVo;
import com.lz.points.model.dto.pointsUsageLog.PointsUsageLogQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 积分使用记录Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IPointsUsageLogService extends IService<PointsUsageLog>
{
    //region mybatis代码
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
     * 批量删除积分使用记录
     *
     * @param logIds 需要删除的积分使用记录主键集合
     * @return 结果
     */
    public int deletePointsUsageLogByLogIds(String[] logIds);

    /**
     * 删除积分使用记录信息
     *
     * @param logId 积分使用记录主键
     * @return 结果
     */
    public int deletePointsUsageLogByLogId(String logId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pointsUsageLogQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PointsUsageLog> getQueryWrapper(PointsUsageLogQuery pointsUsageLogQuery);

    /**
     * 转换vo
     *
     * @param pointsUsageLogList PointsUsageLog集合
     * @return PointsUsageLogVO集合
     */
    List<PointsUsageLogVo> convertVoList(List<PointsUsageLog> pointsUsageLogList);
}
