package com.lz.picture.model.domain;

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
 * 空间扩容信息对象 p_space_dilatation_info
 *
 * @author YY
 * @date 2025-06-28
 */
@TableName("p_space_dilatation_info")
@Data
public class SpaceDilatationInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 申请编号 */
        @Excel(name = "申请编号")
    @TableId(value = "dilatation_id", type = IdType.ASSIGN_ID)
    private String dilatationId;

    /** 扩容KEY */
        @Excel(name = "扩容KEY")
    private String dilatationKey;

    /** 空间编号 */
        @Excel(name = "空间编号")
    private String spaceId;

    /** 空间名称 */
        @Excel(name = "空间名称")
    private String spaceName;

    /** 缩略图 URL */
        @Excel(name = "缩略图 URL")
    private String thumbnailUrl;

    /** 扩容类型 */
        @Excel(name = "扩容类型")
    private String dilatationType;

    /** 扩容单价 */
        @Excel(name = "扩容单价")
    private Long dilatationUnit;

    /** 扩容总数 */
        @Excel(name = "扩容总数")
    private Long dilatationTotal;

    /** 消耗积分 */
        @Excel(name = "消耗积分")
    private Long pointsTotal;

    /** 用户 */
        @Excel(name = "用户")
    private String userId;

    /** 创建时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
