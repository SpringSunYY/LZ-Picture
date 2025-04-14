package com.lz.picture.strategy.userBehaviorInfoStrategy;

import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.picture.model.domain.UserBehaviorInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Project: Picture
 * Package: com.lz.picture.strategy.userBehaviorInfoStrategy
 * Author: YY
 * CreateTime: 2025-04-14  17:32
 * Description: UserBehaviorInfoStrategyExecutor
 * 执行器
 * Version: 1.0
 */
@Service
public class UserBehaviorInfoStrategyExecutor {
    //策略模式列表
    @Resource
    private List<UserBehaviorInfoStrategyService> userBehaviorInfoStrategyServices;

//    @Resource
//    private ApplicationContext applicationContext;

    public UserBehaviorInfo executeGetUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        ThrowUtils.throwIf(StringUtils.isNull(userBehaviorInfo), "用户行为参数错误");
        //根据注解获取对应的策略
        for (UserBehaviorInfoStrategyService userBehaviorInfoStrategyService : userBehaviorInfoStrategyServices) {
            //判断是否存在注解
            if (!userBehaviorInfoStrategyService.getClass().isAnnotationPresent(UserBehaviorInfoStrategyConfig.class)) {
                throw new RuntimeException("用户行为参数错误");
            }
            UserBehaviorInfoStrategyConfig userBehaviorInfoStrategyConfig = userBehaviorInfoStrategyService.getClass().getAnnotation(UserBehaviorInfoStrategyConfig.class);
            if (userBehaviorInfoStrategyConfig.targetType().equals(userBehaviorInfo.getTargetType())
                    && userBehaviorInfoStrategyConfig.type().equals(userBehaviorInfo.getBehaviorType())) {
                return userBehaviorInfoStrategyService.getUserBehaviorInfo(userBehaviorInfo);
            }
        }
        //如果都不存在直接抛出
        throw new RuntimeException("用户行为参数错误");
    }
}
