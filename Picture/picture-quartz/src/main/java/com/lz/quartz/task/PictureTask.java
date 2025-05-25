package com.lz.quartz.task;

import com.lz.picture.service.IPictureAutoTask;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-26  00:09
 * @Version: 1.0
 */
@Component("pictureTask")
public class PictureTask {
    @Resource
    private IPictureAutoTask  pictureAutoTask;
    public void autoUpdateUserViewLogInfo() {
        pictureAutoTask.autoUpdateUserViewLogInfo();
    }
}
