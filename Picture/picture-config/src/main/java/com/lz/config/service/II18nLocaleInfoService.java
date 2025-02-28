package com.lz.config.service;

import java.util.List;
import com.lz.config.model.domain.I18nLocaleInfo;
import com.lz.config.model.vo.i18nLocaleInfo.I18nLocaleInfoVo;
import com.lz.config.model.dto.i18nLocaleInfo.I18nLocaleInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 国际化国家Service接口
 *
 * @author ruoyi
 * @date 2025-02-28
 */
public interface II18nLocaleInfoService extends IService<I18nLocaleInfo>
{
    //region mybatis代码
    /**
     * 查询国际化国家
     *
     * @param localeId 国际化国家主键
     * @return 国际化国家
     */
    public I18nLocaleInfo selectI18nLocaleInfoByLocaleId(Long localeId);

    /**
     * 查询国际化国家列表
     *
     * @param i18nLocaleInfo 国际化国家
     * @return 国际化国家集合
     */
    public List<I18nLocaleInfo> selectI18nLocaleInfoList(I18nLocaleInfo i18nLocaleInfo);

    /**
     * 新增国际化国家
     *
     * @param i18nLocaleInfo 国际化国家
     * @return 结果
     */
    public int insertI18nLocaleInfo(I18nLocaleInfo i18nLocaleInfo);

    /**
     * 修改国际化国家
     *
     * @param i18nLocaleInfo 国际化国家
     * @return 结果
     */
    public int updateI18nLocaleInfo(I18nLocaleInfo i18nLocaleInfo);

    /**
     * 批量删除国际化国家
     *
     * @param localeIds 需要删除的国际化国家主键集合
     * @return 结果
     */
    public int deleteI18nLocaleInfoByLocaleIds(Long[] localeIds);

    /**
     * 删除国际化国家信息
     *
     * @param localeId 国际化国家主键
     * @return 结果
     */
    public int deleteI18nLocaleInfoByLocaleId(Long localeId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param i18nLocaleInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<I18nLocaleInfo> getQueryWrapper(I18nLocaleInfoQuery i18nLocaleInfoQuery);

    /**
     * 转换vo
     *
     * @param i18nLocaleInfoList I18nLocaleInfo集合
     * @return I18nLocaleInfoVO集合
     */
    List<I18nLocaleInfoVo> convertVoList(List<I18nLocaleInfo> i18nLocaleInfoList);
}
