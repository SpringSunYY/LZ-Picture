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
import com.lz.picture.service.IUserBehaviorInfoService;
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
