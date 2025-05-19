package com.lz.points.model.dto.errorLogInfo;

import java.io.Serial;
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
 * @date 2025-05-20
 */
@Data
public class ErrorLogInfoInsert implements Serializable {
    @Serial
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
     * 订单类型
     */
    private String orderType;

    /**
     * 支付方式
     */
    private String methodType;

    /**
     * 第三方支付平台
     */
    private String thirdParty;

    /**
     * 第三方支付平台订单号
     */
    private String thirdPartyOrder;

    /**
     * 异常类型
     */
    private String errorType;

    /**
     * 相关订单编号
     */
    private String relatedOrderId;

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
