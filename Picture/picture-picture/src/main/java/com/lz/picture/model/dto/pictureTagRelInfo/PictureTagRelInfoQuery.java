package com.lz.picture.model.dto.pictureTagRelInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lz.picture.model.domain.PictureTagRelInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 图片标签关联Query对象 p_picture_tag_rel_info
 *
 * @author YY
 * @date 2025-06-04
 */
@Data
public class PictureTagRelInfoQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联编号
     */
    private String relId;

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
     * 所属用户
     */
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

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
