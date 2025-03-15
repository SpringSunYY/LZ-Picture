package com.lz.config.service;

import java.util.List;
import com.lz.config.model.domain.InformTemplateInfo;
import com.lz.config.model.vo.informTemplateInfo.InformTemplateInfoVo;
import com.lz.config.model.dto.informTemplateInfo.InformTemplateInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 通知模版Service接口
 *
 * @author ruoyi
 * @date 2025-03-14
 */
public interface IInformTemplateInfoService extends IService<InformTemplateInfo>
{
    //region mybatis代码
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
     * 批量删除通知模版
     *
     * @param templateIds 需要删除的通知模版主键集合
     * @return 结果
     */
    public int deleteInformTemplateInfoByTemplateIds(Long[] templateIds);

    /**
     * 删除通知模版信息
     *
     * @param templateId 通知模版主键
     * @return 结果
     */
    public int deleteInformTemplateInfoByTemplateId(Long templateId);
    //endregion

    String getExample(InformTemplateInfo informTemplateInfo);

    /**
     * 获取查询条件
     *
     * @param informTemplateInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<InformTemplateInfo> getQueryWrapper(InformTemplateInfoQuery informTemplateInfoQuery);

    /**
     * 转换vo
     *
     * @param informTemplateInfoList InformTemplateInfo集合
     * @return InformTemplateInfoVO集合
     */
    List<InformTemplateInfoVo> convertVoList(List<InformTemplateInfo> informTemplateInfoList);
}
