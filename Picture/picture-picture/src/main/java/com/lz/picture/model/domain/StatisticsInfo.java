package com.lz.picture.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.annotation.Excel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 统计信息对象 p_statistics_info
 *
 * @author YY
 * @date 2025-07-17
 */
@TableName("p_statistics_info")
@Data
public class StatisticsInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 统计编号
     */
    @Excel(name = "统计编号")
    @TableId(value = "statistics_id", type = IdType.ASSIGN_ID)
    private String statisticsId;

    /**
     * 统计粒度
     */
    @Excel(name = "统计粒度")
    private String type;

    /**
     * 统计名称
     */
    @Excel(name = "统计名称")
    private String statisticsName;

    /**
     * KEY
     */
    @Excel(name = "KEY")
    private String statisticsKey;

    /**
     * 期数
     */
    @Excel(name = "期数")
    private Long stages;

    /**
     * 统计内容
     */
    @Excel(name = "统计内容")
    private String content;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
