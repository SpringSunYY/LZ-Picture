package com.lz.picture.model.vo.userinfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 用户信息返回
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-08-18  17:29
 * @Version: 1.0
 */
@Data
public class UserInfoVo {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 性别（0=未知 1=男 2=女）
     */
    private String sex;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 职业
     */
    private String occupation;


    /**
     * 个人简介
     */
    private String introductory;

    /**
     * IP属地
     */
    private String ipAddress;

    /**
     * 图片总数
     */
    private Long pictureCount;

    /**
     * 喜欢总数
     */
    private Long likeCount;

    /**
     * 收藏总数
     */
    private Long collectCount;

    /**
     * 分享
     */
    private Long shareCount;

    /**
     * 对象转封装类
     *
     * @param userInfo UserInfo实体对象
     * @return UserInfoVo
     */
    public static UserInfoVo objToVo(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return userInfoVo;
    }
}
