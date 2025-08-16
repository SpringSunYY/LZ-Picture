package com.lz.config.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 文件日志对象 c_file_log_info
 *
 * @author YY
 * @date 2025-05-11
 */
@TableName("c_file_log_info")
@Data
public class FileLogInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日志编号
     */
    @Excel(name = "日志编号")
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private String logId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 目标对象
     */
    @Excel(name = "目标对象")
    private String targetId;

    /**
     * 目标内容
     */
    @Excel(name = "目标内容")
    private String targetContent;

    /**
     * 文件路径
     */
    @Excel(name = "文件路径")
    private String fileUrl;

    /**
     * 文件类型
     */
    @Excel(name = "文件类型")
    private String fileType;

    /**
     * 状态
     */
    @Excel(name = "状态", readConverterExp = "0=冗余,1=正常,2=删除")
    private String logStatus;

    /**
     * 日志类型（0图片 1空间封面 2头像）
     */
    @Excel(name = "日志类型", readConverterExp = "0=图片,1=空间封面,2=头像")
    private String logType;

    /**
     * 是否压缩（0是 1否）
     */
    @Excel(name = "是否压缩", readConverterExp = "0=是,1=否")
    private String isCompress;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "删除时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    /**
     * IP地址
     */
    @Excel(name = "IP地址")
    private String ipAddr;

    /**
     * 设备唯一标识
     */
    @Excel(name = "设备唯一标识")
    private String deviceId;

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
     * 平台
     */
    @Excel(name = "平台")
    private String platform;

    /**
     * IP属地
     */
    @Excel(name = "IP属地")
    private String ipAddress;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
