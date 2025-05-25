package com.lz.picture.service;

import java.util.List;

import com.lz.picture.model.domain.PictureCategoryInfo;
import com.lz.picture.model.vo.pictureCategoryInfo.PictureCategoryInfoVo;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 图片分类信息Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureCategoryInfoService extends IService<PictureCategoryInfo> {
    //region mybatis代码

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
     * 批量删除图片分类信息
     *
     * @param categoryIds 需要删除的图片分类信息主键集合
     * @return 结果
     */
    public int deletePictureCategoryInfoByCategoryIds(String[] categoryIds);

    /**
     * 删除图片分类信息信息
     *
     * @param categoryId 图片分类信息主键
     * @return 结果
     */
    public int deletePictureCategoryInfoByCategoryId(String categoryId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param pictureCategoryInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureCategoryInfo> getQueryWrapper(PictureCategoryInfoQuery pictureCategoryInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureCategoryInfoList PictureCategoryInfo集合
     * @return PictureCategoryInfoVO集合
     */
    List<PictureCategoryInfoVo> convertVoList(List<PictureCategoryInfo> pictureCategoryInfoList);

    /**
     * 根据分类信息更新图片分类信息，新的，旧的，新的+1，旧的-1，如果相同不更新
     *
     * @param categoryId    新的分类信息
     * @param categoryIdOld 旧的分类信息
     * @return int
     * @author: YY
     * @method: updatePictureCategoryInfo
     * @date: 2025/5/25 22:09
     **/
    int updatePictureCategoryInfo(String categoryId, String categoryIdOld);
}
