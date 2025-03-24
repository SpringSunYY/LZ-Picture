package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureFavoriteInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户图片收藏Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureFavoriteInfoMapper extends BaseMapper<PictureFavoriteInfo>
{
    /**
     * 查询用户图片收藏
     *
     * @param favoriteId 用户图片收藏主键
     * @return 用户图片收藏
     */
    public PictureFavoriteInfo selectPictureFavoriteInfoByFavoriteId(String favoriteId);

    /**
     * 查询用户图片收藏列表
     *
     * @param pictureFavoriteInfo 用户图片收藏
     * @return 用户图片收藏集合
     */
    public List<PictureFavoriteInfo> selectPictureFavoriteInfoList(PictureFavoriteInfo pictureFavoriteInfo);

    /**
     * 新增用户图片收藏
     *
     * @param pictureFavoriteInfo 用户图片收藏
     * @return 结果
     */
    public int insertPictureFavoriteInfo(PictureFavoriteInfo pictureFavoriteInfo);

    /**
     * 修改用户图片收藏
     *
     * @param pictureFavoriteInfo 用户图片收藏
     * @return 结果
     */
    public int updatePictureFavoriteInfo(PictureFavoriteInfo pictureFavoriteInfo);

    /**
     * 删除用户图片收藏
     *
     * @param favoriteId 用户图片收藏主键
     * @return 结果
     */
    public int deletePictureFavoriteInfoByFavoriteId(String favoriteId);

    /**
     * 批量删除用户图片收藏
     *
     * @param favoriteIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureFavoriteInfoByFavoriteIds(String[] favoriteIds);
}
