package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.SpaceInfoMapper;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoQuery;
import com.lz.picture.model.vo.spaceInfo.SpaceInfoVo;

/**
 * 空间信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class SpaceInfoServiceImpl extends ServiceImpl<SpaceInfoMapper, SpaceInfo> implements ISpaceInfoService
{
    @Resource
    private SpaceInfoMapper spaceInfoMapper;

    //region mybatis代码
    /**
     * 查询空间信息
     *
     * @param spaceId 空间信息主键
     * @return 空间信息
     */
    @Override
    public SpaceInfo selectSpaceInfoBySpaceId(String spaceId)
    {
        return spaceInfoMapper.selectSpaceInfoBySpaceId(spaceId);
    }

    /**
     * 查询空间信息列表
     *
     * @param spaceInfo 空间信息
     * @return 空间信息
     */
    @Override
    public List<SpaceInfo> selectSpaceInfoList(SpaceInfo spaceInfo)
    {
        return spaceInfoMapper.selectSpaceInfoList(spaceInfo);
    }

    /**
     * 新增空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    @Override
    public int insertSpaceInfo(SpaceInfo spaceInfo)
    {
        spaceInfo.setCreateTime(DateUtils.getNowDate());
        return spaceInfoMapper.insertSpaceInfo(spaceInfo);
    }

    /**
     * 修改空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    @Override
    public int updateSpaceInfo(SpaceInfo spaceInfo)
    {
      spaceInfo.setUpdateTime(DateUtils.getNowDate());
        return spaceInfoMapper.updateSpaceInfo(spaceInfo);
    }

    /**
     * 批量删除空间信息
     *
     * @param spaceIds 需要删除的空间信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceInfoBySpaceIds(String[] spaceIds)
    {
        return spaceInfoMapper.deleteSpaceInfoBySpaceIds(spaceIds);
    }

    /**
     * 删除空间信息信息
     *
     * @param spaceId 空间信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceInfoBySpaceId(String spaceId)
    {
        return spaceInfoMapper.deleteSpaceInfoBySpaceId(spaceId);
    }
    //endregion
    @Override
    public QueryWrapper<SpaceInfo> getQueryWrapper(SpaceInfoQuery spaceInfoQuery){
        QueryWrapper<SpaceInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = spaceInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String spaceId = spaceInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId) ,"space_id",spaceId);

    String spaceName = spaceInfoQuery.getSpaceName();
        queryWrapper.like(StringUtils.isNotEmpty(spaceName) ,"space_name",spaceName);

    String ossType = spaceInfoQuery.getOssType();
        queryWrapper.eq(StringUtils.isNotEmpty(ossType) ,"oss_type",ossType);

    String userId = spaceInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String spaceStatus = spaceInfoQuery.getSpaceStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceStatus) ,"space_status",spaceStatus);

    String spaceType = spaceInfoQuery.getSpaceType();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceType) ,"space_type",spaceType);

    Date createTime = spaceInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date lastUpdateTime = spaceInfoQuery.getLastUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginLastUpdateTime"))&&StringUtils.isNotNull(params.get("endLastUpdateTime")),"last_update_time",params.get("beginLastUpdateTime"),params.get("endLastUpdateTime"));

    Date updateTime = spaceInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

    String isDelete = spaceInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete) ,"is_delete",isDelete);

    Date deletedTime = spaceInfoQuery.getDeletedTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginDeletedTime"))&&StringUtils.isNotNull(params.get("endDeletedTime")),"deleted_time",params.get("beginDeletedTime"),params.get("endDeletedTime"));

        return queryWrapper;
    }

    @Override
    public List<SpaceInfoVo> convertVoList(List<SpaceInfo> spaceInfoList) {
        if (StringUtils.isEmpty(spaceInfoList)) {
            return Collections.emptyList();
        }
        return spaceInfoList.stream().map(SpaceInfoVo::objToVo).collect(Collectors.toList());
    }

}
