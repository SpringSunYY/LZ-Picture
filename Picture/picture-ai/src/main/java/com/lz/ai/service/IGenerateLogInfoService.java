package com.lz.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoQuery;
import com.lz.ai.model.dto.generateLogInfo.UserGenerateLogInfoRequest;
import com.lz.ai.model.vo.generateLogInfo.GenerateLogInfoVo;
import com.lz.ai.strategy.generate.domain.AiGenerateRequest;
import com.lz.ai.model.vo.generateLogInfo.GenerateResponse;
import com.lz.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 用户生成记录Service接口
 *
 * @author YY
 * @date 2025-08-08
 */
public interface IGenerateLogInfoService extends IService<GenerateLogInfo> {
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

    /**
     * 用户生成图片
     *
     * @param request 请求参数
     * @return int
     * @author: YY
     * @method: userGenerate
     * @date: 2025/8/8 23:39
     **/
    List<GenerateResponse> userGenerate(AiGenerateRequest request);

    /**
     * 用户查询自己的生成记录
     *
     * @param request 查询条件
     * @return TableDataInfo
     * @author: YY
     * @method: userSelectGenerateLogInfoList
     * @date: 2025/8/12 00:05
     **/
    TableDataInfo userSelectGenerateLogInfoList(UserGenerateLogInfoRequest request);

    /**
     * 生成任务查询
     *
     * @param logId
     * @param userId
     * @param username
     * @return
     */
    GenerateLogInfo queryTask(String logId, String userId, String username);

    /**
     * 查询未删除的生成记录
     * @param logId
     * @return
     */
    GenerateLogInfo selectNormalGenerateLogInfoByLogId(String logId);

    /**
     * 用户删除生成记录
     *
     * @param logId
     * @param userId
     * @return
     */
    int userDeleteGenerateLogInfoByLogId(String logId, String userId);
}
