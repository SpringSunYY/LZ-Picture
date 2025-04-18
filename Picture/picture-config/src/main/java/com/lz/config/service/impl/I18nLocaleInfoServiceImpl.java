package com.lz.config.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.core.redis.RedisCache;
import com.lz.common.exception.sql.SQLDuplicateKeyException;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import com.lz.config.mapper.I18nMessageInfoMapper;
import com.lz.config.model.domain.I18nMessageInfo;
import com.lz.config.model.enmus.CLocaleStatusEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.config.mapper.I18nLocaleInfoMapper;
import com.lz.config.model.domain.I18nLocaleInfo;
import com.lz.config.service.II18nLocaleInfoService;
import com.lz.config.model.dto.i18nLocaleInfo.I18nLocaleInfoQuery;
import com.lz.config.model.vo.i18nLocaleInfo.I18nLocaleInfoVo;

import static com.lz.common.constant.redis.UserConfigRedisConstants.CONFIG_LOCALIZATION;

/**
 * 国际化国家Service业务层处理
 *
 * @author YY
 * @date 2025-02-28
 */
@Slf4j
@Service
public class I18nLocaleInfoServiceImpl extends ServiceImpl<I18nLocaleInfoMapper, I18nLocaleInfo> implements II18nLocaleInfoService {
    @Resource
    private I18nLocaleInfoMapper i18nLocaleInfoMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private I18nMessageInfoMapper i18nMessageInfoMapper;

    /**
     * 项目启动时，初始化message 到缓存
     */
    @PostConstruct
    public void init() {
        loadingLocaleInfo();
    }

    @Override
    public void loadingLocaleInfo() {
        I18nLocaleInfo i18nLocaleInfo = new I18nLocaleInfo();
        i18nLocaleInfo.setLocaleStatus(CLocaleStatusEnum.LOCALE_STATUS_0.getValue());
        List<I18nLocaleInfo> i18nLocaleInfos = i18nLocaleInfoMapper.selectI18nLocaleInfoList(i18nLocaleInfo);
        for (I18nLocaleInfo info : i18nLocaleInfos) {
            setMessageCache(info);
        }
    }

    /**
     * description: 设置message缓存
     * author: YY
     * method: setMessageCache
     * date: 2025/1/10 17:21
     * param:
     * param: i18nLocaleInfo
     * return: void
     **/
    private void setMessageCache(I18nLocaleInfo i18nLocaleInfo) {
        I18nMessageInfo i18nMessageInfo = new I18nMessageInfo();
        i18nMessageInfo.setLocale(i18nLocaleInfo.getLocale());
        List<I18nMessageInfo> i18nMessageInfos = i18nMessageInfoMapper.selectI18nMessageInfoList(i18nMessageInfo);
        Map<String, String> map = i18nMessageInfos.stream().collect(Collectors.toMap(I18nMessageInfo::getMessageKey, I18nMessageInfo::getMessage));
        redisCache.setCacheMap(CONFIG_LOCALIZATION + i18nLocaleInfo.getLocale(), map);
    }

    //region mybatis代码

    /**
     * 查询国际化国家
     *
     * @param localeId 国际化国家主键
     * @return 国际化国家
     */
    @Override
    public I18nLocaleInfo selectI18nLocaleInfoByLocaleId(Long localeId) {
        return i18nLocaleInfoMapper.selectI18nLocaleInfoByLocaleId(localeId);
    }

    /**
     * 查询国际化国家列表
     *
     * @param i18nLocaleInfo 国际化国家
     * @return 国际化国家
     */
    @Override
    public List<I18nLocaleInfo> selectI18nLocaleInfoList(I18nLocaleInfo i18nLocaleInfo) {
        return i18nLocaleInfoMapper.selectI18nLocaleInfoList(i18nLocaleInfo);
    }

    /**
     * 新增国际化国家
     *
     * @param i18nLocaleInfo 国际化国家
     * @return 结果
     */
    @Override
    public int insertI18nLocaleInfo(I18nLocaleInfo i18nLocaleInfo) {
        i18nLocaleInfo.setCreateBy(SecurityUtils.getUsername());
        i18nLocaleInfo.setCreateTime(DateUtils.getNowDate());
        try {
            return i18nLocaleInfoMapper.insertI18nLocaleInfo(i18nLocaleInfo);
        } catch (Exception e) {
            log.error("插入国际化国家失败：{}", e.getMessage(), e.getCause());
            throw new SQLDuplicateKeyException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 修改国际化国家
     *
     * @param i18nLocaleInfo 国际化国家
     * @return 结果
     */
    @Override
    public int updateI18nLocaleInfo(I18nLocaleInfo i18nLocaleInfo) {
        i18nLocaleInfo.setUpdateBy(SecurityUtils.getUsername());
        i18nLocaleInfo.setUpdateTime(DateUtils.getNowDate());
        try {
            return i18nLocaleInfoMapper.updateI18nLocaleInfo(i18nLocaleInfo);
        } catch (Exception e) {
            log.error("修改国际化国家失败：{}", e.getMessage(), e.getCause());
            throw new SQLDuplicateKeyException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 批量删除国际化国家
     *
     * @param localeIds 需要删除的国际化国家主键
     * @return 结果
     */
    @Override
    public int deleteI18nLocaleInfoByLocaleIds(Long[] localeIds) {
        return i18nLocaleInfoMapper.deleteI18nLocaleInfoByLocaleIds(localeIds);
    }

    /**
     * 删除国际化国家信息
     *
     * @param localeId 国际化国家主键
     * @return 结果
     */
    @Override
    public int deleteI18nLocaleInfoByLocaleId(Long localeId) {
        return i18nLocaleInfoMapper.deleteI18nLocaleInfoByLocaleId(localeId);
    }

    //endregion
    @Override
    public QueryWrapper<I18nLocaleInfo> getQueryWrapper(I18nLocaleInfoQuery i18nLocaleInfoQuery) {
        QueryWrapper<I18nLocaleInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = i18nLocaleInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String localeName = i18nLocaleInfoQuery.getLocaleName();
        queryWrapper.like(StringUtils.isNotEmpty(localeName), "locale_name", localeName);

        String locale = i18nLocaleInfoQuery.getLocale();
        queryWrapper.eq(StringUtils.isNotEmpty(locale), "locale", locale);

        String localeStatus = i18nLocaleInfoQuery.getLocaleStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(localeStatus), "locale_status", localeStatus);

        String createBy = i18nLocaleInfoQuery.getCreateBy();
        queryWrapper.like(StringUtils.isNotEmpty(createBy), "create_by", createBy);

        Date createTime = i18nLocaleInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String updateBy = i18nLocaleInfoQuery.getUpdateBy();
        queryWrapper.like(StringUtils.isNotEmpty(updateBy), "update_by", updateBy);

        Date updateTime = i18nLocaleInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<I18nLocaleInfoVo> convertVoList(List<I18nLocaleInfo> i18nLocaleInfoList) {
        if (StringUtils.isEmpty(i18nLocaleInfoList)) {
            return Collections.emptyList();
        }
        return i18nLocaleInfoList.stream().map(I18nLocaleInfoVo::objToVo).collect(Collectors.toList());
    }

}
