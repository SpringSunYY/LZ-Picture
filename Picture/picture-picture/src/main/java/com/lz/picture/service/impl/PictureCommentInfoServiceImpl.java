package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.picture.mapper.PictureCommentInfoMapper;
import com.lz.picture.model.domain.PictureCommentInfo;
import com.lz.picture.model.dto.pictureCommentInfo.PictureCommentInfoQuery;
import com.lz.picture.model.vo.pictureCommentInfo.PictureCommentInfoVo;
import com.lz.picture.service.IPictureCommentInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 图片评论Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureCommentInfoServiceImpl extends ServiceImpl<PictureCommentInfoMapper, PictureCommentInfo> implements IPictureCommentInfoService {
    @Resource
    private PictureCommentInfoMapper pictureCommentInfoMapper;

    //region mybatis代码

    /**
     * 查询图片评论
     *
     * @param commentId 图片评论主键
     * @return 图片评论
     */
    @Override
    public PictureCommentInfo selectPictureCommentInfoByCommentId(String commentId) {
        return pictureCommentInfoMapper.selectPictureCommentInfoByCommentId(commentId);
    }

    /**
     * 查询图片评论列表
     *
     * @param pictureCommentInfo 图片评论
     * @return 图片评论
     */
    @Override
    public List<PictureCommentInfo> selectPictureCommentInfoList(PictureCommentInfo pictureCommentInfo) {
        return pictureCommentInfoMapper.selectPictureCommentInfoList(pictureCommentInfo);
    }

    /**
     * 新增图片评论
     *
     * @param pictureCommentInfo 图片评论
     * @return 结果
     */
    @Override
    public int insertPictureCommentInfo(PictureCommentInfo pictureCommentInfo) {
        pictureCommentInfo.setCreateTime(DateUtils.getNowDate());
        return pictureCommentInfoMapper.insertPictureCommentInfo(pictureCommentInfo);
    }

    /**
     * 修改图片评论
     *
     * @param pictureCommentInfo 图片评论
     * @return 结果
     */
    @Override
    public int updatePictureCommentInfo(PictureCommentInfo pictureCommentInfo) {
        return pictureCommentInfoMapper.updatePictureCommentInfo(pictureCommentInfo);
    }

    /**
     * 批量删除图片评论
     *
     * @param commentIds 需要删除的图片评论主键
     * @return 结果
     */
    @Override
    public int deletePictureCommentInfoByCommentIds(String[] commentIds) {
        return pictureCommentInfoMapper.deletePictureCommentInfoByCommentIds(commentIds);
    }

    /**
     * 删除图片评论信息
     *
     * @param commentId 图片评论主键
     * @return 结果
     */
    @Override
    public int deletePictureCommentInfoByCommentId(String commentId) {
        return pictureCommentInfoMapper.deletePictureCommentInfoByCommentId(commentId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureCommentInfo> getQueryWrapper(PictureCommentInfoQuery pictureCommentInfoQuery) {
        QueryWrapper<PictureCommentInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureCommentInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String commentId = pictureCommentInfoQuery.getCommentId();
        queryWrapper.eq(StringUtils.isNotEmpty(commentId), "comment_id", commentId);

        String userId = pictureCommentInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String parentId = pictureCommentInfoQuery.getParentId();
        queryWrapper.eq(StringUtils.isNotEmpty(parentId), "parent_id", parentId);

        String pictureId = pictureCommentInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId), "picture_id", pictureId);

        String categoryId = pictureCommentInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId), "category_id", categoryId);

        String tags = pictureCommentInfoQuery.getTags();
        queryWrapper.like(StringUtils.isNotEmpty(tags), "tags", tags);

        String content = pictureCommentInfoQuery.getContent();
        queryWrapper.eq(StringUtils.isNotEmpty(content), "content", content);

        Date createTime = pictureCommentInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Long likeCount = pictureCommentInfoQuery.getLikeCount();
        queryWrapper.eq(StringUtils.isNotNull(likeCount), "like_count", likeCount);

        String ipAddress = pictureCommentInfoQuery.getIpAddress();
        queryWrapper.eq(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        String commentStatus = pictureCommentInfoQuery.getCommentStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(commentStatus), "comment_status", commentStatus);

        return queryWrapper;
    }

    @Override
    public List<PictureCommentInfoVo> convertVoList(List<PictureCommentInfo> pictureCommentInfoList) {
        if (StringUtils.isEmpty(pictureCommentInfoList)) {
            return Collections.emptyList();
        }
        return pictureCommentInfoList.stream().map(PictureCommentInfoVo::objToVo).collect(Collectors.toList());
    }

}
