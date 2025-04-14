package com.lz.picture.strategy.userBehaviorInfoStrategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.PictureTagInfo;
import com.lz.picture.model.domain.PictureTagRelInfo;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.model.enums.PTagStatus;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.IPictureTagInfoService;
import com.lz.picture.service.IPictureTagRelInfoService;
import com.lz.picture.service.IUserBehaviorInfoService;
import com.lz.picture.strategy.userBehaviorInfoStrategy.UserBehaviorInfoStrategyService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.TimerTask;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;

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
    private IPictureTagInfoService pictureTagInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    /**
     * description: 之前图片前操作
     * author: YY
     * method: beforeExecution
     * date: 2025/4/15 00:01
     * param:
     * param: userBehaviorInfo
     * return: com.lz.picture.model.domain.UserBehaviorInfo
     **/
    public UserBehaviorInfo beforeExecution(UserBehaviorInfo userBehaviorInfo) {
        //判断此用户此类型是否存在
        UserBehaviorInfo behaviorInfo = userBehaviorInfoService.getOne(new LambdaQueryWrapper<UserBehaviorInfo>()
                .eq(UserBehaviorInfo::getUserId, userBehaviorInfo.getUserId())
                .eq(UserBehaviorInfo::getTargetType, userBehaviorInfo.getTargetType())
                .eq(UserBehaviorInfo::getBehaviorType, userBehaviorInfo.getBehaviorType())
                .eq(UserBehaviorInfo::getTargetId, userBehaviorInfo.getTargetId()));
        //存在表示要删除 不存在则是要添加
        if (StringUtils.isNotNull(behaviorInfo)) {
            userBehaviorInfoService.deleteUserBehaviorInfoByBehaviorId(behaviorInfo.getBehaviorId());
            return null;
        }
        return userBehaviorInfo;
    }

    /**
     * description: 返回操作
     * author: YY
     * method: returnExecution
     * date: 2025/4/15 00:01
     * param:
     * param: userBehaviorInfo
     * return: com.lz.picture.model.domain.UserBehaviorInfo
     **/
    public UserBehaviorInfo returnExecution(UserBehaviorInfo userBehaviorInfo) {
        //获取图片信息
        PictureInfo pictureInfo = pictureInfoService.selectPictureInfoByPictureId(userBehaviorInfo.getTargetId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo), "图片不存在");
        //赋值图片信息
        userBehaviorInfo.setTargetContent(pictureInfo.getName());
        userBehaviorInfo.setTargetCover(pictureInfo.getThumbnailUrl());
        userBehaviorInfo.setSpaceId(pictureInfo.getSpaceId());
        //查询标签关联
        List<String> relTagList = pictureTagRelInfoService.list(new LambdaQueryWrapper<PictureTagRelInfo>()
                        .eq(PictureTagRelInfo::getPictureId, userBehaviorInfo.getTargetId()))
                .stream()
                .map(PictureTagRelInfo::getTagId).toList();
        if (StringUtils.isEmpty(relTagList)) {
            return userBehaviorInfo;
        }
        //查询标签名称
        List<String> tags = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>()
                        .in(PictureTagInfo::getTagId, relTagList))
                .stream()
                .filter(tag -> {
                    return PTagStatus.TAG_STATUS_0.getValue().equals(tag.getTagsStatus());
                })
                .map(PictureTagInfo::getName)
                .toList();
        if (StringUtils.isEmpty(tags)) {
            return userBehaviorInfo;
        }
        userBehaviorInfo.setTags(String.join(COMMON_SEPARATOR, tags));
        return userBehaviorInfo;
    }

    @Override
    public UserBehaviorInfo getUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
        //执行前操作
        UserBehaviorInfo behaviorInfo = beforeExecution(userBehaviorInfo);
        if (StringUtils.isNull(behaviorInfo)) {
            return null;
        }
        UserBehaviorInfo info = returnExecution(userBehaviorInfo);
        //重新获取信息 异步去更新缓存
        getTargetInfo(info);
        return info;
    }

    public void getTargetInfo(UserBehaviorInfo info) {
        PictureAsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                pictureInfoService.resetPictureInfoCache(info.getTargetId());
            }
        });
    }
}
