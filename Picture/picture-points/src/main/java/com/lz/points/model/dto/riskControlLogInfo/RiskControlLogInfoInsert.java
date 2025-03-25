package com.lz.points.model.dto.riskControlLogInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.RiskControlLogInfo;
/**
 * 风控日志Vo对象 po_risk_control_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class RiskControlLogInfoInsert implements Serializable
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

    /** 风险描述 */
    private String riskDescription;

    /** 采取措施 */
    private String actionTaken;

    /** 措施时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date actionTime;

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

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param riskControlLogInfoInsert 插入对象
     * @return RiskControlLogInfoInsert
     */
    public static RiskControlLogInfo insertToObj(RiskControlLogInfoInsert riskControlLogInfoInsert) {
        if (riskControlLogInfoInsert == null) {
            return null;
        }
        RiskControlLogInfo riskControlLogInfo = new RiskControlLogInfo();
        BeanUtils.copyProperties(riskControlLogInfoInsert, riskControlLogInfo);
        return riskControlLogInfo;
    }
}
