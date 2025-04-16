package com.lz.picture.service;

import java.util.List;

import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.vo.pictureInfo.PictureInfoVo;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.model.vo.pictureInfo.UserPictureDetailInfoVo;

/**
 * 图片信息Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureInfoService extends IService<PictureInfo> {
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
     * @param pictureInfo 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureInfo> getQueryWrapper(PictureInfo pictureInfo);

    /**
     * 转换vo
     *
     * @param pictureInfoList PictureInfo集合
     * @return PictureInfoVO集合
     */
    List<PictureInfoVo> convertVoList(List<PictureInfo> pictureInfoList);

    /**
     * 用户新增图片信息
     *
     * @param pictureInfo
     * @return
     */
    int userInsertPictureInfo(PictureInfo pictureInfo);

    /**
     * description: 查询图片详细信息，包括空间、用户、标签、文件夹
     * author: YY
     * method: userSelectPictureInfoByPictureId
     * date: 2025/4/11 15:47
     * param:
     * param: pictureId
     * return: com.lz.picture.model.vo.pictureInfo.UserPictureDetailInfoVo
     **/
    UserPictureDetailInfoVo userSelectPictureInfoByPictureId(String pictureId,String userId);

    /**
     * description: 重新设置缓存
     * author: YY
     * method: resetPictureInfoCache
     * date: 2025/4/15 00:04
     * param:
     * param: pictureId 图片id
     * return: void
     **/
    void resetPictureInfoCache(String pictureId);
}
