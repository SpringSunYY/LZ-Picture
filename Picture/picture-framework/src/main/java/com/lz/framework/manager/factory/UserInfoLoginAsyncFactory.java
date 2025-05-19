package com.lz.framework.manager.factory;

import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.core.domain.model.AuthUserInfo;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.userauth.model.domain.AuthLoginLogInfo;
import com.lz.userauth.service.IAuthLoginLogInfoService;
import com.lz.userauth.service.IAuthUserInfoService;

import java.util.TimerTask;

/**
 * Project: Picture
 * Package: com.lz.framework.manager.factory
 * Author: YY
 * CreateTime: 2025-04-12  00:02
 * Description: UserInfoLoginAsyncFactory
 * Version: 1.0
 */
public class UserInfoLoginAsyncFactory {
    /**
     * description: 记录登录日志
     * author: YY
     * method: userInfoLogin
     * date: 2025/4/12 00:12
     * param:
     * param: username
     * param: userId
     * param: type
     * param: status
     * param: msg
     * return: void
     *
     * @return
     */
    public static TimerTask userInfoLogin(String username, String userId, String type, String status, String msg) {
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();
        return new TimerTask() {
            @Override
            public void run() {
                AuthLoginLogInfo authLoginLogInfo = new AuthLoginLogInfo();
                BeanUtils.copyProperties(deviceInfo, authLoginLogInfo);
                authLoginLogInfo.setIpaddr(deviceInfo.getIpAddr());
                authLoginLogInfo.setUserId(userId);
                authLoginLogInfo.setUserName(username);
                authLoginLogInfo.setLoginType(type);
                authLoginLogInfo.setStatus(status);
                authLoginLogInfo.setMsg(msg);
                authLoginLogInfo.setLoginLocation(deviceInfo.getIpAddress());
                // 插入数据
                SpringUtils.getBean(IAuthLoginLogInfoService.class).insertLoginLogInfo(authLoginLogInfo);
                //更新用户信息
                AuthUserInfo authUserInfo = new AuthUserInfo();
                authUserInfo.setUserId(userId);
                authUserInfo.setLastLoginTime(authLoginLogInfo.getLoginTime());
                authUserInfo.setLastLoginIp(authLoginLogInfo.getLoginLocation());
                authUserInfo.setIpAddress(deviceInfo.getIpAddress());
                SpringUtils.getBean(IAuthUserInfoService.class).updateById(authUserInfo);
            }
        };
    }
}
