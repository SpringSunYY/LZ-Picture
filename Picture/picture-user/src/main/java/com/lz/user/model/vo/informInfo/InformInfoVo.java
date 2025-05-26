package com.lz.user.model.vo.informInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.user.model.domain.InformInfo;

/**
 * 用户通知记录Vo对象 u_inform_info
 *
 * @author YY
 * @date 2025-05-27
 */
@Data
public class InformInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知记录编号
     */
    @Excel(name = "通知记录编号")
    private String recordId;

    /**
     * 模板KEY
     */
    @Excel(name = "模板KEY")
    private Long templateKey;

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
    private Integer status;

    /**
     * 是否已读（0=未读 1=已读）
     */
    @Excel(name = "是否已读", readConverterExp = "0=未读,1=已读")
    private Integer isRead;

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
    private Integer isDelete;


    /**
     * 对象转封装类
     *
     * @param informInfo InformInfo实体对象
     * @return InformInfoVo
     */
    public static InformInfoVo objToVo(InformInfo informInfo) {
        if (informInfo == null) {
            return null;
        }
        InformInfoVo informInfoVo = new InformInfoVo();
        BeanUtils.copyProperties(informInfo, informInfoVo);
        return informInfoVo;
    }
}
