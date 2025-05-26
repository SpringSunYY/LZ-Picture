package com.lz.quartz.task;

import com.lz.picture.service.IPictureAutoTask;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 图片模块定时任务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-26  00:09
 * @Version: 1.0
 */
@Component("pictureTask")
public class PictureTask {
    @Resource
    private IPictureAutoTask pictureAutoTask;

    /**
     * 自动更新查看记录信息
     */
    public void autoUpdateUserViewLogInfo() {
        pictureAutoTask.autoUpdateUserViewLogInfo();
    }

    /**
     * 自动更新下载记录信息
     */
    public void autoUpdatePictureDownloadLogInfo() {
        pictureAutoTask.autoUpdatePictureDownloadLogInfo();
    }

    /**
     * 自动更新用户行为
     */
    public void autoUpdateUserBehaviorInfo() {
        pictureAutoTask.autoUpdateUserBehaviorInfo();
    }
}
