package com.lz.ai.model.dto.conversationLogInfo;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.ai.model.domain.ConversationLogInfo;
/**
 * AI对话明细记录Query对象 ai_conversation_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class ConversationLogInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 对话记录编号 */
    private String conversationId;

    /** 会话编号 */
    private String sessionId;

    /** 用户编号 */
    private String userId;

    /** 请求时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requestTime;

    /** 响应时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date responseTime;

    /** 状态（0=成功 1=失败） */
    private String conversationStatus;

    /** 模型返回码 */
    private String aiStatusCode;

    /** 对话类型（0文本 1图片） */
    private String conversationType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param conversationLogInfoQuery 查询对象
     * @return ConversationLogInfo
     */
    public static ConversationLogInfo queryToObj(ConversationLogInfoQuery conversationLogInfoQuery) {
        if (conversationLogInfoQuery == null) {
            return null;
        }
        ConversationLogInfo conversationLogInfo = new ConversationLogInfo();
        BeanUtils.copyProperties(conversationLogInfoQuery, conversationLogInfo);
        return conversationLogInfo;
    }
}
