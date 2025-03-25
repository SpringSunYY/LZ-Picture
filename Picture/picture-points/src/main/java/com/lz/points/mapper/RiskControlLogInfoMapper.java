package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.RiskControlLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 风控日志Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface RiskControlLogInfoMapper extends BaseMapper<RiskControlLogInfo>
{
    /**
     * 查询风控日志
     *
     * @param logId 风控日志主键
     * @return 风控日志
     */
    public RiskControlLogInfo selectRiskControlLogInfoByLogId(String logId);

    /**
     * 查询风控日志列表
     *
     * @param riskControlLogInfo 风控日志
     * @return 风控日志集合
     */
    public List<RiskControlLogInfo> selectRiskControlLogInfoList(RiskControlLogInfo riskControlLogInfo);

    /**
     * 新增风控日志
     *
     * @param riskControlLogInfo 风控日志
     * @return 结果
     */
    public int insertRiskControlLogInfo(RiskControlLogInfo riskControlLogInfo);

    /**
     * 修改风控日志
     *
     * @param riskControlLogInfo 风控日志
     * @return 结果
     */
    public int updateRiskControlLogInfo(RiskControlLogInfo riskControlLogInfo);

    /**
     * 删除风控日志
     *
     * @param logId 风控日志主键
     * @return 结果
     */
    public int deleteRiskControlLogInfoByLogId(String logId);

    /**
     * 批量删除风控日志
     *
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRiskControlLogInfoByLogIds(String[] logIds);
}
