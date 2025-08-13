package com.lz.ai.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.ai.mapper.ModelParamsInfoMapper;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.model.dto.modelParamsInfo.ModelParamsInfoQuery;
import com.lz.ai.model.dto.modelParamsInfo.ModelParamsInfoRequest;
import com.lz.ai.model.enums.AiModelParamsStatusEnum;
import com.lz.ai.model.vo.modelParamsInfo.ModelParamsInfoVo;
import com.lz.ai.model.vo.modelParamsInfo.UserModelParamsInfoVo;
import com.lz.ai.service.IModelParamsInfoService;
import com.lz.common.annotation.CustomCacheEvict;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.uuid.IdUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.redis.AiRedisConstants.*;

/**
 * AI模型参数配置Service业务层处理
 *
 * @author YY
 * @date 2025-08-08
 */
@Service
public class ModelParamsInfoServiceImpl extends ServiceImpl<ModelParamsInfoMapper, ModelParamsInfo> implements IModelParamsInfoService {
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
    public ModelParamsInfo selectModelParamsInfoByModelId(String modelId) {
        return modelParamsInfoMapper.selectModelParamsInfoByModelId(modelId);
    }

    /**
     * 查询AI模型参数配置列表
     *
     * @param modelParamsInfo AI模型参数配置
     * @return AI模型参数配置
     */
    @CustomSort(sortFields = {"priceUse", "pointsEarned", "pointsNeed", "orderNum", "createTime", "updateTime"},
            sortMappingFields = {"price_use", "points_earned", "points_need", "order_num", "create_time", "update_time"})
    @Override
    public List<ModelParamsInfo> selectModelParamsInfoList(ModelParamsInfo modelParamsInfo) {
        List<ModelParamsInfo> modelParamsInfos = modelParamsInfoMapper.selectModelParamsInfoList(modelParamsInfo);
        for (ModelParamsInfo info : modelParamsInfos) {
            info.setSecretKey(null);
            info.setApiKey(null);
        }
        return modelParamsInfos;
    }

    /**
     * 新增AI模型参数配置
     *
     * @param modelParamsInfo AI模型参数配置
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = {AI_MODEL_DETAIL, AI_MODEL_LIST}, keyFields = {"modelParamsInfo.modelKey"})
    @Override
    public int insertModelParamsInfo(ModelParamsInfo modelParamsInfo) {
        checkStr(modelParamsInfo);
        //查询模型KEY是否存在
        ModelParamsInfo modelParamsInfoByModelKey = modelParamsInfoMapper.selectModelParamsInfoByModelKey(modelParamsInfo.getModelKey());
        ThrowUtils.throwIf(StringUtils.isNotNull(modelParamsInfoByModelKey), "模型KEY已存在");
        modelParamsInfo.setModelId(IdUtils.snowflakeId().toString());
        modelParamsInfo.setCreateTime(DateUtils.getNowDate());
        modelParamsInfo.setPointsEarned(0L);
        modelParamsInfo.setUsageCount(0L);
        return modelParamsInfoMapper.insertModelParamsInfo(modelParamsInfo);
    }

    /**
     * 校验参数字符串是否正确
     *
     * @param modelParamsInfo
     */
    private void checkStr(ModelParamsInfo modelParamsInfo) {
        if (StringUtils.isNotEmpty(modelParamsInfo.getModelParams())) {
            try {
                JSONObject.parseObject(modelParamsInfo.getModelParams());
            } catch (Exception e) {
                throw new ServiceException("模型参数(modelParams)格式不正确: " + e.getMessage());
            }
        }
        if (StringUtils.isNotEmpty(modelParamsInfo.getExtendConfig())) {
            try {
                JSONObject.parseObject(modelParamsInfo.getExtendConfig());
            } catch (Exception e) {
                throw new ServiceException("扩展配置(extendConfig)格式不正确: " + e.getMessage());
            }
        }
    }

