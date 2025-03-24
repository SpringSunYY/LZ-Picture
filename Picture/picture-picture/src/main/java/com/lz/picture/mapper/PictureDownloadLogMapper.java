package com.lz.picture.mapper;

import java.util.List;
import com.lz.picture.model.domain.PictureDownloadLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 图片下载记录Mapper接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface PictureDownloadLogMapper extends BaseMapper<PictureDownloadLog>
{
    /**
     * 查询图片下载记录
     *
     * @param downloadId 图片下载记录主键
     * @return 图片下载记录
     */
    public PictureDownloadLog selectPictureDownloadLogByDownloadId(String downloadId);

    /**
     * 查询图片下载记录列表
     *
     * @param pictureDownloadLog 图片下载记录
     * @return 图片下载记录集合
     */
    public List<PictureDownloadLog> selectPictureDownloadLogList(PictureDownloadLog pictureDownloadLog);

    /**
     * 新增图片下载记录
     *
     * @param pictureDownloadLog 图片下载记录
     * @return 结果
     */
    public int insertPictureDownloadLog(PictureDownloadLog pictureDownloadLog);

    /**
     * 修改图片下载记录
     *
     * @param pictureDownloadLog 图片下载记录
     * @return 结果
     */
    public int updatePictureDownloadLog(PictureDownloadLog pictureDownloadLog);

    /**
     * 删除图片下载记录
     *
     * @param downloadId 图片下载记录主键
     * @return 结果
     */
    public int deletePictureDownloadLogByDownloadId(String downloadId);

    /**
     * 批量删除图片下载记录
     *
     * @param downloadIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePictureDownloadLogByDownloadIds(String[] downloadIds);
}
