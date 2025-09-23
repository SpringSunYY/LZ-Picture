package com.lz.points.service.impl;

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
import com.lz.points.mapper.PoStatisticsInfoMapper;
import com.lz.points.model.domain.PoStatisticsInfo;
import com.lz.points.service.IPoStatisticsInfoService;
import com.lz.points.model.dto.poStatisticsInfo.PoStatisticsInfoQuery;
import com.lz.points.model.vo.poStatisticsInfo.PoStatisticsInfoVo;

/**
 * 统计信息Service业务层处理
 *
 * @author YY
 * @date 2025-09-23
 */
@Service
public class PoStatisticsInfoServiceImpl extends ServiceImpl<PoStatisticsInfoMapper, PoStatisticsInfo> implements IPoStatisticsInfoService
{
    @Resource
    private PoStatisticsInfoMapper poStatisticsInfoMapper;

    //region mybatis代码
    /**
     * 查询统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 统计信息
     */
    @Override
    public PoStatisticsInfo selectPoStatisticsInfoByStatisticsId(String statisticsId)
    {
        return poStatisticsInfoMapper.selectPoStatisticsInfoByStatisticsId(statisticsId);
    }

    /**
     * 查询统计信息列表
     *
     * @param poStatisticsInfo 统计信息
     * @return 统计信息
     */
    @Override
    public List<PoStatisticsInfo> selectPoStatisticsInfoList(PoStatisticsInfo poStatisticsInfo)
    {
        return poStatisticsInfoMapper.selectPoStatisticsInfoList(poStatisticsInfo);
    }

    /**
     * 新增统计信息
     *
     * @param poStatisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int insertPoStatisticsInfo(PoStatisticsInfo poStatisticsInfo)
    {
        poStatisticsInfo.setCreateTime(DateUtils.getNowDate());
        return poStatisticsInfoMapper.insertPoStatisticsInfo(poStatisticsInfo);
    }

    /**
     * 修改统计信息
     *
     * @param poStatisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int updatePoStatisticsInfo(PoStatisticsInfo poStatisticsInfo)
    {
        return poStatisticsInfoMapper.updatePoStatisticsInfo(poStatisticsInfo);
    }

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的统计信息主键
     * @return 结果
     */
    @Override
    public int deletePoStatisticsInfoByStatisticsIds(String[] statisticsIds)
    {
        return poStatisticsInfoMapper.deletePoStatisticsInfoByStatisticsIds(statisticsIds);
    }

    /**
     * 删除统计信息信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    @Override
    public int deletePoStatisticsInfoByStatisticsId(String statisticsId)
    {
        return poStatisticsInfoMapper.deletePoStatisticsInfoByStatisticsId(statisticsId);
    }
    //endregion
    @Override
    public QueryWrapper<PoStatisticsInfo> getQueryWrapper(PoStatisticsInfoQuery poStatisticsInfoQuery){
        QueryWrapper<PoStatisticsInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = poStatisticsInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String statisticsId = poStatisticsInfoQuery.getStatisticsId();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsId) ,"statistics_id",statisticsId);

    String type = poStatisticsInfoQuery.getType();
        queryWrapper.eq(StringUtils.isNotEmpty(type) ,"type",type);

    String statisticsName = poStatisticsInfoQuery.getStatisticsName();
        queryWrapper.like(StringUtils.isNotEmpty(statisticsName) ,"statistics_name",statisticsName);

    String commonKey = poStatisticsInfoQuery.getCommonKey();
        queryWrapper.eq(StringUtils.isNotEmpty(commonKey) ,"common_key",commonKey);

    String statisticsKey = poStatisticsInfoQuery.getStatisticsKey();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsKey) ,"statistics_key",statisticsKey);

    Long stages = poStatisticsInfoQuery.getStages();
        queryWrapper.eq( StringUtils.isNotNull(stages),"stages",stages);

    Date createTime = poStatisticsInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<PoStatisticsInfoVo> convertVoList(List<PoStatisticsInfo> poStatisticsInfoList) {
        if (StringUtils.isEmpty(poStatisticsInfoList)) {
            return Collections.emptyList();
        }
        return poStatisticsInfoList.stream().map(PoStatisticsInfoVo::objToVo).collect(Collectors.toList());
    }

}
