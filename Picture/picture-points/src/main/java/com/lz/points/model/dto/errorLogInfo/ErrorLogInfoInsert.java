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
 * @date 2025-05-19
 */
@Data
public class ErrorLogInfoInsert implements Serializable {
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
     * 额外信息
     */
    private String paymentExtend;

    /**
     * 相关订单编号
     */
    private String relatedOrderId;

    /**
     * 解决时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resolveTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param errorLogInfoInsert 插入对象
     * @return ErrorLogInfoInsert
     */
    public static ErrorLogInfo insertToObj(ErrorLogInfoInsert errorLogInfoInsert) {
        if (errorLogInfoInsert == null) {
            return null;
        }
        ErrorLogInfo errorLogInfo = new ErrorLogInfo();
        BeanUtils.copyProperties(errorLogInfoInsert, errorLogInfo);
        return errorLogInfo;
    }
}
