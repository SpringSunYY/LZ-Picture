package com.lz.picture.service;

import java.util.List;

import com.lz.picture.model.domain.PictureTagRelInfo;
import com.lz.picture.model.vo.pictureTagRelInfo.PictureTagRelInfoVo;
import com.lz.picture.model.dto.pictureTagRelInfo.PictureTagRelInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 图片标签关联Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureTagRelInfoService extends IService<PictureTagRelInfo> {
    //region mybatis代码

    /**
     * 查询图片标签关联
     *
     * @param relId 图片标签关联主键
     * @return 图片标签关联
     */
    public PictureTagRelInfo selectPictureTagRelInfoByRelId(String relId);

    /**
     * 查询图片标签关联列表
     *
     * @param pictureTagRelInfo 图片标签关联
     * @return 图片标签关联集合
     */
    public List<PictureTagRelInfo> selectPictureTagRelInfoList(PictureTagRelInfo pictureTagRelInfo);

    /**
     * 新增图片标签关联
     *
     * @param pictureTagRelInfo 图片标签关联
     * @return 结果
     */
    public int insertPictureTagRelInfo(PictureTagRelInfo pictureTagRelInfo);

    /**
     * 修改图片标签关联
     *
     * @param pictureTagRelInfo 图片标签关联
     * @return 结果
     */
    public int updatePictureTagRelInfo(PictureTagRelInfo pictureTagRelInfo);

    /**
     * 批量删除图片标签关联
     *
     * @param relIds 需要删除的图片标签关联主键集合
     * @return 结果
     */
    public int deletePictureTagRelInfoByRelIds(String[] relIds);

    /**
     * 删除图片标签关联信息
     *
     * @param relId 图片标签关联主键
     * @return 结果
     */
    public int deletePictureTagRelInfoByRelId(String relId);
    //endregion

    /**
     * 获取查询条件
     *
     * @param pictureTagRelInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureTagRelInfo> getQueryWrapper(PictureTagRelInfoQuery pictureTagRelInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureTagRelInfoList PictureTagRelInfo集合
     * @return PictureTagRelInfoVO集合
     */
    List<PictureTagRelInfoVo> convertVoList(List<PictureTagRelInfo> pictureTagRelInfoList);

    /**
     * 查询到该图片关联的标签名称
     *
     * @param pictureId 图片编号
     * @return List<String>
     * @author: YY
     * @method: getPictureTagNames
     * @date: 2025/5/25 19:56
     **/
    public List<String> getPictureTagNames(String pictureId);

    /**
     * 查询到该图片关联的标签名称
     *
     * @param pictureId 图片编号
     * @return String
     * @author: YY
     * @method: getPictureTagNamesStr
     * @date: 2025/5/25 20:04
     **/
    public String getPictureTagNamesStr(String pictureId);

    /**
     * 删除图片关联信息
     *
     * @param pictureId 图片编号
     * @return 删除结果
     */
    int deletePictureTagRelInfoByPictureId(String pictureId);
}
