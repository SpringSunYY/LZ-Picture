package com.lz.picture.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.common.annotation.Excel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 图片标签关联对象 p_picture_tag_rel_info
 *
 * @author YY
 * @date 2025-06-05
 */
@TableName("p_picture_tag_rel_info")
@Data
public class PictureTagRelInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联编号
     */
    @Excel(name = "关联编号")
    @TableId(value = "rel_id", type = IdType.ASSIGN_ID)
    private String relId;

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
     * 查看次数
     */
    @Excel(name = "查看次数")
    private Long lookCount;

    /**
     * 收藏次数
     */
    @Excel(name = "收藏次数")
    private Long collectCount;

    /**
     * 点赞次数
     */
    @Excel(name = "点赞次数")
    private Long likeCount;

    /**
     * 分享次数
     */
    @Excel(name = "分享次数")
    private Long shareCount;

    /**
     * 下载次数
     */
    @Excel(name = "下载次数")
    private Long downloadCount;

    /**
     * 所属用户
     */
    @Excel(name = "所属用户")
    private String userId;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
