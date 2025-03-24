package com.lz.picture.model.vo.pictureTagRelInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureTagRelInfo;
/**
 * 图片标签关联Vo对象 p_picture_tag_rel_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureTagRelInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 图片编号 */
    @Excel(name = "图片编号")
    private String pictureId;

    /** 标签编号 */
    @Excel(name = "标签编号")
    private String tagId;


     /**
     * 对象转封装类
     *
     * @param pictureTagRelInfo PictureTagRelInfo实体对象
     * @return PictureTagRelInfoVo
     */
    public static PictureTagRelInfoVo objToVo(PictureTagRelInfo pictureTagRelInfo) {
        if (pictureTagRelInfo == null) {
            return null;
        }
        PictureTagRelInfoVo pictureTagRelInfoVo = new PictureTagRelInfoVo();
        BeanUtils.copyProperties(pictureTagRelInfo, pictureTagRelInfoVo);
        return pictureTagRelInfoVo;
    }
}
