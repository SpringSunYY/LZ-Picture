package com.lz.points.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * 支付方式对象 po_payment_method_info
 *
 * @author YY
 * @date 2025-03-25
 */
@TableName("po_payment_method_info")
@Data
public class PaymentMethodInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 支付方式编号 */
        @Excel(name = "支付方式编号")
    @TableId(value = "method_id", type = IdType.ASSIGN_ID)
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

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
