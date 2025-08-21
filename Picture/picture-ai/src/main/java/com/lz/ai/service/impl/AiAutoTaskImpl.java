package com.lz.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.enums.AiLogStatusEnum;
import com.lz.ai.service.IAiAutoTask;
import com.lz.ai.service.IGenerateLogInfoService;
import com.lz.ai.strategy.generate.AiGenerateStrategyExecutor;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.StringUtils;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.redis.AiRedisConstants.AI_GENERATE_QUERY_TASK;

/**
 * 自动更新订单
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-21  16:57
 * @Version: 1.0
 */
@Slf4j
@Service
public class AiAutoTaskImpl implements IAiAutoTask {
    @Resource
    private IGenerateLogInfoService generateLogInfoService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private AiGenerateStrategyExecutor aiGenerateStrategyExecutor;

    @Resource
    private IUserInfoService userInfoService;

    /**
     * 自动更新订单
     */
    @Override
    public void autoExecuteUpdateTask() {
        //查询到所有的进行中的订单
        List<GenerateLogInfo> generateLogInfos = generateLogInfoService.list(
                new LambdaQueryWrapper<GenerateLogInfo>()
                        .eq(GenerateLogInfo::getLogStatus, AiLogStatusEnum.LOG_STATUS_0.getValue())
                        .eq(GenerateLogInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue()));
        for (GenerateLogInfo generateLogInfo : generateLogInfos) {
            String logId = generateLogInfo.getLogId();
            String userId = generateLogInfo.getUserId();
            UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
            String username = userId;
            if (StringUtils.isNotNull(userInfo)) {
                username = userInfo.getUserName();
            }
            //使用redis锁住任务，避免重复查询
            String lockKey = AI_GENERATE_QUERY_TASK + COMMON_SEPARATOR_CACHE + logId;
            RLock rLock = redissonClient.getLock(lockKey);
            boolean locked = false;
            try {
                locked = rLock.tryLock(5, 10, TimeUnit.SECONDS);
                if (locked) {
                    aiGenerateStrategyExecutor.executeQuery(generateLogInfo, username);
                }
            } catch (InterruptedException e) {
                log.error("获取锁失败，生成任务：{}", logId, e);
                Thread.currentThread().interrupt();
            }
        }
        generateLogInfoService.updateBatchById(generateLogInfos);
    }
}
