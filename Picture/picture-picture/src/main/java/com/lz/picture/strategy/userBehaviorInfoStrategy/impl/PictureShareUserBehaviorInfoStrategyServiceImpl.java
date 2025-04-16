package com.lz.picture.strategy.userBehaviorInfoStrategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.IUserBehaviorInfoService;
import com.lz.picture.strategy.userBehaviorInfoStrategy.UserBehaviorInfoStrategyConfig;
import jakarta.annotation.Resource;

import java.util.Date;
import java.util.TimerTask;

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

    @Resource
    private IPictureInfoService pictureInfoService;

    @Override
    public UserBehaviorInfo getUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        //判断今天是否分享过
        //如果不存在则添加，也就是分享记录每天只记录一次
        boolean b = !judgeExist(userBehaviorInfo);
        UserBehaviorInfo detailInfo = getDetailInfo(userBehaviorInfo);
        if (b) {
            detailInfo.setBehaviorId(IdUtils.snowflakeId().toString());
            userBehaviorInfoService.insertUserBehaviorInfo(detailInfo);
            //重新获取信息 异步去更新缓存
            asyncUpdate(userBehaviorInfo);
        }
        return detailInfo;
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
    @Override
    public void asyncUpdate(UserBehaviorInfo info) {
        PictureAsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                pictureInfoService.resetPictureInfoCache(info.getTargetId());
            }
        });
    }

}
