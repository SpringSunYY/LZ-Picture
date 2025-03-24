package com.lz.picture.model.dto.pictureCommentLikeInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureCommentLikeInfo;
/**
 * 评论点赞记录Query对象 p_picture_comment_like_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureCommentLikeInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 点赞编号 */
    private String likeId;

    /** 用户编号 */
    private String userId;

    /** 图片编号 */
    private String pictureId;

    /** 封面URL */
    private String targetCover;

    /** 点赞时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureCommentLikeInfoQuery 查询对象
     * @return PictureCommentLikeInfo
     */
    public static PictureCommentLikeInfo queryToObj(PictureCommentLikeInfoQuery pictureCommentLikeInfoQuery) {
        if (pictureCommentLikeInfoQuery == null) {
            return null;
        }
        PictureCommentLikeInfo pictureCommentLikeInfo = new PictureCommentLikeInfo();
        BeanUtils.copyProperties(pictureCommentLikeInfoQuery, pictureCommentLikeInfo);
        return pictureCommentLikeInfo;
    }
}
