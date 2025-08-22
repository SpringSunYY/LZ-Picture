package com.lz.quartz.task;

import com.lz.config.service.IFileLogInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 文件自定义定时任务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-11  18:01
 * @Version: 1.0
 */
@Component("fileLogTask")
public class FileLogTask {

    @Resource
    private IFileLogInfoService fileLogInfoService;

    /**
     * 自动删除文件日志
     *
     * @param size 长度
     * @return void
     * @author: YY
     * @method: autoDeleteFile
     * @date: 2025/5/11 18:02
     **/
    public void autoDeleteFile(Integer size, Integer days) {
        System.out.println("自动删除文件 = " + size + "，天数" + days);
        fileLogInfoService.autoDeleteFile(size, days);
    }
}
