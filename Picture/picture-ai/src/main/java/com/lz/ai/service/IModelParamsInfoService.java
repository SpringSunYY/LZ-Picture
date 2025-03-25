package com.lz.ai.service;

import java.util.List;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.model.vo.modelParamsInfo.ModelParamsInfoVo;
import com.lz.ai.model.dto.modelParamsInfo.ModelParamsInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * AI模型参数配置Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IModelParamsInfoService extends IService<ModelParamsInfo>
{
    //region mybatis代码
    /**
     * 查询AI模型参数配置
     *
     * @param modelId AI模型参数配置主键
     * @return AI模型参数配置
     */
    public ModelParamsInfo selectModelParamsInfoByModelId(String modelId);

    /**
     * 查询AI模型参数配置列表
     *
     * @param modelParamsInfo AI模型参数配置
     * @return AI模型参数配置集合
     */
    public List<ModelParamsInfo> selectModelParamsInfoList(ModelParamsInfo modelParamsInfo);

    /**
     * 新增AI模型参数配置
     *
     * @param modelParamsInfo AI模型参数配置
     * @return 结果
     */
    public int insertModelParamsInfo(ModelParamsInfo modelParamsInfo);

    /**
     * 修改AI模型参数配置
     *
     * @param modelParamsInfo AI模型参数配置
     * @return 结果
     */
    public int updateModelParamsInfo(ModelParamsInfo modelParamsInfo);

    /**
     * 批量删除AI模型参数配置
     *
     * @param modelIds 需要删除的AI模型参数配置主键集合
     * @return 结果
     */
    public int deleteModelParamsInfoByModelIds(String[] modelIds);

    /**
     * 删除AI模型参数配置信息
     *
     * @param modelId AI模型参数配置主键
     * @return 结果
     */
    public int deleteModelParamsInfoByModelId(String modelId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param modelParamsInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<ModelParamsInfo> getQueryWrapper(ModelParamsInfoQuery modelParamsInfoQuery);

    /**
     * 转换vo
     *
     * @param modelParamsInfoList ModelParamsInfo集合
     * @return ModelParamsInfoVO集合
     */
    List<ModelParamsInfoVo> convertVoList(List<ModelParamsInfo> modelParamsInfoList);
}
