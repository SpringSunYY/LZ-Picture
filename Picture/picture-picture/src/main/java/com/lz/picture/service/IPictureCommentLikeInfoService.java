package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureCommentLikeInfo;
import com.lz.picture.model.vo.pictureCommentLikeInfo.PictureCommentLikeInfoVo;
import com.lz.picture.model.dto.pictureCommentLikeInfo.PictureCommentLikeInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 评论点赞记录Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureCommentLikeInfoService extends IService<PictureCommentLikeInfo>
{
    //region mybatis代码
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
     * 批量删除评论点赞记录
     *
     * @param likeIds 需要删除的评论点赞记录主键集合
     * @return 结果
     */
    public int deletePictureCommentLikeInfoByLikeIds(String[] likeIds);

    /**
     * 删除评论点赞记录信息
     *
     * @param likeId 评论点赞记录主键
     * @return 结果
     */
    public int deletePictureCommentLikeInfoByLikeId(String likeId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureCommentLikeInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureCommentLikeInfo> getQueryWrapper(PictureCommentLikeInfoQuery pictureCommentLikeInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureCommentLikeInfoList PictureCommentLikeInfo集合
     * @return PictureCommentLikeInfoVO集合
     */
    List<PictureCommentLikeInfoVo> convertVoList(List<PictureCommentLikeInfo> pictureCommentLikeInfoList);
}
