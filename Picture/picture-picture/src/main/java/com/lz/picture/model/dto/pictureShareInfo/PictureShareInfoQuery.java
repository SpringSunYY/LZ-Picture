package com.lz.picture.model.dto.pictureShareInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureShareInfo;
/**
 * 图片转发记录Query对象 p_picture_share_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureShareInfoQuery implements Serializable
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

    /** 转发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureShareInfoQuery 查询对象
     * @return PictureShareInfo
     */
    public static PictureShareInfo queryToObj(PictureShareInfoQuery pictureShareInfoQuery) {
        if (pictureShareInfoQuery == null) {
            return null;
        }
        PictureShareInfo pictureShareInfo = new PictureShareInfo();
        BeanUtils.copyProperties(pictureShareInfoQuery, pictureShareInfo);
        return pictureShareInfo;
    }
}
