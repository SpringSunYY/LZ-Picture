package com.lz.picture.model.dto.pictureRecommendInfo;

import java.io.Serializable;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureRecommendInfo;
/**
 * 用户图片推荐模型Vo对象 p_picture_recommend_info
 *
 * @author YY
 * @date 2025-06-10
 */
@Data
public class PictureRecommendInfoEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 推荐编号 */
    private String recommendId;

    /** 分类分数 */
    private String categoryScores;

    /** 高兴趣分类 */
    private String topCategories;

    /** 归一化分类分数 */
    private String normalizedCategoryScores;

    /** 标签分数 */
    private String tagScores;

    /** 高兴趣标签 */
    private String topTags;

    /** 归一化标签分数 */
    private String normalizedTagScores;

    /** 更多信息 */
    private String moreInfo;

    /** 用户编号 */
    private String userId;

    /**
     * 对象转封装类
     *
     * @param pictureRecommendInfoEdit 编辑对象
     * @return PictureRecommendInfo
     */
    public static PictureRecommendInfo editToObj(PictureRecommendInfoEdit pictureRecommendInfoEdit) {
        if (pictureRecommendInfoEdit == null) {
            return null;
        }
        PictureRecommendInfo pictureRecommendInfo = new PictureRecommendInfo();
        BeanUtils.copyProperties(pictureRecommendInfoEdit, pictureRecommendInfo);
        return pictureRecommendInfo;
    }
}
