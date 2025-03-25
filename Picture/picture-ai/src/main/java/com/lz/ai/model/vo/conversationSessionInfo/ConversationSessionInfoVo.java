package com.lz.ai.model.vo.conversationSessionInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.ai.model.domain.ConversationSessionInfo;
/**
 * AI会话管理Vo对象 ai_conversation_session_info
 *
 * @author YY
 * @date 2025-03-25
 */
@Data
public class ConversationSessionInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 会话编号 */
    @Excel(name = "会话编号")
    private String sessionId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** AI会话编号 */
    @Excel(name = "AI会话编号")
    private String conversationId;

    /** 对话名称 */
    @Excel(name = "对话名称")
    private String sessionName;

    /** 累计消耗Tokens */
    @Excel(name = "累计消耗Tokens")
    private Long tokensTotalUsed;

    /** 累计消耗积分 */
    @Excel(name = "累计消耗积分")
    private Long pointsTotalUsed;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 用户IP地址 */
    @Excel(name = "用户IP地址")
    private String ipAddr;

    /** 设备唯一标识 */
    @Excel(name = "设备唯一标识")
    private String deviceId;

    /** 浏览器类型 */
    @Excel(name = "浏览器类型")
    private String browser;

    /** 操作系统 */
    @Excel(name = "操作系统")
    private String os;

    /** 平台 */
    @Excel(name = "平台")
    private String platform;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除（0正常 1删除） */
    @Excel(name = "删除", readConverterExp = "0=正常,1=删除")
    private String isDelete;


     /**
     * 对象转封装类
     *
     * @param conversationSessionInfo ConversationSessionInfo实体对象
     * @return ConversationSessionInfoVo
     */
    public static ConversationSessionInfoVo objToVo(ConversationSessionInfo conversationSessionInfo) {
        if (conversationSessionInfo == null) {
            return null;
        }
        ConversationSessionInfoVo conversationSessionInfoVo = new ConversationSessionInfoVo();
        BeanUtils.copyProperties(conversationSessionInfo, conversationSessionInfoVo);
        return conversationSessionInfoVo;
    }
}
