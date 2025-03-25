package com.lz.points.model.dto.riskControlLogInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.RiskControlLogInfo;
/**
 * 风控日志Query对象 po_risk_control_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class RiskControlLogInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 风控日志编号 */
    private String logId;

    /** 用户编号 */
    private String userId;

    /** 支付方式 */
    private String methodId;

    /** 风险类型 */
    private String riskType;

    /** 风险等级（0低 1中 2高） */
    private String riskLevel;

    /** 措施时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date actionTime;

    /** 记录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台（Web/APP） */
    private String platform;

    /** IP地址 */
    private String ipAddr;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param riskControlLogInfoQuery 查询对象
     * @return RiskControlLogInfo
     */
    public static RiskControlLogInfo queryToObj(RiskControlLogInfoQuery riskControlLogInfoQuery) {
        if (riskControlLogInfoQuery == null) {
            return null;
        }
        RiskControlLogInfo riskControlLogInfo = new RiskControlLogInfo();
        BeanUtils.copyProperties(riskControlLogInfoQuery, riskControlLogInfo);
        return riskControlLogInfo;
    }
}
