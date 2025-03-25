package com.lz.ai.model.dto.conversationSessionInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.ai.model.domain.ConversationSessionInfo;
/**
 * AI会话管理Vo对象 ai_conversation_session_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class ConversationSessionInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 会话编号 */
    private String sessionId;

    /** 用户编号 */
    private String userId;

    /** AI会话编号 */
    private String conversationId;

    /** 对话名称 */
    private String sessionName;

    /** 累计消耗Tokens */
    private Long tokensTotalUsed;

    /** 累计消耗积分 */
    private Long pointsTotalUsed;

    /** 备注 */
    private String remark;

    /** 用户IP地址 */
    private String ipAddr;

    /** 设备唯一标识 */
    private String deviceId;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 平台 */
    private String platform;

    /** 删除（0正常 1删除） */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param conversationSessionInfoEdit 编辑对象
     * @return ConversationSessionInfo
     */
    public static ConversationSessionInfo editToObj(ConversationSessionInfoEdit conversationSessionInfoEdit) {
        if (conversationSessionInfoEdit == null) {
            return null;
        }
        ConversationSessionInfo conversationSessionInfo = new ConversationSessionInfo();
        BeanUtils.copyProperties(conversationSessionInfoEdit, conversationSessionInfo);
        return conversationSessionInfo;
    }
}
