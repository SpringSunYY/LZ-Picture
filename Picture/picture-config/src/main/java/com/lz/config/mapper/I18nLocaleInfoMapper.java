package com.lz.config.mapper;

import java.util.List;
import com.lz.config.model.domain.I18nLocaleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 国际化国家Mapper接口
 *
 * @author YY
 * @date 2025-02-28
 */
public interface I18nLocaleInfoMapper extends BaseMapper<I18nLocaleInfo>
{
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
     * 删除国际化国家
     *
     * @param localeId 国际化国家主键
     * @return 结果
     */
    public int deleteI18nLocaleInfoByLocaleId(Long localeId);

    /**
     * 批量删除国际化国家
     *
     * @param localeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteI18nLocaleInfoByLocaleIds(Long[] localeIds);
}
