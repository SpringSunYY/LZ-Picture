package com.lz.user.model.dto.userInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息修改参数
 * 更新头像
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserInfoUpdateAvatar implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotEmpty(message = "用户编号不能为空")
    private String userId;

    /**
     * 头像
     */
    @NotEmpty(message = "头像不能为空")
    private String avatarUrl;

    /**
     * 对象转封装类
     *
     * @param userInfoEdit 编辑对象
     * @return UserInfo
     */
    public static UserInfo editToObj(UserInfoUpdateAvatar userInfoEdit) {
        if (userInfoEdit == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoEdit, userInfo);
        return userInfo;
    }
}
