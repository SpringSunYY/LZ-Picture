package com.lz.picture.model.dto.pictureCommentInfo;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureCommentInfo;
/**
 * 图片评论Vo对象 p_picture_comment_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureCommentInfoEdit implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /** 评论编号 */
    private String commentId;

    /** 用户编号 */
    private String userId;

    /** 父级评论编号 */
    private String parentId;

    /** 图片编号 */
    private String pictureId;

    /** 图片分类 */
    private String categoryId;

    /** 图片标签（格式："标签1","标签2"） */
    private String tags;

    /** 评论内容 */
    private String content;

    /** 点赞数 */
    private Long likeCount;

    /** 评论者IP属地 */
    private String ipAddress;

    /** 评论图片URL */
    private String pictureUrl;

    /** 评论状态（0正常 1异常） */
    private String commentStatus;

    /**
     * 对象转封装类
     *
     * @param pictureCommentInfoEdit 编辑对象
     * @return PictureCommentInfo
     */
    public static PictureCommentInfo editToObj(PictureCommentInfoEdit pictureCommentInfoEdit) {
        if (pictureCommentInfoEdit == null) {
            return null;
        }
        PictureCommentInfo pictureCommentInfo = new PictureCommentInfo();
        BeanUtils.copyProperties(pictureCommentInfoEdit, pictureCommentInfo);
        return pictureCommentInfo;
    }
}
