package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.vo.pictureInfo.PictureInfoVo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 图片信息Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureInfoService extends IService<PictureInfo>
{
    //region mybatis代码
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
     * 批量删除图片信息
     *
     * @param pictureIds 需要删除的图片信息主键集合
     * @return 结果
     */
    public int deletePictureInfoByPictureIds(String[] pictureIds);

    /**
     * 删除图片信息信息
     *
     * @param pictureId 图片信息主键
     * @return 结果
     */
    public int deletePictureInfoByPictureId(String pictureId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureInfo> getQueryWrapper(PictureInfoQuery pictureInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureInfoList PictureInfo集合
     * @return PictureInfoVO集合
     */
    List<PictureInfoVo> convertVoList(List<PictureInfo> pictureInfoList);
}
