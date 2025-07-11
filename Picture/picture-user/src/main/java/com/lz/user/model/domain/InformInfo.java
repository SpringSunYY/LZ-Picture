package com.lz.user.model.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 用户通知记录对象 u_inform_info
 *
 * @author YY
 * @date 2025-05-27
 */
@TableName("u_inform_info")
@Data
public class InformInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知记录编号
     */
    @Excel(name = "通知记录编号")
    @TableId(value = "record_id", type = IdType.ASSIGN_UUID)
    private String recordId;

    /**
     * 模板KEY
     */
    @Excel(name = "模板KEY")
    private String templateKey;

    /**
     * 模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）
     */
    @Excel(name = "模版类型", readConverterExp = "1=短信,2=邮件,3=站内通知,4=APP推送,5=微信模板")
    private String templateType;

    /**
     * 语言（默认zh-CN）
     */
    @Excel(name = "语言")
    private String locale;

    /**
     * 通知标题
     */
    @Excel(name = "通知标题")
    private String informTitle;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号")
    private String userId;

    /**
     * 实际发送内容
     */
    @Excel(name = "实际发送内容")
    private String content;

    /**
     * 通知类型
     */
    @Excel(name = "通知类型",readConverterExp = "1=公共,0=通知")
    private String informType;

    /**
     * 发送状态（0=待发送 1=已发送 2=发送失败 3=已撤回）
     */
    @Excel(name = "发送状态", readConverterExp = "0=待发送,1=已发送,2=发送失败,3=已撤回")
    private String status;

    /**
     * 是否已读（0=未读 1=已读）
     */
    @Excel(name = "是否已读", readConverterExp = "0=未读,1=已读")
    private String isRead;

    /**
     * 读取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "读取时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    /**
     * 重试次数
     */
    @Excel(name = "重试次数")
    private Long retryCount;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 删除（0=正常 1=删除）
     */
    @Excel(name = "删除", readConverterExp = "0=正常,1=删除")
    private String isDelete;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
