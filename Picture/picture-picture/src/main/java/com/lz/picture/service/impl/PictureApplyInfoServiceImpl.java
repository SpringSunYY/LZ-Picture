package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.config.OssConfig;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.picture.mapper.PictureApplyInfoMapper;
import com.lz.picture.model.domain.PictureApplyInfo;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureApplyInfo.PictureApplyInfoQuery;
import com.lz.picture.model.enums.PictureApplyStatusEnum;
import com.lz.picture.model.vo.pictureApplyInfo.PictureApplyInfoVo;
import com.lz.picture.service.IPictureApplyInfoService;
import com.lz.picture.service.IPictureInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 图片申请信息Service业务层处理
 *
 * @author YY
 * @date 2025-06-17
 */
@Service
public class PictureApplyInfoServiceImpl extends ServiceImpl<PictureApplyInfoMapper, PictureApplyInfo> implements IPictureApplyInfoService {
    @Resource
    private PictureApplyInfoMapper pictureApplyInfoMapper;

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private OssConfig ossConfig;

    //region mybatis代码

    /**
     * 查询图片申请信息
     *
     * @param applyId 图片申请信息主键
     * @return 图片申请信息
     */
    @Override
    public PictureApplyInfo selectPictureApplyInfoByApplyId(String applyId) {
        return pictureApplyInfoMapper.selectPictureApplyInfoByApplyId(applyId);
    }

    /**
     * 查询图片申请信息列表
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 图片申请信息
     */
    @Override
    public List<PictureApplyInfo> selectPictureApplyInfoList(PictureApplyInfo pictureApplyInfo) {
        return pictureApplyInfoMapper.selectPictureApplyInfoList(pictureApplyInfo);
    }

    /**
     * 新增图片申请信息
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 结果
     */
    @Override
    public int insertPictureApplyInfo(PictureApplyInfo pictureApplyInfo) {
        pictureApplyInfo.setCreateTime(DateUtils.getNowDate());
        return pictureApplyInfoMapper.insertPictureApplyInfo(pictureApplyInfo);
    }

    /**
     * 修改图片申请信息
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 结果
     */
    @Override
    public int updatePictureApplyInfo(PictureApplyInfo pictureApplyInfo) {
        pictureApplyInfo.setUpdateTime(DateUtils.getNowDate());
        return pictureApplyInfoMapper.updatePictureApplyInfo(pictureApplyInfo);
    }

    /**
     * 批量删除图片申请信息
     *
     * @param applyIds 需要删除的图片申请信息主键
     * @return 结果
     */
    @Override
    public int deletePictureApplyInfoByApplyIds(String[] applyIds) {
        return pictureApplyInfoMapper.deletePictureApplyInfoByApplyIds(applyIds);
    }

    /**
     * 删除图片申请信息信息
     *
     * @param applyId 图片申请信息主键
     * @return 结果
     */
    @Override
    public int deletePictureApplyInfoByApplyId(String applyId) {
        return pictureApplyInfoMapper.deletePictureApplyInfoByApplyId(applyId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureApplyInfo> getQueryWrapper(PictureApplyInfoQuery pictureApplyInfoQuery) {
        QueryWrapper<PictureApplyInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureApplyInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String applyId = pictureApplyInfoQuery.getApplyId();
        queryWrapper.eq(StringUtils.isNotEmpty(applyId), "apply_id", applyId);

        String pictureId = pictureApplyInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId), "picture_id", pictureId);

        String pictureName = pictureApplyInfoQuery.getPictureName();
        queryWrapper.like(StringUtils.isNotEmpty(pictureName), "picture_name", pictureName);

        String applyType = pictureApplyInfoQuery.getApplyType();
        queryWrapper.eq(StringUtils.isNotEmpty(applyType), "apply_type", applyType);

        String userId = pictureApplyInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = pictureApplyInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = pictureApplyInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String reviewStatus = pictureApplyInfoQuery.getReviewStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(reviewStatus), "review_status", reviewStatus);

        Long reviewUserId = pictureApplyInfoQuery.getReviewUserId();
        queryWrapper.eq(StringUtils.isNotNull(reviewUserId), "review_user_id", reviewUserId);

        Date reviewTime = pictureApplyInfoQuery.getReviewTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginReviewTime")) && StringUtils.isNotNull(params.get("endReviewTime")), "review_time", params.get("beginReviewTime"), params.get("endReviewTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureApplyInfoVo> convertVoList(List<PictureApplyInfo> pictureApplyInfoList) {
        if (StringUtils.isEmpty(pictureApplyInfoList)) {
            return Collections.emptyList();
        }
        return pictureApplyInfoList.stream().map(PictureApplyInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public int userInsertPictureApplyInfo(PictureApplyInfo pictureApplyInfo) {
        //如果传过来的积分不为空，判断是否为10的倍数或者0
        ThrowUtils.throwIf(StringUtils.isNotNull(pictureApplyInfo.getPointsNeed())
                && pictureApplyInfo.getPointsNeed() % 10 != 0
                || pictureApplyInfo.getPointsNeed() < 0, "积分必须是10的倍数或者0");
        //判断图片是否存在，获取缩略图url
        PictureInfo pictureInfo = pictureInfoService.selectNormalPictureInfoByPictureId(pictureApplyInfo.getPictureId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo) || !pictureApplyInfo.getUserId().equals(pictureInfo.getUserId()), "图片不存在");
        String url = ossConfig.builderUrl(pictureInfo.getThumbnailUrl(), ossConfig.getDnsUrl());
        pictureApplyInfo.setThumbnailUrl(url);
        pictureApplyInfo.setPictureName(pictureInfo.getName());
        //初始值
        pictureApplyInfo.setCreateTime(DateUtils.getNowDate());
        pictureApplyInfo.setReviewStatus(PictureApplyStatusEnum.PICTURE_APPLY_STATUS_0.getValue());

        return this.save(pictureApplyInfo) ? 1 : 0;
    }

}
