package com.lz.picture.model.vo.pictureCommentInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureCommentInfo;
/**
 * 图片评论Vo对象 p_picture_comment_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureCommentInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 评论编号 */
    @Excel(name = "评论编号")
    private String commentId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 父级评论编号 */
    @Excel(name = "父级评论编号")
    private String parentId;

    /** 图片编号 */
    @Excel(name = "图片编号")
    private String pictureId;

    /** 图片分类 */
    @Excel(name = "图片分类")
    private String categoryId;

    /** 图片标签（格式："标签1","标签2"） */
    @Excel(name = "图片标签" )
    private String tags;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 评论时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "评论时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Long likeCount;

    /** 评论者IP属地 */
    @Excel(name = "评论者IP属地")
    private String ipAddress;

    /** 评论图片URL */
    @Excel(name = "评论图片URL")
    private String pictureUrl;

    /** 评论状态（0正常 1异常） */
    @Excel(name = "评论状态", readConverterExp = "0=正常,1=异常")
    private String commentStatus;


     /**
     * 对象转封装类
     *
     * @param pictureCommentInfo PictureCommentInfo实体对象
     * @return PictureCommentInfoVo
     */
    public static PictureCommentInfoVo objToVo(PictureCommentInfo pictureCommentInfo) {
        if (pictureCommentInfo == null) {
            return null;
        }
        PictureCommentInfoVo pictureCommentInfoVo = new PictureCommentInfoVo();
        BeanUtils.copyProperties(pictureCommentInfo, pictureCommentInfoVo);
        return pictureCommentInfoVo;
    }
}
