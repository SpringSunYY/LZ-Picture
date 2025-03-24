package com.lz.picture.model.dto.pictureCommentInfo;

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
public class PictureCommentInfoInsert implements Serializable
{
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
     * @param pictureCommentInfoInsert 插入对象
     * @return PictureCommentInfoInsert
     */
    public static PictureCommentInfo insertToObj(PictureCommentInfoInsert pictureCommentInfoInsert) {
        if (pictureCommentInfoInsert == null) {
            return null;
        }
        PictureCommentInfo pictureCommentInfo = new PictureCommentInfo();
        BeanUtils.copyProperties(pictureCommentInfoInsert, pictureCommentInfo);
        return pictureCommentInfo;
    }
}
