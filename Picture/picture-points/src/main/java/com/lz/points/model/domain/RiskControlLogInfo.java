package com.lz.points.model.domain;

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
 * 风控日志对象 po_risk_control_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@TableName("po_risk_control_log_info")
@Data
public class RiskControlLogInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 风控日志编号 */
        @Excel(name = "风控日志编号")
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private String logId;

    /** 用户编号 */
        @Excel(name = "用户编号")
    private String userId;

    /** 支付方式 */
        @Excel(name = "支付方式")
    private String methodId;

    /** 风险类型 */
        @Excel(name = "风险类型")
    private String riskType;

    /** 风险等级（0低 1中 2高） */
        @Excel(name = "风险等级", readConverterExp = "0=低,1=中,2=高")
    private String riskLevel;

    /** 风险描述 */
        @Excel(name = "风险描述")
    private String riskDescription;

    /** 采取措施 */
        @Excel(name = "采取措施")
    private String actionTaken;

    /** 措施时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "措施时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date actionTime;

    /** 记录时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "记录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 设备唯一标识 */
        @Excel(name = "设备唯一标识")
    private String deviceId;

    /** 浏览器类型 */
        @Excel(name = "浏览器类型")
    private String browser;

    /** 操作系统 */
        @Excel(name = "操作系统")
    private String os;

    /** 平台（Web/APP） */
        @Excel(name = "平台", readConverterExp = "W=eb/APP")
    private String platform;

    /** IP地址 */
        @Excel(name = "IP地址")
    private String ipAddr;

    /** 备注 */
        @Excel(name = "备注")
    private String remark;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
