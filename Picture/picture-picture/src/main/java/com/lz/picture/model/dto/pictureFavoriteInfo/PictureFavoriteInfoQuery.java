package com.lz.picture.model.dto.pictureFavoriteInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureFavoriteInfo;
/**
 * 用户图片收藏Query对象 p_picture_favorite_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureFavoriteInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 收藏编号 */
    private String favoriteId;

    /** 用户编号 */
    private String userId;

    /** 图片编号 */
    private String pictureId;

    /** 图片分类 */
    private String categoryId;

    /** 图片标签 */
    private String tags;

    /** 收藏时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureFavoriteInfoQuery 查询对象
     * @return PictureFavoriteInfo
     */
    public static PictureFavoriteInfo queryToObj(PictureFavoriteInfoQuery pictureFavoriteInfoQuery) {
        if (pictureFavoriteInfoQuery == null) {
            return null;
        }
        PictureFavoriteInfo pictureFavoriteInfo = new PictureFavoriteInfo();
        BeanUtils.copyProperties(pictureFavoriteInfoQuery, pictureFavoriteInfo);
        return pictureFavoriteInfo;
    }
}
