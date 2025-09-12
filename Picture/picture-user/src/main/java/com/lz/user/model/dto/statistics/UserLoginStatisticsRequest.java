package com.lz.user.model.dto.statistics;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录统计信息请求参数
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-09-10  15:48
 * @Version: 1.0
 */
@Data
public class UserLoginStatisticsRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "开始时间不能为空")
    private String startDate;
    @NotBlank(message = "结束时间不能为空")
    private String endDate;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录方式
     */
    private String loginType;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录平台
     */
    private String platform;

    /**
     * 状态（0成功 1失败）
     */
    private String status;
}
