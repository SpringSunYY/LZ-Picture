package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.StringUtils;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.model.domain.PictureTagInfo;
import com.lz.picture.model.domain.SpaceFolderInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.enums.PPictureReviewStatus;
import com.lz.picture.model.enums.PSpaceStatus;
import com.lz.picture.model.enums.PSpaceType;
import com.lz.picture.model.enums.PTagStatus;
import com.lz.picture.service.IPictureTagInfoService;
import com.lz.picture.service.ISpaceFolderInfoService;
import com.lz.picture.service.ISpaceInfoService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.PictureInfoMapper;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.model.dto.pictureInfo.PictureInfoQuery;
import com.lz.picture.model.vo.pictureInfo.PictureInfoVo;
import org.springframework.transaction.support.TransactionTemplate;

import static com.lz.common.constant.config.ConfigKeyConstants.PICTURE_POINTS_MAX;
import static com.lz.common.constant.config.ConfigKeyConstants.PICTURE_POINTS_MIN;
import static com.lz.common.utils.SecurityUtils.getLoginUser;

/**
 * 图片信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureInfoServiceImpl extends ServiceImpl<PictureInfoMapper, PictureInfo> implements IPictureInfoService {
    @Resource
    private PictureInfoMapper pictureInfoMapper;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    @Lazy
    private ISpaceFolderInfoService spaceFolderInfoService;

    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    @Resource
    private IConfigInfoService configInfoService;

    @Resource
    private TransactionTemplate transactionTemplate;

    //region mybatis代码

    /**
     * 查询图片信息
     *
     * @param pictureId 图片信息主键
     * @return 图片信息
     */
    @Override
    public PictureInfo selectPictureInfoByPictureId(String pictureId) {
        return pictureInfoMapper.selectPictureInfoByPictureId(pictureId);
    }

    /**
     * 查询图片信息列表
     *
     * @param pictureInfo 图片信息
     * @return 图片信息
     */
    @Override
    public List<PictureInfo> selectPictureInfoList(PictureInfo pictureInfo) {
        return pictureInfoMapper.selectPictureInfoList(pictureInfo);
    }

    /**
     * 新增图片信息
     *
     * @param pictureInfo 图片信息
     * @return 结果
     */
    @Override
    public int insertPictureInfo(PictureInfo pictureInfo) {
        pictureInfo.setCreateTime(DateUtils.getNowDate());
        return pictureInfoMapper.insertPictureInfo(pictureInfo);
    }

    /**
     * 修改图片信息
     *
     * @param pictureInfo 图片信息
     * @return 结果
     */
    @Override
    public int updatePictureInfo(PictureInfo pictureInfo) {
        pictureInfo.setUpdateTime(DateUtils.getNowDate());
        return pictureInfoMapper.updatePictureInfo(pictureInfo);
    }

    /**
     * 批量删除图片信息
     *
     * @param pictureIds 需要删除的图片信息主键
     * @return 结果
     */
    @Override
    public int deletePictureInfoByPictureIds(String[] pictureIds) {
        return pictureInfoMapper.deletePictureInfoByPictureIds(pictureIds);
    }

    /**
     * 删除图片信息信息
     *
     * @param pictureId 图片信息主键
     * @return 结果
     */
    @Override
    public int deletePictureInfoByPictureId(String pictureId) {
        return pictureInfoMapper.deletePictureInfoByPictureId(pictureId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureInfo> getQueryWrapper(PictureInfoQuery pictureInfoQuery) {
        QueryWrapper<PictureInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String pictureId = pictureInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId), "picture_id", pictureId);

        String name = pictureInfoQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);

        String categoryId = pictureInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId), "category_id", categoryId);

        Long picWidth = pictureInfoQuery.getPicWidth();
        queryWrapper.eq(StringUtils.isNotNull(picWidth), "pic_width", picWidth);

        Long picHeight = pictureInfoQuery.getPicHeight();
        queryWrapper.eq(StringUtils.isNotNull(picHeight), "pic_height", picHeight);

        Double picScale = pictureInfoQuery.getPicScale();
        queryWrapper.eq(StringUtils.isNotNull(picScale), "pic_scale", picScale);

        String picFormat = pictureInfoQuery.getPicFormat();
        queryWrapper.eq(StringUtils.isNotEmpty(picFormat), "pic_format", picFormat);

        String userId = pictureInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = pictureInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date editTime = pictureInfoQuery.getEditTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginEditTime")) && StringUtils.isNotNull(params.get("endEditTime")), "edit_time", params.get("beginEditTime"), params.get("endEditTime"));

        Date updateTime = pictureInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String pictureStatus = pictureInfoQuery.getPictureStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureStatus), "picture_status", pictureStatus);

        Long reviewStatus = pictureInfoQuery.getReviewStatus();
        queryWrapper.eq(StringUtils.isNotNull(reviewStatus), "review_status", reviewStatus);

        Long reviewUserId = pictureInfoQuery.getReviewUserId();
        queryWrapper.eq(StringUtils.isNotNull(reviewUserId), "review_user_id", reviewUserId);

        Date reviewTime = pictureInfoQuery.getReviewTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginReviewTime")) && StringUtils.isNotNull(params.get("endReviewTime")), "review_time", params.get("beginReviewTime"), params.get("endReviewTime"));

        String spaceId = pictureInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String folderId = pictureInfoQuery.getFolderId();
        queryWrapper.eq(StringUtils.isNotEmpty(folderId), "folder_id", folderId);

        String picColor = pictureInfoQuery.getPicColor();
        queryWrapper.eq(StringUtils.isNotEmpty(picColor), "pic_color", picColor);

        String isDelete = pictureInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        Date deletedTime = pictureInfoQuery.getDeletedTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginDeletedTime")) && StringUtils.isNotNull(params.get("endDeletedTime")), "deleted_time", params.get("beginDeletedTime"), params.get("endDeletedTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureInfoVo> convertVoList(List<PictureInfo> pictureInfoList) {
        if (StringUtils.isEmpty(pictureInfoList)) {
            return Collections.emptyList();
        }
        return pictureInfoList.stream().map(PictureInfoVo::objToVo).collect(Collectors.toList());
    }
    //TODO 更新图库信息
    @Override
    public int userInsertPictureInfo(PictureInfo pictureInfo) {
        //查询空间是否存在
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(pictureInfo.getSpaceId());
        if (StringUtils.isNull(spaceInfo) || !spaceInfo.getIsDelete().equals(CommonDeleteEnum.NORMAL.getValue())) {
            throw new ServiceException("空间不存在");
        }
        //如果空间不是公共的，则需要判断用户是否是空间所有者
        //TODO 后续团队空间判断不一样
        if (!spaceInfo.getSpaceType().equals(PSpaceType.SPACE_TYPE_0.getValue()) && !spaceInfo.getUserId().equals(pictureInfo.getUserId())) {
            throw new ServiceException("您不是该空间所有者，无法上传图片");
        }
        //判断文件夹是否存在且文件夹作者是自己
        //TODO 后续团队空间判断不一样
        if (StringUtils.isNotEmpty(pictureInfo.getFolderId())) {
            SpaceFolderInfo spaceFolderInfo = spaceFolderInfoService.selectSpaceFolderInfoByFolderId(pictureInfo.getFolderId());
            if (StringUtils.isNull(spaceFolderInfo)) {
                throw new ServiceException("文件夹不存在");
            }
            if (!spaceFolderInfo.getUserId().equals(pictureInfo.getUserId())) {
                throw new ServiceException("您不是该文件夹所有者，无法上传图片");
            }
        }
        //获取积分最大值和最小值
        String pointsNeedMax = configInfoService.getConfigInfoInCache(PICTURE_POINTS_MAX);
        String pointsNeedMin = configInfoService.getConfigInfoInCache(PICTURE_POINTS_MIN);
        //判断积分是否比最小值大最大值小
        if (!(Long.parseLong(pointsNeedMax) >= pictureInfo.getPointsNeed() && pictureInfo.getPointsNeed() >= Long.parseLong(pointsNeedMin))) {
            throw new ServiceException(StringUtils.format("图片所需积分不在范围内，最小值：{}，最大值：{}", pointsNeedMin, pointsNeedMax));
        }
        //判断是否是十的倍数
        if (pictureInfo.getPicSize() % 10 != 0) {
            throw new ServiceException("图片体积必须是10的倍数");
        }
        //查询标签是否存在
        List<String> tags = pictureInfo.getTags();
        List<PictureTagInfo> tagInfoList = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>().in(PictureTagInfo::getName, tags));
        //遍历两个标签，如果查询到的标签并且此标签为禁止状态，删除tags的标签
        for (PictureTagInfo tagInfo : tagInfoList) {
            if (tagInfo.getTagsStatus().equals(PTagStatus.TAG_STATUS_1.getValue())) {
                tags.remove(tagInfo.getName());
                tagInfoList.remove(tagInfo);
            }
        }
        List<PictureTagInfo> addTagInfoList = new ArrayList<>();
        //遍历剩下的tagInfoList，如果标签不存在，则添加新的标签
        for (PictureTagInfo info : tagInfoList) {
            //如果包含说明数据库已经有此标签
            if (tags.contains(info.getName())) {
                PictureTagInfo pictureTagInfo = new PictureTagInfo();
                BeanUtils.copyBeanProp(pictureTagInfo, info);
                pictureTagInfo.setUsageCount(info.getUsageCount() + 1);
                addTagInfoList.add(pictureTagInfo);
                tags.remove(info.getName());
            }
        }
        for (String tag : tags) {
            PictureTagInfo pictureTagInfo = new PictureTagInfo();
            pictureTagInfo.setLookCount(0L);
            pictureTagInfo.setDownloadCount(0L);
            pictureTagInfo.setUserId(pictureInfo.getUserId());
            pictureTagInfo.setCreateTime(new Date());
            pictureTagInfo.setName(tag);
            pictureTagInfo.setTagsStatus(PTagStatus.TAG_STATUS_0.getValue());
            pictureTagInfo.setUsageCount(1L);
            addTagInfoList.add(pictureTagInfo);
        }
        // 计算宽高比例
        pictureInfo.setPicScale( ((double)pictureInfo.getPicWidth() /(double) pictureInfo.getPicHeight()));
        pictureInfo.setReviewStatus(Long.parseLong(PPictureReviewStatus.PICTURE_REVIEW_STATUS_0.getValue()));
        Integer execute = transactionTemplate.execute(result -> {
            if (StringUtils.isNotEmpty(addTagInfoList)) {
                pictureTagInfoService.saveOrUpdateBatch(addTagInfoList);
            }
            pictureInfo.setPictureId(IdUtils.snowflakeId().toString());
            return pictureInfoMapper.insertPictureInfo(pictureInfo);
        });
        return StringUtils.isNull(execute) ? 0 : execute;
    }

}
