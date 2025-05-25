package com.lz.picture.model.domain;

import java.io.Serial;
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
 * 图片标签关联对象 p_picture_tag_rel_info
 *
 * @author YY
 * @date 2025-05-25
 */
@TableName("p_picture_tag_rel_info")
@Data
public class PictureTagRelInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    @Excel(name = "图片编号")
    private String pictureId;

    /**
     * 图片名称
     */
    @Excel(name = "图片名称")
    private String pictureName;

    /**
     * 标签编号
     */
    @Excel(name = "标签编号")
    private String tagId;

    /**
     * 标签名称
     */
    @Excel(name = "标签名称")
    private String tagName;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
