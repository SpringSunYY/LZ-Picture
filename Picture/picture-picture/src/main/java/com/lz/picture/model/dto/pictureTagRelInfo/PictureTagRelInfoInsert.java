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
 * @date 2025-06-04
 */
@Data
public class PictureTagRelInfoInsert implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /** 关联编号 */
    private String relId;

    /**
     * 对象转封装类
     *
     * @param pictureTagRelInfoInsert 插入对象
     * @return PictureTagRelInfoInsert
     */
    public static PictureTagRelInfo insertToObj(PictureTagRelInfoInsert pictureTagRelInfoInsert) {
        if (pictureTagRelInfoInsert == null) {
            return null;
        }
        PictureTagRelInfo pictureTagRelInfo = new PictureTagRelInfo();
        BeanUtils.copyProperties(pictureTagRelInfoInsert, pictureTagRelInfo);
        return pictureTagRelInfo;
    }
}
