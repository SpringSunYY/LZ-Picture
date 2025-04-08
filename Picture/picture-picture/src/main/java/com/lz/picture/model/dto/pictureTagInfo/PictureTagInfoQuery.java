package com.lz.picture.model.dto.pictureTagInfo;

import java.util.Map;
import java.io.Serializable;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureTagInfo;

/**
 * 图片标签信息Query对象 p_picture_tag_info
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureTagInfoQuery implements Serializable {
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
     * 所属用户
     */
    private String userId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureTagInfoQuery 查询对象
     * @return PictureTagInfo
     */
    public static PictureTagInfo queryToObj(PictureTagInfoQuery pictureTagInfoQuery) {
        if (pictureTagInfoQuery == null) {
            return null;
        }
        PictureTagInfo pictureTagInfo = new PictureTagInfo();
        BeanUtils.copyProperties(pictureTagInfoQuery, pictureTagInfo);
        return pictureTagInfo;
    }
}
