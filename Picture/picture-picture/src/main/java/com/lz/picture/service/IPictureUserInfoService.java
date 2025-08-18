package com.lz.picture.service;

import com.lz.picture.model.vo.userinfo.UserInfoVo;

/**
 * 图片用户查询用户信息
 */
public interface IPictureUserInfoService {
    /**
     * 查询到用户信息
     *
     * @param username
     * @return
     */
    UserInfoVo getPictureUserInfo(String username);
}
