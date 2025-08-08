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
 * 提示词信息对象 ai_prompt_info
 *
 * @author YY
 * @date 2025-08-08
 */
@TableName("ai_prompt_info")
@Data
public class PromptInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
        @Excel(name = "编号")
    @TableId(value = "info_id", type = IdType.ASSIGN_ID)
    private String infoId;

    /** 名称 */
        @Excel(name = "名称")
    private String name;

    /** 提示内容 */
        @Excel(name = "提示内容")
    private String content;

    /** 排序 */
        @Excel(name = "排序")
    private Long orderNum;

    /** 创建人 */
        @Excel(name = "创建人")
    private String createBy;

    /** 创建时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新人 */
        @Excel(name = "更新人")
    private String updateBy;

    /** 更新时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
        @Excel(name = "备注")
    private String remark;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
