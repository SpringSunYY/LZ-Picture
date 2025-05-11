package com.lz.picture.manager.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.manager.file.model.PictureFileResponse;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.config.model.dto.fileLogInfo.FileLogUpdate;
import com.lz.config.model.enmus.CFileLogStatusEnum;
import com.lz.config.model.enmus.CFileLogTypeEnum;
import com.lz.config.service.IFileLogInfoService;
import com.lz.picture.model.domain.PictureInfo;
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
     * 记录文件日志
     *
     * @param pictureFileResponse 返回图片信息
     * @param userId              用户编号
     * @param ossType             存储类型
     * @param logType             日志类型
     * @return TimerTask
     * @author: YY
     * @date: 2025/5/10 22:51
     **/
    public static TimerTask recordFileLog(PictureFileResponse pictureFileResponse, String userId, String ossType, String logType) {
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        return new TimerTask() {
            @Override
            public void run() {
                //插入文件日志
                SpringUtils.getBean(IFileLogInfoService.class).recordFileLog(pictureFileResponse, userId, ossType, logType, deviceInfo);
            }
        };
    }

    /**
     * 异步更新文件日志，判断是否和存入数据库日志一样，如果不一样则更新，如果一样则不更新，表示没有新上传图片
     * 因为新增时图片已经为正常
     * 不一样表示新上传图片，要把上传的图片更新为正常，老图片为冗余
     *
     * @param pictureInfoDb
     * @param pictureInfo
     * @return TimerTask
     * @author: YY
     * @method: updateFileLogInfo
     * @date: 2025/5/11 15:25
     **/
    public static TimerTask updateFileLogInfo(PictureInfo pictureInfoDb, PictureInfo pictureInfo) {
        return new TimerTask() {
            @Override
            public void run() {
                //判断是否和存入数据库日志一样，如果不一样则更新，如果一样则不更新，因为新增时就是正常
                if (!pictureInfoDb.getPictureUrl().equals(pictureInfo.getPictureUrl())) {
                    //如果不一样，更新新的为正常，老的为冗余
                    FileLogUpdate newFileLogUpdate = new FileLogUpdate();
                    newFileLogUpdate.setPictureUrl(pictureInfo.getPictureUrl());
                    newFileLogUpdate.setThumbnailUrl(pictureInfo.getThumbnailUrl());
                    newFileLogUpdate.setUserId(pictureInfo.getUserId());
                    newFileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_0.getValue());
                    newFileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                    newFileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                    //插入文件日志
                    SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(newFileLogUpdate);
                    FileLogUpdate oldFileLogUpdate = new FileLogUpdate();
                    oldFileLogUpdate.setPictureUrl(pictureInfoDb.getPictureUrl());
                    oldFileLogUpdate.setThumbnailUrl(pictureInfoDb.getThumbnailUrl());
                    oldFileLogUpdate.setUserId(pictureInfoDb.getUserId());
                    oldFileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_0.getValue());
                    oldFileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                    oldFileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                    SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(oldFileLogUpdate);
                }
            }
        };
    }


    /**
     * 记录空间封面记录
     *
     * @param spaceInfo
     * @return TimerTask
     * @author: YY
     * @method: recordSpaceCoverFileInfoLog
     * @date: 2025/5/11 16:40
     **/
    public static TimerTask recordSpaceCoverFileInfoLog(SpaceInfo spaceInfo) {
        return new TimerTask() {
            @Override
            public void run() {
                FileLogUpdate newFileLogUpdate = new FileLogUpdate();
                newFileLogUpdate.setThumbnailUrl(spaceInfo.getSpaceAvatar());
                newFileLogUpdate.setUserId(spaceInfo.getUserId());
                newFileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_1.getValue());
                newFileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                newFileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                //插入文件日志
                SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(newFileLogUpdate);
            }
        };
    }


    /**
     * 更新空间封面记录
     * 如果新的封面和原来的封面不一样，则更新新的为正常，老的为冗余
     *
     * @param old
     * @param spaceInfo
     * @return TimerTask
     * @author: YY
     * @method: updateSpaceCoverFileInfoLog
     * @date: 2025/5/11 16:40
     **/
    public static TimerTask updateSpaceCoverFileInfoLog(SpaceInfo old, SpaceInfo spaceInfo) {
        return new TimerTask() {
            @Override
            public void run() {
                if (!old.getSpaceAvatar().equals(spaceInfo.getSpaceAvatar())) {
                    FileLogUpdate newFileLogUpdate = new FileLogUpdate();
                    newFileLogUpdate.setThumbnailUrl(spaceInfo.getSpaceAvatar());
                    newFileLogUpdate.setUserId(spaceInfo.getUserId());
                    newFileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_1.getValue());
                    newFileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                    newFileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                    SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(newFileLogUpdate);

                    FileLogUpdate oldFileLogUpdate = new FileLogUpdate();
                    oldFileLogUpdate.setThumbnailUrl(old.getSpaceAvatar());
                    oldFileLogUpdate.setUserId(old.getUserId());
                    oldFileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_1.getValue());
                    oldFileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                    oldFileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                    SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(oldFileLogUpdate);
                }
            }
        };
    }

    /**
     * 更新图片信息为正常
     *
     * @param pictureInfo
     * @return TimerTask
     * @Author: YY
     * @method: updateNormalFileLog
     * @date: 2025/5/11 14:50
     **/
    public static TimerTask updateNormalFileLog(PictureInfo pictureInfo) {
        return new TimerTask() {
            @Override
            public void run() {
                FileLogUpdate fileLogUpdate = new FileLogUpdate();
                fileLogUpdate.setPictureUrl(pictureInfo.getPictureUrl());
                fileLogUpdate.setThumbnailUrl(pictureInfo.getThumbnailUrl());
                fileLogUpdate.setUserId(pictureInfo.getUserId());
                fileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_0.getValue());
                fileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                fileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                //插入文件日志
                SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(fileLogUpdate);
            }
        };
    }

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
