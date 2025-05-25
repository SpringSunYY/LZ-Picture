package com.lz.picture.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import com.lz.picture.model.dto.pictureDownloadLogInfo.UserPictureDownloadLogInfoQuery;
import com.lz.picture.model.vo.pictureDownloadLogInfo.PictureDownloadLogInfoVo;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 图片下载记录Service接口
 *
 * @author YY
 * @date 2025-05-24
 */
public interface IPictureDownloadLogInfoService extends IService<PictureDownloadLogInfo>
{
    //region mybatis代码
    /**
     * 查询图片下载记录
     *
     * @param downloadId 图片下载记录主键
     * @return 图片下载记录
     */
    public PictureDownloadLogInfo selectPictureDownloadLogInfoByDownloadId(String downloadId);

    /**
     * 查询图片下载记录列表
     *
     * @param pictureDownloadLogInfo 图片下载记录
     * @return 图片下载记录集合
     */
    public List<PictureDownloadLogInfo> selectPictureDownloadLogInfoList(PictureDownloadLogInfo pictureDownloadLogInfo);

    /**
     * 新增图片下载记录
     *
     * @param pictureDownloadLogInfo 图片下载记录
     * @return 结果
     */
    public int insertPictureDownloadLogInfo(PictureDownloadLogInfo pictureDownloadLogInfo);

    /**
     * 修改图片下载记录
     *
     * @param pictureDownloadLogInfo 图片下载记录
     * @return 结果
     */
    public int updatePictureDownloadLogInfo(PictureDownloadLogInfo pictureDownloadLogInfo);

    /**
     * 批量删除图片下载记录
     *
     * @param downloadIds 需要删除的图片下载记录主键集合
     * @return 结果
     */
    public int deletePictureDownloadLogInfoByDownloadIds(String[] downloadIds);

    /**
     * 删除图片下载记录信息
     *
     * @param downloadId 图片下载记录主键
     * @return 结果
     */
    public int deletePictureDownloadLogInfoByDownloadId(String downloadId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureDownloadLogInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureDownloadLogInfo> getQueryWrapper(PictureDownloadLogInfoQuery pictureDownloadLogInfoQuery);

    /**
     * 转换vo
     *
     * @param pictureDownloadLogInfoList PictureDownloadLogInfo集合
     * @return PictureDownloadLogInfoVO集合
     */
    List<PictureDownloadLogInfoVo> convertVoList(List<PictureDownloadLogInfo> pictureDownloadLogInfoList);

    /**
     * 用户自己查询下载记录列表
     * @author: YY
     * @method: selectUserPictureDownloadLogInfoList
     * @date: 2025/5/25 23:18
     * @param pictureDownloadLogInfo 查询条件
     * @return Page<PictureDownloadLogInfo>
     **/
    Page<PictureDownloadLogInfo> selectUserPictureDownloadLogInfoList(UserPictureDownloadLogInfoQuery pictureDownloadLogInfo);
}
