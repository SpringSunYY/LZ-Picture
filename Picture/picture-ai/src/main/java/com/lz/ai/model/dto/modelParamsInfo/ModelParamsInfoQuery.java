package com.lz.ai.model.dto.modelParamsInfo;

import java.util.Map;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.ai.model.domain.ModelParamsInfo;
/**
 * AI模型参数配置Query对象 ai_model_params_info
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class ModelParamsInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 模型编号 */
    private String modelId;

    /** 模型KEY */
    private String modelKey;

    /** 模型名称 */
    private String modelName;

    /** 模型类型 */
    private String modelType;

    /** 模型 */
    private String model;

    /** 名称 */
    private String modelLabel;

    /** 状态 */
    private String paramsStatus;

    /** 排序 */
    private Long orderNum;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新人 */
    private String updateBy;

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
     * @param modelParamsInfoQuery 查询对象
     * @return ModelParamsInfo
     */
    public static ModelParamsInfo queryToObj(ModelParamsInfoQuery modelParamsInfoQuery) {
        if (modelParamsInfoQuery == null) {
            return null;
        }
        ModelParamsInfo modelParamsInfo = new ModelParamsInfo();
        BeanUtils.copyProperties(modelParamsInfoQuery, modelParamsInfo);
        return modelParamsInfo;
    }
}
