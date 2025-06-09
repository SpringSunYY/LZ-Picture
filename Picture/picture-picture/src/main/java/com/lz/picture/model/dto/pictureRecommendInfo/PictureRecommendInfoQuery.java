package com.lz.picture.model.dto.pictureRecommendInfo;

import java.util.Map;
import java.io.Serializable;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureRecommendInfo;
/**
 * 用户图片推荐模型Query对象 p_picture_recommend_info
 *
 * @author YY
 * @date 2025-06-10
 */
@Data
public class PictureRecommendInfoQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 推荐编号 */
    private String recommendId;

    /** 用户编号 */
    private String userId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param pictureRecommendInfoQuery 查询对象
     * @return PictureRecommendInfo
     */
    public static PictureRecommendInfo queryToObj(PictureRecommendInfoQuery pictureRecommendInfoQuery) {
        if (pictureRecommendInfoQuery == null) {
            return null;
        }
        PictureRecommendInfo pictureRecommendInfo = new PictureRecommendInfo();
        BeanUtils.copyProperties(pictureRecommendInfoQuery, pictureRecommendInfo);
        return pictureRecommendInfo;
    }
}
