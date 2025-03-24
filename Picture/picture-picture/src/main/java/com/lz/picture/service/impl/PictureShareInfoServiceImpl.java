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
import com.lz.picture.mapper.PictureShareInfoMapper;
import com.lz.picture.model.domain.PictureShareInfo;
import com.lz.picture.service.IPictureShareInfoService;
import com.lz.picture.model.dto.pictureShareInfo.PictureShareInfoQuery;
import com.lz.picture.model.vo.pictureShareInfo.PictureShareInfoVo;

/**
 * 图片转发记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureShareInfoServiceImpl extends ServiceImpl<PictureShareInfoMapper, PictureShareInfo> implements IPictureShareInfoService
{
    @Resource
    private PictureShareInfoMapper pictureShareInfoMapper;

    //region mybatis代码
    /**
     * 查询图片转发记录
     *
     * @param shareId 图片转发记录主键
     * @return 图片转发记录
     */
    @Override
    public PictureShareInfo selectPictureShareInfoByShareId(String shareId)
    {
        return pictureShareInfoMapper.selectPictureShareInfoByShareId(shareId);
    }

    /**
     * 查询图片转发记录列表
     *
     * @param pictureShareInfo 图片转发记录
     * @return 图片转发记录
     */
    @Override
    public List<PictureShareInfo> selectPictureShareInfoList(PictureShareInfo pictureShareInfo)
    {
        return pictureShareInfoMapper.selectPictureShareInfoList(pictureShareInfo);
    }

    /**
     * 新增图片转发记录
     *
     * @param pictureShareInfo 图片转发记录
     * @return 结果
     */
    @Override
    public int insertPictureShareInfo(PictureShareInfo pictureShareInfo)
    {
        pictureShareInfo.setCreateTime(DateUtils.getNowDate());
        return pictureShareInfoMapper.insertPictureShareInfo(pictureShareInfo);
    }

    /**
     * 修改图片转发记录
     *
     * @param pictureShareInfo 图片转发记录
     * @return 结果
     */
    @Override
    public int updatePictureShareInfo(PictureShareInfo pictureShareInfo)
    {
        return pictureShareInfoMapper.updatePictureShareInfo(pictureShareInfo);
    }

    /**
     * 批量删除图片转发记录
     *
     * @param shareIds 需要删除的图片转发记录主键
     * @return 结果
     */
    @Override
    public int deletePictureShareInfoByShareIds(String[] shareIds)
    {
        return pictureShareInfoMapper.deletePictureShareInfoByShareIds(shareIds);
    }

    /**
     * 删除图片转发记录信息
     *
     * @param shareId 图片转发记录主键
     * @return 结果
     */
    @Override
    public int deletePictureShareInfoByShareId(String shareId)
    {
        return pictureShareInfoMapper.deletePictureShareInfoByShareId(shareId);
    }
    //endregion
    @Override
    public QueryWrapper<PictureShareInfo> getQueryWrapper(PictureShareInfoQuery pictureShareInfoQuery){
        QueryWrapper<PictureShareInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureShareInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String shareId = pictureShareInfoQuery.getShareId();
        queryWrapper.eq(StringUtils.isNotEmpty(shareId) ,"share_id",shareId);

    String userId = pictureShareInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String pictureId = pictureShareInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId) ,"picture_id",pictureId);

    String categoryId = pictureShareInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId) ,"category_id",categoryId);

    String tags = pictureShareInfoQuery.getTags();
        queryWrapper.like(StringUtils.isNotEmpty(tags) ,"tags",tags);

    Date createTime = pictureShareInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureShareInfoVo> convertVoList(List<PictureShareInfo> pictureShareInfoList) {
        if (StringUtils.isEmpty(pictureShareInfoList)) {
            return Collections.emptyList();
        }
        return pictureShareInfoList.stream().map(PictureShareInfoVo::objToVo).collect(Collectors.toList());
    }

}
