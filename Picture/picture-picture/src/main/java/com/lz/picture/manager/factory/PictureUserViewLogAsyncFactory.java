package com.lz.picture.manager.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lz.common.config.OssConfig;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.userViewLogInfo.UserViewLogTargetInfoDto;
import com.lz.picture.model.vo.pictureInfo.UserPictureDetailInfoVo;
import com.lz.picture.service.IUserViewLogInfoService;
import com.lz.user.model.domain.UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.TimerTask;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;
import static com.lz.common.constant.picture.PictureInfoConstants.PICTURE_RECOMMEND_MODEL_VIEW_TYPE;
import static com.lz.picture.model.enums.PViewLogTargetTypeEnum.VIEW_LOG_TARGET_TYPE_0;

/**
 * 图片用户浏览异步任务异步工厂（产生任务用）
 *
 * @author YY
 */
@Slf4j
public class PictureUserViewLogAsyncFactory {

    /**
     * description: 记录图片浏览记录
     * author: YY
     * method: recordUserViewLog
     * date: 2025/4/12 22:42
     * param:
     * param: userId
     * param: targetType
     * param: score
     * param: deviceInfo
     * param: jsonResult
     * return: java.util.TimerTask
     **/
    public static TimerTask recordUserViewLog(String userId, String targetType,  DeviceInfo deviceInfo, Object jsonResult, Date nowDate) {
        UserViewLogTargetInfoDto userViewLogTargetInfoDto = new UserViewLogTargetInfoDto();

        return new TimerTask() {
            @Override
            public void run() {
                //获取目标id和内容
                getTargetInfo(targetType, userViewLogTargetInfoDto, jsonResult);
                //插入用户浏览记录
                int i = SpringUtils.getBean(IUserViewLogInfoService.class).recordUserViewLog(userId, targetType, userViewLogTargetInfoDto, nowDate, deviceInfo);
                //如果是图片
                if (targetType.equals(VIEW_LOG_TARGET_TYPE_0.getValue()) && i > 0) {
                    PictureAsyncManager.me().execute(PictureRecommendAsyncFactory.insertUserInterestModel(userId, PICTURE_RECOMMEND_MODEL_VIEW_TYPE));
                }
            }
        };
    }

    /**
     * description: 获取目标内容
     * author: YY
     * method: getTargetInfo
     * date: 2025/4/13 00:02
     * param:
     * param: targetType
     * param: userViewLogTargetInfo
     * param: jsonResult
     * return: void
     **/
    private static void getTargetInfo(String targetType, UserViewLogTargetInfoDto userViewLogTargetInfoDto, Object jsonResult) {
        //获取到返回结构的data
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(jsonResult));
        JSONObject data = jsonObject.getJSONObject("data");
        //判断目标类型
        switch (targetType) {
            case "0":
                getTargetIdAndContentByPicture(data, userViewLogTargetInfoDto);
                break;
            case "1":
                getTargetIdAndContentBySpace(data, userViewLogTargetInfoDto);
                break;
            case "2":
                getTargetIdAndContentByUserInfo(data, userViewLogTargetInfoDto);
                break;
            default:
                break;
        }
    }

    /**
     * description: 获取信息根据用户
     * author: YY
     * method: getTargetIdAndContentByUserInfo
     * date: 2025/4/12 23:13
     * param:
     * param: data
     * param: userViewLogTargetInfo
     * return: void
     **/
    private static void getTargetIdAndContentByUserInfo(JSONObject data, UserViewLogTargetInfoDto userViewLogTargetInfoDto) {
        UserInfo userInfo = JSONObject.parseObject(JSON.toJSONString(data), UserInfo.class);
        userViewLogTargetInfoDto.setTargetId(userInfo.getUserId());
        userViewLogTargetInfoDto.setTargetContent(userInfo.getNickName());
        //需要去除头像的域名
        //如果以我们域名开头
        if (userInfo.getAvatarUrl().startsWith(OssConfig.getDnsUrl())) {
            userViewLogTargetInfoDto.setTargetCover(userInfo.getAvatarUrl().replace(OssConfig.getDnsUrl(), ""));
        }else {
            userViewLogTargetInfoDto.setTargetCover(userInfo.getAvatarUrl());
        }
    }

    /**
     * description: 获取信息根据空间
     * author: YY
     * method: getTargetIdAndContentBySpace
     * date: 2025/4/12 23:09
     * param:
     * param: data
     * param: userViewLogTargetInfo
     * return: void
     **/
    private static void getTargetIdAndContentBySpace(JSONObject data, UserViewLogTargetInfoDto userViewLogTargetInfoDto) {
        SpaceInfo spaceInfo = JSONObject.parseObject(JSON.toJSONString(data), SpaceInfo.class);
        userViewLogTargetInfoDto.setSpaceId(spaceInfo.getSpaceId());
        userViewLogTargetInfoDto.setTargetId(spaceInfo.getSpaceId());
        userViewLogTargetInfoDto.setTargetContent(spaceInfo.getSpaceName());
        //如果以我们域名开头
        if (spaceInfo.getSpaceAvatar().startsWith(OssConfig.getDnsUrl())) {
            userViewLogTargetInfoDto.setTargetCover(spaceInfo.getSpaceAvatar().replace(OssConfig.getDnsUrl(), ""));
        }else {
            userViewLogTargetInfoDto.setTargetCover(spaceInfo.getSpaceAvatar());
        }
    }

    /**
     * description: 获取信息根据图片
     * author: YY
     * method: getTargetIdAndContentByPicture
     * date: 2025/4/12 23:04
     * param:
     * param: data
     * param: userViewLogTargetInfo
     * return: void
     **/
    private static void getTargetIdAndContentByPicture(JSONObject data, UserViewLogTargetInfoDto userViewLogTargetInfoDto) {
        UserPictureDetailInfoVo pictureInfo = JSONObject.parseObject(JSON.toJSONString(data), UserPictureDetailInfoVo.class);
        //获取图片信息
        if (StringUtils.isNotEmpty(pictureInfo.getPictureTags())) {
            userViewLogTargetInfoDto.setTags(String.join(COMMON_SEPARATOR, pictureInfo.getPictureTags()));
        }
        userViewLogTargetInfoDto.setTargetId(pictureInfo.getPictureId());
        userViewLogTargetInfoDto.setTargetContent(pictureInfo.getName());
        //如果以我们域名开头
        if (pictureInfo.getThumbnailUrl().startsWith(OssConfig.getDnsUrl())) {
            userViewLogTargetInfoDto.setTargetCover(pictureInfo.getThumbnailUrl().replace(OssConfig.getDnsUrl(), ""));
        }else {
            userViewLogTargetInfoDto.setTargetCover(pictureInfo.getThumbnailUrl());
        }
        userViewLogTargetInfoDto.setCategoryId(pictureInfo.getCategoryId());
        userViewLogTargetInfoDto.setSpaceId(pictureInfo.getSpaceId());
    }
}
