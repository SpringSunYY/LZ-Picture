package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.PictureInfoMapper;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.model.dto.pictureInfo.PictureInfoQuery;
import com.lz.picture.model.vo.pictureInfo.PictureInfoVo;

/**
 * 图片信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureInfoServiceImpl extends ServiceImpl<PictureInfoMapper, PictureInfo> implements IPictureInfoService
{
    @Resource
    private PictureInfoMapper pictureInfoMapper;

    //region mybatis代码
    /**
     * 查询图片信息
     *
     * @param pictureId 图片信息主键
     * @return 图片信息
     */
    @Override
    public PictureInfo selectPictureInfoByPictureId(String pictureId)
    {
        return pictureInfoMapper.selectPictureInfoByPictureId(pictureId);
    }

    /**
     * 查询图片信息列表
     *
     * @param pictureInfo 图片信息
     * @return 图片信息
     */
    @Override
    public List<PictureInfo> selectPictureInfoList(PictureInfo pictureInfo)
    {
        return pictureInfoMapper.selectPictureInfoList(pictureInfo);
    }

    /**
     * 新增图片信息
     *
     * @param pictureInfo 图片信息
     * @return 结果
     */
    @Override
    public int insertPictureInfo(PictureInfo pictureInfo)
    {
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
    public int updatePictureInfo(PictureInfo pictureInfo)
    {
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
    public int deletePictureInfoByPictureIds(String[] pictureIds)
    {
        return pictureInfoMapper.deletePictureInfoByPictureIds(pictureIds);
    }

    /**
     * 删除图片信息信息
     *
     * @param pictureId 图片信息主键
     * @return 结果
     */
    @Override
    public int deletePictureInfoByPictureId(String pictureId)
    {
        return pictureInfoMapper.deletePictureInfoByPictureId(pictureId);
    }
    //endregion
    @Override
    public QueryWrapper<PictureInfo> getQueryWrapper(PictureInfoQuery pictureInfoQuery){
        QueryWrapper<PictureInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String pictureId = pictureInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId) ,"picture_id",pictureId);

    String name = pictureInfoQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name) ,"name",name);

    String categoryId = pictureInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId) ,"category_id",categoryId);

    Long picWidth = pictureInfoQuery.getPicWidth();
        queryWrapper.eq( StringUtils.isNotNull(picWidth),"pic_width",picWidth);

    Long picHeight = pictureInfoQuery.getPicHeight();
        queryWrapper.eq( StringUtils.isNotNull(picHeight),"pic_height",picHeight);

    Long picScale = pictureInfoQuery.getPicScale();
        queryWrapper.eq( StringUtils.isNotNull(picScale),"pic_scale",picScale);

    String picFormat = pictureInfoQuery.getPicFormat();
        queryWrapper.eq(StringUtils.isNotEmpty(picFormat) ,"pic_format",picFormat);

    String userId = pictureInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    Date createTime = pictureInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date editTime = pictureInfoQuery.getEditTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginEditTime"))&&StringUtils.isNotNull(params.get("endEditTime")),"edit_time",params.get("beginEditTime"),params.get("endEditTime"));

    Date updateTime = pictureInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

    String pictureStatus = pictureInfoQuery.getPictureStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureStatus) ,"picture_status",pictureStatus);

    Long reviewStatus = pictureInfoQuery.getReviewStatus();
        queryWrapper.eq( StringUtils.isNotNull(reviewStatus),"review_status",reviewStatus);

    Long reviewUserId = pictureInfoQuery.getReviewUserId();
        queryWrapper.eq( StringUtils.isNotNull(reviewUserId),"review_user_id",reviewUserId);

    Date reviewTime = pictureInfoQuery.getReviewTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginReviewTime"))&&StringUtils.isNotNull(params.get("endReviewTime")),"review_time",params.get("beginReviewTime"),params.get("endReviewTime"));

    String spaceId = pictureInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId) ,"space_id",spaceId);

    String folderId = pictureInfoQuery.getFolderId();
        queryWrapper.eq(StringUtils.isNotEmpty(folderId) ,"folder_id",folderId);

    String picColor = pictureInfoQuery.getPicColor();
        queryWrapper.eq(StringUtils.isNotEmpty(picColor) ,"pic_color",picColor);

    String isDelete = pictureInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete) ,"is_delete",isDelete);

    Date deletedTime = pictureInfoQuery.getDeletedTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginDeletedTime"))&&StringUtils.isNotNull(params.get("endDeletedTime")),"deleted_time",params.get("beginDeletedTime"),params.get("endDeletedTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureInfoVo> convertVoList(List<PictureInfo> pictureInfoList) {
        if (StringUtils.isEmpty(pictureInfoList)) {
            return Collections.emptyList();
        }
        return pictureInfoList.stream().map(PictureInfoVo::objToVo).collect(Collectors.toList());
    }

}
