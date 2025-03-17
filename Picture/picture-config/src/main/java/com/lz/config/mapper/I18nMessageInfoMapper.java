package com.lz.config.mapper;

import java.util.List;
import com.lz.config.model.domain.I18nMessageInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 国际化信息Mapper接口
 *
 * @author YY
 * @date 2025-02-28
 */
public interface I18nMessageInfoMapper extends BaseMapper<I18nMessageInfo>
{
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
     * 删除国际化信息
     *
     * @param messageId 国际化信息主键
     * @return 结果
     */
    public int deleteI18nMessageInfoByMessageId(Long messageId);

    /**
     * 批量删除国际化信息
     *
     * @param messageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteI18nMessageInfoByMessageIds(Long[] messageIds);
}
