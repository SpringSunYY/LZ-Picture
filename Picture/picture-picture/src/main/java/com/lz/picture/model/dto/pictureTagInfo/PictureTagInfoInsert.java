package com.lz.picture.model.dto.pictureTagInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureTagInfo;
/**
 * 图片标签信息Vo对象 p_picture_tag_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureTagInfoInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 标签编号 */
    private String tagId;

    /** 标签名称 */
    private String name;

    /** 标签描述 */
    private String tagDesc;

    /** 使用次数 */
    private Long usageCount;

    /** 查看次数 */
    private Long lookCount;

    /** 下载次数 */
    private Long downloadCount;

    /** 所属用户 */
    private String userId;

    /**
     * 对象转封装类
     *
     * @param pictureTagInfoInsert 插入对象
     * @return PictureTagInfoInsert
     */
    public static PictureTagInfo insertToObj(PictureTagInfoInsert pictureTagInfoInsert) {
        if (pictureTagInfoInsert == null) {
            return null;
        }
        PictureTagInfo pictureTagInfo = new PictureTagInfo();
        BeanUtils.copyProperties(pictureTagInfoInsert, pictureTagInfo);
        return pictureTagInfo;
    }
}
