package com.lz.ai.service;

/**
 * ai自动任务 Service接口
 */
public interface IAiAutoTask {
    /**
     * 自动更新任务
     */
    void autoExecuteUpdateTask();

    /**
     * 自动统计生成记录使用量
     */
    void autoStatisticsGenerateRecordUsage();

}
