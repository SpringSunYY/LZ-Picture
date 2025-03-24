package com.lz.picture.model.vo.pictureCommentLikeInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureCommentLikeInfo;
/**
 * 评论点赞记录Vo对象 p_picture_comment_like_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureCommentLikeInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 点赞编号 */
    @Excel(name = "点赞编号")
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


     /**
     * 对象转封装类
     *
     * @param pictureCommentLikeInfo PictureCommentLikeInfo实体对象
     * @return PictureCommentLikeInfoVo
     */
    public static PictureCommentLikeInfoVo objToVo(PictureCommentLikeInfo pictureCommentLikeInfo) {
        if (pictureCommentLikeInfo == null) {
            return null;
        }
        PictureCommentLikeInfoVo pictureCommentLikeInfoVo = new PictureCommentLikeInfoVo();
        BeanUtils.copyProperties(pictureCommentLikeInfo, pictureCommentLikeInfoVo);
        return pictureCommentLikeInfoVo;
    }
}
