package com.lz.config.service;

import java.util.List;
import com.lz.config.model.domain.ConfigInfo;
import com.lz.config.model.vo.configInfo.ConfigInfoVo;
import com.lz.config.model.dto.configInfo.ConfigInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 配置信息Service接口
 *
 * @author YY
 * @date 2025-02-28
 */
public interface IConfigInfoService extends IService<ConfigInfo>
{
    //region mybatis代码
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
     * 批量删除配置信息
     *
     * @param configIds 需要删除的配置信息主键集合
     * @return 结果
     */
    public int deleteConfigInfoByConfigIds(Long[] configIds);

    /**
     * 删除配置信息信息
     *
     * @param configId 配置信息主键
     * @return 结果
     */
    public int deleteConfigInfoByConfigId(Long configId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param configInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<ConfigInfo> getQueryWrapper(ConfigInfoQuery configInfoQuery);

    /**
     * 转换vo
     *
     * @param configInfoList ConfigInfo集合
     * @return ConfigInfoVO集合
     */
    List<ConfigInfoVo> convertVoList(List<ConfigInfo> configInfoList);
}
