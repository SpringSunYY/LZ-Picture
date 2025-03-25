package com.lz.ai.model.dto.modelParamsInfo;

import java.io.Serializable;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.ai.model.domain.ModelParamsInfo;

/**
 * AI模型参数配置Vo对象 ai_model_params_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class ModelParamsInfoEdit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 模型编号
     */
    private String modelId;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型类型
     */
    private String modelType;

    /**
     * 安全密钥
     */
    private String apiKey;

    /**
     * 安全KEY
     */
    private String secretKey;

    /**
     * 模型参数
     */
    private String modelParams;

    /**
     * 模型介绍
     */
    private String modelDescription;

    /**
     * 平均使用tokens/次
     */
    private Long tokensAvg;

    /**
     * 累计消耗Tokens
     */
    private Long tokensTotal;

    /**
     * 使用次数
     */
    private Long usageCount;

    /**
     * 积分消耗比例
     */
    private Long pointsNeed;

    /**
     * 扩展配置
     */
    private String extendConfig;

    /**
     * 状态（0开启 1关闭）
     */
    private String paramsStatus;

    /**
     * 管理员编号
     */
    private Long userId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param modelParamsInfoEdit 编辑对象
     * @return ModelParamsInfo
     */
    public static ModelParamsInfo editToObj(ModelParamsInfoEdit modelParamsInfoEdit) {
        if (modelParamsInfoEdit == null) {
            return null;
        }
        ModelParamsInfo modelParamsInfo = new ModelParamsInfo();
        BeanUtils.copyProperties(modelParamsInfoEdit, modelParamsInfo);
        return modelParamsInfo;
    }
}
