package com.lz.ai.model.vo.modelParamsInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.common.annotation.Excel;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * AI模型参数配置Vo对象 ai_model_params_info
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class ModelParamsInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型编号
     */
    @Excel(name = "模型编号")
    private String modelId;

    /**
     * 模型KEY
     */
    @Excel(name = "模型KEY")
    private String modelKey;

    /**
     * 模型名称
     */
    @Excel(name = "模型名称")
    private String modelName;

    /**
     * 模型类型
     */
    @Excel(name = "模型类型")
    private String modelType;

    /**
     * 模型
     */
    @Excel(name = "模型")
    private String model;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String modelLabel;

    /**
     * API地址
     */
    @Excel(name = "API地址")
    private String apiUrl;

    /**
     * 安全密钥
     */
    @Excel(name = "安全密钥")
    private String apiKey;

    /**
     * 安全KEY
     */
    @Excel(name = "安全KEY")
    private String secretKey;

    /**
     * 价格
     */
    @Excel(name = "价格")
    private BigDecimal priceUse;

    /**
     * 模型参数
     */
    @Excel(name = "模型参数")
    private String modelParams;

    /**
     * 模型介绍
     */
    @Excel(name = "模型介绍")
    private String modelDescription;

    /**
     * 使用次数
     */
    @Excel(name = "使用次数")
    private Long usageCount;

    /**
     * 赚取积分
     */
    @Excel(name = "赚取积分")
    private Long pointsEarned;

    /**
     * 积分
     */
    @Excel(name = "积分")
    private Long pointsNeed;

    /**
     * 扩展配置
     */
    @Excel(name = "扩展配置")
    private String extendConfig;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String paramsStatus;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long orderNum;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;


    /**
     * 对象转封装类
     *
     * @param modelParamsInfo ModelParamsInfo实体对象
     * @return ModelParamsInfoVo
     */
    public static ModelParamsInfoVo objToVo(ModelParamsInfo modelParamsInfo) {
        if (modelParamsInfo == null) {
            return null;
        }
        ModelParamsInfoVo modelParamsInfoVo = new ModelParamsInfoVo();
        BeanUtils.copyProperties(modelParamsInfo, modelParamsInfoVo);
        return modelParamsInfoVo;
    }
}
