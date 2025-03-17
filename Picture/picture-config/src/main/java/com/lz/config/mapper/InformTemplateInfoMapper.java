package com.lz.config.mapper;

import java.util.List;
import com.lz.config.model.domain.InformTemplateInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 通知模版Mapper接口
 *
 * @author YY
 * @date 2025-03-14
 */
public interface InformTemplateInfoMapper extends BaseMapper<InformTemplateInfo>
{
    /**
     * 查询通知模版
     *
     * @param templateId 通知模版主键
     * @return 通知模版
     */
    public InformTemplateInfo selectInformTemplateInfoByTemplateId(Long templateId);

    /**
     * 查询通知模版列表
     *
     * @param informTemplateInfo 通知模版
     * @return 通知模版集合
     */
    public List<InformTemplateInfo> selectInformTemplateInfoList(InformTemplateInfo informTemplateInfo);

    /**
     * 新增通知模版
     *
     * @param informTemplateInfo 通知模版
     * @return 结果
     */
    public int insertInformTemplateInfo(InformTemplateInfo informTemplateInfo);

    /**
     * 修改通知模版
     *
     * @param informTemplateInfo 通知模版
     * @return 结果
     */
    public int updateInformTemplateInfo(InformTemplateInfo informTemplateInfo);

    /**
     * 删除通知模版
     *
     * @param templateId 通知模版主键
     * @return 结果
     */
    public int deleteInformTemplateInfoByTemplateId(Long templateId);

    /**
     * 批量删除通知模版
     *
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInformTemplateInfoByTemplateIds(Long[] templateIds);
}
