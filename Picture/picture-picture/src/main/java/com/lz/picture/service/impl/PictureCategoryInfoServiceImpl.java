package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.uuid.IdUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.PictureCategoryInfoMapper;
import com.lz.picture.model.domain.PictureCategoryInfo;
import com.lz.picture.service.IPictureCategoryInfoService;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoQuery;
import com.lz.picture.model.vo.pictureCategoryInfo.PictureCategoryInfoVo;

/**
 * 图片分类信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureCategoryInfoServiceImpl extends ServiceImpl<PictureCategoryInfoMapper, PictureCategoryInfo> implements IPictureCategoryInfoService {
    @Resource
    private PictureCategoryInfoMapper pictureCategoryInfoMapper;

    //region mybatis代码

    /**
     * 查询图片分类信息
     *
     * @param categoryId 图片分类信息主键
     * @return 图片分类信息
     */
    @Override
    public PictureCategoryInfo selectPictureCategoryInfoByCategoryId(String categoryId) {
        return pictureCategoryInfoMapper.selectPictureCategoryInfoByCategoryId(categoryId);
    }

    /**
     * 查询图片分类信息列表
     *
     * @param pictureCategoryInfo 图片分类信息
     * @return 图片分类信息
     */
    @Override
    public List<PictureCategoryInfo> selectPictureCategoryInfoList(PictureCategoryInfo pictureCategoryInfo) {
        return pictureCategoryInfoMapper.selectPictureCategoryInfoList(pictureCategoryInfo);
    }

    /**
     * 新增图片分类信息
     *
     * @param pictureCategoryInfo 图片分类信息
     * @return 结果
     */
    @Override
    public int insertPictureCategoryInfo(PictureCategoryInfo pictureCategoryInfo) {
        //设置初始值
        pictureCategoryInfo.setCategoryId(IdUtils.snowflakeId().toString());
        pictureCategoryInfo.setUsageCount(0L);
        pictureCategoryInfo.setLookCount(0L);
        pictureCategoryInfo.setDownloadCount(0L);
        pictureCategoryInfo.setCreateTime(DateUtils.getNowDate());
        return pictureCategoryInfoMapper.insertPictureCategoryInfo(pictureCategoryInfo);
    }

    /**
     * 修改图片分类信息
     *
     * @param pictureCategoryInfo 图片分类信息
     * @return 结果
     */
    @Override
    public int updatePictureCategoryInfo(PictureCategoryInfo pictureCategoryInfo) {
        pictureCategoryInfo.setUpdateTime(DateUtils.getNowDate());
        return pictureCategoryInfoMapper.updatePictureCategoryInfo(pictureCategoryInfo);
    }

    /**
     * 批量删除图片分类信息
     *
     * @param categoryIds 需要删除的图片分类信息主键
     * @return 结果
     */
    @Override
    public int deletePictureCategoryInfoByCategoryIds(String[] categoryIds) {
        return pictureCategoryInfoMapper.deletePictureCategoryInfoByCategoryIds(categoryIds);
    }

    /**
     * 删除图片分类信息信息
     *
     * @param categoryId 图片分类信息主键
     * @return 结果
     */
    @Override
    public int deletePictureCategoryInfoByCategoryId(String categoryId) {
        return pictureCategoryInfoMapper.deletePictureCategoryInfoByCategoryId(categoryId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureCategoryInfo> getQueryWrapper(PictureCategoryInfoQuery pictureCategoryInfoQuery) {
        QueryWrapper<PictureCategoryInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureCategoryInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String categoryId = pictureCategoryInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId), "category_id", categoryId);

        String name = pictureCategoryInfoQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);

        String categoryStatus = pictureCategoryInfoQuery.getCategoryStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryStatus), "category_status", categoryStatus);

        String queryStatus = pictureCategoryInfoQuery.getQueryStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(queryStatus), "query_status", queryStatus);

        Date createTime = pictureCategoryInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = pictureCategoryInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String isDelete = pictureCategoryInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        return queryWrapper;
    }

    @Override
    public List<PictureCategoryInfoVo> convertVoList(List<PictureCategoryInfo> pictureCategoryInfoList) {
        if (StringUtils.isEmpty(pictureCategoryInfoList)) {
            return Collections.emptyList();
        }
        return pictureCategoryInfoList.stream().map(PictureCategoryInfoVo::objToVo).collect(Collectors.toList());
    }

}
