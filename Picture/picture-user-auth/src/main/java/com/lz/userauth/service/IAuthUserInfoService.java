package com.lz.userauth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.userauth.model.domain.AuthUserInfo;
import com.lz.userauth.model.domain.ForgetPasswordBody;
import com.lz.userauth.model.domain.RegisterLoginBody;

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

    /**
     * description:  根据手机号查询用户信息
     * author: YY
     * method: selectUserInfoByPhone
     * date: 2025/3/19 10:00
     * param:
     * param: phone 手机号码
     * param: countryCode 国家码
     * return: com.lz.userauth.model.domain.AuthUserInfo
     **/
    AuthUserInfo selectUserInfoByPhone(String phone, String countryCode);

    AuthUserInfo register(RegisterLoginBody registerLoginBody);

    /**
     * description: 忘记密码修改密码
     * author: YY
     * method: forgetPassword
     * date: 2025/3/20 09:12
     * param:
     * param: forgetPasswordBody
     * return: com.lz.userauth.model.domain.AuthUserInfo
     **/
    AuthUserInfo forgetPassword(ForgetPasswordBody forgetPasswordBody);
}
