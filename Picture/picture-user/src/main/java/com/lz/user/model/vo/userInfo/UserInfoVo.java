package com.lz.user.model.vo.userInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.UserInfo;
/**
 * 用户信息Vo对象 u_user_info
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class UserInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 国家代码 */
    @Excel(name = "国家代码")
    private String countryCode;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickName;

    /** 头像地址 */
    @Excel(name = "头像地址")
    private String avatarUrl;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 状态（0=正常 1=异常 2=禁用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=异常,2=禁用")
    private String status;

    /** 加密盐 */
    @Excel(name = "加密盐")
    private String salt;

    /** 性别（0=未知 1=男 2=女） */
    @Excel(name = "性别", readConverterExp = "0=未知,1=男,2=女")
    private String sex;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "生日", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /** 职业 */
    @Excel(name = "职业")
    private String occupation;

    /** 偏好语言 */
    @Excel(name = "偏好语言")
    private String preferredLanguageLocale;

    /** 个人简介 */
    @Excel(name = "个人简介")
    private String introductory;

    /** IP属地 */
    @Excel(name = "IP属地")
    private String ipAddress;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /** 最后登录IP */
    @Excel(name = "最后登录IP")
    private String lastLoginIp;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除标记（0=未删除 1=已删除） */
    @Excel(name = "删除标记", readConverterExp = "0=未删除,1=已删除")
    private String isDelete;


     /**
     * 对象转封装类
     *
     * @param userInfo UserInfo实体对象
     * @return UserInfoVo
     */
    public static UserInfoVo objToVo(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return userInfoVo;
    }
}
