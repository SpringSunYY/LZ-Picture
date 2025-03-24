package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureCommentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 图片评论Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureCommentInfoMapper extends BaseMapper<PictureCommentInfo>
{
    /**
     * 查询图片评论
     *
     * @param commentId 图片评论主键
     * @return 图片评论
     */
    public PictureCommentInfo selectPictureCommentInfoByCommentId(String commentId);

    /**
     * 查询图片评论列表
     *
     * @param pictureCommentInfo 图片评论
     * @return 图片评论集合
     */
    public List<PictureCommentInfo> selectPictureCommentInfoList(PictureCommentInfo pictureCommentInfo);

    /**
     * 新增图片评论
     *
     * @param pictureCommentInfo 图片评论
     * @return 结果
     */
    public int insertPictureCommentInfo(PictureCommentInfo pictureCommentInfo);

    /**
     * 修改图片评论
     *
     * @param pictureCommentInfo 图片评论
     * @return 结果
     */
    public int updatePictureCommentInfo(PictureCommentInfo pictureCommentInfo);

    /**
     * 删除图片评论
     *
     * @param commentId 图片评论主键
     * @return 结果
     */
    public int deletePictureCommentInfoByCommentId(String commentId);

    /**
     * 批量删除图片评论
     *
     * @param commentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureCommentInfoByCommentIds(String[] commentIds);
}
