package com.lz.ai.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * AI会话管理对象 ai_conversation_session_info
 *
 * @author YY
 * @date 2025-03-25
 */
@TableName("ai_conversation_session_info")
@Data
public class ConversationSessionInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 会话编号 */
        @Excel(name = "会话编号")
    @TableId(value = "session_id", type = IdType.ASSIGN_ID)
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

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
