package com.lz.config.model.domain;

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
 * 国际化信息对象 c_i18n_message_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
@TableName("c_i18n_message_info")
@Data
public class I18nMessageInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Excel(name = "主键")
    @TableId(value = "message_id", type = IdType.ASSIGN_ID)
    private Long messageId;

    /**
     * 键
     */
    @Excel(name = "键")
    private String messageKey;

    /**
     * 简称
     */
    @Excel(name = "简称")
    private String locale;

    /**
     * 消息
     */
    @Excel(name = "消息")
    private String message;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    @Excel(name = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
