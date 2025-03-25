package com.lz.ai.model.vo.conversationLogInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.ai.model.domain.ConversationLogInfo;
/**
 * AI对话明细记录Vo对象 ai_conversation_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class ConversationLogInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 对话记录编号 */
    @Excel(name = "对话记录编号")
    private String conversationId;

    /** 会话编号 */
    @Excel(name = "会话编号")
    private String sessionId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 用户输入文本 */
    @Excel(name = "用户输入文本")
    private String inputText;

    /** AI返回文本 */
    @Excel(name = "AI返回文本")
    private String outputText;

    /** 请求时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "请求时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date requestTime;

    /** 响应时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "响应时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date responseTime;

    /** 消耗Tokens数量 */
    @Excel(name = "消耗Tokens数量")
    private Long tokensUsed;

    /** 消耗积分 */
    @Excel(name = "消耗积分")
    private Long pointsUsed;

    /** 状态（0=成功 1=失败） */
    @Excel(name = "状态", readConverterExp = "0==成功,1==失败")
    private String conversationStatus;

    /** 模型返回码 */
    @Excel(name = "模型返回码")
    private String aiStatusCode;

    /** 失败原因 */
    @Excel(name = "失败原因")
    private String failReason;

    /** 对话类型（0文本 1图片） */
    @Excel(name = "对话类型", readConverterExp = "0=文本,1=图片")
    private String conversationType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


     /**
     * 对象转封装类
     *
     * @param conversationLogInfo ConversationLogInfo实体对象
     * @return ConversationLogInfoVo
     */
    public static ConversationLogInfoVo objToVo(ConversationLogInfo conversationLogInfo) {
        if (conversationLogInfo == null) {
            return null;
        }
        ConversationLogInfoVo conversationLogInfoVo = new ConversationLogInfoVo();
        BeanUtils.copyProperties(conversationLogInfo, conversationLogInfoVo);
        return conversationLogInfoVo;
    }
}
