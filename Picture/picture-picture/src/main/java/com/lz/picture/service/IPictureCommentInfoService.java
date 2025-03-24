package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureCommentInfo;
import com.lz.picture.model.vo.pictureCommentInfo.PictureCommentInfoVo;
import com.lz.picture.model.dto.pictureCommentInfo.PictureCommentInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 图片评论Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureCommentInfoService extends IService<PictureCommentInfo>
{
    //region mybatis代码
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
     * 批量删除图片评论
     *
     * @param commentIds 需要删除的图片评论主键集合
     * @return 结果
     */
    public int deletePictureCommentInfoByCommentIds(String[] commentIds);

    /**
     * 删除图片评论信息
     *
     * @param commentId 图片评论主键
     * @return 结果
     */
    public int deletePictureCommentInfoByCommentId(String commentId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureCommentInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureCommentInfo> getQueryWrapper(PictureCommentInfoQuery pictureCommentInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureCommentInfoList PictureCommentInfo集合
     * @return PictureCommentInfoVO集合
     */
    List<PictureCommentInfoVo> convertVoList(List<PictureCommentInfo> pictureCommentInfoList);
}
