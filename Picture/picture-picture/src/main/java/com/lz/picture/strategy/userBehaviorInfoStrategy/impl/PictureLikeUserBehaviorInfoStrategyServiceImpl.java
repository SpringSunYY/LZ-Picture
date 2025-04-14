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
 * Description: PictureLikeUserBehaviorInfoStrategyServiceImpl
 * Version: 1.0
 */
@UserBehaviorInfoStrategyConfig(targetType = "0", type = "0")
public class PictureLikeUserBehaviorInfoStrategyServiceImpl implements UserBehaviorInfoStrategyService {
    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

    @Override
    public UserBehaviorInfo getUserBehaviorInfo(UserBehaviorInfo userBehaviorInfo) {
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
}