    /**
     * 修改AI模型参数配置
     *
     * @param modelParamsInfo AI模型参数配置
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = {AI_MODEL_DETAIL, AI_MODEL_LIST}, keyFields = {"modelParamsInfo.modelKey"})
    @Override
    public int updateModelParamsInfo(ModelParamsInfo modelParamsInfo) {
        checkStr(modelParamsInfo);
        //查询模型KEY是否存在
        ModelParamsInfo modelParamsInfoByModelKey = modelParamsInfoMapper.selectModelParamsInfoByModelKey(modelParamsInfo.getModelKey());
        ThrowUtils.throwIf(StringUtils.isNotNull(modelParamsInfoByModelKey)
                        && !modelParamsInfoByModelKey.getModelId().equals(modelParamsInfo.getModelId()),
                "模型KEY已存在");
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
    public int deleteModelParamsInfoByModelIds(String[] modelIds) {
        return modelParamsInfoMapper.deleteModelParamsInfoByModelIds(modelIds);
    }

    /**
     * 删除AI模型参数配置信息
     *
     * @param modelId AI模型参数配置主键
     * @return 结果
     */
    @Override
    public int deleteModelParamsInfoByModelId(String modelId) {
        return modelParamsInfoMapper.deleteModelParamsInfoByModelId(modelId);
    }

    //endregion
    @Override
    public QueryWrapper<ModelParamsInfo> getQueryWrapper(ModelParamsInfoQuery modelParamsInfoQuery) {
        QueryWrapper<ModelParamsInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = modelParamsInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String modelId = modelParamsInfoQuery.getModelId();
        queryWrapper.eq(StringUtils.isNotEmpty(modelId), "model_id", modelId);

        String modelKey = modelParamsInfoQuery.getModelKey();
        queryWrapper.like(StringUtils.isNotEmpty(modelKey), "model_key", modelKey);

        String modelName = modelParamsInfoQuery.getModelName();
        queryWrapper.like(StringUtils.isNotEmpty(modelName), "model_name", modelName);

        String modelType = modelParamsInfoQuery.getModelType();
        queryWrapper.eq(StringUtils.isNotEmpty(modelType), "model_type", modelType);

        String model = modelParamsInfoQuery.getModel();
        queryWrapper.like(StringUtils.isNotEmpty(model), "model", model);

        String modelLabel = modelParamsInfoQuery.getModelLabel();
        queryWrapper.like(StringUtils.isNotEmpty(modelLabel), "model_label", modelLabel);

        String paramsStatus = modelParamsInfoQuery.getParamsStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(paramsStatus), "params_status", paramsStatus);

        String createBy = modelParamsInfoQuery.getCreateBy();
        queryWrapper.like(StringUtils.isNotEmpty(createBy), "create_by", createBy);

        Date createTime = modelParamsInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String updateBy = modelParamsInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy), "update_by", updateBy);

        Date updateTime = modelParamsInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<ModelParamsInfoVo> convertVoList(List<ModelParamsInfo> modelParamsInfoList) {
        if (StringUtils.isEmpty(modelParamsInfoList)) {
            return Collections.emptyList();
        }
        return modelParamsInfoList.stream().map(ModelParamsInfoVo::objToVo).collect(Collectors.toList());
    }

    @CustomCacheable(keyPrefix = AI_MODEL_DETAIL,
            expireTime = AI_MODEL_DETAIL_EXPIRE_TIME,
            keyField = "modelKey")
    @Override
    public ModelParamsInfo selectModelParamsInfoByModelKey(String modelKey) {
        return modelParamsInfoMapper.selectModelParamsInfoByModelKey(modelKey);
    }

    @CustomCacheable(keyPrefix = AI_MODEL_LIST,
            expireTime = AI_MODEL_LIST_EXPIRE_TIME,
            keyField = "request.modelType",
            useQueryParamsAsKey = true
    )
    @Override
    public List<UserModelParamsInfoVo> userSelectModelParamsInfoList(ModelParamsInfoRequest request) {
        List<ModelParamsInfo> paramsInfos = this.list(new LambdaQueryWrapper<ModelParamsInfo>()
                .eq(StringUtils.isNotEmpty(request.getModelType()), ModelParamsInfo::getModelType, request.getModelType())
                .eq(ModelParamsInfo::getParamsStatus, AiModelParamsStatusEnum.MODEL_PARAMS_STATUS_0.getValue())
                .orderByAsc(ModelParamsInfo::getOrderNum)
        );
        return UserModelParamsInfoVo.voToObj(paramsInfos);
    }

}
