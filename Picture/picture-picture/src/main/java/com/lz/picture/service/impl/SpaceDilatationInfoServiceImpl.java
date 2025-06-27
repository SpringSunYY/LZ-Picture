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
import com.lz.picture.mapper.SpaceDilatationInfoMapper;
import com.lz.picture.model.domain.SpaceDilatationInfo;
import com.lz.picture.service.ISpaceDilatationInfoService;
import com.lz.picture.model.dto.spaceDilatationInfo.SpaceDilatationInfoQuery;
import com.lz.picture.model.vo.spaceDilatationInfo.SpaceDilatationInfoVo;

/**
 * 空间扩容信息Service业务层处理
 *
 * @author YY
 * @date 2025-06-28
 */
@Service
public class SpaceDilatationInfoServiceImpl extends ServiceImpl<SpaceDilatationInfoMapper, SpaceDilatationInfo> implements ISpaceDilatationInfoService
{
    @Resource
    private SpaceDilatationInfoMapper spaceDilatationInfoMapper;

    //region mybatis代码
    /**
     * 查询空间扩容信息
     *
     * @param dilatationId 空间扩容信息主键
     * @return 空间扩容信息
     */
    @Override
    public SpaceDilatationInfo selectSpaceDilatationInfoByDilatationId(String dilatationId)
    {
        return spaceDilatationInfoMapper.selectSpaceDilatationInfoByDilatationId(dilatationId);
    }

    /**
     * 查询空间扩容信息列表
     *
     * @param spaceDilatationInfo 空间扩容信息
     * @return 空间扩容信息
     */
    @Override
    public List<SpaceDilatationInfo> selectSpaceDilatationInfoList(SpaceDilatationInfo spaceDilatationInfo)
    {
        return spaceDilatationInfoMapper.selectSpaceDilatationInfoList(spaceDilatationInfo);
    }

    /**
     * 新增空间扩容信息
     *
     * @param spaceDilatationInfo 空间扩容信息
     * @return 结果
     */
    @Override
    public int insertSpaceDilatationInfo(SpaceDilatationInfo spaceDilatationInfo)
    {
        spaceDilatationInfo.setCreateTime(DateUtils.getNowDate());
        return spaceDilatationInfoMapper.insertSpaceDilatationInfo(spaceDilatationInfo);
    }

    /**
     * 修改空间扩容信息
     *
     * @param spaceDilatationInfo 空间扩容信息
     * @return 结果
     */
    @Override
    public int updateSpaceDilatationInfo(SpaceDilatationInfo spaceDilatationInfo)
    {
        return spaceDilatationInfoMapper.updateSpaceDilatationInfo(spaceDilatationInfo);
    }

    /**
     * 批量删除空间扩容信息
     *
     * @param dilatationIds 需要删除的空间扩容信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceDilatationInfoByDilatationIds(String[] dilatationIds)
    {
        return spaceDilatationInfoMapper.deleteSpaceDilatationInfoByDilatationIds(dilatationIds);
    }

    /**
     * 删除空间扩容信息信息
     *
     * @param dilatationId 空间扩容信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceDilatationInfoByDilatationId(String dilatationId)
    {
        return spaceDilatationInfoMapper.deleteSpaceDilatationInfoByDilatationId(dilatationId);
    }
    //endregion
    @Override
    public QueryWrapper<SpaceDilatationInfo> getQueryWrapper(SpaceDilatationInfoQuery spaceDilatationInfoQuery){
        QueryWrapper<SpaceDilatationInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = spaceDilatationInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String dilatationId = spaceDilatationInfoQuery.getDilatationId();
        queryWrapper.eq(StringUtils.isNotEmpty(dilatationId) ,"dilatation_id",dilatationId);

    String dilatationKey = spaceDilatationInfoQuery.getDilatationKey();
        queryWrapper.eq(StringUtils.isNotEmpty(dilatationKey) ,"dilatation_key",dilatationKey);

    String spaceId = spaceDilatationInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId) ,"space_id",spaceId);

    String spaceName = spaceDilatationInfoQuery.getSpaceName();
        queryWrapper.like(StringUtils.isNotEmpty(spaceName) ,"space_name",spaceName);

    String dilatationType = spaceDilatationInfoQuery.getDilatationType();
        queryWrapper.eq(StringUtils.isNotEmpty(dilatationType) ,"dilatation_type",dilatationType);

    Long pointsTotal = spaceDilatationInfoQuery.getPointsTotal();
        queryWrapper.eq( StringUtils.isNotNull(pointsTotal),"points_total",pointsTotal);

    String userId = spaceDilatationInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    Date createTime = spaceDilatationInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<SpaceDilatationInfoVo> convertVoList(List<SpaceDilatationInfo> spaceDilatationInfoList) {
        if (StringUtils.isEmpty(spaceDilatationInfoList)) {
            return Collections.emptyList();
        }
        return spaceDilatationInfoList.stream().map(SpaceDilatationInfoVo::objToVo).collect(Collectors.toList());
    }

}
