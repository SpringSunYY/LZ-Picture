package com.lz.picture.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import com.lz.picture.model.dto.pictureDownloadLogInfo.UserPictureDownloadLogInfoQuery;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.PictureDownloadLogInfoMapper;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import com.lz.picture.service.IPictureDownloadLogInfoService;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoQuery;
import com.lz.picture.model.vo.pictureDownloadLogInfo.PictureDownloadLogInfoVo;

/**
 * 图片下载记录Service业务层处理
 *
 * @author YY
 * @date 2025-05-24
 */
@Service
public class PictureDownloadLogInfoServiceImpl extends ServiceImpl<PictureDownloadLogInfoMapper, PictureDownloadLogInfo> implements IPictureDownloadLogInfoService {
    @Resource
    private PictureDownloadLogInfoMapper pictureDownloadLogInfoMapper;

    //region mybatis代码

    /**
     * 查询图片下载记录
     *
     * @param downloadId 图片下载记录主键
     * @return 图片下载记录
     */
    @Override
    public PictureDownloadLogInfo selectPictureDownloadLogInfoByDownloadId(String downloadId) {
        return pictureDownloadLogInfoMapper.selectPictureDownloadLogInfoByDownloadId(downloadId);
    }

    /**
     * 查询图片下载记录列表
     *
     * @param pictureDownloadLogInfo 图片下载记录
     * @return 图片下载记录
     */
    @Override
    public List<PictureDownloadLogInfo> selectPictureDownloadLogInfoList(PictureDownloadLogInfo pictureDownloadLogInfo) {
        return pictureDownloadLogInfoMapper.selectPictureDownloadLogInfoList(pictureDownloadLogInfo);
    }

    /**
     * 新增图片下载记录
     *
     * @param pictureDownloadLogInfo 图片下载记录
     * @return 结果
     */
    @Override
    public int insertPictureDownloadLogInfo(PictureDownloadLogInfo pictureDownloadLogInfo) {
        pictureDownloadLogInfo.setCreateTime(DateUtils.getNowDate());
        return pictureDownloadLogInfoMapper.insertPictureDownloadLogInfo(pictureDownloadLogInfo);
    }

    /**
     * 修改图片下载记录
     *
     * @param pictureDownloadLogInfo 图片下载记录
     * @return 结果
     */
    @Override
    public int updatePictureDownloadLogInfo(PictureDownloadLogInfo pictureDownloadLogInfo) {
        return pictureDownloadLogInfoMapper.updatePictureDownloadLogInfo(pictureDownloadLogInfo);
    }

    /**
     * 批量删除图片下载记录
     *
     * @param downloadIds 需要删除的图片下载记录主键
     * @return 结果
     */
    @Override
    public int deletePictureDownloadLogInfoByDownloadIds(String[] downloadIds) {
        return pictureDownloadLogInfoMapper.deletePictureDownloadLogInfoByDownloadIds(downloadIds);
    }

