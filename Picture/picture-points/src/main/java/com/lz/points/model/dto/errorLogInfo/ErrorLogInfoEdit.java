package com.lz.points.model.dto.errorLogInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.ErrorLogInfo;
/**
 * 异常捕获Vo对象 po_error_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class ErrorLogInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 异常编号 */
    private String errorId;

    /** 用户编号 */
    private String userId;

    /** 支付方式 */
    private String methodId;

    /** 异常类型 */
    private String errorType;

    /** 异常信息 */
    private String errorMessage;

    /** 异常详细信息 */
    private String errorDetails;

    /** 关联订单编号 */
    private String relatedOrderId;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台（如Web、APP） */
    private String platform;

    /** IP地址 */
    private String ipAddr;

    /** 解决状态（0未处理 1处理中 2已解决） */
    private String resolveStatus;

    /** 解决时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resolveTime;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param errorLogInfoEdit 编辑对象
     * @return ErrorLogInfo
     */
    public static ErrorLogInfo editToObj(ErrorLogInfoEdit errorLogInfoEdit) {
        if (errorLogInfoEdit == null) {
            return null;
        }
        ErrorLogInfo errorLogInfo = new ErrorLogInfo();
        BeanUtils.copyProperties(errorLogInfoEdit, errorLogInfo);
        return errorLogInfo;
    }
}
