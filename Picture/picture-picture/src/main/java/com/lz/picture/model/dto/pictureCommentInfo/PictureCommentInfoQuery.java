package com.lz.picture.model.dto.pictureCommentInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureCommentInfo;
/**
 * 图片评论Query对象 p_picture_comment_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureCommentInfoQuery implements Serializable
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

    /** 评论时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 点赞数 */
    private Long likeCount;

    /** 评论者IP属地 */
    private String ipAddress;

    /** 评论状态（0正常 1异常） */
    private String commentStatus;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureCommentInfoQuery 查询对象
     * @return PictureCommentInfo
     */
    public static PictureCommentInfo queryToObj(PictureCommentInfoQuery pictureCommentInfoQuery) {
        if (pictureCommentInfoQuery == null) {
            return null;
        }
        PictureCommentInfo pictureCommentInfo = new PictureCommentInfo();
        BeanUtils.copyProperties(pictureCommentInfoQuery, pictureCommentInfo);
        return pictureCommentInfo;
    }
}
