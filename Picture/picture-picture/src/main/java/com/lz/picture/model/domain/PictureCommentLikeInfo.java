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
 * 评论点赞记录对象 p_picture_comment_like_info
 *
 * @author YY
 * @date 2025-03-24
 */
@TableName("p_picture_comment_like_info")
@Data
public class PictureCommentLikeInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 点赞编号 */
        @Excel(name = "点赞编号")
    @TableId(value = "like_id", type = IdType.ASSIGN_ID)
    private String likeId;

    /** 用户编号 */
        @Excel(name = "用户编号")
    private String userId;

    /** 图片编号 */
        @Excel(name = "图片编号")
    private String pictureId;

    /** 封面URL */
        @Excel(name = "封面URL")
    private String targetCover;

    /** 点赞时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Excel(name = "点赞时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
