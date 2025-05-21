package com.lz.user.model.dto.userInfo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户更改密码实体
 * Version: 1.0
 */
@Data
public class UserPasswordUploadRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotEmpty(message = "用户编号不能为空")
    private String userId;
    /**
     * 旧密码
     */
    @NotEmpty(message = "旧密码不能为空")
    @Size(min = 8, max = 20, message = "旧密码长度在8-20个字符之间")
    private String oldPassword;
    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度在8-20个字符之间")
    private String password;

    /**
     * 确认密码
     */
    @NotNull(message = "确认密码不能为空")
    @Size(min = 8, max = 20, message = "确认密码长度在8-20个字符之间")
    private String confirmPassword;
}
