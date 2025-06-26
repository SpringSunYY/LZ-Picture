package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.config.OssConfig;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.config.service.impl.ConfigInfoServiceImpl;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureFileLogAsyncFactory;
import com.lz.picture.mapper.PictureApplyInfoMapper;
import com.lz.picture.model.domain.PictureApplyInfo;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureApplyInfo.PictureApplyInfoQuery;
import com.lz.picture.model.enums.PPictureStatusEnum;
import com.lz.picture.model.enums.PictureApplyStatusEnum;
import com.lz.picture.model.vo.pictureApplyInfo.PictureApplyInfoVo;
import com.lz.picture.service.IPictureApplyInfoService;
import com.lz.picture.service.IPictureInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;
import static com.lz.common.constant.config.UserConfigKeyConstants.PICTURE_INDEX_P;

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

    @Resource
    private ConfigInfoServiceImpl configInfoService;

    //region mybatis代码

    /**
     * 查询图片申请信息
     *
     * @param applyId 图片申请信息主键
     * @return 图片申请信息
     */
    @Override
    public PictureApplyInfo selectPictureApplyInfoByApplyId(String applyId) {
        PictureApplyInfo pictureApplyInfo = pictureApplyInfoMapper.selectPictureApplyInfoByApplyId(applyId);
        //压缩图片
        String p = "?x-oss-process=image/resize,p_" + configInfoService.getConfigInfoInCache(PICTURE_INDEX_P);
        if (StringUtils.isNotNull(pictureApplyInfo)) {
            builderUrl(pictureApplyInfo, p);
        }
        return pictureApplyInfo;
    }

    /**
     * 查询图片申请信息列表
     *
     * @param pictureApplyInfo 图片申请信息
     * @return 图片申请信息
     */
    @Override
    public List<PictureApplyInfo> selectPictureApplyInfoList(PictureApplyInfo pictureApplyInfo) {
        List<PictureApplyInfo> pictureApplyInfos = pictureApplyInfoMapper.selectPictureApplyInfoList(pictureApplyInfo);
        //压缩图片
        String p = "?x-oss-process=image/resize,p_" + configInfoService.getConfigInfoInCache(PICTURE_INDEX_P);
        for (PictureApplyInfo info : pictureApplyInfos) {
            builderUrl(info, p);
        }
        return pictureApplyInfos;
    }

    private void builderUrl(PictureApplyInfo info, String p) {
        //构建url
        if (StringUtils.isNotEmpty(info.getApplyImage())) {
            String url = builderPictureUrl(info.getApplyImage(), p);
            info.setApplyImage(url);
        }
        if (StringUtils.isNotEmpty(info.getApplyFile())) {
            String url = builderUrl(info.getApplyFile());
            info.setApplyFile(url);
        }
        if (StringUtils.isNotEmpty(info.getThumbnailUrl())) {
            String url = ossConfig.builderUrl(info.getThumbnailUrl());
            info.setThumbnailUrl(url);
        }
    }

    private String builderPictureUrl(String urls, String p) {
        String[] split = urls.split(COMMON_SEPARATOR);
        StringBuilder buffer = new StringBuilder();
        for (String str : split) {
            buffer.append(ossConfig.builderUrl(str)).append(p).append(COMMON_SEPARATOR);
        }
        //删除尾部逗号
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    private String builderUrl(String urls) {
        String[] split = urls.split(COMMON_SEPARATOR);
        StringBuilder buffer = new StringBuilder();
        for (String str : split) {
            buffer.append(ossConfig.builderUrl(str)).append(COMMON_SEPARATOR);
        }
        //删除尾部逗号
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
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
        //获取数据库数据
        PictureApplyInfo db = selectPictureApplyInfoByApplyId(pictureApplyInfo.getApplyId());
        ThrowUtils.throwIf(StringUtils.isNull(db), "图片申请信息不存在");
        ThrowUtils.throwIf(db.getReviewStatus().equals(PictureApplyStatusEnum.PICTURE_APPLY_STATUS_1.getValue()), "图片申请信息已审核通过，请勿重复操作");
        //查询图片
        PictureInfo pictureInfo = pictureInfoService.selectNormalPictureInfoByPictureId(pictureApplyInfo.getPictureId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo), "图片不存在");
        ThrowUtils.throwIf(!pictureInfo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue()), "图片已发布，请勿重复操作");
        pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue());
        pictureInfoService.updatePictureInfo(pictureInfo);
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
        ThrowUtils.throwIf(pictureInfo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue()), "当前图片已经公开！！！");
        pictureApplyInfo.setThumbnailUrl(pictureInfo.getThumbnailUrl());
        pictureApplyInfo.setPictureName(pictureInfo.getName());
        //初始值
        pictureApplyInfo.setCreateTime(DateUtils.getNowDate());
        pictureApplyInfo.setReviewStatus(PictureApplyStatusEnum.PICTURE_APPLY_STATUS_0.getValue());
        //更新文件日志信息
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.updateNormalPictureApplyFileLog(pictureApplyInfo));
        return this.save(pictureApplyInfo) ? 1 : 0;
    }

}
