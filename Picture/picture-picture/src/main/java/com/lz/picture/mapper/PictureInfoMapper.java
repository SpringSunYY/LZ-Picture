package com.lz.picture.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoRecommendRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 图片信息Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureInfoMapper extends BaseMapper<PictureInfo> {
    /**
     * 查询图片信息
     *
     * @param pictureId 图片信息主键
     * @return 图片信息
     */
    public PictureInfo selectPictureInfoByPictureId(String pictureId);

    /**
     * 查询图片信息列表
     *
     * @param pictureInfo 图片信息
     * @return 图片信息集合
     */
    public List<PictureInfo> selectPictureInfoList(PictureInfo pictureInfo);

    /**
     * 新增图片信息
     *
     * @param pictureInfo 图片信息
     * @return 结果
     */
    public int insertPictureInfo(PictureInfo pictureInfo);

    /**
     * 修改图片信息
     *
     * @param pictureInfo 图片信息
     * @return 结果
     */
    public int updatePictureInfo(PictureInfo pictureInfo);

    /**
     * 删除图片信息
     *
     * @param pictureId 图片信息主键
     * @return 结果
     */
    public int deletePictureInfoByPictureId(String pictureId);

    /**
     * 批量删除图片信息
     *
     * @param pictureIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureInfoByPictureIds(String[] pictureIds);

    /**
     * 获取图片详情推荐
     *
     * @param pictureInfoRecommendRequest
     * @return List<PictureInfo>
     * @author: YY
     * @method: getPictureInfoDetailRecommend
     * @date: 2025/6/5 23:58
     **/
    List<PictureInfo> getPictureInfoDetailRecommend(PictureInfoRecommendRequest pictureInfoRecommendRequest);

}
