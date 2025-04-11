package com.lz.user.model.vo.userInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.user.model.domain.UserInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息Vo对象 u_user_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /**
     * 职业
     */
    private String occupation;

    /**
     * 偏好语言
     */
    private String preferredLanguageLocale;

    /**
     * 个人简介
     */
    private String introductory;

    /**
     * IP属地
     */
    private String ipAddress;


    /**
     * 对象转封装类
     *
     * @param userInfo UserInfo实体对象
     * @return UserInfoVo
     */
    public static UserVo objToVo(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserVo userInfoVo = new UserVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return userInfoVo;
    }
}
