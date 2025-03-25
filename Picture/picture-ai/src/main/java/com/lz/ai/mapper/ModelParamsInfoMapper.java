package com.lz.ai.mapper;

import java.util.List;
import com.lz.ai.model.domain.ModelParamsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * AI模型参数配置Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface ModelParamsInfoMapper extends BaseMapper<ModelParamsInfo>
{
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
     * 删除AI模型参数配置
     *
     * @param modelId AI模型参数配置主键
     * @return 结果
     */
    public int deleteModelParamsInfoByModelId(String modelId);

    /**
     * 批量删除AI模型参数配置
     *
     * @param modelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteModelParamsInfoByModelIds(String[] modelIds);
}
