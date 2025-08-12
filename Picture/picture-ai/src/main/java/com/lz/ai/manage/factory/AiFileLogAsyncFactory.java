package com.lz.ai.manage.factory;

import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.manager.file.model.FileResponse;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.config.model.enmus.CFileLogOssTypeEnum;
import com.lz.config.model.enmus.CFileLogTypeEnum;
import com.lz.config.service.IFileLogInfoService;

import java.util.TimerTask;

/**
 * 文件插入入职
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-12  17:23
 * @Version: 1.0
 */
public class AiFileLogAsyncFactory {
    /**
     * 记录文件日志,直接插入生成的正常的
     *
     * @param fileResponse  文件信息
     * @param userId        用户编号
     * @param deviceInfo    设备信息
     * @param targetId      目标编号
     * @param targetContent 目标内容
     * @return TimerTask
     * @author: YY
     * @date: 2025/5/10 22:51
     **/
    public static TimerTask recordFileLog(FileResponse fileResponse, String userId, String targetId, String targetContent, DeviceInfo deviceInfo) {
        if (StringUtils.isNull(deviceInfo)) {
            deviceInfo = IpUtils.getDeviceInfo();
        }
        DeviceInfo finalDeviceInfo = deviceInfo;
        return new TimerTask() {
            @Override
            public void run() {
                //插入文件日志
                SpringUtils.getBean(IFileLogInfoService.class).recordNormalFileLog(
                        fileResponse, userId, targetId, targetContent,
                        CFileLogOssTypeEnum.OSS_TYPE_0.getValue(), CFileLogTypeEnum.LOG_TYPE_5.getValue(),
                        finalDeviceInfo);
            }
        };
    }
}
