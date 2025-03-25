package com.lz.points.service;

import java.util.List;
import com.lz.points.model.domain.RiskControlLogInfo;
import com.lz.points.model.vo.riskControlLogInfo.RiskControlLogInfoVo;
import com.lz.points.model.dto.riskControlLogInfo.RiskControlLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 风控日志Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IRiskControlLogInfoService extends IService<RiskControlLogInfo>
{
    //region mybatis代码
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
     * 批量删除风控日志
     *
     * @param logIds 需要删除的风控日志主键集合
     * @return 结果
     */
    public int deleteRiskControlLogInfoByLogIds(String[] logIds);

    /**
     * 删除风控日志信息
     *
     * @param logId 风控日志主键
     * @return 结果
     */
    public int deleteRiskControlLogInfoByLogId(String logId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param riskControlLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<RiskControlLogInfo> getQueryWrapper(RiskControlLogInfoQuery riskControlLogInfoQuery);

    /**
     * 转换vo
     *
     * @param riskControlLogInfoList RiskControlLogInfo集合
     * @return RiskControlLogInfoVO集合
     */
    List<RiskControlLogInfoVo> convertVoList(List<RiskControlLogInfo> riskControlLogInfoList);
}
