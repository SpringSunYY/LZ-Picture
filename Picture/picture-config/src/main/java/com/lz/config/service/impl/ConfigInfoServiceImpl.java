package com.lz.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.exception.sql.SQLDuplicateKeyException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.config.mapper.ConfigInfoMapper;
import com.lz.config.model.domain.ConfigInfo;
import com.lz.config.model.dto.configInfo.ConfigInfoQuery;
import com.lz.config.model.vo.configInfo.ConfigInfoVo;
import com.lz.config.service.IConfigInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.redis.ConfigRedisConstants.CONFIG_CONFIG_INFO_KEY;

/**
 * 配置信息Service业务层处理
 *
 * @author YY
 * @date 2025-02-28
 */
@Slf4j
@Service
public class ConfigInfoServiceImpl extends ServiceImpl<ConfigInfoMapper, ConfigInfo> implements IConfigInfoService {
    @Resource
    private ConfigInfoMapper configInfoMapper;

    @Resource
    private RedisCache redisCache;

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
        int i = 0;
        try {
            i = configInfoMapper.insertConfigInfo(configInfo);
        } catch (Exception e) {
            log.error("插入配置信息失败：{}", e.getMessage());
            throw new SQLDuplicateKeyException(e.getMessage(),e.getCause());
        }
        //存入缓存
        redisCache.setCacheObject(CONFIG_CONFIG_INFO_KEY + configInfo.getConfigName(), configInfo.getConfigValue());
        return i;
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
        int i = 0;
        try {
            i = configInfoMapper.updateConfigInfo(configInfo);
        } catch (Exception e) {
            log.error("修改配置信息失败：{}", e.getMessage());
            throw new SQLDuplicateKeyException(e.getMessage(),e.getCause());
        }
        //存入缓存
        redisCache.setCacheObject(CONFIG_CONFIG_INFO_KEY + configInfo.getConfigName(), configInfo.getConfigValue());
        return i;
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


    @Override
    public String getConfigInfoCache(String configName) {
        //先根据名称获取缓存
        String cacheObject = redisCache.getCacheObject(CONFIG_CONFIG_INFO_KEY + configName);
        if (StringUtils.isNotEmpty(cacheObject)) {
            return cacheObject;
        }
        //如果没有则查数据库
        ConfigInfo configInfo = configInfoMapper.selectOne(new QueryWrapper<ConfigInfo>().eq("config_name", configName));
        if (StringUtils.isNull(configInfo)) {
            //如果没有则返回空
            return "";
        }
        //数据库如果有则存缓存
        redisCache.setCacheObject(CONFIG_CONFIG_INFO_KEY + configName, configInfo.getConfigValue());
        return configInfo.getConfigValue();
    }
}
