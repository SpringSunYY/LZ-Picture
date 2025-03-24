package com.lz.picture.model.vo.pictureFavoriteInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureFavoriteInfo;
/**
 * 用户图片收藏Vo对象 p_picture_favorite_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureFavoriteInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 收藏编号 */
    @Excel(name = "收藏编号")
    private String favoriteId;

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

    /** 收藏时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "收藏时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


     /**
     * 对象转封装类
     *
     * @param pictureFavoriteInfo PictureFavoriteInfo实体对象
     * @return PictureFavoriteInfoVo
     */
    public static PictureFavoriteInfoVo objToVo(PictureFavoriteInfo pictureFavoriteInfo) {
        if (pictureFavoriteInfo == null) {
            return null;
        }
        PictureFavoriteInfoVo pictureFavoriteInfoVo = new PictureFavoriteInfoVo();
        BeanUtils.copyProperties(pictureFavoriteInfo, pictureFavoriteInfoVo);
        return pictureFavoriteInfoVo;
    }
}
