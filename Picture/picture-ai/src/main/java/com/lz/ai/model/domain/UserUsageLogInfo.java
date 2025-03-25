package com.lz.ai.model.domain;

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
 * 用户AI使用记录对象 ai_user_usage_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@TableName("ai_user_usage_log_info")
@Data
public class UserUsageLogInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
        @Excel(name = "记录编号")
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private String logId;

    /** 用户编号 */
        @Excel(name = "用户编号")
    private String userId;

    /** 模型编号 */
        @Excel(name = "模型编号")
    private String modelId;

    /** 输入参数 */
        @Excel(name = "输入参数")
    private String inputParams;

    /** 返回结果 */
        @Excel(name = "返回结果")
    private String outputResult;

    /** 请求时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "请求时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date requestTime;

    /** 请求时长（毫秒） */
        @Excel(name = "请求时长", readConverterExp = "毫=秒")
    private Long requestDuration;

    /** 消耗Tokens数量 */
        @Excel(name = "消耗Tokens数量")
    private Long tokensUsed;

    /** 消耗积分 */
        @Excel(name = "消耗积分")
    private Long pointsUsed;

    /** 使用类型（0AI扩图 1AI编辑 2AI搜索） */
        @Excel(name = "使用类型", readConverterExp = "0=AI扩图,1=AI编辑,2=AI搜索")
    private String usageType;

    /** 目标编号 */
        @Excel(name = "目标编号")
    private String targetId;

    /** 状态（0成功 1失败 2超时） */
        @Excel(name = "状态", readConverterExp = "0=成功,1=失败,2=超时")
    private String logStatus;

    /** 模型返回码 */
        @Excel(name = "模型返回码")
    private String aiStatusCode;

    /** 失败原因 */
        @Excel(name = "失败原因")
    private String failReason;

    /** 用户IP地址 */
        @Excel(name = "用户IP地址")
    private String ipAddr;

    /** 设备唯一标识 */
        @Excel(name = "设备唯一标识")
    private String deviceId;

    /** 浏览器类型 */
        @Excel(name = "浏览器类型")
    private String browser;

    /** 操作系统 */
        @Excel(name = "操作系统")
    private String os;

    /** 平台 */
        @Excel(name = "平台")
    private String platform;

    /** 创建时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除（0正常 1删除） */
        @Excel(name = "删除", readConverterExp = "0=正常,1=删除")
    private String isDelete;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
