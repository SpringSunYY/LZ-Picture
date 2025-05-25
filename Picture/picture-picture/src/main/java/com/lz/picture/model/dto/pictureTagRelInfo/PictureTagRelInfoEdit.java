package com.lz.picture.model.dto.pictureTagRelInfo;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureTagRelInfo;

/**
 * 图片标签关联Vo对象 p_picture_tag_rel_info
 *
 * @author YY
 * @date 2025-05-25
 */
@Data
public class PictureTagRelInfoEdit implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    private String pictureId;

    /**
     * 图片名称
     */
    private String pictureName;

    /**
     * 标签编号
     */
    private String tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 对象转封装类
     *
     * @param pictureTagRelInfoEdit 编辑对象
     * @return PictureTagRelInfo
     */
    public static PictureTagRelInfo editToObj(PictureTagRelInfoEdit pictureTagRelInfoEdit) {
        if (pictureTagRelInfoEdit == null) {
            return null;
        }
        PictureTagRelInfo pictureTagRelInfo = new PictureTagRelInfo();
        BeanUtils.copyProperties(pictureTagRelInfoEdit, pictureTagRelInfo);
        return pictureTagRelInfo;
    }
}
