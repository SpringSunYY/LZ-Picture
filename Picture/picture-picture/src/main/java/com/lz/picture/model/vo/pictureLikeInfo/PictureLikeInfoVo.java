package com.lz.picture.model.vo.pictureLikeInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureLikeInfo;
/**
 * 图片点赞记录Vo对象 p_picture_like_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureLikeInfoVo implements Serializable
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

    /** 图片分类 */
    @Excel(name = "图片分类")
    private String categoryId;

    /** 图片标签 */
    @Excel(name = "图片标签")
    private String tags;

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
     * @param pictureLikeInfo PictureLikeInfo实体对象
     * @return PictureLikeInfoVo
     */
    public static PictureLikeInfoVo objToVo(PictureLikeInfo pictureLikeInfo) {
        if (pictureLikeInfo == null) {
            return null;
        }
        PictureLikeInfoVo pictureLikeInfoVo = new PictureLikeInfoVo();
        BeanUtils.copyProperties(pictureLikeInfo, pictureLikeInfoVo);
        return pictureLikeInfoVo;
    }
}
