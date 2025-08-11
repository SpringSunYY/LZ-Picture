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
import java.util.List;

/**
 * AI模型参数配置Vo对象 ai_model_params_info
 * 用户获取模型信息
 * <p style="color:'red'">理想是前行的舟，当下是掌托的帆</p>
 *
 * @author YY
 * @date 2025-08-08
 */
@Data
public class UserModelParamsInfoVo implements Serializable {
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
     * 模型介绍
     */
    private String modelDescription;


    /**
     * 积分
     */
    private Long pointsNeed;


    /**
     * 对象转封装类
     *
     * @param modelParamsInfo ModelParamsInfo实体对象
     * @return ModelParamsInfoVo
     */
    public static UserModelParamsInfoVo objToVo(ModelParamsInfo modelParamsInfo) {
        if (modelParamsInfo == null) {
            return null;
        }
        UserModelParamsInfoVo modelParamsInfoVo = new UserModelParamsInfoVo();
        BeanUtils.copyProperties(modelParamsInfo, modelParamsInfoVo);
        return modelParamsInfoVo;
    }

    /**
     * 封装类转对象
     *
     * @param modelParamsInfoList 对象
     * @return ModelParamsInfo
     */
    public static List<UserModelParamsInfoVo> voToObj(List<ModelParamsInfo> modelParamsInfoList) {
        if (modelParamsInfoList == null) {
            return null;
        }
        return modelParamsInfoList.stream().map(UserModelParamsInfoVo::objToVo).toList();
    }
}
