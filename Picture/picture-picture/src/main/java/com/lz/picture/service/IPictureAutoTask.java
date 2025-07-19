package com.lz.picture.service;

/**
 * 自动更新任务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-26  00:10
 * @Version: 1.0
 */
public interface IPictureAutoTask {
    /**
     * 自动更新用户浏览信息
     */
    void autoUpdateUserViewLogInfo();

    /**
     * 自动更新图片下载信息
     *
     * @return void
     * @author: YY
     * @method: autoUpdatePictureDownloadLogInfo
     * @date: 2025/5/26 14:06
     **/
    void autoUpdatePictureDownloadLogInfo();

    /**
     * 自动更新行为收藏信息
     *
     * @return void
     * @author: YY
     * @method: autoUpdateUserBehaviorCollectInfo
     * @date: 2025/5/26 14:57
     **/
    void autoUpdateUserBehaviorInfo();

    /**
     * 图片热门统计根据天
     * @author: YY
     * @method: autoStatisticsPictureByDay
     * @date: 2025/7/19 02:04
     * @param
     * @return void
     **/
    void autoStatisticsPictureByDay();
}
