package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureTagInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 图片标签信息Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureTagInfoMapper extends BaseMapper<PictureTagInfo>
{
    /**
     * 查询图片标签信息
     *
     * @param tagId 图片标签信息主键
     * @return 图片标签信息
     */
    public PictureTagInfo selectPictureTagInfoByTagId(String tagId);

    /**
     * 查询图片标签信息列表
     *
     * @param pictureTagInfo 图片标签信息
     * @return 图片标签信息集合
     */
    public List<PictureTagInfo> selectPictureTagInfoList(PictureTagInfo pictureTagInfo);

    /**
     * 新增图片标签信息
     *
     * @param pictureTagInfo 图片标签信息
     * @return 结果
     */
    public int insertPictureTagInfo(PictureTagInfo pictureTagInfo);

    /**
     * 修改图片标签信息
     *
     * @param pictureTagInfo 图片标签信息
     * @return 结果
     */
    public int updatePictureTagInfo(PictureTagInfo pictureTagInfo);

    /**
     * 删除图片标签信息
     *
     * @param tagId 图片标签信息主键
     * @return 结果
     */
    public int deletePictureTagInfoByTagId(String tagId);

    /**
     * 批量删除图片标签信息
     *
     * @param tagIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureTagInfoByTagIds(String[] tagIds);
}
