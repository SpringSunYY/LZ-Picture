package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.PictureDownloadLogMapper;
import com.lz.picture.model.domain.PictureDownloadLog;
import com.lz.picture.service.IPictureDownloadLogService;
import com.lz.picture.model.dto.pictureDownloadLog.PictureDownloadLogQuery;
import com.lz.picture.model.vo.pictureDownloadLog.PictureDownloadLogVo;

/**
 * 图片下载记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureDownloadLogServiceImpl extends ServiceImpl<PictureDownloadLogMapper, PictureDownloadLog> implements IPictureDownloadLogService
{
    @Resource
    private PictureDownloadLogMapper pictureDownloadLogMapper;

    //region mybatis代码
    /**
     * 查询图片下载记录
     *
     * @param downloadId 图片下载记录主键
     * @return 图片下载记录
     */
    @Override
    public PictureDownloadLog selectPictureDownloadLogByDownloadId(String downloadId)
    {
        return pictureDownloadLogMapper.selectPictureDownloadLogByDownloadId(downloadId);
    }

    /**
     * 查询图片下载记录列表
     *
     * @param pictureDownloadLog 图片下载记录
     * @return 图片下载记录
     */
    @Override
    public List<PictureDownloadLog> selectPictureDownloadLogList(PictureDownloadLog pictureDownloadLog)
    {
        return pictureDownloadLogMapper.selectPictureDownloadLogList(pictureDownloadLog);
    }

    /**
     * 新增图片下载记录
     *
     * @param pictureDownloadLog 图片下载记录
     * @return 结果
     */
    @Override
    public int insertPictureDownloadLog(PictureDownloadLog pictureDownloadLog)
    {
        pictureDownloadLog.setCreateTime(DateUtils.getNowDate());
        return pictureDownloadLogMapper.insertPictureDownloadLog(pictureDownloadLog);
    }

    /**
     * 修改图片下载记录
     *
     * @param pictureDownloadLog 图片下载记录
     * @return 结果
     */
    @Override
    public int updatePictureDownloadLog(PictureDownloadLog pictureDownloadLog)
    {
        return pictureDownloadLogMapper.updatePictureDownloadLog(pictureDownloadLog);
    }

    /**
     * 批量删除图片下载记录
     *
     * @param downloadIds 需要删除的图片下载记录主键
     * @return 结果
     */
    @Override
    public int deletePictureDownloadLogByDownloadIds(String[] downloadIds)
    {
        return pictureDownloadLogMapper.deletePictureDownloadLogByDownloadIds(downloadIds);
    }

    /**
     * 删除图片下载记录信息
     *
     * @param downloadId 图片下载记录主键
     * @return 结果
     */
    @Override
    public int deletePictureDownloadLogByDownloadId(String downloadId)
    {
        return pictureDownloadLogMapper.deletePictureDownloadLogByDownloadId(downloadId);
    }
    //endregion
    @Override
    public QueryWrapper<PictureDownloadLog> getQueryWrapper(PictureDownloadLogQuery pictureDownloadLogQuery){
        QueryWrapper<PictureDownloadLog> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureDownloadLogQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String downloadId = pictureDownloadLogQuery.getDownloadId();
        queryWrapper.eq(StringUtils.isNotEmpty(downloadId) ,"download_id",downloadId);

    String userId = pictureDownloadLogQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String pictureId = pictureDownloadLogQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId) ,"picture_id",pictureId);

    String categoryId = pictureDownloadLogQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId) ,"category_id",categoryId);

    String tags = pictureDownloadLogQuery.getTags();
        queryWrapper.like(StringUtils.isNotEmpty(tags) ,"tags",tags);

    String spaceId = pictureDownloadLogQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId) ,"space_id",spaceId);

    String downloadIp = pictureDownloadLogQuery.getDownloadIp();
        queryWrapper.like(StringUtils.isNotEmpty(downloadIp) ,"download_ip",downloadIp);

    String deviceId = pictureDownloadLogQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId) ,"device_id",deviceId);

    String browser = pictureDownloadLogQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser) ,"browser",browser);

    String os = pictureDownloadLogQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os) ,"os",os);

    String platform = pictureDownloadLogQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform) ,"platform",platform);

    Long pointsCost = pictureDownloadLogQuery.getPointsCost();
        queryWrapper.eq( StringUtils.isNotNull(pointsCost),"points_cost",pointsCost);

    String isFree = pictureDownloadLogQuery.getIsFree();
        queryWrapper.eq(StringUtils.isNotEmpty(isFree) ,"is_free",isFree);

    Long proportion = pictureDownloadLogQuery.getProportion();
        queryWrapper.eq( StringUtils.isNotNull(proportion),"proportion",proportion);

    Date createTime = pictureDownloadLogQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    String downloadStatus = pictureDownloadLogQuery.getDownloadStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(downloadStatus) ,"download_status",downloadStatus);

    String downloadType = pictureDownloadLogQuery.getDownloadType();
        queryWrapper.eq(StringUtils.isNotEmpty(downloadType) ,"download_type",downloadType);

    String referSource = pictureDownloadLogQuery.getReferSource();
        queryWrapper.eq(StringUtils.isNotEmpty(referSource) ,"refer_source",referSource);

        return queryWrapper;
    }

    @Override
    public List<PictureDownloadLogVo> convertVoList(List<PictureDownloadLog> pictureDownloadLogList) {
        if (StringUtils.isEmpty(pictureDownloadLogList)) {
            return Collections.emptyList();
        }
        return pictureDownloadLogList.stream().map(PictureDownloadLogVo::objToVo).collect(Collectors.toList());
    }

}
