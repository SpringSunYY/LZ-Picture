package com.lz.ai.model.dto.conversationSessionInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.ai.model.domain.ConversationSessionInfo;
/**
 * AI会话管理Query对象 ai_conversation_session_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class ConversationSessionInfoQuery implements Serializable
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

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 删除（0正常 1删除） */
    private String isDelete;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param conversationSessionInfoQuery 查询对象
     * @return ConversationSessionInfo
     */
    public static ConversationSessionInfo queryToObj(ConversationSessionInfoQuery conversationSessionInfoQuery) {
        if (conversationSessionInfoQuery == null) {
            return null;
        }
        ConversationSessionInfo conversationSessionInfo = new ConversationSessionInfo();
        BeanUtils.copyProperties(conversationSessionInfoQuery, conversationSessionInfo);
        return conversationSessionInfo;
    }
}
