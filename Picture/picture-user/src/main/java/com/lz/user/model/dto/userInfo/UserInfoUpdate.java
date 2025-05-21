package com.lz.user.model.dto.userInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息修改参数
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserInfoUpdate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickName;

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
     * 对象转封装类
     *
     * @param userInfoEdit 编辑对象
     * @return UserInfo
     */
    public static UserInfo editToObj(UserInfoUpdate userInfoEdit) {
        if (userInfoEdit == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoEdit, userInfo);
        return userInfo;
    }
}
