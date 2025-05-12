package com.lz.picture.strategy.userBehaviorInfoStrategy.impl;

import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.strategy.userBehaviorInfoStrategy.UserBehaviorInfoStrategyConfig;

/**
 * Project: Picture
 * Package: com.lz.picture.strategy.userBehaviorInfoStrategy
 * Author: YY
 * CreateTime: 2025-04-14  17:43
 * Description: PictureLikeUserBehaviorInfoStrategyServiceImpl
 * Version: 1.0
 */
@UserBehaviorInfoStrategyConfig(targetType = "0", type = "0")
public class PictureLikeUserBehaviorInfoStrategyServiceImpl extends UserBehaviorInfoStrategyTemplate {

    @Override
    public UserBehaviorInfo getUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        return super.getUserBehaviorInfo(userBehaviorInfo);
    }

}
