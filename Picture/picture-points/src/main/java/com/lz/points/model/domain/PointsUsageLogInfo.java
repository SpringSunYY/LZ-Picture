package com.lz.points.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 积分使用记录对象 po_points_usage_log_info
 *
 * @author YY
 * @date 2025-05-23
 */
@TableName("po_points_usage_log_info")
@Data
public class PointsUsageLogInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @Excel(name = "记录编号")
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private String logId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 给予用户编号
     */
    @Excel(name = "给予用户编号")
    private String giveUserId;

    /**
     * 日志类型（0充值 1消费 2提成 3提现）
     */
    @Excel(name = "日志类型", readConverterExp = "0=充值,1=消费,2=提成,3=提现")
    private String logType;

    /**
     * 使用类型（0下载图片 1AI服务）
     */
    @Excel(name = "使用类型", readConverterExp = "0=下载图片,1=AI服务")
    private String usageType;

    /**
     * 目标编号
     */
    @Excel(name = "目标编号")
    private String targetId;

    /**
     * 使用前积分
     */
    @Excel(name = "使用前积分")
    private Long pointsBefore;

    /**
     * 消费积分
     */
    @Excel(name = "消费积分")
    private Long pointsUsed;

    /**
     * 使用后积分
     */
    @Excel(name = "使用后积分")
    private Long pointsAfter;

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
     * IP地址
     */
    @Excel(name = "IP地址")
    private String ipAddr;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 删除（0否 1是）
     */
    @Excel(name = "删除", readConverterExp = "0=否,1=是")
    private String isDelete;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
