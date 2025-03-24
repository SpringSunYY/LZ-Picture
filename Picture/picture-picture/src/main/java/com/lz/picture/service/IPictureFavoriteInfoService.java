package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureFavoriteInfo;
import com.lz.picture.model.vo.pictureFavoriteInfo.PictureFavoriteInfoVo;
import com.lz.picture.model.dto.pictureFavoriteInfo.PictureFavoriteInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户图片收藏Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureFavoriteInfoService extends IService<PictureFavoriteInfo>
{
    //region mybatis代码
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
     * 批量删除用户图片收藏
     *
     * @param favoriteIds 需要删除的用户图片收藏主键集合
     * @return 结果
     */
    public int deletePictureFavoriteInfoByFavoriteIds(String[] favoriteIds);

    /**
     * 删除用户图片收藏信息
     *
     * @param favoriteId 用户图片收藏主键
     * @return 结果
     */
    public int deletePictureFavoriteInfoByFavoriteId(String favoriteId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureFavoriteInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureFavoriteInfo> getQueryWrapper(PictureFavoriteInfoQuery pictureFavoriteInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureFavoriteInfoList PictureFavoriteInfo集合
     * @return PictureFavoriteInfoVO集合
     */
    List<PictureFavoriteInfoVo> convertVoList(List<PictureFavoriteInfo> pictureFavoriteInfoList);
}
