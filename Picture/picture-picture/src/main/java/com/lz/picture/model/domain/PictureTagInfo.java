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
 * 图片标签信息对象 p_picture_tag_info
 *
 * @author YY
 * @date 2025-03-24
 */
@TableName("p_picture_tag_info")
@Data
public class PictureTagInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 标签编号 */
        @Excel(name = "标签编号")
    @TableId(value = "tag_id", type = IdType.ASSIGN_ID)
    private String tagId;

    /** 标签名称 */
        @Excel(name = "标签名称")
    private String name;

    /** 标签描述 */
        @Excel(name = "标签描述")
    private String tagDesc;

    /** 使用次数 */
        @Excel(name = "使用次数")
    private Long usageCount;

    /** 查看次数 */
        @Excel(name = "查看次数")
    private Long lookCount;

    /** 下载次数 */
        @Excel(name = "下载次数")
    private Long downloadCount;

    /** 所属用户 */
        @Excel(name = "所属用户")
    private String userId;

    /** 创建时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
