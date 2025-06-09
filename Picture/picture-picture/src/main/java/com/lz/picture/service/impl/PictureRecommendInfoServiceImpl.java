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
import com.lz.picture.mapper.PictureRecommendInfoMapper;
import com.lz.picture.model.domain.PictureRecommendInfo;
import com.lz.picture.service.IPictureRecommendInfoService;
import com.lz.picture.model.dto.pictureRecommendInfo.PictureRecommendInfoQuery;
import com.lz.picture.model.vo.pictureRecommendInfo.PictureRecommendInfoVo;

/**
 * 用户图片推荐模型Service业务层处理
 *
 * @author YY
 * @date 2025-06-10
 */
@Service
public class PictureRecommendInfoServiceImpl extends ServiceImpl<PictureRecommendInfoMapper, PictureRecommendInfo> implements IPictureRecommendInfoService
{
    @Resource
    private PictureRecommendInfoMapper pictureRecommendInfoMapper;

    //region mybatis代码
    /**
     * 查询用户图片推荐模型
     *
     * @param recommendId 用户图片推荐模型主键
     * @return 用户图片推荐模型
     */
    @Override
    public PictureRecommendInfo selectPictureRecommendInfoByRecommendId(String recommendId)
    {
        return pictureRecommendInfoMapper.selectPictureRecommendInfoByRecommendId(recommendId);
    }

    /**
     * 查询用户图片推荐模型列表
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 用户图片推荐模型
     */
    @Override
    public List<PictureRecommendInfo> selectPictureRecommendInfoList(PictureRecommendInfo pictureRecommendInfo)
    {
        return pictureRecommendInfoMapper.selectPictureRecommendInfoList(pictureRecommendInfo);
    }

    /**
     * 新增用户图片推荐模型
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 结果
     */
    @Override
    public int insertPictureRecommendInfo(PictureRecommendInfo pictureRecommendInfo)
    {
        pictureRecommendInfo.setCreateTime(DateUtils.getNowDate());
        return pictureRecommendInfoMapper.insertPictureRecommendInfo(pictureRecommendInfo);
    }

    /**
     * 修改用户图片推荐模型
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 结果
     */
    @Override
    public int updatePictureRecommendInfo(PictureRecommendInfo pictureRecommendInfo)
    {
        return pictureRecommendInfoMapper.updatePictureRecommendInfo(pictureRecommendInfo);
    }

    /**
     * 批量删除用户图片推荐模型
     *
     * @param recommendIds 需要删除的用户图片推荐模型主键
     * @return 结果
     */
    @Override
    public int deletePictureRecommendInfoByRecommendIds(String[] recommendIds)
    {
        return pictureRecommendInfoMapper.deletePictureRecommendInfoByRecommendIds(recommendIds);
    }

    /**
     * 删除用户图片推荐模型信息
     *
     * @param recommendId 用户图片推荐模型主键
     * @return 结果
     */
    @Override
    public int deletePictureRecommendInfoByRecommendId(String recommendId)
    {
        return pictureRecommendInfoMapper.deletePictureRecommendInfoByRecommendId(recommendId);
    }
    //endregion
    @Override
    public QueryWrapper<PictureRecommendInfo> getQueryWrapper(PictureRecommendInfoQuery pictureRecommendInfoQuery){
        QueryWrapper<PictureRecommendInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureRecommendInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String recommendId = pictureRecommendInfoQuery.getRecommendId();
        queryWrapper.eq(StringUtils.isNotEmpty(recommendId) ,"recommend_id",recommendId);

    String userId = pictureRecommendInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    Date createTime = pictureRecommendInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureRecommendInfoVo> convertVoList(List<PictureRecommendInfo> pictureRecommendInfoList) {
        if (StringUtils.isEmpty(pictureRecommendInfoList)) {
            return Collections.emptyList();
        }
        return pictureRecommendInfoList.stream().map(PictureRecommendInfoVo::objToVo).collect(Collectors.toList());
    }

}
