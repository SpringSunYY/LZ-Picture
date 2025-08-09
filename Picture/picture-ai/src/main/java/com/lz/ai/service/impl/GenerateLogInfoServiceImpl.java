package com.lz.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.ai.mapper.GenerateLogInfoMapper;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoQuery;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoRequest;
import com.lz.ai.model.vo.generateLogInfo.GenerateLogInfoVo;
import com.lz.ai.service.IGenerateLogInfoService;
import com.lz.ai.strategy.generate.AiGenerateStrategyExecutor;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户生成记录Service业务层处理
 *
 * @author YY
 * @date 2025-08-08
 */
@Service
public class GenerateLogInfoServiceImpl extends ServiceImpl<GenerateLogInfoMapper, GenerateLogInfo> implements IGenerateLogInfoService {
    @Resource
    private GenerateLogInfoMapper generateLogInfoMapper;

    @Resource
    private AiGenerateStrategyExecutor aiGenerateStrategyExecutor;
    //region mybatis代码

    /**
     * 查询用户生成记录
     *
     * @param logId 用户生成记录主键
     * @return 用户生成记录
     */
    @Override
    public GenerateLogInfo selectGenerateLogInfoByLogId(String logId) {
        return generateLogInfoMapper.selectGenerateLogInfoByLogId(logId);
    }

    /**
     * 查询用户生成记录列表
     *
     * @param generateLogInfo 用户生成记录
     * @return 用户生成记录
     */
    @Override
    public List<GenerateLogInfo> selectGenerateLogInfoList(GenerateLogInfo generateLogInfo) {
        return generateLogInfoMapper.selectGenerateLogInfoList(generateLogInfo);
    }

    /**
     * 新增用户生成记录
     *
     * @param generateLogInfo 用户生成记录
     * @return 结果
     */
    @Override
    public int insertGenerateLogInfo(GenerateLogInfo generateLogInfo) {
        generateLogInfo.setCreateTime(DateUtils.getNowDate());
        return generateLogInfoMapper.insertGenerateLogInfo(generateLogInfo);
    }

    /**
     * 修改用户生成记录
     *
     * @param generateLogInfo 用户生成记录
     * @return 结果
     */
    @Override
    public int updateGenerateLogInfo(GenerateLogInfo generateLogInfo) {
        generateLogInfo.setUpdateTime(DateUtils.getNowDate());
        return generateLogInfoMapper.updateGenerateLogInfo(generateLogInfo);
    }

    /**
     * 批量删除用户生成记录
     *
     * @param logIds 需要删除的用户生成记录主键
     * @return 结果
     */
    @Override
    public int deleteGenerateLogInfoByLogIds(String[] logIds) {
        return generateLogInfoMapper.deleteGenerateLogInfoByLogIds(logIds);
    }

    /**
     * 删除用户生成记录信息
     *
     * @param logId 用户生成记录主键
     * @return 结果
     */
    @Override
    public int deleteGenerateLogInfoByLogId(String logId) {
        return generateLogInfoMapper.deleteGenerateLogInfoByLogId(logId);
    }

    //endregion
    @Override
    public QueryWrapper<GenerateLogInfo> getQueryWrapper(GenerateLogInfoQuery generateLogInfoQuery) {
        QueryWrapper<GenerateLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = generateLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String logId = generateLogInfoQuery.getLogId();
        queryWrapper.eq(StringUtils.isNotEmpty(logId), "log_id", logId);

        String userId = generateLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String modelKey = generateLogInfoQuery.getModelKey();
        queryWrapper.eq(StringUtils.isNotEmpty(modelKey), "model_key", modelKey);

        String modelType = generateLogInfoQuery.getModelType();
        queryWrapper.eq(StringUtils.isNotEmpty(modelType), "model_type", modelType);

        String prompt = generateLogInfoQuery.getPrompt();
        queryWrapper.eq(StringUtils.isNotEmpty(prompt), "prompt", prompt);

        String negativePrompt = generateLogInfoQuery.getNegativePrompt();
        queryWrapper.eq(StringUtils.isNotEmpty(negativePrompt), "negative_prompt", negativePrompt);

        Float seed = generateLogInfoQuery.getSeed();
        queryWrapper.eq(StringUtils.isNotNull(seed), "seed", seed);

        Integer numbers = generateLogInfoQuery.getNumbers();
        queryWrapper.eq(StringUtils.isNotNull(numbers), "numbers", numbers);

        String taskId = generateLogInfoQuery.getTaskId();
        queryWrapper.eq(StringUtils.isNotEmpty(taskId), "task_id", taskId);

        Integer width = generateLogInfoQuery.getWidth();
        queryWrapper.eq(StringUtils.isNotNull(width), "width", width);

        Integer height = generateLogInfoQuery.getHeight();
        queryWrapper.eq(StringUtils.isNotNull(height), "height", height);

        Long requestDuration = generateLogInfoQuery.getRequestDuration();
        queryWrapper.eq(StringUtils.isNotNull(requestDuration), "request_duration", requestDuration);

        String targetId = generateLogInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId), "target_id", targetId);

        String logStatus = generateLogInfoQuery.getLogStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(logStatus), "log_status", logStatus);

        String aiStatusCode = generateLogInfoQuery.getAiStatusCode();
        queryWrapper.eq(StringUtils.isNotEmpty(aiStatusCode), "ai_status_code", aiStatusCode);

        String ipAddr = generateLogInfoQuery.getIpAddr();
        queryWrapper.eq(StringUtils.isNotEmpty(ipAddr), "ip_addr", ipAddr);

        String deviceId = generateLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = generateLogInfoQuery.getBrowser();
        queryWrapper.like(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = generateLogInfoQuery.getOs();
        queryWrapper.like(StringUtils.isNotEmpty(os), "os", os);

        String platform = generateLogInfoQuery.getPlatform();
        queryWrapper.like(StringUtils.isNotEmpty(platform), "platform", platform);

        Date createTime = generateLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = generateLogInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String isDelete = generateLogInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        return queryWrapper;
    }

    @Override
    public List<GenerateLogInfoVo> convertVoList(List<GenerateLogInfo> generateLogInfoList) {
        if (StringUtils.isEmpty(generateLogInfoList)) {
            return Collections.emptyList();
        }
        return generateLogInfoList.stream().map(GenerateLogInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public String userGenerate(GenerateLogInfoRequest request) {
        return aiGenerateStrategyExecutor.executeUserGenerate(request);
    }

}
