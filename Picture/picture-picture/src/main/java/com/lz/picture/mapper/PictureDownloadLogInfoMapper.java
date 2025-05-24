package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 图片下载记录Mapper接口
 *
 * @author YY
 * @date 2025-05-24
 */
public interface PictureDownloadLogInfoMapper extends BaseMapper<PictureDownloadLogInfo>
{
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
     * 删除图片下载记录
     *
     * @param downloadId 图片下载记录主键
     * @return 结果
     */
    public int deletePictureDownloadLogInfoByDownloadId(String downloadId);

    /**
     * 批量删除图片下载记录
     *
     * @param downloadIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureDownloadLogInfoByDownloadIds(String[] downloadIds);
}
