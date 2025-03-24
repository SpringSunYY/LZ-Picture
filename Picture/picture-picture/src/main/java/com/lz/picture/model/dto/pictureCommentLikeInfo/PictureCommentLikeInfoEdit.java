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
public class PictureCommentLikeInfoEdit implements Serializable
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
     * @param pictureCommentLikeInfoEdit 编辑对象
     * @return PictureCommentLikeInfo
     */
    public static PictureCommentLikeInfo editToObj(PictureCommentLikeInfoEdit pictureCommentLikeInfoEdit) {
        if (pictureCommentLikeInfoEdit == null) {
            return null;
        }
        PictureCommentLikeInfo pictureCommentLikeInfo = new PictureCommentLikeInfo();
        BeanUtils.copyProperties(pictureCommentLikeInfoEdit, pictureCommentLikeInfo);
        return pictureCommentLikeInfo;
    }
}
