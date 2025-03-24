package com.lz.picture.model.dto.pictureCommentLikeInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureCommentLikeInfo;
/**
 * 评论点赞记录Vo对象 p_picture_comment_like_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureCommentLikeInfoInsert implements Serializable
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

    /**
     * 对象转封装类
     *
     * @param pictureCommentLikeInfoInsert 插入对象
     * @return PictureCommentLikeInfoInsert
     */
    public static PictureCommentLikeInfo insertToObj(PictureCommentLikeInfoInsert pictureCommentLikeInfoInsert) {
        if (pictureCommentLikeInfoInsert == null) {
            return null;
        }
        PictureCommentLikeInfo pictureCommentLikeInfo = new PictureCommentLikeInfo();
        BeanUtils.copyProperties(pictureCommentLikeInfoInsert, pictureCommentLikeInfo);
        return pictureCommentLikeInfo;
    }
}
