package com.lz.picture.model.vo.pictureShareInfo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import com.lz.common.annotation.Excel;
import org.springframework.beans.BeanUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.picture.model.domain.PictureShareInfo;
/**
 * 图片转发记录Vo对象 p_picture_share_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class PictureShareInfoVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 转发编号 */
    @Excel(name = "转发编号")
    private String shareId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private String userId;

    /** 图片编号 */
    @Excel(name = "图片编号")
    private String pictureId;

    /** 图片分类 */
    @Excel(name = "图片分类")
    private String categoryId;

    /** 图片标签 */
    @Excel(name = "图片标签")
    private String tags;

    /** 封面URL */
    @Excel(name = "封面URL")
    private String targetCover;

    /** 转发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "转发时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


     /**
     * 对象转封装类
     *
     * @param pictureShareInfo PictureShareInfo实体对象
     * @return PictureShareInfoVo
     */
    public static PictureShareInfoVo objToVo(PictureShareInfo pictureShareInfo) {
        if (pictureShareInfo == null) {
            return null;
        }
        PictureShareInfoVo pictureShareInfoVo = new PictureShareInfoVo();
        BeanUtils.copyProperties(pictureShareInfo, pictureShareInfoVo);
        return pictureShareInfoVo;
    }
}
