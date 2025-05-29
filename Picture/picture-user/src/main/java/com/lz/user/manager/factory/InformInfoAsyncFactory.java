package com.lz.user.manager.factory;

import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IInformInfoService;
import com.lz.user.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class InformInfoAsyncFactory {
    public static TimerTask sendInform(String userId, String templateKey, String local, String templateType, String informType, Map<String, String> params) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    int i;
                    //如果传过来的local为空，则查询用户的偏好语言
                    if (StringUtils.isEmpty(local)) {
                        //                    System.out.println("用户没有设置语言，使用默认语言");
                        UserInfo userInfo = SpringUtils.getBean(IUserInfoService.class).selectUserInfoByUserId(userId);
                        String effectiveLocal = userInfo.getPreferredLanguageLocale();
                        //                    System.out.println("用户偏好语言：" + effectiveLocal);
                        i = SpringUtils.getBean(IInformInfoService.class).sendInform(userId, templateKey, effectiveLocal, templateType, informType, params);
                        //                    System.out.println("发送消息结果：" + i);
                    } else {
                        //发送消息
                        i = SpringUtils.getBean(IInformInfoService.class).sendInform(userId, templateKey, local, templateType, informType, params);
                        //                    System.out.println("发送消息结果：" + i);
                    }
                    if (i == 0) {
                        log.error("时间：{}，key:{}，语言：{}，用户：{}.", DateUtils.getNowDate(), templateKey, local, userId);
                    }
                } catch (Exception e) {
                    log.error("时间：{}，发送消息异常：{}", DateUtils.getNowDate(), e.getMessage());
                }
            }
        };
    }
}
