package com.lz.picture.model.dto.pictureLikeInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureLikeInfo;
/**
 * 图片点赞记录Vo对象 p_picture_like_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureLikeInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 点赞编号 */
    private String likeId;

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
     * @param pictureLikeInfoInsert 插入对象
     * @return PictureLikeInfoInsert
     */
    public static PictureLikeInfo insertToObj(PictureLikeInfoInsert pictureLikeInfoInsert) {
        if (pictureLikeInfoInsert == null) {
            return null;
        }
        PictureLikeInfo pictureLikeInfo = new PictureLikeInfo();
        BeanUtils.copyProperties(pictureLikeInfoInsert, pictureLikeInfo);
        return pictureLikeInfo;
    }
}
