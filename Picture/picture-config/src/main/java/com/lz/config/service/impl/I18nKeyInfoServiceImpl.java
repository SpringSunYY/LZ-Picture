package com.lz.config.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.exception.ServiceException;
import com.lz.common.exception.sql.SQLDuplicateKeyException;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.config.mapper.I18nKeyInfoMapper;
import com.lz.config.model.domain.I18nKeyInfo;
import com.lz.config.service.II18nKeyInfoService;
import com.lz.config.model.dto.i18nKeyInfo.I18nKeyInfoQuery;
import com.lz.config.model.vo.i18nKeyInfo.I18nKeyInfoVo;

/**
 * 国际化键名Service业务层处理
 *
 * @author YY
 * @date 2025-02-28
 */
@Slf4j
@Service
public class I18nKeyInfoServiceImpl extends ServiceImpl<I18nKeyInfoMapper, I18nKeyInfo> implements II18nKeyInfoService {
    @Resource
    private I18nKeyInfoMapper i18nKeyInfoMapper;

    //region mybatis代码

    /**
     * 查询国际化键名
     *
     * @param keyId 国际化键名主键
     * @return 国际化键名
     */
    @Override
    public I18nKeyInfo selectI18nKeyInfoByKeyId(Long keyId) {
        return i18nKeyInfoMapper.selectI18nKeyInfoByKeyId(keyId);
    }

    /**
     * 查询国际化键名列表
     *
     * @param i18nKeyInfo 国际化键名
     * @return 国际化键名
     */
    @Override
    public List<I18nKeyInfo> selectI18nKeyInfoList(I18nKeyInfo i18nKeyInfo) {
        return i18nKeyInfoMapper.selectI18nKeyInfoList(i18nKeyInfo);
    }

    /**
     * 新增国际化键名
     *
     * @param i18nKeyInfo 国际化键名
     * @return 结果
     */
    @Override
    public int insertI18nKeyInfo(I18nKeyInfo i18nKeyInfo) {
        i18nKeyInfo.setCreateBy(SecurityUtils.getUsername());
        i18nKeyInfo.setCreateTime(DateUtils.getNowDate());
        try {
            return i18nKeyInfoMapper.insertI18nKeyInfo(i18nKeyInfo);
        } catch (Exception e) {
            log.error("新增国际化键名失败：{}", e.getMessage(), e.getCause());
            throw new SQLDuplicateKeyException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 修改国际化键名
     *
     * @param i18nKeyInfo 国际化键名
     * @return 结果
     */
    @Override
    public int updateI18nKeyInfo(I18nKeyInfo i18nKeyInfo) {
        //先判断是否更改了key
        I18nKeyInfo old = i18nKeyInfoMapper.selectI18nKeyInfoByKeyId(i18nKeyInfo.getKeyId());
        if (StringUtils.isNotNull(old)&&!old.getKeyName().equals(i18nKeyInfo.getKeyName())) {
            //如果更改了key直接报错
            throw new ServiceException("key不可以修改！！！");
        }
        i18nKeyInfo.setUpdateBy(i18nKeyInfo.getUpdateBy());
        i18nKeyInfo.setUpdateTime(DateUtils.getNowDate());
        return i18nKeyInfoMapper.updateI18nKeyInfo(i18nKeyInfo);
    }

    /**
     * 批量删除国际化键名
     *
     * @param keyIds 需要删除的国际化键名主键
     * @return 结果
     */
    @Override
    public int deleteI18nKeyInfoByKeyIds(Long[] keyIds) {
        return i18nKeyInfoMapper.deleteI18nKeyInfoByKeyIds(keyIds);
    }

    /**
     * 删除国际化键名信息
     *
     * @param keyId 国际化键名主键
     * @return 结果
     */
    @Override
    public int deleteI18nKeyInfoByKeyId(Long keyId) {
        return i18nKeyInfoMapper.deleteI18nKeyInfoByKeyId(keyId);
    }

    //endregion
    @Override
    public QueryWrapper<I18nKeyInfo> getQueryWrapper(I18nKeyInfoQuery i18nKeyInfoQuery) {
        QueryWrapper<I18nKeyInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = i18nKeyInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String keyName = i18nKeyInfoQuery.getKeyName();
        queryWrapper.like(StringUtils.isNotEmpty(keyName), "key_name", keyName);

        Long orderNum = i18nKeyInfoQuery.getOrderNum();
        queryWrapper.eq(StringUtils.isNotNull(orderNum), "order_num", orderNum);

        String createBy = i18nKeyInfoQuery.getCreateBy();
        queryWrapper.like(StringUtils.isNotEmpty(createBy), "create_by", createBy);

        Date createTime = i18nKeyInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String updateBy = i18nKeyInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy), "update_by", updateBy);

        Date updateTime = i18nKeyInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<I18nKeyInfoVo> convertVoList(List<I18nKeyInfo> i18nKeyInfoList) {
        if (StringUtils.isEmpty(i18nKeyInfoList)) {
            return Collections.emptyList();
        }
        return i18nKeyInfoList.stream().map(I18nKeyInfoVo::objToVo).collect(Collectors.toList());
    }

}
