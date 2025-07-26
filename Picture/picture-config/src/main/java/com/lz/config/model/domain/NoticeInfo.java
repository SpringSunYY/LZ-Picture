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
 * 用户公告对象 c_notice_info
 *
 * @author YY
 * @date 2025-07-26
 */
@TableName("c_notice_info")
@Data
public class NoticeInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 公告编号 */
        @Excel(name = "公告编号")
    @TableId(value = "notice_id", type = IdType.ASSIGN_ID)
    private String noticeId;

    /** 语言 默认zh-CN */
        @Excel(name = "语言 默认zh-CN")
    private String locale;

    /** 通知平台 */
        @Excel(name = "通知平台")
    private String platform;

    /** 公告类型 */
        @Excel(name = "公告类型")
    private String noticeType;

    /** 是否展示 */
        @Excel(name = "是否展示")
    private String isExhibit;

    /** 公告标题 */
        @Excel(name = "公告标题")
    private String noticeTitle;

    /** 公告内容 */
        @Excel(name = "公告内容")
    private String content;

    /** 排序 */
        @Excel(name = "排序")
    private Long orderNum;

    /** 公告状态 */
        @Excel(name = "公告状态")
    private String noticeStatus;

    /** 创建人 */
        @Excel(name = "创建人")
    private Long userId;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 备注 */
        @Excel(name = "备注")
    private String remark;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
