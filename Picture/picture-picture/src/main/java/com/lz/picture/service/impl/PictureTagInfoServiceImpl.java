package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.picture.mapper.PictureTagInfoMapper;
import com.lz.picture.model.domain.PictureTagInfo;
import com.lz.picture.model.dto.pictureTagInfo.PictureTagInfoQuery;
import com.lz.picture.model.vo.pictureTagInfo.PictureTagInfoVo;
import com.lz.picture.service.IPictureTagInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 图片标签信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureTagInfoServiceImpl extends ServiceImpl<PictureTagInfoMapper, PictureTagInfo> implements IPictureTagInfoService {
    @Resource
    private PictureTagInfoMapper pictureTagInfoMapper;

    //region mybatis代码

    /**
     * 查询图片标签信息
     *
     * @param tagId 图片标签信息主键
     * @return 图片标签信息
     */
    @Override
    public PictureTagInfo selectPictureTagInfoByTagId(String tagId) {
        return pictureTagInfoMapper.selectPictureTagInfoByTagId(tagId);
    }

    /**
     * 查询图片标签信息列表
     *
     * @param pictureTagInfo 图片标签信息
     * @return 图片标签信息
     */
    @Override
    public List<PictureTagInfo> selectPictureTagInfoList(PictureTagInfo pictureTagInfo) {
        return pictureTagInfoMapper.selectPictureTagInfoList(pictureTagInfo);
    }

    /**
     * 新增图片标签信息
     *
     * @param pictureTagInfo 图片标签信息
     * @return 结果
     */
    @Override
    public int insertPictureTagInfo(PictureTagInfo pictureTagInfo) {
        pictureTagInfo.setCreateTime(DateUtils.getNowDate());
        return pictureTagInfoMapper.insertPictureTagInfo(pictureTagInfo);
    }

    /**
     * 修改图片标签信息
     *
     * @param pictureTagInfo 图片标签信息
     * @return 结果
     */
    @Override
    public int updatePictureTagInfo(PictureTagInfo pictureTagInfo) {
        pictureTagInfo.setUpdateTime(DateUtils.getNowDate());
        return pictureTagInfoMapper.updatePictureTagInfo(pictureTagInfo);
    }

    /**
     * 批量删除图片标签信息
     *
     * @param tagIds 需要删除的图片标签信息主键
     * @return 结果
     */
    @Override
    public int deletePictureTagInfoByTagIds(String[] tagIds) {
        return pictureTagInfoMapper.deletePictureTagInfoByTagIds(tagIds);
    }

    /**
     * 删除图片标签信息信息
     *
     * @param tagId 图片标签信息主键
     * @return 结果
     */
    @Override
    public int deletePictureTagInfoByTagId(String tagId) {
        return pictureTagInfoMapper.deletePictureTagInfoByTagId(tagId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureTagInfo> getQueryWrapper(PictureTagInfoQuery pictureTagInfoQuery) {
        QueryWrapper<PictureTagInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureTagInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String tagId = pictureTagInfoQuery.getTagId();
        queryWrapper.eq(StringUtils.isNotEmpty(tagId), "tag_id", tagId);

        String name = pictureTagInfoQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);

        String userId = pictureTagInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = pictureTagInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = pictureTagInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureTagInfoVo> convertVoList(List<PictureTagInfo> pictureTagInfoList) {
        if (StringUtils.isEmpty(pictureTagInfoList)) {
            return Collections.emptyList();
        }
        return pictureTagInfoList.stream().map(PictureTagInfoVo::objToVo).collect(Collectors.toList());
    }

}
