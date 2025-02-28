package com.lz.config.service;

import java.util.List;
import com.lz.config.model.domain.I18nKeyInfo;
import com.lz.config.model.vo.i18nKeyInfo.I18nKeyInfoVo;
import com.lz.config.model.dto.i18nKeyInfo.I18nKeyInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 国际化键名Service接口
 *
 * @author ruoyi
 * @date 2025-02-28
 */
public interface II18nKeyInfoService extends IService<I18nKeyInfo>
{
    //region mybatis代码
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
     * 批量删除国际化键名
     *
     * @param keyIds 需要删除的国际化键名主键集合
     * @return 结果
     */
    public int deleteI18nKeyInfoByKeyIds(Long[] keyIds);

    /**
     * 删除国际化键名信息
     *
     * @param keyId 国际化键名主键
     * @return 结果
     */
    public int deleteI18nKeyInfoByKeyId(Long keyId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param i18nKeyInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<I18nKeyInfo> getQueryWrapper(I18nKeyInfoQuery i18nKeyInfoQuery);

    /**
     * 转换vo
     *
     * @param i18nKeyInfoList I18nKeyInfo集合
     * @return I18nKeyInfoVO集合
     */
    List<I18nKeyInfoVo> convertVoList(List<I18nKeyInfo> i18nKeyInfoList);
}
