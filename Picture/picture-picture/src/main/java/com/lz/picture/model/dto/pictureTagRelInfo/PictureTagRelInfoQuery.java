package com.lz.picture.model.dto.pictureTagRelInfo;

import java.io.Serial;
import java.util.Map;
import java.io.Serializable;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureTagRelInfo;

/**
 * 图片标签关联Query对象 p_picture_tag_rel_info
 *
 * @author YY
 * @date 2025-05-25
 */
@Data
public class PictureTagRelInfoQuery implements Serializable {
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
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureTagRelInfoQuery 查询对象
     * @return PictureTagRelInfo
     */
    public static PictureTagRelInfo queryToObj(PictureTagRelInfoQuery pictureTagRelInfoQuery) {
        if (pictureTagRelInfoQuery == null) {
            return null;
        }
        PictureTagRelInfo pictureTagRelInfo = new PictureTagRelInfo();
        BeanUtils.copyProperties(pictureTagRelInfoQuery, pictureTagRelInfo);
        return pictureTagRelInfo;
    }
}
