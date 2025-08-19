package com.lz.picture.model.vo.pictureInfo;

import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 图片信息Vo对象 p_picture_info
 * ai图片详情推荐
 *
 * @author YY
 * @date 2025-04-09
 */
@Data
public class PictureInfoAiDetailRecommendVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    private String pictureId;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;



    /**
     * 对象转封装类
     *
     * @param pictureInfo PictureInfo实体对象
     * @return PictureInfoVo
     */
    public static PictureInfoAiDetailRecommendVo objToVo(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        PictureInfoAiDetailRecommendVo pictureInfoVo = new PictureInfoAiDetailRecommendVo();
        BeanUtils.copyProperties(pictureInfo, pictureInfoVo);
        return pictureInfoVo;
    }

    /**
     * 对象转换集合
     *
     * @param pictureInfoList
     * @return
     */
    public static List<PictureInfoAiDetailRecommendVo> objToVo(List<PictureInfo> pictureInfoList) {
        if (pictureInfoList == null) {
            return null;
        }
        return pictureInfoList.stream().map(PictureInfoAiDetailRecommendVo::objToVo).toList();
    }
}
