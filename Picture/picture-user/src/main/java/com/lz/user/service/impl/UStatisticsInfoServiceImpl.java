package com.lz.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomSort;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.user.mapper.UStatisticsInfoMapper;
import com.lz.user.model.domain.UStatisticsInfo;
import com.lz.user.model.dto.uStatisticsInfo.UStatisticsInfoQuery;
import com.lz.user.model.vo.uStatisticsInfo.UStatisticsInfoVo;
import com.lz.user.service.IUStatisticsInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计信息Service业务层处理
 *
 * @author YY
 * @date 2025-09-09
 */
@Service
public class UStatisticsInfoServiceImpl extends ServiceImpl<UStatisticsInfoMapper, UStatisticsInfo> implements IUStatisticsInfoService {
    @Resource
    private UStatisticsInfoMapper uStatisticsInfoMapper;

    //region mybatis代码

    /**
     * 查询统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 统计信息
     */
    @Override
    public UStatisticsInfo selectUStatisticsInfoByStatisticsId(String statisticsId) {
        return uStatisticsInfoMapper.selectUStatisticsInfoByStatisticsId(statisticsId);
    }

    /**
     * 查询统计信息列表
     *
     * @param uStatisticsInfo 统计信息
     * @return 统计信息
     */
    @CustomSort(sortFields = {"statisticsName", "commonKey", "statisticsKey", "createTime"},
            sortMappingFields = {"statistics_name", "common_key", "statistics_key", "create_time"})
    @Override
    public List<UStatisticsInfo> selectUStatisticsInfoList(UStatisticsInfo uStatisticsInfo) {
        return uStatisticsInfoMapper.selectUStatisticsInfoList(uStatisticsInfo);
    }

    /**
     * 新增统计信息
     *
     * @param uStatisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int insertUStatisticsInfo(UStatisticsInfo uStatisticsInfo) {
        uStatisticsInfo.setCreateTime(DateUtils.getNowDate());
        return uStatisticsInfoMapper.insertUStatisticsInfo(uStatisticsInfo);
    }

    /**
     * 修改统计信息
     *
     * @param uStatisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int updateUStatisticsInfo(UStatisticsInfo uStatisticsInfo) {
        return uStatisticsInfoMapper.updateUStatisticsInfo(uStatisticsInfo);
    }

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的统计信息主键
     * @return 结果
     */
    @Override
    public int deleteUStatisticsInfoByStatisticsIds(String[] statisticsIds) {
        return uStatisticsInfoMapper.deleteUStatisticsInfoByStatisticsIds(statisticsIds);
    }

    /**
     * 删除统计信息信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    @Override
    public int deleteUStatisticsInfoByStatisticsId(String statisticsId) {
        return uStatisticsInfoMapper.deleteUStatisticsInfoByStatisticsId(statisticsId);
    }

    //endregion
    @Override
    public QueryWrapper<UStatisticsInfo> getQueryWrapper(UStatisticsInfoQuery uStatisticsInfoQuery) {
        QueryWrapper<UStatisticsInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = uStatisticsInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String statisticsId = uStatisticsInfoQuery.getStatisticsId();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsId), "statistics_id", statisticsId);

        String type = uStatisticsInfoQuery.getType();
        queryWrapper.eq(StringUtils.isNotEmpty(type), "type", type);

        String statisticsName = uStatisticsInfoQuery.getStatisticsName();
        queryWrapper.like(StringUtils.isNotEmpty(statisticsName), "statistics_name", statisticsName);

        String commonKey = uStatisticsInfoQuery.getCommonKey();
        queryWrapper.like(StringUtils.isNotEmpty(commonKey), "common_key", commonKey);

        String statisticsKey = uStatisticsInfoQuery.getStatisticsKey();
        queryWrapper.like(StringUtils.isNotEmpty(statisticsKey), "statistics_key", statisticsKey);

        Long stages = uStatisticsInfoQuery.getStages();
        queryWrapper.eq(StringUtils.isNotNull(stages), "stages", stages);

        Date createTime = uStatisticsInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<UStatisticsInfoVo> convertVoList(List<UStatisticsInfo> uStatisticsInfoList) {
        if (StringUtils.isEmpty(uStatisticsInfoList)) {
            return Collections.emptyList();
        }
        return uStatisticsInfoList.stream().map(UStatisticsInfoVo::objToVo).collect(Collectors.toList());
    }

}
