package com.lz.picture.manager.factory;

import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.manager.file.model.PictureFileResponse;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.config.model.dto.fileLogInfo.FileLogUpdate;
import com.lz.config.model.enmus.CFileLogStatusEnum;
import com.lz.config.model.enmus.CFileLogTypeEnum;
import com.lz.config.service.IFileLogInfoService;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.SpaceInfo;

import java.util.TimerTask;

/**
 * 图片文件日志异步任务异步工厂（产生任务用）
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-11  16:45
 * @Version: 1.0
 */
public class SearchLogAsyncFactory {
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
    public static TimerTask recordSearchLog(PictureFileResponse pictureFileResponse, String userId, String ossType, String logType) {
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        return new TimerTask() {
            @Override
            public void run() {
                //插入文件日志
                SpringUtils.getBean(IFileLogInfoService.class).recordFileLog(pictureFileResponse, userId, ossType, logType, deviceInfo);
            }
        };
    }

}
