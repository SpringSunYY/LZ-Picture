package com.lz.picture.service;

import java.util.List;
import com.lz.picture.model.domain.PictureDownloadLog;
import com.lz.picture.model.vo.pictureDownloadLog.PictureDownloadLogVo;
import com.lz.picture.model.dto.pictureDownloadLog.PictureDownloadLogQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 图片下载记录Service接口
 *
 * @author YY
 * @date 2025-03-24
 */
public interface IPictureDownloadLogService extends IService<PictureDownloadLog>
{
    //region mybatis代码
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
     * 批量删除图片下载记录
     *
     * @param downloadIds 需要删除的图片下载记录主键集合
     * @return 结果
     */
    public int deletePictureDownloadLogByDownloadIds(String[] downloadIds);

    /**
     * 删除图片下载记录信息
     *
     * @param downloadId 图片下载记录主键
     * @return 结果
     */
    public int deletePictureDownloadLogByDownloadId(String downloadId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param pictureDownloadLogQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PictureDownloadLog> getQueryWrapper(PictureDownloadLogQuery pictureDownloadLogQuery);

    /**
     * 转换vo
     *
     * @param pictureDownloadLogList PictureDownloadLog集合
     * @return PictureDownloadLogVO集合
     */
    List<PictureDownloadLogVo> convertVoList(List<PictureDownloadLog> pictureDownloadLogList);
}
