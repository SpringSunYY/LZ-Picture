package com.lz.userauth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.userauth.model.domain.AuthUserInfo;

import java.util.Set;

/**
 * 用户信息Service接口
 *
 * @author YY
 * @date 2025-03-17
 */
public interface IAuthUserInfoService extends IService<AuthUserInfo> {


    /**
     * description: 根据用户名查询用户信息
     * author: YY
     * method: selectUserInfoByUserName
     * date: 2025/3/17 13:58
     * param:
     * param: username
     * return: com.lz.user.model.domain.AuthUserInfo
     **/
    AuthUserInfo selectUserInfoByUserName(String username);

    Set<String> getUserPermission(AuthUserInfo user);
}
