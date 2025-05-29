package com.lz.user.model.dto.loginLogInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.core.page.PageDomain;
import com.lz.user.model.domain.LoginLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户登录日志Query对象 u_login_log_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserLoginLogInfoQuery extends PageDomain implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private String userId;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param loginLogInfoQuery 查询对象
     * @return LoginLogInfo
     */
    public static LoginLogInfo queryToObj(UserLoginLogInfoQuery loginLogInfoQuery) {
        if (loginLogInfoQuery == null) {
            return null;
        }
        LoginLogInfo loginLogInfo = new LoginLogInfo();
        BeanUtils.copyProperties(loginLogInfoQuery, loginLogInfo);
        return loginLogInfo;
    }
}
