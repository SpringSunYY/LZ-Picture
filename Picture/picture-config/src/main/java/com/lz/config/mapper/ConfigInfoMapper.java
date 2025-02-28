package com.lz.config.mapper;

import java.util.List;
import com.lz.config.model.domain.ConfigInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 配置信息Mapper接口
 *
 * @author YY
 * @date 2025-02-28
 */
public interface ConfigInfoMapper extends BaseMapper<ConfigInfo>
{
    /**
     * 查询配置信息
     *
     * @param configId 配置信息主键
     * @return 配置信息
     */
    public ConfigInfo selectConfigInfoByConfigId(Long configId);

    /**
     * 查询配置信息列表
     *
     * @param configInfo 配置信息
     * @return 配置信息集合
     */
    public List<ConfigInfo> selectConfigInfoList(ConfigInfo configInfo);

    /**
     * 新增配置信息
     *
     * @param configInfo 配置信息
     * @return 结果
     */
    public int insertConfigInfo(ConfigInfo configInfo);

    /**
     * 修改配置信息
     *
     * @param configInfo 配置信息
     * @return 结果
     */
    public int updateConfigInfo(ConfigInfo configInfo);

    /**
     * 删除配置信息
     *
     * @param configId 配置信息主键
     * @return 结果
     */
    public int deleteConfigInfoByConfigId(Long configId);

    /**
     * 批量删除配置信息
     *
     * @param configIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteConfigInfoByConfigIds(Long[] configIds);
}
