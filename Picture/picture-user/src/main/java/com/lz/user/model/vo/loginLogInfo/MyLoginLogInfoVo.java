package com.lz.user.model.vo.loginLogInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.user.model.domain.LoginLogInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户登录日志Vo对象 u_login_log_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class MyLoginLogInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @Excel(name = "编号")
    private String infoId;

    /**
     * 登录方式
     */
    @Excel(name = "登录方式")
    private String loginType;

    /**
     * 登录地点
     */
    @Excel(name = "登录地点")
    private String loginLocation;


    /**
     * 登录IP地址
     */
    private String ipaddr;
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
    public static MyLoginLogInfoVo objToVo(LoginLogInfo loginLogInfo) {
        if (loginLogInfo == null) {
            return null;
        }
        MyLoginLogInfoVo loginLogInfoVo = new MyLoginLogInfoVo();
        BeanUtils.copyProperties(loginLogInfo, loginLogInfoVo);
        return loginLogInfoVo;
    }

    /**
     * 对象转封装类
     *
     * @param loginLogInfoList LoginLogInfo列表
     * @return List
     */
    public static List<MyLoginLogInfoVo> objToVo(List<LoginLogInfo> loginLogInfoList) {
        if (loginLogInfoList == null) {
            return null;
        }
        return loginLogInfoList.stream().map(MyLoginLogInfoVo::objToVo).toList();
    }

}
