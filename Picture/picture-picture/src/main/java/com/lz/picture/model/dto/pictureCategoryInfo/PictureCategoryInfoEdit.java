package com.lz.picture.model.dto.pictureCategoryInfo;

import java.io.Serializable;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.picture.model.domain.PictureCategoryInfo;

/**
 * 图片分类信息Vo对象 p_picture_category_info
 *
 * @author YY
 * @date 2025-04-08
 */
@Data
public class PictureCategoryInfoEdit implements Serializable {
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
     * 祖级列表
     */
    private String ancestors;

    /**
     * 封面图URL
     */
    private String coverUrl;


    /**
     * 封面图标
     */
    private String categoryIcon;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String categoryDesc;

    /**
     * 分类状态（0正常 1关闭）
     */
    private String categoryStatus;

    /**
     * 分类类型
     */
    private String categoryType;

    /**
     * 查询状态（0是 1否）
     */
    private String queryStatus;

    /**
     * 使用次数
     */
    private Long usageCount;

    /**
     * 查看次数
     */
    private Long lookCount;

    /**
     * 下载次数
     */
    private Long downloadCount;

    /**
     * 删除标记（0否 1是）
     */
    private String isDelete;

    /**
     * 对象转封装类
     *
     * @param pictureCategoryInfoEdit 编辑对象
     * @return PictureCategoryInfo
     */
    public static PictureCategoryInfo editToObj(PictureCategoryInfoEdit pictureCategoryInfoEdit) {
        if (pictureCategoryInfoEdit == null) {
            return null;
        }
        PictureCategoryInfo pictureCategoryInfo = new PictureCategoryInfo();
        BeanUtils.copyProperties(pictureCategoryInfoEdit, pictureCategoryInfo);
        return pictureCategoryInfo;
    }
}
