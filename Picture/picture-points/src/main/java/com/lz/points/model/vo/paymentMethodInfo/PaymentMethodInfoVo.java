package com.lz.points.model.vo.paymentMethodInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.points.model.domain.PaymentMethodInfo;
/**
 * 支付方式Vo对象 po_payment_method_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class PaymentMethodInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 支付方式编号 */
    @Excel(name = "支付方式编号")
    private String methodId;

    /** 名称 */
    @Excel(name = "名称")
    private String methodName;

    /** 第三方支付平台 */
    @Excel(name = "第三方支付平台")
    private String thirdParty;

    /** 类型 */
    @Excel(name = "类型")
    private String methodType;

    /** 支付接口URL */
    @Excel(name = "支付接口URL")
    private String apiUrl;

    /** 商户号 */
    @Excel(name = "商户号")
    private String merchantId;

    /** 应用编号 */
    @Excel(name = "应用编号")
    private String appId;

    /** 秘钥 */
    @Excel(name = "秘钥")
    private String secretKey;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String contactInformation;

    /** 扩展配置 */
    @Excel(name = "扩展配置")
    private String extendConfig;

    /** 状态（0使用 1未使用） */
    @Excel(name = "状态", readConverterExp = "0=使用,1=未使用")
    private String methodStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


     /**
     * 对象转封装类
     *
     * @param paymentMethodInfo PaymentMethodInfo实体对象
     * @return PaymentMethodInfoVo
     */
    public static PaymentMethodInfoVo objToVo(PaymentMethodInfo paymentMethodInfo) {
        if (paymentMethodInfo == null) {
            return null;
        }
        PaymentMethodInfoVo paymentMethodInfoVo = new PaymentMethodInfoVo();
        BeanUtils.copyProperties(paymentMethodInfo, paymentMethodInfoVo);
        return paymentMethodInfoVo;
    }
}
