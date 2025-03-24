package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureLikeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 图片点赞记录Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureLikeInfoMapper extends BaseMapper<PictureLikeInfo>
{
    /**
     * 查询图片点赞记录
     *
     * @param likeId 图片点赞记录主键
     * @return 图片点赞记录
     */
    public PictureLikeInfo selectPictureLikeInfoByLikeId(String likeId);

    /**
     * 查询图片点赞记录列表
     *
     * @param pictureLikeInfo 图片点赞记录
     * @return 图片点赞记录集合
     */
    public List<PictureLikeInfo> selectPictureLikeInfoList(PictureLikeInfo pictureLikeInfo);

    /**
     * 新增图片点赞记录
     *
     * @param pictureLikeInfo 图片点赞记录
     * @return 结果
     */
    public int insertPictureLikeInfo(PictureLikeInfo pictureLikeInfo);

    /**
     * 修改图片点赞记录
     *
     * @param pictureLikeInfo 图片点赞记录
     * @return 结果
     */
    public int updatePictureLikeInfo(PictureLikeInfo pictureLikeInfo);

    /**
     * 删除图片点赞记录
     *
     * @param likeId 图片点赞记录主键
     * @return 结果
     */
    public int deletePictureLikeInfoByLikeId(String likeId);

    /**
     * 批量删除图片点赞记录
     *
     * @param likeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureLikeInfoByLikeIds(String[] likeIds);
}
