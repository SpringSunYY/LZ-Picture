package com.lz.user.model.dto.userInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.user.model.domain.UserInfo;
/**
 * 用户信息Vo对象 u_user_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private String userId;

    /** 用户名 */
    private String userName;

    /** 手机号 */
    private String phone;

    /** 国家代码 */
    private String countryCode;

    /** 昵称 */
    private String nickName;

    /** 头像地址 */
    private String avatarUrl;

    /** 密码 */
    private String password;

    /** 状态（0=正常 1=异常 2=禁用） */
    private String status;

    /** 加密盐 */
    private String salt;

    /** 性别（0=未知 1=男 2=女） */
    private String sex;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /** 职业 */
    private String occupation;

    /** 偏好语言 */
    private String preferredLanguageLocale;

    /** 个人简介 */
    private String introductory;

    /** IP属地 */
    private String ipAddress;

    /** 删除标记（0=未删除 1=已删除） */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param userInfoEdit 编辑对象
     * @return UserInfo
     */
    public static UserInfo editToObj(UserInfoEdit userInfoEdit) {
        if (userInfoEdit == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoEdit, userInfo);
        return userInfo;
    }
}
