package com.lz.ai.model.domain;

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
 * 官方AI操作日志对象 ai_official_usage_log_info
 *
 * @author YY
 * @date 2025-03-25
 */
@TableName("ai_official_usage_log_info")
@Data
public class OfficialUsageLogInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 记录编号 */
        @Excel(name = "记录编号")
    @TableId(value = "log_id", type = IdType.ASSIGN_ID)
    private String logId;

    /** 管理员编号 */
        @Excel(name = "管理员编号")
    private Long userId;

    /** 模型编号 */
        @Excel(name = "模型编号")
    private String modelId;

    /** 操作类型（如：data_analysis） */
        @Excel(name = "操作类型", readConverterExp = "如=：data_analysis")
    private String operationType;

    /** 输入参数 */
        @Excel(name = "输入参数")
    private String inputParams;

    /** 模型返回结果（JSON/Text格式） */
        @Excel(name = "模型返回结果", readConverterExp = "J=SON/Text格式")
    private String outputResult;

    /** 请求时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "请求时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date requestTime;

    /** 请求时长（毫秒） */
        @Excel(name = "请求时长", readConverterExp = "毫=秒")
    private Long requestDuration;

    /** 消耗Tokens数量 */
        @Excel(name = "消耗Tokens数量")
    private Long tokensUsed;

    /** 状态（0成功 1失败） */
        @Excel(name = "状态", readConverterExp = "0=成功,1=失败")
    private String logStatus;

    /** 模型返回状态码 */
        @Excel(name = "模型返回状态码")
    private String aiStatusCode;

    /** 失败原因 */
        @Excel(name = "失败原因")
    private String failReason;

    /** 备注 */
        @Excel(name = "备注")
    private String remark;

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
