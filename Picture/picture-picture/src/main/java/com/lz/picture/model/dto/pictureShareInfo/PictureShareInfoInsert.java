package com.lz.picture.model.dto.pictureShareInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureShareInfo;
/**
 * 图片转发记录Vo对象 p_picture_share_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureShareInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 转发编号 */
    private String shareId;

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
     * @param pictureShareInfoInsert 插入对象
     * @return PictureShareInfoInsert
     */
    public static PictureShareInfo insertToObj(PictureShareInfoInsert pictureShareInfoInsert) {
        if (pictureShareInfoInsert == null) {
            return null;
        }
        PictureShareInfo pictureShareInfo = new PictureShareInfo();
        BeanUtils.copyProperties(pictureShareInfoInsert, pictureShareInfo);
        return pictureShareInfo;
    }
}
