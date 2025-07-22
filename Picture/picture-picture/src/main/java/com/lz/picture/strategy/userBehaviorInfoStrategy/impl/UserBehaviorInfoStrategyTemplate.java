package com.lz.picture.strategy.userBehaviorInfoStrategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.config.OssConfig;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureRecommendAsyncFactory;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.IPictureTagRelInfoService;
import com.lz.picture.service.IUserBehaviorInfoService;
import com.lz.picture.strategy.userBehaviorInfoStrategy.UserBehaviorInfoStrategyService;
import com.lz.picture.utils.PictureStatisticsUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

import static com.lz.common.constant.picture.PictureInfoConstants.PICTURE_RECOMMEND_MODEL_BEHAVIOR_TYPE;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_USER_BEHAVIOR;
import static com.lz.picture.model.enums.PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0;

/**
 * Project: Picture
 * Package: com.lz.picture.strategy.userBehaviorInfoStrategy.template
 * Author: YY
 * CreateTime: 2025-04-14  23:33
 * Description: UserBehaviorInfoStrategyTemplate
 * Version: 1.0
 */
@Slf4j
public class UserBehaviorInfoStrategyTemplate implements UserBehaviorInfoStrategyService {
    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private PictureStatisticsUtil pictureStatisticsUtil;

    /**
     * description: 判断是否存在
     * author: YY
     * method: judgeExist
     * date: 2025/4/15 00:01
     * param:
     * param: userBehaviorInfo
     * return: com.lz.picture.model.domain.UserBehaviorInfo
     **/
    public boolean judgeExist(UserBehaviorInfo userBehaviorInfo) {
        //判断此用户此类型是否存在
        UserBehaviorInfo behaviorInfo = userBehaviorInfoService.getOne(new LambdaQueryWrapper<UserBehaviorInfo>()
                .eq(UserBehaviorInfo::getUserId, userBehaviorInfo.getUserId())
                .eq(UserBehaviorInfo::getTargetType, userBehaviorInfo.getTargetType())
                .eq(UserBehaviorInfo::getBehaviorType, userBehaviorInfo.getBehaviorType())
                .eq(UserBehaviorInfo::getTargetId, userBehaviorInfo.getTargetId()));
        //存在表示要删除 存在是要删除
        if (StringUtils.isNotNull(behaviorInfo)) {
            userBehaviorInfoService.deleteUserBehaviorInfoByBehaviorId(behaviorInfo.getBehaviorId());
            //如果是图片，存在需要减分，因为是删除
            if (USER_BEHAVIOR_TARGET_TYPE_0.getValue().equals(userBehaviorInfo.getTargetType())) {
                double score = 0 - userBehaviorInfo.getScore();
                pictureStatisticsUtil.pictureHotStatisticsIncrementScore(userBehaviorInfo.getTargetId(), score);
            }
            return true;
        } else {
            //如果是图片，不存在既要加分
            if (USER_BEHAVIOR_TARGET_TYPE_0.getValue().equals(userBehaviorInfo.getTargetType())) {
                Double score = userBehaviorInfo.getScore();
                pictureStatisticsUtil.pictureHotStatisticsIncrementScore(userBehaviorInfo.getTargetId(), score);
            }
        }
        return false;
    }

    /**
     * description: 获取详细信息
     * author: YY
     * method: getDetailInfo
     * date: 2025/4/15 00:01
     * param:
     * param: userBehaviorInfo
     * return: com.lz.picture.model.domain.UserBehaviorInfo
     **/
    public UserBehaviorInfo getDetailInfo(UserBehaviorInfo userBehaviorInfo) {
        //获取图片信息
        PictureInfo pictureInfo = pictureInfoService.selectPictureInfoByPictureId(userBehaviorInfo.getTargetId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo), "图片不存在");
        //赋值图片信息
        userBehaviorInfo.setTargetContent(pictureInfo.getName());
        //如果以我们域名开头
        if (pictureInfo.getThumbnailUrl().startsWith(OssConfig.getDnsUrl())) {
            userBehaviorInfo.setTargetCover(pictureInfo.getThumbnailUrl().replace(OssConfig.getDnsUrl(), ""));
        } else {
            userBehaviorInfo.setTargetCover(pictureInfo.getThumbnailUrl());
        }
        userBehaviorInfo.setSpaceId(pictureInfo.getSpaceId());
        userBehaviorInfo.setCategoryId(pictureInfo.getCategoryId());
        //查询标签关联
        userBehaviorInfo.setTags(pictureTagRelInfoService.getPictureTagNamesStr(userBehaviorInfo.getTargetId()));
        return userBehaviorInfo;
    }

    @Override
    public Boolean getUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        //执行前操作 判断是否存在 如果是已经存在，就给之前的数据删除了
        boolean exist = judgeExist(userBehaviorInfo);
        UserBehaviorInfo info = getDetailInfo(userBehaviorInfo);
        //插入数据库 如果记录不存在
        if (StringUtils.isNotNull(info) && !exist) {
            info.setBehaviorId(IdUtils.snowflakeId().toString());
            userBehaviorInfoService.insertUserBehaviorInfo(info);
            PictureAsyncManager.me().execute(PictureRecommendAsyncFactory.insertUserInterestModel(userBehaviorInfo.getUserId(), PICTURE_RECOMMEND_MODEL_BEHAVIOR_TYPE));
        }
        //重新获取信息 异步去更新缓存
        asyncUpdate(info, exist);
        return !exist;
    }

    public void asyncUpdate(UserBehaviorInfo info, Boolean exist) {
        PictureAsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                String behaviorKey = PICTURE_USER_BEHAVIOR + info.getUserId() + ":" + info.getTargetType() + ":" + info.getTargetId();
                redisCache.deleteObject(behaviorKey);
                pictureInfoService.resetPictureInfoCacheByBehavior(info.getTargetId(), info.getBehaviorType(), exist);
            }
        });
    }
}
