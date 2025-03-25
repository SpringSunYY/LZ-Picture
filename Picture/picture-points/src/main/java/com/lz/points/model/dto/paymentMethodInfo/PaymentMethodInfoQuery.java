package com.lz.points.model.dto.paymentMethodInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PaymentMethodInfo;
/**
 * 支付方式Query对象 po_payment_method_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PaymentMethodInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 支付方式编号 */
    private String methodId;

    /** 名称 */
    private String methodName;

    /** 第三方支付平台 */
    private String thirdParty;

    /** 类型 */
    private String methodType;

    /** 支付接口URL */
    private String apiUrl;

    /** 商户号 */
    private String merchantId;

    /** 状态（0使用 1未使用） */
    private String methodStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param paymentMethodInfoQuery 查询对象
     * @return PaymentMethodInfo
     */
    public static PaymentMethodInfo queryToObj(PaymentMethodInfoQuery paymentMethodInfoQuery) {
        if (paymentMethodInfoQuery == null) {
            return null;
        }
        PaymentMethodInfo paymentMethodInfo = new PaymentMethodInfo();
        BeanUtils.copyProperties(paymentMethodInfoQuery, paymentMethodInfo);
        return paymentMethodInfo;
    }
}
