package com.lz.picture.model.vo.pictureInfo;

import com.lz.picture.model.domain.PictureInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户搜索建议返回VO
 *
 * @author YY
 * @date 2025-06-04
 */
@Data
public class PictureInfoSearchSuggestionVo implements Serializable {
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
     * 对象转封装类
     *
     * @param pictureInfo PictureInfo实体对象
     * @return PictureInfoVo
     */
    public static PictureInfoSearchSuggestionVo objToVo(PictureInfo pictureInfo) {
        if (pictureInfo == null) {
            return null;
        }
        PictureInfoSearchSuggestionVo pictureInfoVo = new PictureInfoSearchSuggestionVo();
        BeanUtils.copyProperties(pictureInfo, pictureInfoVo);
        return pictureInfoVo;
    }

    /**
     * 对象封装列表
     */
    public static List<PictureInfoSearchSuggestionVo> objToVo(List<PictureInfo> pictureInfoList) {
        return pictureInfoList.stream().map(PictureInfoSearchSuggestionVo::objToVo).toList();
    }
}
