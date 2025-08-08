package com.lz.ai.model.dto.modelParamsInfo;

import com.lz.ai.model.domain.ModelParamsInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * AI模型参数配置Vo对象 ai_model_params_info
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class ModelParamsInfoInsert implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型编号
     */
    private String modelId;

    /**
     * 模型KEY
     */
    private String modelKey;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型类型
     */
    private String modelType;

    /**
     * 模型
     */
    private String model;

    /**
     * 名称
     */
    private String modelLabel;

    /**
     * API地址
     */
    private String apiUrl;

    /**
     * 安全密钥
     */
    private String apiKey;

    /**
     * 安全KEY
     */
    private String secretKey;

    /**
     * 价格
     */
    private BigDecimal priceUse;

    /**
     * 模型参数
     */
    private String modelParams;

    /**
     * 模型介绍
     */
    private String modelDescription;

    /**
     * 积分
     */
    private Long pointsNeed;

    /**
     * 扩展配置
     */
    private String extendConfig;

    /**
     * 状态
     */
    private String paramsStatus;

    /**
     * 排序
     */
    private Long orderNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param modelParamsInfoInsert 插入对象
     * @return ModelParamsInfoInsert
     */
    public static ModelParamsInfo insertToObj(ModelParamsInfoInsert modelParamsInfoInsert) {
        if (modelParamsInfoInsert == null) {
            return null;
        }
        ModelParamsInfo modelParamsInfo = new ModelParamsInfo();
        BeanUtils.copyProperties(modelParamsInfoInsert, modelParamsInfo);
        return modelParamsInfo;
    }
}
