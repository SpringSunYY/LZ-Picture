package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheEvict;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.mapper.PictureCategoryInfoMapper;
import com.lz.picture.model.domain.PictureCategoryInfo;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoQuery;
import com.lz.picture.model.vo.pictureCategoryInfo.PictureCategoryInfoVo;
import com.lz.picture.service.IPictureCategoryInfoService;
import com.lz.picture.utils.TreeUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_CATEGORY;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_CATEGORY_EXPIRE_TIME;

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

    @Resource
    private TransactionTemplate transactionTemplate;

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
    @CustomSort(
            sortFields = {"usageCount", "lookCount", "downloadCount", "createTime", "updateTime", "orderNum"},
            sortMappingFields = {
                    "usage_count", "look_count", "download_count", "create_time", "update_time", "order_num"
            })
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
    @CustomCacheEvict(keyPrefixes = PICTURE_CATEGORY)
    @Override
    public int insertPictureCategoryInfo(PictureCategoryInfo pictureCategoryInfo) {
        //设置初始值
        pictureCategoryInfo.setCategoryId(IdUtils.snowflakeId().toString());
        pictureCategoryInfo.setUsageCount(0L);
        pictureCategoryInfo.setLookCount(0L);
        pictureCategoryInfo.setDownloadCount(0L);
        pictureCategoryInfo.setCreateTime(DateUtils.getNowDate());
        StringBuilder ancestors = TreeUtils.getAncestors(pictureCategoryInfo.getParentId(), new StringBuilder(), pictureCategoryInfoMapper::selectPictureCategoryInfoByCategoryId, PictureCategoryInfo::getCategoryId, PictureCategoryInfo::getParentId, "0", ",");
        pictureCategoryInfo.setAncestors(ancestors.toString());
        return pictureCategoryInfoMapper.insertPictureCategoryInfo(pictureCategoryInfo);
    }

    /**
     * 修改图片分类信息
     *
     * @param pictureCategoryInfo 图片分类信息
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = PICTURE_CATEGORY)
    @Override
    public int updatePictureCategoryInfo(PictureCategoryInfo pictureCategoryInfo) {
        StringBuilder ancestors = TreeUtils.getAncestors(pictureCategoryInfo.getParentId(), new StringBuilder(pictureCategoryInfo.getCategoryId()), pictureCategoryInfoMapper::selectPictureCategoryInfoByCategoryId, PictureCategoryInfo::getCategoryId, PictureCategoryInfo::getParentId, "0", ",");
        pictureCategoryInfo.setAncestors(ancestors.toString());
        pictureCategoryInfo.setUpdateTime(DateUtils.getNowDate());
        return pictureCategoryInfoMapper.updatePictureCategoryInfo(pictureCategoryInfo);
    }

    /**
     * 批量删除图片分类信息
     *
     * @param categoryIds 需要删除的图片分类信息主键
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = PICTURE_CATEGORY)
    @Override
    public int deletePictureCategoryInfoByCategoryIds(String[] categoryIds) {
        ArrayList<PictureCategoryInfo> categorys = new ArrayList<>();
        for (String categoryId : categoryIds) {
            PictureCategoryInfo pictureCategoryInfo = new PictureCategoryInfo();
            pictureCategoryInfo.setIsDelete(CommonDeleteEnum.DELETED.getValue());
            pictureCategoryInfo.setCategoryId(categoryId);
            categorys.add(pictureCategoryInfo);
        }
        return this.updateBatchById(categorys) ? 1 : 0;
    }

    /**
     * 删除图片分类信息信息
     *
     * @param categoryId 图片分类信息主键
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = PICTURE_CATEGORY)
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

    @Override
    public int updatePictureCategoryInfo(String categoryId, String categoryIdOld) {
        //判断是否和存入数据库数据一样，如果一样则不更新
        if (categoryId.equals(categoryIdOld)) {
            return 0;
        }
        PictureCategoryInfo newCategory = selectPictureCategoryInfoByCategoryId(categoryId);
        newCategory.setUsageCount(newCategory.getUsageCount() + 1);
        if (StringUtils.isEmpty(categoryIdOld)) {
            return this.updateById(newCategory) ? 1 : 0;
        }
        PictureCategoryInfo oldCategory = selectPictureCategoryInfoByCategoryId(categoryIdOld);
        if (StringUtils.isNull(oldCategory)) {
            return this.updateById(newCategory) ? 1 : 0;
        }
        oldCategory.setUsageCount(oldCategory.getUsageCount() - 1);
        if (oldCategory.getUsageCount() < 0) {
            oldCategory.setUsageCount(0L);
        }
        transactionTemplate.execute(result -> {
            this.updateById(newCategory);
            return this.updateById(oldCategory);
        });
        return 1;
    }

    @CustomCacheable(keyPrefix = PICTURE_CATEGORY, expireTime = PICTURE_CATEGORY_EXPIRE_TIME, useQueryParamsAsKey = true)
    @Override
    public List<PictureCategoryInfo> userSelectPictureCategoryInfoList(PictureCategoryInfo pictureCategoryInfo) {
        return this.list(new LambdaQueryWrapper<PictureCategoryInfo>()
                .eq(StringUtils.isNotEmpty(pictureCategoryInfo.getParentId()), PictureCategoryInfo::getParentId, pictureCategoryInfo.getParentId())
                .eq(StringUtils.isNotEmpty(pictureCategoryInfo.getQueryStatus()), PictureCategoryInfo::getQueryStatus, pictureCategoryInfo.getQueryStatus())
                .eq(StringUtils.isNotEmpty(pictureCategoryInfo.getCategoryStatus()), PictureCategoryInfo::getCategoryStatus, pictureCategoryInfo.getCategoryStatus())
                .eq(PictureCategoryInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .orderByAsc(PictureCategoryInfo::getOrderNum)
                .orderBy(true, false, PictureCategoryInfo::getUsageCount)
        );
    }

    @Override
    public List<PictureCategoryInfo> findCategoryChildren(String categoryId) {
        return pictureCategoryInfoMapper.findCategoryChildren(categoryId);
    }

}
