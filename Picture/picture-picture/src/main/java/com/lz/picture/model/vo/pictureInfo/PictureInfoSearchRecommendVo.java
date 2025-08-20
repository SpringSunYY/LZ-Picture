package com.lz.picture.model.vo.pictureInfo;

import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户搜索推荐返回VO
 *
 * @author YY
 * @date 2025-06-04
 */
@Data
public class PictureInfoSearchRecommendVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片编号
     */
    private String pictureId;

    /**
     * 图片名称
     */
    private String name;

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
    public static PictureInfoSearchRecommendVo objToVo(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        PictureInfoSearchRecommendVo pictureInfoVo = new PictureInfoSearchRecommendVo();
        BeanUtils.copyProperties(pictureInfo, pictureInfoVo);
        return pictureInfoVo;
    }

    /**
     * 对象封装列表
     */
    public static List<PictureInfoSearchRecommendVo> objToVo(List<PictureInfo> pictureInfoList) {
        return pictureInfoList.stream().map(PictureInfoSearchRecommendVo::objToVo).toList();
    }

    /**
     * 统计对象转封装类
     */
    public static PictureInfoSearchRecommendVo statisticsObjToVo(PictureInfoStatisticsVo statisticsVo) {
        if (statisticsVo == null) {
            return null;
        }
        PictureInfoSearchRecommendVo pictureInfoVo = new PictureInfoSearchRecommendVo();
        BeanUtils.copyProperties(statisticsVo, pictureInfoVo);
        return pictureInfoVo;
    }

    /**
     * 统计对象转封装类列表
     */
    public static List<PictureInfoSearchRecommendVo> statisticsObjToVo(List<PictureInfoStatisticsVo> statisticsVoList) {
        return statisticsVoList.stream().map(PictureInfoSearchRecommendVo::statisticsObjToVo).toList();
    }
}
