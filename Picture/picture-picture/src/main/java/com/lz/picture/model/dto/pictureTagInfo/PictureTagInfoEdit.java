package com.lz.picture.model.dto.pictureTagInfo;

import java.io.Serializable;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureTagInfo;

/**
 * 图片标签信息Vo对象 p_picture_tag_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureTagInfoEdit implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签编号
     */
    private String tagId;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签状态
     */
    private String tagsStatus;

    /**
     * 标签描述
     */
    private String tagDesc;

    /**
     * 使用次数
     */
    private Long usageCount;

    /**
     * 查看次数
     */
    private Long lookCount;

    /**
     * 下载次数
     */
    private Long downloadCount;

    /**
     * 对象转封装类
     *
     * @param pictureTagInfoEdit 编辑对象
     * @return PictureTagInfo
     */
    public static PictureTagInfo editToObj(PictureTagInfoEdit pictureTagInfoEdit) {
        if (pictureTagInfoEdit == null) {
            return null;
        }
        PictureTagInfo pictureTagInfo = new PictureTagInfo();
        BeanUtils.copyProperties(pictureTagInfoEdit, pictureTagInfo);
        return pictureTagInfo;
    }
}
