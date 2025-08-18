package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.constant.HttpStatus;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.enums.PPictureStatusEnum;
import com.lz.picture.model.vo.userinfo.UserInfoVo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.IPictureUserInfoService;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_USER_INFO;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_USER_INFO_EXPIRE_TIME;

/**
 * 图片用户查询用户信息
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-18  17:25
 * @Version: 1.0
 */
@Service
public class PictureUserInfoServiceImpl implements IPictureUserInfoService {
    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IPictureInfoService pictureInfoService;

    @CustomCacheable(keyPrefix = PICTURE_USER_INFO, expireTime = PICTURE_USER_INFO_EXPIRE_TIME,
            keyField = "username")
    @Override
    public UserInfoVo getPictureUserInfo(String username) {
        //查询用户信息
        UserInfo userInfo = userInfoService.selectUserByUserName(username);
        ThrowUtils.throwIf(StringUtils.isNull(userInfo), HttpStatus.NO_CONTENT, "用户不存在");
        UserInfoVo userInfoVo = UserInfoVo.objToVo(userInfo);
        long pictureCount = 0L;
        long collectCount = 0L;
        long likeCount = 0L;
        long shareCount = 0L;
        List<PictureInfo> pictureInfos = pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>()
                .select(PictureInfo::getCollectCount, PictureInfo::getShareCount, PictureInfo::getLikeCount)
                .eq(PictureInfo::getUserId, userInfo.getUserId())
                .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue()));
        if (StringUtils.isEmpty(pictureInfos)) {
            return userInfoVo;
        }
        //遍历结果拿到值
        pictureCount = (long) pictureInfos.size();
        for (PictureInfo pictureInfo : pictureInfos) {
            collectCount += pictureInfo.getCollectCount();
            likeCount += pictureInfo.getLikeCount();
            shareCount += pictureInfo.getShareCount();
        }
        userInfoVo.setPictureCount(pictureCount);
        userInfoVo.setCollectCount(collectCount);
        userInfoVo.setLikeCount(likeCount);
        userInfoVo.setShareCount(shareCount);
        return userInfoVo;
    }
}
