package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureShareInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 图片转发记录Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureShareInfoMapper extends BaseMapper<PictureShareInfo>
{
    /**
     * 查询图片转发记录
     *
     * @param shareId 图片转发记录主键
     * @return 图片转发记录
     */
    public PictureShareInfo selectPictureShareInfoByShareId(String shareId);

    /**
     * 查询图片转发记录列表
     *
     * @param pictureShareInfo 图片转发记录
     * @return 图片转发记录集合
     */
    public List<PictureShareInfo> selectPictureShareInfoList(PictureShareInfo pictureShareInfo);

    /**
     * 新增图片转发记录
     *
     * @param pictureShareInfo 图片转发记录
     * @return 结果
     */
    public int insertPictureShareInfo(PictureShareInfo pictureShareInfo);

    /**
     * 修改图片转发记录
     *
     * @param pictureShareInfo 图片转发记录
     * @return 结果
     */
    public int updatePictureShareInfo(PictureShareInfo pictureShareInfo);

    /**
     * 删除图片转发记录
     *
     * @param shareId 图片转发记录主键
     * @return 结果
     */
    public int deletePictureShareInfoByShareId(String shareId);

    /**
     * 批量删除图片转发记录
     *
     * @param shareIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureShareInfoByShareIds(String[] shareIds);
}
