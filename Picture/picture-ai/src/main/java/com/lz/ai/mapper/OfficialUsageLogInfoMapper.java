package com.lz.ai.mapper;

import java.util.List;
import com.lz.ai.model.domain.OfficialUsageLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 官方AI操作日志Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface OfficialUsageLogInfoMapper extends BaseMapper<OfficialUsageLogInfo>
{
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
     * 删除官方AI操作日志
     *
     * @param logId 官方AI操作日志主键
     * @return 结果
     */
    public int deleteOfficialUsageLogInfoByLogId(String logId);

    /**
     * 批量删除官方AI操作日志
     *
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOfficialUsageLogInfoByLogIds(String[] logIds);
}
