package com.lz.user.manager.factory;

import com.lz.common.utils.spring.SpringUtils;
import com.lz.config.model.dto.fileLogInfo.FileLogUpdate;
import com.lz.config.model.enmus.CFileLogStatusEnum;
import com.lz.config.model.enmus.CFileLogTypeEnum;
import com.lz.config.service.IFileLogInfoService;
import com.lz.user.model.domain.UserInfo;

import java.util.TimerTask;

/**
 * 用户模块文件日志异步工厂
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-31  15:59
 * @Version: 1.0
 */
public class UserFileLogAsyncFactory {
    /**
     * 异步更新文件日志，判断是否和存入数据库日志一样，如果不一样则更新，如果一样则不更新，表示没有新上传图片
     * 因为新增时图片已经为正常
     * 不一样表示新上传图片，要把上传的图片更新为正常，老图片为冗余
     *
     * @param old 老的用户信息
     * @param userInfo 新的用户信息
     * @return TimerTask
     * @author: YY
     * @method: updateUserInfoAvatarFileLog
     * @date: 2025/5/31 16:03
     **/
    public static TimerTask updateUserInfoAvatarFileLog(UserInfo old, UserInfo userInfo) {
        return new TimerTask() {
            @Override
            public void run() {
                //如果头像地址不一致
                if (!old.getAvatarUrl().equals(userInfo.getAvatarUrl())) {
                    FileLogUpdate newFileLogUpdate = new FileLogUpdate();
                    newFileLogUpdate.setUserId(userInfo.getUserId());
                    newFileLogUpdate.setTargetId(userInfo.getUserId());
                    newFileLogUpdate.setTargetContent(userInfo.getUserName());
                    //为什么只要压缩图片？？？因为头像和封面只有压缩图
                    newFileLogUpdate.setThumbnailUrl(userInfo.getAvatarUrl());
                    newFileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_2.getValue());
                    //新的新增时为冗余，要更新为正常
                    newFileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                    newFileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                    SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(newFileLogUpdate);

                    FileLogUpdate oldFileLogUpdate = new FileLogUpdate();
                    oldFileLogUpdate.setTargetId(old.getUserId());
                    oldFileLogUpdate.setTargetContent(old.getUserName());
                    oldFileLogUpdate.setThumbnailUrl(old.getAvatarUrl());
                    oldFileLogUpdate.setUserId(old.getUserId());
                    oldFileLogUpdate.setQueryLogType(CFileLogTypeEnum.LOG_TYPE_2.getValue());
                    oldFileLogUpdate.setQueryLogStatus(CFileLogStatusEnum.LOG_STATUS_1.getValue());
                    oldFileLogUpdate.setUpdateLogStatus(CFileLogStatusEnum.LOG_STATUS_0.getValue());
                    SpringUtils.getBean(IFileLogInfoService.class).updateFileLog(oldFileLogUpdate);
                }
            }
        };
    }
}