    /**
     * 删除图片下载记录信息
     *
     * @param downloadId 图片下载记录主键
     * @return 结果
     */
    @Override
    public int deletePictureDownloadLogInfoByDownloadId(String downloadId) {
        return pictureDownloadLogInfoMapper.deletePictureDownloadLogInfoByDownloadId(downloadId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureDownloadLogInfo> getQueryWrapper(PictureDownloadLogInfoQuery pictureDownloadLogInfoQuery){
        QueryWrapper<PictureDownloadLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureDownloadLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String downloadId = pictureDownloadLogInfoQuery.getDownloadId();
        queryWrapper.eq(StringUtils.isNotEmpty(downloadId) ,"download_id",downloadId);

        String userId = pictureDownloadLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

        String pictureId = pictureDownloadLogInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId) ,"picture_id",pictureId);

        String categoryId = pictureDownloadLogInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId) ,"category_id",categoryId);

        String pictureName = pictureDownloadLogInfoQuery.getPictureName();
        queryWrapper.like(StringUtils.isNotEmpty(pictureName) ,"picture_name",pictureName);

        String tags = pictureDownloadLogInfoQuery.getTags();
        queryWrapper.eq(StringUtils.isNotEmpty(tags) ,"tags",tags);

        String spaceId = pictureDownloadLogInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId) ,"space_id",spaceId);

        Date createTime = pictureDownloadLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        String downloadStatus = pictureDownloadLogInfoQuery.getDownloadStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(downloadStatus) ,"download_status",downloadStatus);

        String downloadType = pictureDownloadLogInfoQuery.getDownloadType();
        queryWrapper.eq(StringUtils.isNotEmpty(downloadType) ,"download_type",downloadType);

        String referSource = pictureDownloadLogInfoQuery.getReferSource();
        queryWrapper.eq(StringUtils.isNotEmpty(referSource) ,"refer_source",referSource);

        String hasStatistics = pictureDownloadLogInfoQuery.getHasStatistics();
        queryWrapper.eq(StringUtils.isNotEmpty(hasStatistics) ,"has_statistics",hasStatistics);

        Double score = pictureDownloadLogInfoQuery.getScore();
        queryWrapper.eq( StringUtils.isNotNull(score),"score",score);

        String ipAddr = pictureDownloadLogInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr) ,"ip_addr",ipAddr);

        String ipAddress = pictureDownloadLogInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress) ,"ip_address",ipAddress);

        String deviceId = pictureDownloadLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId) ,"device_id",deviceId);

        String browser = pictureDownloadLogInfoQuery.getBrowser();
        queryWrapper.like(StringUtils.isNotEmpty(browser) ,"browser",browser);

        String os = pictureDownloadLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os) ,"os",os);

        String platform = pictureDownloadLogInfoQuery.getPlatform();
        queryWrapper.like(StringUtils.isNotEmpty(platform) ,"platform",platform);
        return queryWrapper;
    }

    @Override
    public List<PictureDownloadLogInfoVo> convertVoList(List<PictureDownloadLogInfo> pictureDownloadLogInfoList) {
        if (StringUtils.isEmpty(pictureDownloadLogInfoList)) {
            return Collections.emptyList();
        }
        return pictureDownloadLogInfoList.stream().map(PictureDownloadLogInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public Page<PictureDownloadLogInfo> selectUserPictureDownloadLogInfoList(UserPictureDownloadLogInfoQuery query) {
        //提取基础参数
        // 提取基础参数
        Integer pageNum = query.getPageNum();
        Integer pageSize = query.getPageSize();
        Map<String, Object> params = query.getParams();

        // 提取 beginCreateTime 和 endCreateTime（安全获取）
        String beginCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("beginCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);

        String endCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("endCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);
        return this.page(
                new Page<PictureDownloadLogInfo>(pageNum, pageSize),
                new LambdaQueryWrapper<PictureDownloadLogInfo>()
                        .eq(StringUtils.isNotEmpty(query.getUserId()), PictureDownloadLogInfo::getUserId, query.getUserId())
                        .like(StringUtils.isNotEmpty(query.getPictureName()), PictureDownloadLogInfo::getPictureName, query.getPictureName())
                        .eq(StringUtils.isNotEmpty(query.getDownloadStatus()), PictureDownloadLogInfo::getDownloadStatus, query.getDownloadStatus())
                        .eq(StringUtils.isNotEmpty(query.getDownloadType()), PictureDownloadLogInfo::getDownloadType, query.getDownloadType())
                        .eq(StringUtils.isNotEmpty(query.getReferSource()), PictureDownloadLogInfo::getReferSource, query.getReferSource())
                        .apply(StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                                "create_time between {0} and {1}",
                                beginCreateTime, endCreateTime)
                        .orderBy(StringUtils.isNotEmpty(query.getIsAsc()),
                                query.getIsAsc().equals("asc"),
                                PictureDownloadLogInfo::getCreateTime)
        );
    }

}
