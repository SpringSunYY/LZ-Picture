package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureTagInfo;
import com.lz.picture.model.vo.pictureTagInfo.PictureTagInfoVo;
import com.lz.picture.model.dto.pictureTagInfo.PictureTagInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 图片标签信息Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureTagInfoService extends IService<PictureTagInfo>
{
    //region mybatis代码
    /**
     * 查询图片标签信息
     *
     * @param tagId 图片标签信息主键
     * @return 图片标签信息
     */
    public PictureTagInfo selectPictureTagInfoByTagId(String tagId);

    /**
     * 查询图片标签信息列表
     *
     * @param pictureTagInfo 图片标签信息
     * @return 图片标签信息集合
     */
    public List<PictureTagInfo> selectPictureTagInfoList(PictureTagInfo pictureTagInfo);

    /**
     * 新增图片标签信息
     *
     * @param pictureTagInfo 图片标签信息
     * @return 结果
     */
    public int insertPictureTagInfo(PictureTagInfo pictureTagInfo);

    /**
     * 修改图片标签信息
     *
     * @param pictureTagInfo 图片标签信息
     * @return 结果
     */
    public int updatePictureTagInfo(PictureTagInfo pictureTagInfo);

    /**
     * 批量删除图片标签信息
     *
     * @param tagIds 需要删除的图片标签信息主键集合
     * @return 结果
     */
    public int deletePictureTagInfoByTagIds(String[] tagIds);

    /**
     * 删除图片标签信息信息
     *
     * @param tagId 图片标签信息主键
     * @return 结果
     */
    public int deletePictureTagInfoByTagId(String tagId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureTagInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureTagInfo> getQueryWrapper(PictureTagInfoQuery pictureTagInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureTagInfoList PictureTagInfo集合
     * @return PictureTagInfoVO集合
     */
    List<PictureTagInfoVo> convertVoList(List<PictureTagInfo> pictureTagInfoList);
}
