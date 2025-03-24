package com.lz.picture.model.dto.pictureLikeInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureLikeInfo;
/**
 * 图片点赞记录Query对象 p_picture_like_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureLikeInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 点赞编号 */
    private String likeId;

    /** 用户编号 */
    private String userId;

    /** 图片编号 */
    private String pictureId;

    /** 图片分类 */
    private String categoryId;

    /** 图片标签 */
    private String tags;

    /** 点赞时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureLikeInfoQuery 查询对象
     * @return PictureLikeInfo
     */
    public static PictureLikeInfo queryToObj(PictureLikeInfoQuery pictureLikeInfoQuery) {
        if (pictureLikeInfoQuery == null) {
            return null;
        }
        PictureLikeInfo pictureLikeInfo = new PictureLikeInfo();
        BeanUtils.copyProperties(pictureLikeInfoQuery, pictureLikeInfo);
        return pictureLikeInfo;
    }
}
