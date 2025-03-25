package com.lz.points.model.vo.errorLogInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.ErrorLogInfo;
/**
 * 异常捕获Vo对象 po_error_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class ErrorLogInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 异常编号 */
    @Excel(name = "异常编号")
    private String errorId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 支付方式 */
    @Excel(name = "支付方式")
    private String methodId;

    /** 异常类型 */
    @Excel(name = "异常类型")
    private String errorType;

    /** 异常信息 */
    @Excel(name = "异常信息")
    private String errorMessage;

    /** 异常详细信息 */
    @Excel(name = "异常详细信息")
    private String errorDetails;

    /** 关联订单编号 */
    @Excel(name = "关联订单编号")
    private String relatedOrderId;

    /** 异常记录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "异常记录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
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

    /** 平台（如Web、APP） */
    @Excel(name = "平台", readConverterExp = "如=Web、APP")
    private String platform;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddr;

    /** 解决状态（0未处理 1处理中 2已解决） */
    @Excel(name = "解决状态", readConverterExp = "0=未处理,1=处理中,2=已解决")
    private String resolveStatus;

    /** 解决时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "解决时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date resolveTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


     /**
     * 对象转封装类
     *
     * @param errorLogInfo ErrorLogInfo实体对象
     * @return ErrorLogInfoVo
     */
    public static ErrorLogInfoVo objToVo(ErrorLogInfo errorLogInfo) {
        if (errorLogInfo == null) {
            return null;
        }
        ErrorLogInfoVo errorLogInfoVo = new ErrorLogInfoVo();
        BeanUtils.copyProperties(errorLogInfo, errorLogInfoVo);
        return errorLogInfoVo;
    }
}
