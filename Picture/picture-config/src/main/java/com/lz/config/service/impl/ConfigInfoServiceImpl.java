package com.lz.config.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.config.mapper.ConfigInfoMapper;
import com.lz.config.model.domain.ConfigInfo;
import com.lz.config.service.IConfigInfoService;
import com.lz.config.model.dto.configInfo.ConfigInfoQuery;
import com.lz.config.model.vo.configInfo.ConfigInfoVo;

/**
 * 配置信息Service业务层处理
 *
 * @author YY
 * @date 2025-02-28
 */
@Service
public class ConfigInfoServiceImpl extends ServiceImpl<ConfigInfoMapper, ConfigInfo> implements IConfigInfoService {
    @Resource
    private ConfigInfoMapper configInfoMapper;

    //region mybatis代码

    /**
     * 查询配置信息
     *
     * @param configId 配置信息主键
     * @return 配置信息
     */
    @Override
    public ConfigInfo selectConfigInfoByConfigId(Long configId) {
        return configInfoMapper.selectConfigInfoByConfigId(configId);
    }

    /**
     * 查询配置信息列表
     *
     * @param configInfo 配置信息
     * @return 配置信息
     */
    @Override
    public List<ConfigInfo> selectConfigInfoList(ConfigInfo configInfo) {
        return configInfoMapper.selectConfigInfoList(configInfo);
    }

    /**
     * 新增配置信息
     *
     * @param configInfo 配置信息
     * @return 结果
     */
    @Override
    public int insertConfigInfo(ConfigInfo configInfo) {
        configInfo.setCreateBy(SecurityUtils.getUsername());
        configInfo.setCreateTime(DateUtils.getNowDate());
        return configInfoMapper.insertConfigInfo(configInfo);
    }

    /**
     * 修改配置信息
     *
     * @param configInfo 配置信息
     * @return 结果
     */
    @Override
    public int updateConfigInfo(ConfigInfo configInfo) {
        configInfo.setUpdateBy(SecurityUtils.getUsername());
        configInfo.setUpdateTime(DateUtils.getNowDate());
        return configInfoMapper.updateConfigInfo(configInfo);
    }

    /**
     * 批量删除配置信息
     *
     * @param configIds 需要删除的配置信息主键
     * @return 结果
     */
    @Override
    public int deleteConfigInfoByConfigIds(Long[] configIds) {
        return configInfoMapper.deleteConfigInfoByConfigIds(configIds);
    }

    /**
     * 删除配置信息信息
     *
     * @param configId 配置信息主键
     * @return 结果
     */
    @Override
    public int deleteConfigInfoByConfigId(Long configId) {
        return configInfoMapper.deleteConfigInfoByConfigId(configId);
    }

    //endregion
    @Override
    public QueryWrapper<ConfigInfo> getQueryWrapper(ConfigInfoQuery configInfoQuery) {
        QueryWrapper<ConfigInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = configInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String configName = configInfoQuery.getConfigName();
        queryWrapper.like(StringUtils.isNotEmpty(configName), "config_name", configName);

        String configKey = configInfoQuery.getConfigKey();
        queryWrapper.eq(StringUtils.isNotEmpty(configKey), "config_key", configKey);

        String configValue = configInfoQuery.getConfigValue();
        queryWrapper.eq(StringUtils.isNotEmpty(configValue), "config_value", configValue);

        String configType = configInfoQuery.getConfigType();
        queryWrapper.eq(StringUtils.isNotEmpty(configType), "config_type", configType);

        String createBy = configInfoQuery.getCreateBy();
        queryWrapper.like(StringUtils.isNotEmpty(createBy), "create_by", createBy);

        Date createTime = configInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String updateBy = configInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy), "update_by", updateBy);

        Date updateTime = configInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<ConfigInfoVo> convertVoList(List<ConfigInfo> configInfoList) {
        if (StringUtils.isEmpty(configInfoList)) {
            return Collections.emptyList();
        }
        return configInfoList.stream().map(ConfigInfoVo::objToVo).collect(Collectors.toList());
    }

}
