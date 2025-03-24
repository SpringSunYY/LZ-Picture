package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureLikeInfo;
import com.lz.picture.model.vo.pictureLikeInfo.PictureLikeInfoVo;
import com.lz.picture.model.dto.pictureLikeInfo.PictureLikeInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 图片点赞记录Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureLikeInfoService extends IService<PictureLikeInfo>
{
    //region mybatis代码
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
     * 批量删除图片点赞记录
     *
     * @param likeIds 需要删除的图片点赞记录主键集合
     * @return 结果
     */
    public int deletePictureLikeInfoByLikeIds(String[] likeIds);

    /**
     * 删除图片点赞记录信息
     *
     * @param likeId 图片点赞记录主键
     * @return 结果
     */
    public int deletePictureLikeInfoByLikeId(String likeId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureLikeInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureLikeInfo> getQueryWrapper(PictureLikeInfoQuery pictureLikeInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureLikeInfoList PictureLikeInfo集合
     * @return PictureLikeInfoVO集合
     */
    List<PictureLikeInfoVo> convertVoList(List<PictureLikeInfo> pictureLikeInfoList);
}
