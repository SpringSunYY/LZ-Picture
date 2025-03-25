package com.lz.ai.mapper;

import java.util.List;
import com.lz.ai.model.domain.ConversationSessionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * AI会话管理Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface ConversationSessionInfoMapper extends BaseMapper<ConversationSessionInfo>
{
    /**
     * 查询AI会话管理
     *
     * @param sessionId AI会话管理主键
     * @return AI会话管理
     */
    public ConversationSessionInfo selectConversationSessionInfoBySessionId(String sessionId);

    /**
     * 查询AI会话管理列表
     *
     * @param conversationSessionInfo AI会话管理
     * @return AI会话管理集合
     */
    public List<ConversationSessionInfo> selectConversationSessionInfoList(ConversationSessionInfo conversationSessionInfo);

    /**
     * 新增AI会话管理
     *
     * @param conversationSessionInfo AI会话管理
     * @return 结果
     */
    public int insertConversationSessionInfo(ConversationSessionInfo conversationSessionInfo);

    /**
     * 修改AI会话管理
     *
     * @param conversationSessionInfo AI会话管理
     * @return 结果
     */
    public int updateConversationSessionInfo(ConversationSessionInfo conversationSessionInfo);

    /**
     * 删除AI会话管理
     *
     * @param sessionId AI会话管理主键
     * @return 结果
     */
    public int deleteConversationSessionInfoBySessionId(String sessionId);

    /**
     * 批量删除AI会话管理
     *
     * @param sessionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteConversationSessionInfoBySessionIds(String[] sessionIds);
}
