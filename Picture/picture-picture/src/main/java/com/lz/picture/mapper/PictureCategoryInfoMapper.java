package com.lz.picture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.picture.model.domain.PictureCategoryInfo;

import java.util.List;

/**
 * 图片分类信息Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureCategoryInfoMapper extends BaseMapper<PictureCategoryInfo> {
    /**
     * 查询图片分类信息
     *
     * @param categoryId 图片分类信息主键
     * @return 图片分类信息
     */
    public PictureCategoryInfo selectPictureCategoryInfoByCategoryId(String categoryId);

    /**
     * 查询图片分类信息列表
     *
     * @param pictureCategoryInfo 图片分类信息
     * @return 图片分类信息集合
     */
    public List<PictureCategoryInfo> selectPictureCategoryInfoList(PictureCategoryInfo pictureCategoryInfo);

    /**
     * 新增图片分类信息
     *
     * @param pictureCategoryInfo 图片分类信息
     * @return 结果
     */
    public int insertPictureCategoryInfo(PictureCategoryInfo pictureCategoryInfo);

    /**
     * 修改图片分类信息
     *
     * @param pictureCategoryInfo 图片分类信息
     * @return 结果
     */
    public int updatePictureCategoryInfo(PictureCategoryInfo pictureCategoryInfo);

    /**
     * 删除图片分类信息
     *
     * @param categoryId 图片分类信息主键
     * @return 结果
     */
    public int deletePictureCategoryInfoByCategoryId(String categoryId);

    /**
     * 批量删除图片分类信息
     *
     * @param categoryIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureCategoryInfoByCategoryIds(String[] categoryIds);

    /**
     * 查询图片分类信息列表
     *
     * @param categoryId 图片编号
     * @return List<PictureCategoryInfo>
     * @author: YY
     * @method: findCategoryChildren 查询图片分类信息列表
     * @date: 2025/7/28 22:54
     **/
    List<PictureCategoryInfo> findCategoryChildren(String categoryId);
}
