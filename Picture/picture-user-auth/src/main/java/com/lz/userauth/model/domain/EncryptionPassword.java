package com.lz.userauth.model.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 校验密码返回
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-21  23:40
 * @Version: 1.0
 */
@Data
public class EncryptionPassword implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密方式
     */
    private String salt;
}
