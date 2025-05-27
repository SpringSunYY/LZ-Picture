package com.lz.user.manager.factory;

import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.user.service.IInformInfoService;

import java.util.Date;
import java.util.Map;
import java.util.TimerTask;

/**
 * 消息发送异步工厂
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-28  01:29
 * @Version: 1.0
 */
public class InformInfoAsyncFactory {
    public static TimerTask sendInform(String userId, String templateKey, String local, String templateType, String informType, Map<String, String> params) {
        return new TimerTask() {
            @Override
            public void run() {
                //发送消息
                SpringUtils.getBean(IInformInfoService.class).sendInform(userId,templateKey,  local, templateType, informType, params);
            }
        };
    }
}
