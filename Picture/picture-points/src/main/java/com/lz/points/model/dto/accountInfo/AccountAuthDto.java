package com.lz.points.model.dto.accountInfo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 账户密码校验
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-24  15:15
 * @Version: 1.0
 */
@Data
public class AccountAuthDto implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;
    /** 用户编号 */
    private String userId;

    /** 支付密码 */
    private String password;
}
