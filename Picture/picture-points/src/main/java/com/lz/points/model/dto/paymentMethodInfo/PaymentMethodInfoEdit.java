package com.lz.points.model.dto.paymentMethodInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PaymentMethodInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付方式Vo对象 po_payment_method_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PaymentMethodInfoEdit implements Serializable
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
     * @param paymentMethodInfoEdit 编辑对象
     * @return PaymentMethodInfo
     */
    public static PaymentMethodInfo editToObj(PaymentMethodInfoEdit paymentMethodInfoEdit) {
        if (paymentMethodInfoEdit == null) {
            return null;
        }
        PaymentMethodInfo paymentMethodInfo = new PaymentMethodInfo();
        BeanUtils.copyProperties(paymentMethodInfoEdit, paymentMethodInfo);
        return paymentMethodInfo;
    }
}
