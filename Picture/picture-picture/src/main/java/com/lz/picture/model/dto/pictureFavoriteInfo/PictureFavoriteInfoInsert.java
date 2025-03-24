package com.lz.picture.model.dto.pictureFavoriteInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureFavoriteInfo;
/**
 * 用户图片收藏Vo对象 p_picture_favorite_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureFavoriteInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 收藏编号 */
    private String favoriteId;

    /** 用户编号 */
    private String userId;

    /** 图片编号 */
    private String pictureId;

    /** 图片分类 */
    private String categoryId;

    /** 图片标签 */
    private String tags;

    /** 封面URL */
    private String targetCover;

    /**
     * 对象转封装类
     *
     * @param pictureFavoriteInfoInsert 插入对象
     * @return PictureFavoriteInfoInsert
     */
    public static PictureFavoriteInfo insertToObj(PictureFavoriteInfoInsert pictureFavoriteInfoInsert) {
        if (pictureFavoriteInfoInsert == null) {
            return null;
        }
        PictureFavoriteInfo pictureFavoriteInfo = new PictureFavoriteInfo();
        BeanUtils.copyProperties(pictureFavoriteInfoInsert, pictureFavoriteInfo);
        return pictureFavoriteInfo;
    }
}
