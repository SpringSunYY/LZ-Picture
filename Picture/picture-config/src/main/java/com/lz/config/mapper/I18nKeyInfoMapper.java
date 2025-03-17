package com.lz.config.mapper;

import java.util.List;
import com.lz.config.model.domain.I18nKeyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 国际化键名Mapper接口
 *
 * @author YY
 * @date 2025-02-28
 */
public interface I18nKeyInfoMapper extends BaseMapper<I18nKeyInfo>
{
    /**
     * 查询国际化键名
     *
     * @param keyId 国际化键名主键
     * @return 国际化键名
     */
    public I18nKeyInfo selectI18nKeyInfoByKeyId(Long keyId);

    /**
     * 查询国际化键名列表
     *
     * @param i18nKeyInfo 国际化键名
     * @return 国际化键名集合
     */
    public List<I18nKeyInfo> selectI18nKeyInfoList(I18nKeyInfo i18nKeyInfo);

    /**
     * 新增国际化键名
     *
     * @param i18nKeyInfo 国际化键名
     * @return 结果
     */
    public int insertI18nKeyInfo(I18nKeyInfo i18nKeyInfo);

    /**
     * 修改国际化键名
     *
     * @param i18nKeyInfo 国际化键名
     * @return 结果
     */
    public int updateI18nKeyInfo(I18nKeyInfo i18nKeyInfo);

    /**
     * 删除国际化键名
     *
     * @param keyId 国际化键名主键
     * @return 结果
     */
    public int deleteI18nKeyInfoByKeyId(Long keyId);

    /**
     * 批量删除国际化键名
     *
     * @param keyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteI18nKeyInfoByKeyIds(Long[] keyIds);
}
