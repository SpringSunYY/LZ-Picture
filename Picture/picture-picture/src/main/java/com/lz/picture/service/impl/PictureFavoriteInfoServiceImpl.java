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
import com.lz.picture.mapper.PictureFavoriteInfoMapper;
import com.lz.picture.model.domain.PictureFavoriteInfo;
import com.lz.picture.service.IPictureFavoriteInfoService;
import com.lz.picture.model.dto.pictureFavoriteInfo.PictureFavoriteInfoQuery;
import com.lz.picture.model.vo.pictureFavoriteInfo.PictureFavoriteInfoVo;

/**
 * 用户图片收藏Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureFavoriteInfoServiceImpl extends ServiceImpl<PictureFavoriteInfoMapper, PictureFavoriteInfo> implements IPictureFavoriteInfoService
{
    @Resource
    private PictureFavoriteInfoMapper pictureFavoriteInfoMapper;

    //region mybatis代码
    /**
     * 查询用户图片收藏
     *
     * @param favoriteId 用户图片收藏主键
     * @return 用户图片收藏
     */
    @Override
    public PictureFavoriteInfo selectPictureFavoriteInfoByFavoriteId(String favoriteId)
    {
        return pictureFavoriteInfoMapper.selectPictureFavoriteInfoByFavoriteId(favoriteId);
    }

    /**
     * 查询用户图片收藏列表
     *
     * @param pictureFavoriteInfo 用户图片收藏
     * @return 用户图片收藏
     */
    @Override
    public List<PictureFavoriteInfo> selectPictureFavoriteInfoList(PictureFavoriteInfo pictureFavoriteInfo)
    {
        return pictureFavoriteInfoMapper.selectPictureFavoriteInfoList(pictureFavoriteInfo);
    }

    /**
     * 新增用户图片收藏
     *
     * @param pictureFavoriteInfo 用户图片收藏
     * @return 结果
     */
    @Override
    public int insertPictureFavoriteInfo(PictureFavoriteInfo pictureFavoriteInfo)
    {
        pictureFavoriteInfo.setCreateTime(DateUtils.getNowDate());
        return pictureFavoriteInfoMapper.insertPictureFavoriteInfo(pictureFavoriteInfo);
    }

    /**
     * 修改用户图片收藏
     *
     * @param pictureFavoriteInfo 用户图片收藏
     * @return 结果
     */
    @Override
    public int updatePictureFavoriteInfo(PictureFavoriteInfo pictureFavoriteInfo)
    {
        return pictureFavoriteInfoMapper.updatePictureFavoriteInfo(pictureFavoriteInfo);
    }

    /**
     * 批量删除用户图片收藏
     *
     * @param favoriteIds 需要删除的用户图片收藏主键
     * @return 结果
     */
    @Override
    public int deletePictureFavoriteInfoByFavoriteIds(String[] favoriteIds)
    {
        return pictureFavoriteInfoMapper.deletePictureFavoriteInfoByFavoriteIds(favoriteIds);
    }

    /**
     * 删除用户图片收藏信息
     *
     * @param favoriteId 用户图片收藏主键
     * @return 结果
     */
    @Override
    public int deletePictureFavoriteInfoByFavoriteId(String favoriteId)
    {
        return pictureFavoriteInfoMapper.deletePictureFavoriteInfoByFavoriteId(favoriteId);
    }
    //endregion
    @Override
    public QueryWrapper<PictureFavoriteInfo> getQueryWrapper(PictureFavoriteInfoQuery pictureFavoriteInfoQuery){
        QueryWrapper<PictureFavoriteInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureFavoriteInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String favoriteId = pictureFavoriteInfoQuery.getFavoriteId();
        queryWrapper.eq(StringUtils.isNotEmpty(favoriteId) ,"favorite_id",favoriteId);

    String userId = pictureFavoriteInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String pictureId = pictureFavoriteInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId) ,"picture_id",pictureId);

    String categoryId = pictureFavoriteInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId) ,"category_id",categoryId);

    String tags = pictureFavoriteInfoQuery.getTags();
        queryWrapper.like(StringUtils.isNotEmpty(tags) ,"tags",tags);

    Date createTime = pictureFavoriteInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureFavoriteInfoVo> convertVoList(List<PictureFavoriteInfo> pictureFavoriteInfoList) {
        if (StringUtils.isEmpty(pictureFavoriteInfoList)) {
            return Collections.emptyList();
        }
        return pictureFavoriteInfoList.stream().map(PictureFavoriteInfoVo::objToVo).collect(Collectors.toList());
    }

}
