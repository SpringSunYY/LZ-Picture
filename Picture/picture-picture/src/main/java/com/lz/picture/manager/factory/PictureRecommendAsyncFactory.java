package com.lz.picture.manager.factory;

import com.lz.common.utils.spring.SpringUtils;
import com.lz.picture.service.IPictureRecommendInfoService;

import java.util.TimerTask;

/**
 * 图片推荐异步工厂
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-13  22:06
 * @Version: 1.0
 */
public class PictureRecommendAsyncFactory {
    public static TimerTask insertUserInterestModel(String userId,String type)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                SpringUtils.getBean(IPictureRecommendInfoService.class).insertUserInterestModel(userId, type);
            }
        };
    }
}
