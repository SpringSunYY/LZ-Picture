package com.lz.user.model.vo.loginLogInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.LoginLogInfo;

/**
 * 用户登录日志Vo对象 u_login_log_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class LoginLogInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @Excel(name = "编号")
    private String infoId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private String userId;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String userName;

    /**
     * 登录方式
     */
    @Excel(name = "登录方式")
    private String loginType;

    /**
     * 匿名标识
     */
    @Excel(name = "匿名标识")
    private String identifier;

    /**
     * 登录IP地址
     */
    @Excel(name = "登录IP地址")
    private String ipaddr;

    /**
     * 登录地点
     */
    @Excel(name = "登录地点")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @Excel(name = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @Excel(name = "操作系统")
    private String os;

    /**
     * 登录平台
     */
    @Excel(name = "登录平台")
    private String platform;

    /**
     * 设备唯一标识
     */
    @Excel(name = "设备唯一标识")
    private String deviceId;

    /**
     * 状态（0成功 1失败）
     */
    @Excel(name = "状态", readConverterExp = "0=成功,1=失败")
    private String status;

    /**
     * 错误码
     */
    @Excel(name = "错误码")
    private String errorCode;

    /**
     * 提示消息
     */
    @Excel(name = "提示消息")
    private String msg;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;


    /**
     * 对象转封装类
     *
     * @param loginLogInfo LoginLogInfo实体对象
     * @return LoginLogInfoVo
     */
    public static LoginLogInfoVo objToVo(LoginLogInfo loginLogInfo) {
        if (loginLogInfo == null) {
            return null;
        }
        LoginLogInfoVo loginLogInfoVo = new LoginLogInfoVo();
        BeanUtils.copyProperties(loginLogInfo, loginLogInfoVo);
        return loginLogInfoVo;
    }
}
