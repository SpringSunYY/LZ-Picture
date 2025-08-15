package com.lz.picture.manager.factory;

import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.manager.file.model.FileResponse;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.config.model.dto.fileLogInfo.FileLogUpdate;
import com.lz.config.model.enmus.CFileLogStatusEnum;
import com.lz.config.model.enmus.CFileLogTypeEnum;
import com.lz.config.service.IFileLogInfoService;
import com.lz.picture.model.domain.PictureApplyInfo;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.SpaceInfo;

import java.util.TimerTask;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;

/**
 * 图片文件日志异步任务异步工厂（产生任务用）
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-11  16:45
 * @Version: 1.0
 */
public class PictureFileLogAsyncFactory {
    /**
     * 记录文件日志
     *
     * @param fileResponse 返回图片信息
     * @param userId       用户编号
     * @param logType      日志类型
     * @return TimerTask
     * @author: YY
     * @date: 2025/5/10 22:51
     **/
    public static TimerTask recordFileLog(FileResponse fileResponse, String userId, String logType) {
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        return new TimerTask() {
            @Override
            public void run() {
                //插入文件日志
                SpringUtils.getBean(IFileLogInfoService.class).recordFileLog(fileResponse, userId, logType, deviceInfo);
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
                    newFileLogUpdate.setTargetId(pictureInfo.getPictureId());
                    newFileLogUpdate.setTargetContent(pictureInfo.getName());
                    newFileLogUpdate.setPictureUrl(pictureInfo.getPictureUrl());
                    newFileLogUpdate.setThumbnailUrl(pictureInfo.getThumbnailUrl());
                    newFileLogUpdate.setUserId(pictureInfo.getUserId());
                    newFileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_0.getValue());
                    newFileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                    newFileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                    //插入文件日志
                    SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(newFileLogUpdate);

                    //更新老的为冗余
                    FileLogUpdate oldFileLogUpdate = new FileLogUpdate();
                    oldFileLogUpdate.setTargetId(pictureInfoDb.getPictureId());
                    oldFileLogUpdate.setTargetContent(pictureInfoDb.getName());
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
                newFileLogUpdate.setTargetId(spaceInfo.getSpaceId());
                newFileLogUpdate.setTargetContent(spaceInfo.getSpaceName());
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
                    newFileLogUpdate.setTargetId(spaceInfo.getSpaceId());
                    newFileLogUpdate.setTargetContent(spaceInfo.getSpaceName());
                    newFileLogUpdate.setThumbnailUrl(spaceInfo.getSpaceAvatar());
                    newFileLogUpdate.setUserId(spaceInfo.getUserId());
                    newFileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_1.getValue());
                    newFileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                    newFileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                    SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(newFileLogUpdate);

                    FileLogUpdate oldFileLogUpdate = new FileLogUpdate();
                    oldFileLogUpdate.setTargetId(old.getSpaceId());
                    oldFileLogUpdate.setTargetContent(old.getSpaceName());
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
                fileLogUpdate.setTargetId(pictureInfo.getPictureId());
                fileLogUpdate.setTargetContent(pictureInfo.getName());
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
     *
     */
    public static TimerTask updateNormalPictureApplyFileLog(PictureApplyInfo pictureApplyInfo) {
        return new TimerTask() {
            @Override
            public void run() {
                FileLogUpdate fileLogUpdate = new FileLogUpdate();
                fileLogUpdate.setTargetId(pictureApplyInfo.getApplyId());
                fileLogUpdate.setTargetContent(pictureApplyInfo.getPictureId());
                fileLogUpdate.setUserId(pictureApplyInfo.getUserId());
                fileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_3.getValue());
                fileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                fileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                if (StringUtils.isNotEmpty(pictureApplyInfo.getApplyFile())) {
                    String[] split = pictureApplyInfo.getApplyFile().split(COMMON_SEPARATOR);
                    for (String string : split) {
                        fileLogUpdate.setPictureUrl(string);
                        SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(fileLogUpdate);
                    }
                }
                fileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_4.getValue());
                if (StringUtils.isNotEmpty(pictureApplyInfo.getApplyImage())) {
                    String[] split = pictureApplyInfo.getApplyImage().split(COMMON_SEPARATOR);
                    for (String string : split) {
                        fileLogUpdate.setThumbnailUrl(string);
                        SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(fileLogUpdate);
                    }
                }
            }
        };
    }

}
