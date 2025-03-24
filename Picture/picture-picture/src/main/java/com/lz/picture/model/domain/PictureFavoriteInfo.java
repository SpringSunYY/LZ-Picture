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
 * 用户图片收藏对象 p_picture_favorite_info
 *
 * @author YY
 * @date 2025-03-24
 */
@TableName("p_picture_favorite_info")
@Data
public class PictureFavoriteInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 收藏编号 */
        @Excel(name = "收藏编号")
    @TableId(value = "favorite_id", type = IdType.ASSIGN_ID)
    private String favoriteId;

    /** 用户编号 */
        @Excel(name = "用户编号")
    private String userId;

    /** 图片编号 */
        @Excel(name = "图片编号")
    private String pictureId;

    /** 图片分类 */
        @Excel(name = "图片分类")
    private String categoryId;

    /** 图片标签 */
        @Excel(name = "图片标签")
    private String tags;

    /** 封面URL */
        @Excel(name = "封面URL")
    private String targetCover;

    /** 收藏时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "收藏时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
