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
 * 国际化国家对象 c_i18n_locale_info
 *
 * @author ruoyi
 * @date 2025-02-28
 */
        @TableName("c_i18n_locale_info")
        @Data
        public class I18nLocaleInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

        /** 编号 */
            @Excel(name = "编号")
        @TableId(value = "locale_id", type = IdType.ASSIGN_ID)
    private Long localeId;

        /** 国家地区 */
            @Excel(name = "国家地区")
    private String localeName;

        /** 简称 */
            @Excel(name = "简称")
    private String locale;

        /** 状态（0正常 1隐藏） */
            @Excel(name = "状态", readConverterExp = "0=正常,1=隐藏")
    private String localeStatus;

        /** 创建人 */
            @Excel(name = "创建人")
    private String createBy;

        /** 创建时间 */
            @JsonFormat(pattern = "yyyy-MM-dd")
            @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

        /** 更新人 */
            @Excel(name = "更新人")
    private String updateBy;

        /** 更新时间 */
            @JsonFormat(pattern = "yyyy-MM-dd")
            @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateTime;

        /** 备注 */
            @Excel(name = "备注")
    private String remark;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
