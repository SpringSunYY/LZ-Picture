package com.lz.common.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 设备信息
 *
 * @author YY
 * @date 2025-03-17
 */
@Data
public class DeviceInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 登录IP地址
     */
    private String ipaddr;

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
     * 设备唯一标识
     */
    private String deviceId;
}
