package com.lz.user.model.dto.loginLogInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.LoginLogInfo;
/**
 * 用户登录日志Query对象 u_login_log_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class LoginLogInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private String infoId;

    /** 用户ID */
    private String userId;

    /** 用户名 */
    private String userName;

    /** 登录方式 */
    private String loginType;

    /** 登录IP地址 */
    private String ipaddr;

    /** 登录地点 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 登录平台 */
    private String platform;

    /** 状态（0成功 1失败） */
    private String status;

    /** 错误码 */
    private String errorCode;

    /** 提示消息 */
    private String msg;

    /** 登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loginTime;

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
    public static LoginLogInfo queryToObj(LoginLogInfoQuery loginLogInfoQuery) {
        if (loginLogInfoQuery == null) {
            return null;
        }
        LoginLogInfo loginLogInfo = new LoginLogInfo();
        BeanUtils.copyProperties(loginLogInfoQuery, loginLogInfo);
        return loginLogInfo;
    }
}
