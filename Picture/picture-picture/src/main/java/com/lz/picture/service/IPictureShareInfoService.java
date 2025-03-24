package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureShareInfo;
import com.lz.picture.model.vo.pictureShareInfo.PictureShareInfoVo;
import com.lz.picture.model.dto.pictureShareInfo.PictureShareInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 图片转发记录Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureShareInfoService extends IService<PictureShareInfo>
{
    //region mybatis代码
    /**
     * 查询图片转发记录
     *
     * @param shareId 图片转发记录主键
     * @return 图片转发记录
     */
    public PictureShareInfo selectPictureShareInfoByShareId(String shareId);

    /**
     * 查询图片转发记录列表
     *
     * @param pictureShareInfo 图片转发记录
     * @return 图片转发记录集合
     */
    public List<PictureShareInfo> selectPictureShareInfoList(PictureShareInfo pictureShareInfo);

    /**
     * 新增图片转发记录
     *
     * @param pictureShareInfo 图片转发记录
     * @return 结果
     */
    public int insertPictureShareInfo(PictureShareInfo pictureShareInfo);

    /**
     * 修改图片转发记录
     *
     * @param pictureShareInfo 图片转发记录
     * @return 结果
     */
    public int updatePictureShareInfo(PictureShareInfo pictureShareInfo);

    /**
     * 批量删除图片转发记录
     *
     * @param shareIds 需要删除的图片转发记录主键集合
     * @return 结果
     */
    public int deletePictureShareInfoByShareIds(String[] shareIds);

    /**
     * 删除图片转发记录信息
     *
     * @param shareId 图片转发记录主键
     * @return 结果
     */
    public int deletePictureShareInfoByShareId(String shareId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureShareInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureShareInfo> getQueryWrapper(PictureShareInfoQuery pictureShareInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureShareInfoList PictureShareInfo集合
     * @return PictureShareInfoVO集合
     */
    List<PictureShareInfoVo> convertVoList(List<PictureShareInfo> pictureShareInfoList);
}
