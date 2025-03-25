package com.lz.ai.service;

import java.util.List;
import com.lz.ai.model.domain.ConversationLogInfo;
import com.lz.ai.model.vo.conversationLogInfo.ConversationLogInfoVo;
import com.lz.ai.model.dto.conversationLogInfo.ConversationLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * AI对话明细记录Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IConversationLogInfoService extends IService<ConversationLogInfo>
{
    //region mybatis代码
    /**
     * 查询AI对话明细记录
     *
     * @param conversationId AI对话明细记录主键
     * @return AI对话明细记录
     */
    public ConversationLogInfo selectConversationLogInfoByConversationId(String conversationId);

    /**
     * 查询AI对话明细记录列表
     *
     * @param conversationLogInfo AI对话明细记录
     * @return AI对话明细记录集合
     */
    public List<ConversationLogInfo> selectConversationLogInfoList(ConversationLogInfo conversationLogInfo);

    /**
     * 新增AI对话明细记录
     *
     * @param conversationLogInfo AI对话明细记录
     * @return 结果
     */
    public int insertConversationLogInfo(ConversationLogInfo conversationLogInfo);

    /**
     * 修改AI对话明细记录
     *
     * @param conversationLogInfo AI对话明细记录
     * @return 结果
     */
    public int updateConversationLogInfo(ConversationLogInfo conversationLogInfo);

    /**
     * 批量删除AI对话明细记录
     *
     * @param conversationIds 需要删除的AI对话明细记录主键集合
     * @return 结果
     */
    public int deleteConversationLogInfoByConversationIds(String[] conversationIds);

    /**
     * 删除AI对话明细记录信息
     *
     * @param conversationId AI对话明细记录主键
     * @return 结果
     */
    public int deleteConversationLogInfoByConversationId(String conversationId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param conversationLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<ConversationLogInfo> getQueryWrapper(ConversationLogInfoQuery conversationLogInfoQuery);

    /**
     * 转换vo
     *
     * @param conversationLogInfoList ConversationLogInfo集合
     * @return ConversationLogInfoVO集合
     */
    List<ConversationLogInfoVo> convertVoList(List<ConversationLogInfo> conversationLogInfoList);
}
