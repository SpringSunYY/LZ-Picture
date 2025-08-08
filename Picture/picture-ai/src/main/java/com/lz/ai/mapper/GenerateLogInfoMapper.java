package com.lz.ai.mapper;

import java.util.List;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户生成记录Mapper接口
 *
 * @author YY
 * @date 2025-08-08
 */
public interface GenerateLogInfoMapper extends BaseMapper<GenerateLogInfo>
{
    /**
     * 查询用户生成记录
     *
     * @param logId 用户生成记录主键
     * @return 用户生成记录
     */
    public GenerateLogInfo selectGenerateLogInfoByLogId(String logId);

    /**
     * 查询用户生成记录列表
     *
     * @param generateLogInfo 用户生成记录
     * @return 用户生成记录集合
     */
    public List<GenerateLogInfo> selectGenerateLogInfoList(GenerateLogInfo generateLogInfo);

    /**
     * 新增用户生成记录
     *
     * @param generateLogInfo 用户生成记录
     * @return 结果
     */
    public int insertGenerateLogInfo(GenerateLogInfo generateLogInfo);

    /**
     * 修改用户生成记录
     *
     * @param generateLogInfo 用户生成记录
     * @return 结果
     */
    public int updateGenerateLogInfo(GenerateLogInfo generateLogInfo);

    /**
     * 删除用户生成记录
     *
     * @param logId 用户生成记录主键
     * @return 结果
     */
    public int deleteGenerateLogInfoByLogId(String logId);

    /**
     * 批量删除用户生成记录
     *
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGenerateLogInfoByLogIds(String[] logIds);
}
