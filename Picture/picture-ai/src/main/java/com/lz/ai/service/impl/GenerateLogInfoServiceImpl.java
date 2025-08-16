package com.lz.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.ai.mapper.GenerateLogInfoMapper;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoQuery;
import com.lz.ai.model.dto.generateLogInfo.UserGenerateLogInfoRequest;
import com.lz.ai.model.enums.AiLogStatusEnum;
import com.lz.ai.model.vo.generateLogInfo.GenerateLogInfoVo;
import com.lz.ai.model.vo.generateLogInfo.GenerateResponse;
import com.lz.ai.model.vo.generateLogInfo.UserGenerateLogInfoVo;
import com.lz.ai.service.IGenerateLogInfoService;
import com.lz.ai.service.IModelParamsInfoService;
import com.lz.ai.strategy.generate.AiGenerateStrategyExecutor;
import com.lz.ai.strategy.generate.domain.AiGenerateRequest;
import com.lz.common.annotation.CustomCacheEvict;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
import com.lz.common.config.OssConfig;
import com.lz.common.constant.HttpStatus;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.points.service.IAccountInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.redis.AiRedisConstants.*;

/**
 * 用户生成记录Service业务层处理
 *
 * @author YY
 * @date 2025-08-08
 */
@Slf4j
@Service
public class GenerateLogInfoServiceImpl extends ServiceImpl<GenerateLogInfoMapper, GenerateLogInfo> implements IGenerateLogInfoService {
    @Resource
    private GenerateLogInfoMapper generateLogInfoMapper;

    @Resource
    private AiGenerateStrategyExecutor aiGenerateStrategyExecutor;

    @Resource
    private IModelParamsInfoService modelParamsInfoService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private IAccountInfoService accountInfoService;
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
    @CustomSort(sortMappingFields = {"numbers", "width", "request_duration", "price_used", "points_used", "create_time"},
            sortFields = {"numbers", "width", "requestDuration", "priceUsed", "pointsUsed", "createTime"})
    @Override
    public List<GenerateLogInfo> selectGenerateLogInfoList(GenerateLogInfo generateLogInfo) {
        List<GenerateLogInfo> generateLogInfos = generateLogInfoMapper.selectGenerateLogInfoList(generateLogInfo);
        for (GenerateLogInfo info : generateLogInfos) {
            info.setFileUrls(OssConfig.builderBatchPictureUrl(info.getFileUrls(), null));
        }
        return generateLogInfos;
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

        String hasStatistics = generateLogInfoQuery.getHasStatistics();
        queryWrapper.eq(StringUtils.isNotEmpty(hasStatistics), "has_statistics", hasStatistics);

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

    @CustomCacheEvict(keyPrefixes = {AI_GENERATE_LIST}, keyFields = {"request.userId"})
    @Override
    public List<GenerateResponse> userGenerate(AiGenerateRequest request) {
        ThrowUtils.throwIf(accountInfoService.getVerifyPassword(request.getUserId()) != 1,
                "请先输入密码");
        List<GenerateLogInfo> generateLogInfos = aiGenerateStrategyExecutor.executeUserGenerate(request);
        return GenerateResponse.objToResponse(generateLogInfos);
    }

    @CustomCacheable(keyPrefix = AI_GENERATE_LIST, keyField = "request.userId",
            expireTime = AI_GENERATE_LIST_EXPIRE_TIME,
            useQueryParamsAsKey = true)
    @Override
    public TableDataInfo userSelectGenerateLogInfoList(UserGenerateLogInfoRequest request) {
        Page<GenerateLogInfo> pictureInfoPage = new Page<>();
        pictureInfoPage.setCurrent(request.getPageNum());
        pictureInfoPage.setSize(request.getPageSize());
        LambdaQueryWrapper<GenerateLogInfo> query = new LambdaQueryWrapper<GenerateLogInfo>()
                .select(GenerateLogInfo::getLogId, GenerateLogInfo::getModelKey, GenerateLogInfo::getPrompt, GenerateLogInfo::getModelType, GenerateLogInfo::getModelKey,
                        GenerateLogInfo::getNegativePrompt, GenerateLogInfo::getSeed, GenerateLogInfo::getFileUrls, GenerateLogInfo::getPointsUsed,
                        GenerateLogInfo::getWidth, GenerateLogInfo::getHeight, GenerateLogInfo::getCreateTime, GenerateLogInfo::getLogStatus)
                .eq(GenerateLogInfo::getUserId, request.getUserId())
                .eq(GenerateLogInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .and(wrapper ->
                        wrapper.eq(GenerateLogInfo::getLogStatus, AiLogStatusEnum.LOG_STATUS_1.getValue())
                                .or()
                                .eq(GenerateLogInfo::getLogStatus, AiLogStatusEnum.LOG_STATUS_0.getValue()))
                .eq(StringUtils.isNotEmpty(request.getUserId()), GenerateLogInfo::getUserId, request.getUserId())
                .orderByDesc(GenerateLogInfo::getCreateTime);
        Page<GenerateLogInfo> page = this.page(pictureInfoPage, query);
        List<GenerateLogInfo> records = page.getRecords();
        ArrayList<UserGenerateLogInfoVo> userGenerateLogInfoVos = new ArrayList<>();
        for (GenerateLogInfo info : records) {
            ModelParamsInfo modelParamsInfo = modelParamsInfoService.selectModelParamsInfoByModelKey(info.getModelKey());
            UserGenerateLogInfoVo vo = UserGenerateLogInfoVo.objToVo(info);
            vo.setModelName(modelParamsInfo.getModelLabel());
            userGenerateLogInfoVos.add(vo);
        }
        return new TableDataInfo(userGenerateLogInfoVos, Math.toIntExact(page.getTotal()));
    }

    @CustomCacheEvict(keyPrefixes = {AI_GENERATE_LIST}, keyFields = {"userId"})
    @Override
    public GenerateLogInfo queryTask(String logId, String userId, String username) {
        GenerateLogInfo generateLogInfo = this.getById(logId);
        ThrowUtils.throwIf(StringUtils.isNull(generateLogInfo)
                        ||!generateLogInfo.getUserId().equals(userId),
                HttpStatus.NO_CONTENT, "用户未查询到该任务");
        ThrowUtils.throwIf(generateLogInfo.getLogStatus().equals(AiLogStatusEnum.LOG_STATUS_1.getValue()),
                "任务已完成，无需继续查询");
        //使用redis锁住任务，避免重复查询
        String lockKey = AI_GENERATE_QUERY_TASK + COMMON_SEPARATOR_CACHE + logId;
        RLock rLock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = rLock.tryLock(5, 10, TimeUnit.SECONDS);
            if (locked) {
                aiGenerateStrategyExecutor.executeQuery(generateLogInfo, username);
                this.updateById(generateLogInfo);
            }
        } catch (InterruptedException e) {
            log.error("获取锁失败，生成任务：{}", logId, e);
            Thread.currentThread().interrupt();
            return generateLogInfo;
        }
        return generateLogInfo;
    }

}
