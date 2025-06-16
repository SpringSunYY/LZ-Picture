package com.lz.picture.strategy.userBehaviorInfoStrategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureRecommendAsyncFactory;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.service.IPictureRecommendInfoService;
import com.lz.picture.service.IUserBehaviorInfoService;
import com.lz.picture.strategy.userBehaviorInfoStrategy.UserBehaviorInfoStrategyConfig;
import jakarta.annotation.Resource;

import java.util.Date;

import static com.lz.common.constant.picture.PictureInfoConstants.PICTURE_RECOMMEND_MODEL_BEHAVIOR_TYPE;
import static com.lz.common.constant.picture.PictureInfoConstants.PICTURE_RECOMMEND_MODEL_VIEW_TYPE;

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

    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    @Override
    public Boolean getUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        //判断今天是否分享过
        //如果不存在则添加，也就是分享记录每天只记录一次
        boolean exist = judgeExist(userBehaviorInfo);
        UserBehaviorInfo detailInfo = getDetailInfo(userBehaviorInfo);
        //如果不为空表示今天没有需要新增
        if (!exist) {
            detailInfo.setBehaviorId(IdUtils.snowflakeId().toString());
            userBehaviorInfoService.insertUserBehaviorInfo(detailInfo);
            PictureAsyncManager.me().execute(PictureRecommendAsyncFactory.insertUserInterestModel(userBehaviorInfo.getUserId(), PICTURE_RECOMMEND_MODEL_BEHAVIOR_TYPE));
        }
        //重新获取信息 异步去更新缓存
        asyncUpdate(userBehaviorInfo, exist);
        return true;
    }

    @Override
    public boolean judgeExist(UserBehaviorInfo userBehaviorInfo) {
        //判断此用户此类型是否存在
        UserBehaviorInfo behaviorInfo = userBehaviorInfoService.getOne(
                new LambdaQueryWrapper<UserBehaviorInfo>()
                        .eq(UserBehaviorInfo::getUserId, userBehaviorInfo.getUserId())
                        .eq(UserBehaviorInfo::getTargetType, userBehaviorInfo.getTargetType())
                        .eq(UserBehaviorInfo::getBehaviorType, userBehaviorInfo.getBehaviorType())
                        .eq(UserBehaviorInfo::getTargetId, userBehaviorInfo.getTargetId())
                        .apply("DATE_FORMAT(create_time, '%Y-%m-%d') = {0}", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date())));
        //存在表示要删除 不存在则是要添加
        return StringUtils.isNotNull(behaviorInfo);
    }
}
