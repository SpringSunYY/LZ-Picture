package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.domain.PictureTagInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.PictureTagRelInfoMapper;
import com.lz.picture.model.domain.PictureTagRelInfo;
import com.lz.picture.service.IPictureTagRelInfoService;
import com.lz.picture.model.dto.pictureTagRelInfo.PictureTagRelInfoQuery;
import com.lz.picture.model.vo.pictureTagRelInfo.PictureTagRelInfoVo;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;


/**
 * 图片标签关联Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureTagRelInfoServiceImpl extends ServiceImpl<PictureTagRelInfoMapper, PictureTagRelInfo> implements IPictureTagRelInfoService {
    @Resource
    private PictureTagRelInfoMapper pictureTagRelInfoMapper;

    //region mybatis代码

    /**
     * 查询图片标签关联
     *
     * @param pictureId 图片标签关联主键
     * @return 图片标签关联
     */
    @Override
    public PictureTagRelInfo selectPictureTagRelInfoByPictureId(String pictureId) {
        return pictureTagRelInfoMapper.selectPictureTagRelInfoByPictureId(pictureId);
    }

    /**
     * 查询图片标签关联列表
     *
     * @param pictureTagRelInfo 图片标签关联
     * @return 图片标签关联
     */
    @Override
    public List<PictureTagRelInfo> selectPictureTagRelInfoList(PictureTagRelInfo pictureTagRelInfo) {
        return pictureTagRelInfoMapper.selectPictureTagRelInfoList(pictureTagRelInfo);
    }

    /**
     * 新增图片标签关联
     *
     * @param pictureTagRelInfo 图片标签关联
     * @return 结果
     */
    @Override
    public int insertPictureTagRelInfo(PictureTagRelInfo pictureTagRelInfo) {
        return pictureTagRelInfoMapper.insertPictureTagRelInfo(pictureTagRelInfo);
    }

    /**
     * 修改图片标签关联
     *
     * @param pictureTagRelInfo 图片标签关联
     * @return 结果
     */
    @Override
    public int updatePictureTagRelInfo(PictureTagRelInfo pictureTagRelInfo) {
        return pictureTagRelInfoMapper.updatePictureTagRelInfo(pictureTagRelInfo);
    }

    /**
     * 批量删除图片标签关联
     *
     * @param pictureIds 需要删除的图片标签关联主键
     * @return 结果
     */
    @Override
    public int deletePictureTagRelInfoByPictureIds(String[] pictureIds) {
        return pictureTagRelInfoMapper.deletePictureTagRelInfoByPictureIds(pictureIds);
    }

    /**
     * 删除图片标签关联信息
     *
     * @param pictureId 图片标签关联主键
     * @return 结果
     */
    @Override
    public int deletePictureTagRelInfoByPictureId(String pictureId) {
        return pictureTagRelInfoMapper.deletePictureTagRelInfoByPictureId(pictureId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureTagRelInfo> getQueryWrapper(PictureTagRelInfoQuery pictureTagRelInfoQuery) {
        QueryWrapper<PictureTagRelInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureTagRelInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String pictureId = pictureTagRelInfoQuery.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId), "picture_id", pictureId);

        String tagId = pictureTagRelInfoQuery.getTagId();
        queryWrapper.eq(StringUtils.isNotEmpty(tagId), "tag_id", tagId);

        return queryWrapper;
    }

    @Override
    public List<PictureTagRelInfoVo> convertVoList(List<PictureTagRelInfo> pictureTagRelInfoList) {
        if (StringUtils.isEmpty(pictureTagRelInfoList)) {
            return Collections.emptyList();
        }
        return pictureTagRelInfoList.stream().map(PictureTagRelInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public List<String> getPictureTagNames(String pictureId) {
        List<PictureTagRelInfo> pictureTagRelInfoList = this.list(new LambdaQueryWrapper<PictureTagRelInfo>()
                .eq(PictureTagRelInfo::getPictureId, pictureId));
        return pictureTagRelInfoList.stream()
                .map(PictureTagRelInfo::getTagName)
                .filter(StringUtils::isNotEmpty)
                .toList();
    }

    @Override
    public String getPictureTagNamesStr(String pictureId) {
        List<String> pictureTagNames = getPictureTagNames(pictureId);
        return String.join(COMMON_SEPARATOR, pictureTagNames);
    }

}
