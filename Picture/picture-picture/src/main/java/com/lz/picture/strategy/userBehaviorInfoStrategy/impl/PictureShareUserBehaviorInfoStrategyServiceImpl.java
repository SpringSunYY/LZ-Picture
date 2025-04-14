package com.lz.picture.strategy.userBehaviorInfoStrategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.PictureTagInfo;
import com.lz.picture.model.domain.PictureTagRelInfo;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.model.enums.PTagStatus;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.IPictureTagInfoService;
import com.lz.picture.service.IPictureTagRelInfoService;
import com.lz.picture.strategy.userBehaviorInfoStrategy.UserBehaviorInfoStrategyConfig;
import com.lz.picture.strategy.userBehaviorInfoStrategy.UserBehaviorInfoStrategyService;
import jakarta.annotation.Resource;

import java.util.List;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;

/**
 * Project: Picture
 * Package: com.lz.picture.strategy.userBehaviorInfoStrategy
 * Author: YY
 * CreateTime: 2025-04-14  17:43
 * Description: PictureShareUserBehaviorInfoStrategyServiceImpl
 * Version: 1.0
 */
@UserBehaviorInfoStrategyConfig(targetType = "0", type = "2")
public class PictureShareUserBehaviorInfoStrategyServiceImpl extends UserBehaviorInfoStrategyTemplate {

    @Override
    public UserBehaviorInfo getUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        //执行前操作
        UserBehaviorInfo behaviorInfo = beforeExecution(userBehaviorInfo);
        getTargetInfo(behaviorInfo);
        return returnExecution(userBehaviorInfo);
    }

    @Override
    public UserBehaviorInfo beforeExecution(UserBehaviorInfo userBehaviorInfo) {
        //TODO 创建分享链接
        return userBehaviorInfo;
    }
}
