package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureCommentLikeInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 评论点赞记录Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureCommentLikeInfoMapper extends BaseMapper<PictureCommentLikeInfo>
{
    /**
     * 查询评论点赞记录
     *
     * @param likeId 评论点赞记录主键
     * @return 评论点赞记录
     */
    public PictureCommentLikeInfo selectPictureCommentLikeInfoByLikeId(String likeId);

    /**
     * 查询评论点赞记录列表
     *
     * @param pictureCommentLikeInfo 评论点赞记录
     * @return 评论点赞记录集合
     */
    public List<PictureCommentLikeInfo> selectPictureCommentLikeInfoList(PictureCommentLikeInfo pictureCommentLikeInfo);

    /**
     * 新增评论点赞记录
     *
     * @param pictureCommentLikeInfo 评论点赞记录
     * @return 结果
     */
    public int insertPictureCommentLikeInfo(PictureCommentLikeInfo pictureCommentLikeInfo);

    /**
     * 修改评论点赞记录
     *
     * @param pictureCommentLikeInfo 评论点赞记录
     * @return 结果
     */
    public int updatePictureCommentLikeInfo(PictureCommentLikeInfo pictureCommentLikeInfo);

    /**
     * 删除评论点赞记录
     *
     * @param likeId 评论点赞记录主键
     * @return 结果
     */
    public int deletePictureCommentLikeInfoByLikeId(String likeId);

    /**
     * 批量删除评论点赞记录
     *
     * @param likeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureCommentLikeInfoByLikeIds(String[] likeIds);
}
