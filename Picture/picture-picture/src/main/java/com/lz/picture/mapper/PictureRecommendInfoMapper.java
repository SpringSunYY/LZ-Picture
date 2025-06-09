package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureRecommendInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户图片推荐模型Mapper接口
 *
 * @author YY
 * @date 2025-06-10
 */
public interface PictureRecommendInfoMapper extends BaseMapper<PictureRecommendInfo>
{
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
     * 删除用户图片推荐模型
     *
     * @param recommendId 用户图片推荐模型主键
     * @return 结果
     */
    public int deletePictureRecommendInfoByRecommendId(String recommendId);

    /**
     * 批量删除用户图片推荐模型
     *
     * @param recommendIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureRecommendInfoByRecommendIds(String[] recommendIds);
}
