package com.lz.picture.model.vo.pictureCategoryInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.annotation.Excel;
import com.lz.picture.model.domain.PictureCategoryInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 图片分类信息Vo对象 p_picture_category_info
 *
 * @author YY
 * @date 2025-04-08
 */
@Data
public class UserPictureCategoryInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类编号
     */
    private String categoryId;

    /**
     * 父级分类编号
     */
    private String parentId;


    /**
     * 封面图URL
     */
    private String coverUrl;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String categoryDesc;

    /**
     * 对象转封装类
     *
     * @param pictureCategoryInfo PictureCategoryInfo实体对象
     * @return PictureCategoryInfoVo
     */
    public static UserPictureCategoryInfoVo objToVo(PictureCategoryInfo pictureCategoryInfo) {
        if (pictureCategoryInfo == null) {
            return null;
        }
        UserPictureCategoryInfoVo pictureCategoryInfoVo = new UserPictureCategoryInfoVo();
        BeanUtils.copyProperties(pictureCategoryInfo, pictureCategoryInfoVo);
        return pictureCategoryInfoVo;
    }
}
