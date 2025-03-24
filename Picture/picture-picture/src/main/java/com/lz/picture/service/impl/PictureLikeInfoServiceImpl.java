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
import com.lz.picture.mapper.PictureLikeInfoMapper;
import com.lz.picture.model.domain.PictureLikeInfo;
import com.lz.picture.service.IPictureLikeInfoService;
import com.lz.picture.model.dto.pictureLikeInfo.PictureLikeInfoQuery;
import com.lz.picture.model.vo.pictureLikeInfo.PictureLikeInfoVo;

/**
 * 图片点赞记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureLikeInfoServiceImpl extends ServiceImpl<PictureLikeInfoMapper, PictureLikeInfo> implements IPictureLikeInfoService
{
    @Resource
    private PictureLikeInfoMapper pictureLikeInfoMapper;

    //region mybatis代码
    /**
     * 查询图片点赞记录
     *
     * @param likeId 图片点赞记录主键
     * @return 图片点赞记录
     */
    @Override
    public PictureLikeInfo selectPictureLikeInfoByLikeId(String likeId)
    {
        return pictureLikeInfoMapper.selectPictureLikeInfoByLikeId(likeId);
    }

    /**
     * 查询图片点赞记录列表
     *
     * @param pictureLikeInfo 图片点赞记录
     * @return 图片点赞记录
     */
    @Override
    public List<PictureLikeInfo> selectPictureLikeInfoList(PictureLikeInfo pictureLikeInfo)
    {
        return pictureLikeInfoMapper.selectPictureLikeInfoList(pictureLikeInfo);
    }

    /**
     * 新增图片点赞记录
     *
     * @param pictureLikeInfo 图片点赞记录
     * @return 结果
     */
    @Override
    public int insertPictureLikeInfo(PictureLikeInfo pictureLikeInfo)
    {
        pictureLikeInfo.setCreateTime(DateUtils.getNowDate());
        return pictureLikeInfoMapper.insertPictureLikeInfo(pictureLikeInfo);
    }

    /**
     * 修改图片点赞记录
     *
     * @param pictureLikeInfo 图片点赞记录
     * @return 结果
     */
    @Override
    public int updatePictureLikeInfo(PictureLikeInfo pictureLikeInfo)
    {
        return pictureLikeInfoMapper.updatePictureLikeInfo(pictureLikeInfo);
    }

    /**
     * 批量删除图片点赞记录
     *
     * @param likeIds 需要删除的图片点赞记录主键
     * @return 结果
     */
    @Override
    public int deletePictureLikeInfoByLikeIds(String[] likeIds)
    {
        return pictureLikeInfoMapper.deletePictureLikeInfoByLikeIds(likeIds);
    }

    /**
     * 删除图片点赞记录信息
     *
     * @param likeId 图片点赞记录主键
     * @return 结果
     */
    @Override
    public int deletePictureLikeInfoByLikeId(String likeId)
    {
        return pictureLikeInfoMapper.deletePictureLikeInfoByLikeId(likeId);
    }
    //endregion
    @Override
    public QueryWrapper<PictureLikeInfo> getQueryWrapper(PictureLikeInfoQuery pictureLikeInfoQuery){
        QueryWrapper<PictureLikeInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureLikeInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String likeId = pictureLikeInfoQuery.getLikeId();
        queryWrapper.eq(StringUtils.isNotEmpty(likeId) ,"like_id",likeId);

    String userId = pictureLikeInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String pictureId = pictureLikeInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId) ,"picture_id",pictureId);

    String categoryId = pictureLikeInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId) ,"category_id",categoryId);

    String tags = pictureLikeInfoQuery.getTags();
        queryWrapper.like(StringUtils.isNotEmpty(tags) ,"tags",tags);

    Date createTime = pictureLikeInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureLikeInfoVo> convertVoList(List<PictureLikeInfo> pictureLikeInfoList) {
        if (StringUtils.isEmpty(pictureLikeInfoList)) {
            return Collections.emptyList();
        }
        return pictureLikeInfoList.stream().map(PictureLikeInfoVo::objToVo).collect(Collectors.toList());
    }

}
