package com.lz.ai.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import java.math.BigDecimal;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.ai.mapper.ModelParamsInfoMapper;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.service.IModelParamsInfoService;
import com.lz.ai.model.dto.modelParamsInfo.ModelParamsInfoQuery;
import com.lz.ai.model.vo.modelParamsInfo.ModelParamsInfoVo;

/**
 * AI模型参数配置Service业务层处理
 *
 * @author YY
 * @date 2025-08-08
 */
@Service
public class ModelParamsInfoServiceImpl extends ServiceImpl<ModelParamsInfoMapper, ModelParamsInfo> implements IModelParamsInfoService
{
    @Resource
    private ModelParamsInfoMapper modelParamsInfoMapper;

    //region mybatis代码
    /**
     * 查询AI模型参数配置
     *
     * @param modelId AI模型参数配置主键
     * @return AI模型参数配置
     */
    @Override
    public ModelParamsInfo selectModelParamsInfoByModelId(String modelId)
    {
        return modelParamsInfoMapper.selectModelParamsInfoByModelId(modelId);
    }

    /**
     * 查询AI模型参数配置列表
     *
     * @param modelParamsInfo AI模型参数配置
     * @return AI模型参数配置
     */
    @Override
    public List<ModelParamsInfo> selectModelParamsInfoList(ModelParamsInfo modelParamsInfo)
    {
        return modelParamsInfoMapper.selectModelParamsInfoList(modelParamsInfo);
    }

    /**
     * 新增AI模型参数配置
     *
     * @param modelParamsInfo AI模型参数配置
     * @return 结果
     */
    @Override
    public int insertModelParamsInfo(ModelParamsInfo modelParamsInfo)
    {
        modelParamsInfo.setCreateTime(DateUtils.getNowDate());
        return modelParamsInfoMapper.insertModelParamsInfo(modelParamsInfo);
    }

    /**
     * 修改AI模型参数配置
     *
     * @param modelParamsInfo AI模型参数配置
     * @return 结果
     */
    @Override
    public int updateModelParamsInfo(ModelParamsInfo modelParamsInfo)
    {
      modelParamsInfo.setUpdateTime(DateUtils.getNowDate());
        return modelParamsInfoMapper.updateModelParamsInfo(modelParamsInfo);
    }

    /**
     * 批量删除AI模型参数配置
     *
     * @param modelIds 需要删除的AI模型参数配置主键
     * @return 结果
     */
    @Override
    public int deleteModelParamsInfoByModelIds(String[] modelIds)
    {
        return modelParamsInfoMapper.deleteModelParamsInfoByModelIds(modelIds);
    }

    /**
     * 删除AI模型参数配置信息
     *
     * @param modelId AI模型参数配置主键
     * @return 结果
     */
    @Override
    public int deleteModelParamsInfoByModelId(String modelId)
    {
        return modelParamsInfoMapper.deleteModelParamsInfoByModelId(modelId);
    }
    //endregion
    @Override
    public QueryWrapper<ModelParamsInfo> getQueryWrapper(ModelParamsInfoQuery modelParamsInfoQuery){
        QueryWrapper<ModelParamsInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = modelParamsInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String modelId = modelParamsInfoQuery.getModelId();
        queryWrapper.eq(StringUtils.isNotEmpty(modelId) ,"model_id",modelId);

    String modelKey = modelParamsInfoQuery.getModelKey();
        queryWrapper.like(StringUtils.isNotEmpty(modelKey) ,"model_key",modelKey);

    String modelName = modelParamsInfoQuery.getModelName();
        queryWrapper.like(StringUtils.isNotEmpty(modelName) ,"model_name",modelName);

    String modelType = modelParamsInfoQuery.getModelType();
        queryWrapper.eq(StringUtils.isNotEmpty(modelType) ,"model_type",modelType);

    String model = modelParamsInfoQuery.getModel();
        queryWrapper.like(StringUtils.isNotEmpty(model) ,"model",model);

    String modelLabel = modelParamsInfoQuery.getModelLabel();
        queryWrapper.like(StringUtils.isNotEmpty(modelLabel) ,"model_label",modelLabel);

    String paramsStatus = modelParamsInfoQuery.getParamsStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(paramsStatus) ,"params_status",paramsStatus);

    Long orderNum = modelParamsInfoQuery.getOrderNum();
        queryWrapper.eq( StringUtils.isNotNull(orderNum),"order_num",orderNum);

    String createBy = modelParamsInfoQuery.getCreateBy();
        queryWrapper.like(StringUtils.isNotEmpty(createBy) ,"create_by",createBy);

    Date createTime = modelParamsInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    String updateBy = modelParamsInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy) ,"update_by",updateBy);

    Date updateTime = modelParamsInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<ModelParamsInfoVo> convertVoList(List<ModelParamsInfo> modelParamsInfoList) {
        if (StringUtils.isEmpty(modelParamsInfoList)) {
            return Collections.emptyList();
        }
        return modelParamsInfoList.stream().map(ModelParamsInfoVo::objToVo).collect(Collectors.toList());
    }

}
