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
import com.lz.picture.mapper.PictureCommentLikeInfoMapper;
import com.lz.picture.model.domain.PictureCommentLikeInfo;
import com.lz.picture.service.IPictureCommentLikeInfoService;
import com.lz.picture.model.dto.pictureCommentLikeInfo.PictureCommentLikeInfoQuery;
import com.lz.picture.model.vo.pictureCommentLikeInfo.PictureCommentLikeInfoVo;

/**
 * 评论点赞记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureCommentLikeInfoServiceImpl extends ServiceImpl<PictureCommentLikeInfoMapper, PictureCommentLikeInfo> implements IPictureCommentLikeInfoService
{
    @Resource
    private PictureCommentLikeInfoMapper pictureCommentLikeInfoMapper;

    //region mybatis代码
    /**
     * 查询评论点赞记录
     *
     * @param likeId 评论点赞记录主键
     * @return 评论点赞记录
     */
    @Override
    public PictureCommentLikeInfo selectPictureCommentLikeInfoByLikeId(String likeId)
    {
        return pictureCommentLikeInfoMapper.selectPictureCommentLikeInfoByLikeId(likeId);
    }

    /**
     * 查询评论点赞记录列表
     *
     * @param pictureCommentLikeInfo 评论点赞记录
     * @return 评论点赞记录
     */
    @Override
    public List<PictureCommentLikeInfo> selectPictureCommentLikeInfoList(PictureCommentLikeInfo pictureCommentLikeInfo)
    {
        return pictureCommentLikeInfoMapper.selectPictureCommentLikeInfoList(pictureCommentLikeInfo);
    }

    /**
     * 新增评论点赞记录
     *
     * @param pictureCommentLikeInfo 评论点赞记录
     * @return 结果
     */
    @Override
    public int insertPictureCommentLikeInfo(PictureCommentLikeInfo pictureCommentLikeInfo)
    {
        pictureCommentLikeInfo.setCreateTime(DateUtils.getNowDate());
        return pictureCommentLikeInfoMapper.insertPictureCommentLikeInfo(pictureCommentLikeInfo);
    }

    /**
     * 修改评论点赞记录
     *
     * @param pictureCommentLikeInfo 评论点赞记录
     * @return 结果
     */
    @Override
    public int updatePictureCommentLikeInfo(PictureCommentLikeInfo pictureCommentLikeInfo)
    {
        return pictureCommentLikeInfoMapper.updatePictureCommentLikeInfo(pictureCommentLikeInfo);
    }

    /**
     * 批量删除评论点赞记录
     *
     * @param likeIds 需要删除的评论点赞记录主键
     * @return 结果
     */
    @Override
    public int deletePictureCommentLikeInfoByLikeIds(String[] likeIds)
    {
        return pictureCommentLikeInfoMapper.deletePictureCommentLikeInfoByLikeIds(likeIds);
    }

    /**
     * 删除评论点赞记录信息
     *
     * @param likeId 评论点赞记录主键
     * @return 结果
     */
    @Override
    public int deletePictureCommentLikeInfoByLikeId(String likeId)
    {
        return pictureCommentLikeInfoMapper.deletePictureCommentLikeInfoByLikeId(likeId);
    }
    //endregion
    @Override
    public QueryWrapper<PictureCommentLikeInfo> getQueryWrapper(PictureCommentLikeInfoQuery pictureCommentLikeInfoQuery){
        QueryWrapper<PictureCommentLikeInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureCommentLikeInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String likeId = pictureCommentLikeInfoQuery.getLikeId();
        queryWrapper.eq(StringUtils.isNotEmpty(likeId) ,"like_id",likeId);

    String userId = pictureCommentLikeInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String pictureId = pictureCommentLikeInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId) ,"picture_id",pictureId);

    String targetCover = pictureCommentLikeInfoQuery.getTargetCover();
        queryWrapper.eq(StringUtils.isNotEmpty(targetCover) ,"target_cover",targetCover);

    Date createTime = pictureCommentLikeInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureCommentLikeInfoVo> convertVoList(List<PictureCommentLikeInfo> pictureCommentLikeInfoList) {
        if (StringUtils.isEmpty(pictureCommentLikeInfoList)) {
            return Collections.emptyList();
        }
        return pictureCommentLikeInfoList.stream().map(PictureCommentLikeInfoVo::objToVo).collect(Collectors.toList());
    }

}
