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
 * @date 2025-05-19
 */
@Data
public class ErrorLogInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 异常编号
     */
    @Excel(name = "异常编号")
    private String errorId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 支付方式
     */
    @Excel(name = "支付方式")
    private String methodType;

    /**
     * 第三方支付平台
     */
    @Excel(name = "第三方支付平台")
    private String thirdParty;

    /**
     * 异常类型
     */
    @Excel(name = "异常类型")
    private String errorType;

    /**
     * 返回Code
     */
    @Excel(name = "返回Code")
    private String errorCode;

    /**
     * 返回Msg
     */
    @Excel(name = "返回Msg")
    private String errorMsg;

    /**
     * 额外信息
     */
    @Excel(name = "额外信息")
    private String paymentExtend;

    /**
     * 相关订单编号
     */
    @Excel(name = "相关订单编号")
    private String relatedOrderId;

    /**
     * 异常记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "异常记录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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
     * IP属地
     */
    @Excel(name = "IP属地")
    private String ipAddress;

    /**
     * 解决状态
     */
    @Excel(name = "解决状态")
    private String resolveStatus;

    /**
     * 解决时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "解决时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date resolveTime;

    /**
     * 备注
     */
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
