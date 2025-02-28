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
 * 配置信息对象 c_config_info
 *
 * @author YY
 * @date 2025-02-28
 */
        @TableName("c_config_info")
        @Data
        public class ConfigInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

        /** 编号 */
            @Excel(name = "编号")
        @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    private Long configId;

        /** 配置名称 */
            @Excel(name = "配置名称")
    private String configName;

        /** 配置键名 */
            @Excel(name = "配置键名")
    private String configKey;

        /** 配置键值 */
            @Excel(name = "配置键值")
    private String configValue;

        /** 配置类型（1值 2文件） */
            @Excel(name = "配置类型", readConverterExp = "1=值,2=文件")
    private String configType;

        /** 配置排序 */
            @Excel(name = "配置排序")
    private Long orderNum;

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
