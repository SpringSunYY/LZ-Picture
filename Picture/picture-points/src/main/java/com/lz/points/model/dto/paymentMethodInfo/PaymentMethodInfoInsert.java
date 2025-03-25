package com.lz.points.model.dto.paymentMethodInfo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.points.model.domain.PaymentMethodInfo;
/**
 * 支付方式Vo对象 po_payment_method_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PaymentMethodInfoInsert implements Serializable
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

    /** 应用编号 */
    private String appId;

    /** 秘钥 */
    private String secretKey;

    /** 联系方式 */
    private String contactInformation;

    /** 扩展配置 */
    private String extendConfig;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param paymentMethodInfoInsert 插入对象
     * @return PaymentMethodInfoInsert
     */
    public static PaymentMethodInfo insertToObj(PaymentMethodInfoInsert paymentMethodInfoInsert) {
        if (paymentMethodInfoInsert == null) {
            return null;
        }
        PaymentMethodInfo paymentMethodInfo = new PaymentMethodInfo();
        BeanUtils.copyProperties(paymentMethodInfoInsert, paymentMethodInfo);
        return paymentMethodInfo;
    }
}
