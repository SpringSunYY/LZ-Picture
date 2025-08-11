package com.lz.ai.model.dto.modelParamsInfo;

import com.lz.ai.model.domain.ModelParamsInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI模型参数配置Query对象 ai_model_params_info
 * 模型请求
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class ModelParamsInfoRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模型类型
     */
    private String modelType;

    /**
     * 对象转封装类
     *
     * @param modelParamsInfoQuery 查询对象
     * @return ModelParamsInfo
     */
    public static ModelParamsInfo queryToObj(ModelParamsInfoRequest modelParamsInfoQuery) {
        if (modelParamsInfoQuery == null) {
            return null;
        }
        ModelParamsInfo modelParamsInfo = new ModelParamsInfo();
        BeanUtils.copyProperties(modelParamsInfoQuery, modelParamsInfo);
        return modelParamsInfo;
    }
}
