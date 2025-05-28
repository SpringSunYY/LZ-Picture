package com.lz.user.manager.factory;

import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IInformInfoService;
import com.lz.user.service.IUserInfoService;

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
                //如果传过来的local为空，则查询用户的偏好语言
                if (StringUtils.isEmpty(local)) {
                    System.out.println("用户没有设置语言，使用默认语言");
                    UserInfo userInfo = SpringUtils.getBean(IUserInfoService.class).selectUserInfoByUserId(userId);
                    String effectiveLocal = userInfo.getPreferredLanguageLocale();
                    System.out.println("用户偏好语言：" + effectiveLocal);
                    int i = SpringUtils.getBean(IInformInfoService.class).sendInform(userId, templateKey, effectiveLocal, templateType, informType, params);
                    System.out.println("发送消息结果：" + i);
                } else {
                    //发送消息
                    int i = SpringUtils.getBean(IInformInfoService.class).sendInform(userId, templateKey, local, templateType, informType, params);
                    System.out.println("发送消息结果：" + i);
                }
            }
        };
    }
}
