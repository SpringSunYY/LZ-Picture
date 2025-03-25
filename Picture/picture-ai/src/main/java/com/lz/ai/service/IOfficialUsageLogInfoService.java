package com.lz.ai.service;

import java.util.List;
import com.lz.ai.model.domain.OfficialUsageLogInfo;
import com.lz.ai.model.vo.officialUsageLogInfo.OfficialUsageLogInfoVo;
import com.lz.ai.model.dto.officialUsageLogInfo.OfficialUsageLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 官方AI操作日志Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IOfficialUsageLogInfoService extends IService<OfficialUsageLogInfo>
{
    //region mybatis代码
    /**
     * 查询官方AI操作日志
     *
     * @param logId 官方AI操作日志主键
     * @return 官方AI操作日志
     */
    public OfficialUsageLogInfo selectOfficialUsageLogInfoByLogId(String logId);

    /**
     * 查询官方AI操作日志列表
     *
     * @param officialUsageLogInfo 官方AI操作日志
     * @return 官方AI操作日志集合
     */
    public List<OfficialUsageLogInfo> selectOfficialUsageLogInfoList(OfficialUsageLogInfo officialUsageLogInfo);

    /**
     * 新增官方AI操作日志
     *
     * @param officialUsageLogInfo 官方AI操作日志
     * @return 结果
     */
    public int insertOfficialUsageLogInfo(OfficialUsageLogInfo officialUsageLogInfo);

    /**
     * 修改官方AI操作日志
     *
     * @param officialUsageLogInfo 官方AI操作日志
     * @return 结果
     */
    public int updateOfficialUsageLogInfo(OfficialUsageLogInfo officialUsageLogInfo);

    /**
     * 批量删除官方AI操作日志
     *
     * @param logIds 需要删除的官方AI操作日志主键集合
     * @return 结果
     */
    public int deleteOfficialUsageLogInfoByLogIds(String[] logIds);

    /**
     * 删除官方AI操作日志信息
     *
     * @param logId 官方AI操作日志主键
     * @return 结果
     */
    public int deleteOfficialUsageLogInfoByLogId(String logId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param officialUsageLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<OfficialUsageLogInfo> getQueryWrapper(OfficialUsageLogInfoQuery officialUsageLogInfoQuery);

    /**
     * 转换vo
     *
     * @param officialUsageLogInfoList OfficialUsageLogInfo集合
     * @return OfficialUsageLogInfoVO集合
     */
    List<OfficialUsageLogInfoVo> convertVoList(List<OfficialUsageLogInfo> officialUsageLogInfoList);
}
