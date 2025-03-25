package com.lz.ai.model.dto.conversationLogInfo;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.ai.model.domain.ConversationLogInfo;
/**
 * AI对话明细记录Vo对象 ai_conversation_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class ConversationLogInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 对话记录编号 */
    private String conversationId;

    /** 会话编号 */
    private String sessionId;

    /** 用户编号 */
    private String userId;

    /** 用户输入文本 */
    private String inputText;

    /** AI返回文本 */
    private String outputText;

    /** 请求时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestTime;

    /** 响应时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date responseTime;

    /** 消耗Tokens数量 */
    private Long tokensUsed;

    /** 消耗积分 */
    private Long pointsUsed;

    /** 状态（0=成功 1=失败） */
    private String conversationStatus;

    /** 模型返回码 */
    private String aiStatusCode;

    /** 失败原因 */
    private String failReason;

    /** 对话类型（0文本 1图片） */
    private String conversationType;

    /**
     * 对象转封装类
     *
     * @param conversationLogInfoInsert 插入对象
     * @return ConversationLogInfoInsert
     */
    public static ConversationLogInfo insertToObj(ConversationLogInfoInsert conversationLogInfoInsert) {
        if (conversationLogInfoInsert == null) {
            return null;
        }
        ConversationLogInfo conversationLogInfo = new ConversationLogInfo();
        BeanUtils.copyProperties(conversationLogInfoInsert, conversationLogInfo);
        return conversationLogInfo;
    }
}
