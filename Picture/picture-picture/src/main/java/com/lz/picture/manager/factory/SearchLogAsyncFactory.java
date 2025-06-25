package com.lz.picture.manager.factory;

import com.lz.common.utils.spring.SpringUtils;
import com.lz.picture.model.domain.SearchLogInfo;
import com.lz.picture.service.ISearchLogInfoService;

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
     * 新增用户搜索记录
     *
     * @param searchLogInfo 搜索记录
     * @return TimerTask
     * @author: YY
     * @method: recordSearchLog
     * @date: 2025/6/3 21:32
     **/
    public static TimerTask recordSearchLog(SearchLogInfo searchLogInfo) {
        return new TimerTask() {
            @Override
            public void run() {
                //解析返回结果
                SpringUtils.getBean(ISearchLogInfoService.class).insertSearchLogInfo(searchLogInfo);
            }
        };
    }
}
