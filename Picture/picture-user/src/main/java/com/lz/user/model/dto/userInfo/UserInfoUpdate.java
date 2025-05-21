package com.lz.user.model.dto.userInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotEmpty(message = "用户编号不能为空")
    private String userId;

    /**
     * 昵称
     */
    @NotEmpty(message = "昵称不能为空")
    @Size(min = 2, max = 20, message = "昵称长度在2-20个字符之间")
    private String nickName;

    /**
     * 性别（0=未知 1=男 2=女）
     */
    @NotEmpty(message = "性别不能为空")
    @Size(min = 1, max = 1, message = "性别长度错误")
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
