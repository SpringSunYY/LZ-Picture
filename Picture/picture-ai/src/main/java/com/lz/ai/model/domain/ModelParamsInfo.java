package com.lz.ai.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * AI模型参数配置对象 ai_model_params_info
 *
 * @author YY
 * @date 2025-08-08
 */
@TableName("ai_model_params_info")
@Data
public class ModelParamsInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 模型编号 */
        @Excel(name = "模型编号")
    @TableId(value = "model_id", type = IdType.ASSIGN_ID)
    private String modelId;

    /** 模型KEY */
        @Excel(name = "模型KEY")
    private String modelKey;

    /** 模型名称 */
        @Excel(name = "模型名称")
    private String modelName;

    /** 模型类型 */
        @Excel(name = "模型类型")
    private String modelType;

    /** 模型 */
        @Excel(name = "模型")
    private String model;

    /** 名称 */
        @Excel(name = "名称")
    private String modelLabel;

    /** 安全密钥 */
        @Excel(name = "安全密钥")
    private String apiKey;

    /** 安全KEY */
        @Excel(name = "安全KEY")
    private String secretKey;

    /** 价格 */
        @Excel(name = "价格")
    private BigDecimal priceUse;

    /** 模型参数 */
        @Excel(name = "模型参数")
    private String modelParams;

    /** 模型介绍 */
        @Excel(name = "模型介绍")
    private String modelDescription;

    /** 使用次数 */
        @Excel(name = "使用次数")
    private Long usageCount;

    /** 积分 */
        @Excel(name = "积分")
    private Long ponintsNeed;

    /** 扩展配置 */
        @Excel(name = "扩展配置")
    private String extendConfig;

    /** 状态 */
        @Excel(name = "状态")
    private String paramsStatus;

    /** 排序 */
        @Excel(name = "排序")
    private Long orderNum;

    /** 创建人 */
        @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
        @Excel(name = "更新人")
    private String updateBy;

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
