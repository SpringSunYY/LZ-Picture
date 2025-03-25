package com.lz.ai.service;

import java.util.List;
import com.lz.ai.model.domain.ConversationSessionInfo;
import com.lz.ai.model.vo.conversationSessionInfo.ConversationSessionInfoVo;
import com.lz.ai.model.dto.conversationSessionInfo.ConversationSessionInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * AI会话管理Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IConversationSessionInfoService extends IService<ConversationSessionInfo>
{
    //region mybatis代码
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
     * 批量删除AI会话管理
     *
     * @param sessionIds 需要删除的AI会话管理主键集合
     * @return 结果
     */
    public int deleteConversationSessionInfoBySessionIds(String[] sessionIds);

    /**
     * 删除AI会话管理信息
     *
     * @param sessionId AI会话管理主键
     * @return 结果
     */
    public int deleteConversationSessionInfoBySessionId(String sessionId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param conversationSessionInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<ConversationSessionInfo> getQueryWrapper(ConversationSessionInfoQuery conversationSessionInfoQuery);

    /**
     * 转换vo
     *
     * @param conversationSessionInfoList ConversationSessionInfo集合
     * @return ConversationSessionInfoVO集合
     */
    List<ConversationSessionInfoVo> convertVoList(List<ConversationSessionInfo> conversationSessionInfoList);
}
