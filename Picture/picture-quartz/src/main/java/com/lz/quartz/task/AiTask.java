package com.lz.quartz.task;

import com.lz.ai.service.IAiAutoTask;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * AI模块任务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-21  16:54
 * @Version: 1.0
 */
@Component("aiTask")
public class AiTask {

    @Resource
    private IAiAutoTask aiAutoTask;

    /**
     * 自动执行更新任务
     */
    public void autoExecuteUpdateTask() {
        aiAutoTask.autoExecuteUpdateTask();
    }
}
