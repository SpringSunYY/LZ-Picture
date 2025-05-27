package com.lz.user.model.dto.informInfo;

import java.io.Serial;
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
import com.lz.user.model.domain.InformInfo;

/**
 * 用户通知记录Query对象 u_inform_info
 *
 * @author YY
 * @date 2025-05-27
 */
@Data
public class InformInfoQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知记录编号
     */
    private String recordId;

    /**
     * 模板KEY
     */
    private String templateKey;

    /**
     * 模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）
     */
    private String templateType;

    /**
     * 语言（默认zh-CN）
     */
    private String locale;

    /**
     * 通知标题
     */
    private String informTitle;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 通知类型
     */
    private String informType;

    /**
     * 发送状态（0=待发送 1=已发送 2=发送失败 3=已撤回）
     */
    private String status;

    /**
     * 是否已读（0=未读 1=已读）
     */
    private String isRead;

    /**
     * 读取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date readTime;

    /**
     * 重试次数
     */
    private Long retryCount;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendTime;

    /**
     * 删除（0=正常 1=删除）
     */
    private String isDelete;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param informInfoQuery 查询对象
     * @return InformInfo
     */
    public static InformInfo queryToObj(InformInfoQuery informInfoQuery) {
        if (informInfoQuery == null) {
            return null;
        }
        InformInfo informInfo = new InformInfo();
        BeanUtils.copyProperties(informInfoQuery, informInfo);
        return informInfo;
    }
}
