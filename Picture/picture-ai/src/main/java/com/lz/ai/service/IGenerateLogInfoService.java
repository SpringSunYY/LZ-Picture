package com.lz.ai.service;

import java.util.List;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.vo.generateLogInfo.GenerateLogInfoVo;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户生成记录Service接口
 *
 * @author YY
 * @date 2025-08-08
 */
public interface IGenerateLogInfoService extends IService<GenerateLogInfo>
{
    //region mybatis代码
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
     * 批量删除用户生成记录
     *
     * @param logIds 需要删除的用户生成记录主键集合
     * @return 结果
     */
    public int deleteGenerateLogInfoByLogIds(String[] logIds);

    /**
     * 删除用户生成记录信息
     *
     * @param logId 用户生成记录主键
     * @return 结果
     */
    public int deleteGenerateLogInfoByLogId(String logId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param generateLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<GenerateLogInfo> getQueryWrapper(GenerateLogInfoQuery generateLogInfoQuery);

    /**
     * 转换vo
     *
     * @param generateLogInfoList GenerateLogInfo集合
     * @return GenerateLogInfoVO集合
     */
    List<GenerateLogInfoVo> convertVoList(List<GenerateLogInfo> generateLogInfoList);
}
