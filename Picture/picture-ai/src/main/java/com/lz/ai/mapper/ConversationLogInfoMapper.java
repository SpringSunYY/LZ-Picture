package com.lz.ai.mapper;

import java.util.List;
import com.lz.ai.model.domain.ConversationLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * AI对话明细记录Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface ConversationLogInfoMapper extends BaseMapper<ConversationLogInfo>
{
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
     * 删除AI对话明细记录
     *
     * @param conversationId AI对话明细记录主键
     * @return 结果
     */
    public int deleteConversationLogInfoByConversationId(String conversationId);

    /**
     * 批量删除AI对话明细记录
     *
     * @param conversationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteConversationLogInfoByConversationIds(String[] conversationIds);
}
