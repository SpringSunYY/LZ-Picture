package com.lz.points.model.dto.errorLogInfo;

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
import com.lz.points.model.domain.ErrorLogInfo;

/**
 * 异常捕获Query对象 po_error_log_info
 *
 * @author YY
 * @date 2025-05-19
 */
@Data
public class ErrorLogInfoQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 异常编号
     */
    private String errorId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 支付方式
     */
    private String methodType;

    /**
     * 第三方支付平台
     */
    private String thirdParty;

    /**
     * 异常类型
     */
    private String errorType;

    /**
     * 返回Code
     */
    private String errorCode;

    /**
     * 返回Msg
     */
    private String errorMsg;

    /**
     * 相关订单编号
     */
    private String relatedOrderId;

    /**
     * 异常记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 设备唯一标识
     */
    private String deviceId;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 平台
     */
    private String platform;

    /**
     * IP地址
     */
    private String ipAddr;

    /**
     * IP属地
     */
    private String ipAddress;

    /**
     * 解决状态
     */
    private String resolveStatus;

    /**
     * 解决时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resolveTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param errorLogInfoQuery 查询对象
     * @return ErrorLogInfo
     */
    public static ErrorLogInfo queryToObj(ErrorLogInfoQuery errorLogInfoQuery) {
        if (errorLogInfoQuery == null) {
            return null;
        }
        ErrorLogInfo errorLogInfo = new ErrorLogInfo();
        BeanUtils.copyProperties(errorLogInfoQuery, errorLogInfo);
        return errorLogInfo;
    }
}
