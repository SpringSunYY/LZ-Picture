package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureRecommendInfo;
import com.lz.picture.model.vo.pictureRecommendInfo.PictureRecommendInfoVo;
import com.lz.picture.model.dto.pictureRecommendInfo.PictureRecommendInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户图片推荐模型Service接口
 *
 * @author YY
 * @date 2025-06-10
 */
public interface IPictureRecommendInfoService extends IService<PictureRecommendInfo>
{
    //region mybatis代码
    /**
     * 查询用户图片推荐模型
     *
     * @param recommendId 用户图片推荐模型主键
     * @return 用户图片推荐模型
     */
    public PictureRecommendInfo selectPictureRecommendInfoByRecommendId(String recommendId);

    /**
     * 查询用户图片推荐模型列表
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 用户图片推荐模型集合
     */
    public List<PictureRecommendInfo> selectPictureRecommendInfoList(PictureRecommendInfo pictureRecommendInfo);

    /**
     * 新增用户图片推荐模型
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 结果
     */
    public int insertPictureRecommendInfo(PictureRecommendInfo pictureRecommendInfo);

    /**
     * 修改用户图片推荐模型
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 结果
     */
    public int updatePictureRecommendInfo(PictureRecommendInfo pictureRecommendInfo);

    /**
     * 批量删除用户图片推荐模型
     *
     * @param recommendIds 需要删除的用户图片推荐模型主键集合
     * @return 结果
     */
    public int deletePictureRecommendInfoByRecommendIds(String[] recommendIds);

    /**
     * 删除用户图片推荐模型信息
     *
     * @param recommendId 用户图片推荐模型主键
     * @return 结果
     */
    public int deletePictureRecommendInfoByRecommendId(String recommendId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureRecommendInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureRecommendInfo> getQueryWrapper(PictureRecommendInfoQuery pictureRecommendInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureRecommendInfoList PictureRecommendInfo集合
     * @return PictureRecommendInfoVO集合
     */
    List<PictureRecommendInfoVo> convertVoList(List<PictureRecommendInfo> pictureRecommendInfoList);
}
