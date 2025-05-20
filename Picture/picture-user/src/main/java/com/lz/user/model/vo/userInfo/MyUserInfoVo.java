package com.lz.user.model.vo.userInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.vo.loginLogInfo.MyLoginLogInfoVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户详情
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class MyUserInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatarUrl;


    /**
     * 赚取总积分
     */
    private Long pointsEarned;

    /**
     * 使用总积分
     */
    private Long pointsUsed;

    /**
     * 充值总金额（元）
     */
    private BigDecimal rechargeAmount;


    /**
     * 积分余额
     */
    private Long pointsBalance;

    /**
     * 状态（0=正常 1=异常 2=禁用）
     */
    private String status;


    /**
     * 性别（0=未知 1=男 2=女）
     */
    private String sex;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * IP属地
     */
    private String ipAddress;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 登录日志
     */
    private List<MyLoginLogInfoVo> loginLogInfoVos;

    /**
     * 对象转封装类
     *
     * @param userInfo UserInfo实体对象
     * @return UserInfoVo
     */
    public static MyUserInfoVo objToVo(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        MyUserInfoVo userInfoVo = new MyUserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);
        return userInfoVo;
    }
}
