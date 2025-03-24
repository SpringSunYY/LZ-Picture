package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureTagRelInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 图片标签关联Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureTagRelInfoMapper extends BaseMapper<PictureTagRelInfo>
{
    /**
     * 查询图片标签关联
     *
     * @param pictureId 图片标签关联主键
     * @return 图片标签关联
     */
    public PictureTagRelInfo selectPictureTagRelInfoByPictureId(String pictureId);

    /**
     * 查询图片标签关联列表
     *
     * @param pictureTagRelInfo 图片标签关联
     * @return 图片标签关联集合
     */
    public List<PictureTagRelInfo> selectPictureTagRelInfoList(PictureTagRelInfo pictureTagRelInfo);

    /**
     * 新增图片标签关联
     *
     * @param pictureTagRelInfo 图片标签关联
     * @return 结果
     */
    public int insertPictureTagRelInfo(PictureTagRelInfo pictureTagRelInfo);

    /**
     * 修改图片标签关联
     *
     * @param pictureTagRelInfo 图片标签关联
     * @return 结果
     */
    public int updatePictureTagRelInfo(PictureTagRelInfo pictureTagRelInfo);

    /**
     * 删除图片标签关联
     *
     * @param pictureId 图片标签关联主键
     * @return 结果
     */
    public int deletePictureTagRelInfoByPictureId(String pictureId);

    /**
     * 批量删除图片标签关联
     *
     * @param pictureIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureTagRelInfoByPictureIds(String[] pictureIds);
}
