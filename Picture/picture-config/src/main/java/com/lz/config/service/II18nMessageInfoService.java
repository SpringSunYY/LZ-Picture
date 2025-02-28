package com.lz.config.service;

import java.util.List;
import com.lz.config.model.domain.I18nMessageInfo;
import com.lz.config.model.vo.i18nMessageInfo.I18nMessageInfoVo;
import com.lz.config.model.dto.i18nMessageInfo.I18nMessageInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 国际化信息Service接口
 *
 * @author ruoyi
 * @date 2025-02-28
 */
public interface II18nMessageInfoService extends IService<I18nMessageInfo>
{
    //region mybatis代码
    /**
     * 查询国际化信息
     *
     * @param messageId 国际化信息主键
     * @return 国际化信息
     */
    public I18nMessageInfo selectI18nMessageInfoByMessageId(Long messageId);

    /**
     * 查询国际化信息列表
     *
     * @param i18nMessageInfo 国际化信息
     * @return 国际化信息集合
     */
    public List<I18nMessageInfo> selectI18nMessageInfoList(I18nMessageInfo i18nMessageInfo);

    /**
     * 新增国际化信息
     *
     * @param i18nMessageInfo 国际化信息
     * @return 结果
     */
    public int insertI18nMessageInfo(I18nMessageInfo i18nMessageInfo);

    /**
     * 修改国际化信息
     *
     * @param i18nMessageInfo 国际化信息
     * @return 结果
     */
    public int updateI18nMessageInfo(I18nMessageInfo i18nMessageInfo);

    /**
     * 批量删除国际化信息
     *
     * @param messageIds 需要删除的国际化信息主键集合
     * @return 结果
     */
    public int deleteI18nMessageInfoByMessageIds(Long[] messageIds);

    /**
     * 删除国际化信息信息
     *
     * @param messageId 国际化信息主键
     * @return 结果
     */
    public int deleteI18nMessageInfoByMessageId(Long messageId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param i18nMessageInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<I18nMessageInfo> getQueryWrapper(I18nMessageInfoQuery i18nMessageInfoQuery);

    /**
     * 转换vo
     *
     * @param i18nMessageInfoList I18nMessageInfo集合
     * @return I18nMessageInfoVO集合
     */
    List<I18nMessageInfoVo> convertVoList(List<I18nMessageInfo> i18nMessageInfoList);
}
