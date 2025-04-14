package com.lz.picture.manager.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.vo.pictureInfo.UserPictureDetailInfoVo;
import com.lz.picture.model.vo.userViewLogInfo.UserViewLogTargetInfo;
import com.lz.picture.service.IUserViewLogInfoService;
import com.lz.user.model.domain.UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.TimerTask;

/**
 * 图片异步任务异步工厂（产生任务用）
 *
 * @author YY
 */
@Slf4j
public class PictureAsyncFactory {

    public static final String SEPARATION = ";";

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
    public static TimerTask recordUserViewLog(String userId, String targetType, double score, DeviceInfo deviceInfo, Object jsonResult, Date nowDate) {
        UserViewLogTargetInfo userViewLogTargetInfo = new UserViewLogTargetInfo();

        //获取目标id和内容
        getTargetInfo(targetType, userViewLogTargetInfo, jsonResult);

        return new TimerTask() {
            @Override
            public void run() {
                //插入用户浏览记录
                SpringUtils.getBean(IUserViewLogInfoService.class).recordUserViewLog(userId, targetType, score, userViewLogTargetInfo, nowDate, deviceInfo);
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
    private static void getTargetInfo(String targetType, UserViewLogTargetInfo userViewLogTargetInfo, Object jsonResult) {
        //获取到返回结构的data
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(jsonResult));
        JSONObject data = jsonObject.getJSONObject("data");
        //判断目标类型
        switch (targetType) {
            case "0":
                getTargetIdAndContentByPicture(data, userViewLogTargetInfo);
                break;
            case "1":
                getTargetIdAndContentBySpace(data, userViewLogTargetInfo);
                break;
            case "2":
                getTargetIdAndContentByUserInfo(data, userViewLogTargetInfo);
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
    private static void getTargetIdAndContentByUserInfo(JSONObject data, UserViewLogTargetInfo userViewLogTargetInfo) {
        UserInfo userInfo = JSONObject.parseObject(JSON.toJSONString(data), UserInfo.class);
        userViewLogTargetInfo.setTargetId(userInfo.getUserId());
        userViewLogTargetInfo.setTargetContent(userInfo.getNickName());
        userViewLogTargetInfo.setTargetCover(userInfo.getAvatarUrl());
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
    private static void getTargetIdAndContentBySpace(JSONObject data, UserViewLogTargetInfo userViewLogTargetInfo) {
        SpaceInfo spaceInfo = JSONObject.parseObject(JSON.toJSONString(data), SpaceInfo.class);
        userViewLogTargetInfo.setSpaceId(spaceInfo.getSpaceId());
        userViewLogTargetInfo.setTargetId(spaceInfo.getSpaceId());
        userViewLogTargetInfo.setTargetContent(spaceInfo.getSpaceName());
        userViewLogTargetInfo.setTargetCover(spaceInfo.getSpaceAvatar());
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
    private static void getTargetIdAndContentByPicture(JSONObject data, UserViewLogTargetInfo userViewLogTargetInfo) {
        UserPictureDetailInfoVo pictureInfo = JSONObject.parseObject(JSON.toJSONString(data), UserPictureDetailInfoVo.class);
//        System.out.println("pictureInfo = " + pictureInfo);
        //获取图片信息
        if (StringUtils.isNotEmpty(pictureInfo.getPictureTags())) {
            userViewLogTargetInfo.setTags(String.join(SEPARATION, pictureInfo.getPictureTags()));
        }
        userViewLogTargetInfo.setTargetId(pictureInfo.getPictureId());
        userViewLogTargetInfo.setTargetContent(pictureInfo.getName());
        userViewLogTargetInfo.setTargetCover(pictureInfo.getThumbnailUrl());
        userViewLogTargetInfo.setCategoryId(pictureInfo.getCategoryId());
        userViewLogTargetInfo.setSpaceId(pictureInfo.getSpaceId());
    }
}
