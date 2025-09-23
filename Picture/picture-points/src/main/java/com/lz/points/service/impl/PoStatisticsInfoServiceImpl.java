package com.lz.points.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.core.domain.statistics.vo.RadarStatisticsVo;
import com.lz.common.core.domain.statistics.vo.StatisticsVo;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.common.utils.verify.DateVerifyUtils;
import com.lz.points.mapper.PoStatisticsInfoMapper;
import com.lz.points.model.domain.PoStatisticsInfo;
import com.lz.points.model.dto.poStatisticsInfo.PoStatisticsInfoQuery;
import com.lz.points.model.dto.statistics.PointsUsageLogStatisticsRequest;
import com.lz.points.model.dto.statistics.PointsUsageLogStatisticsRo;
import com.lz.points.model.enums.PoPointsUsageTypeEnum;
import com.lz.points.model.enums.PoStatisticsTypeEnum;
import com.lz.points.model.vo.poStatisticsInfo.PoStatisticsInfoVo;
import com.lz.points.service.IPoStatisticsInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.points.PointsStatisticsConstants.POINTS_STATISTICS_POINTS_USAGE_TYPE;
import static com.lz.common.constant.points.PointsStatisticsConstants.POINTS_STATISTICS_POINTS_USAGE_TYPE_NAME;

/**
 * 统计信息Service业务层处理
 *
 * @author YY
 * @date 2025-09-23
 */
@Service
public class PoStatisticsInfoServiceImpl extends ServiceImpl<PoStatisticsInfoMapper, PoStatisticsInfo> implements IPoStatisticsInfoService {
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
    public PoStatisticsInfo selectPoStatisticsInfoByStatisticsId(String statisticsId) {
        return poStatisticsInfoMapper.selectPoStatisticsInfoByStatisticsId(statisticsId);
    }

    /**
     * 查询统计信息列表
     *
     * @param poStatisticsInfo 统计信息
     * @return 统计信息
     */
    @Override
    public List<PoStatisticsInfo> selectPoStatisticsInfoList(PoStatisticsInfo poStatisticsInfo) {
        return poStatisticsInfoMapper.selectPoStatisticsInfoList(poStatisticsInfo);
    }

    /**
     * 新增统计信息
     *
     * @param poStatisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int insertPoStatisticsInfo(PoStatisticsInfo poStatisticsInfo) {
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
    public int updatePoStatisticsInfo(PoStatisticsInfo poStatisticsInfo) {
        return poStatisticsInfoMapper.updatePoStatisticsInfo(poStatisticsInfo);
    }

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的统计信息主键
     * @return 结果
     */
    @Override
    public int deletePoStatisticsInfoByStatisticsIds(String[] statisticsIds) {
        return poStatisticsInfoMapper.deletePoStatisticsInfoByStatisticsIds(statisticsIds);
    }

    /**
     * 删除统计信息信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    @Override
    public int deletePoStatisticsInfoByStatisticsId(String statisticsId) {
        return poStatisticsInfoMapper.deletePoStatisticsInfoByStatisticsId(statisticsId);
    }

    //endregion
    @Override
    public QueryWrapper<PoStatisticsInfo> getQueryWrapper(PoStatisticsInfoQuery poStatisticsInfoQuery) {
        QueryWrapper<PoStatisticsInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = poStatisticsInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String statisticsId = poStatisticsInfoQuery.getStatisticsId();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsId), "statistics_id", statisticsId);

        String type = poStatisticsInfoQuery.getType();
        queryWrapper.eq(StringUtils.isNotEmpty(type), "type", type);

        String statisticsName = poStatisticsInfoQuery.getStatisticsName();
        queryWrapper.like(StringUtils.isNotEmpty(statisticsName), "statistics_name", statisticsName);

        String commonKey = poStatisticsInfoQuery.getCommonKey();
        queryWrapper.eq(StringUtils.isNotEmpty(commonKey), "common_key", commonKey);

        String statisticsKey = poStatisticsInfoQuery.getStatisticsKey();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsKey), "statistics_key", statisticsKey);

        Long stages = poStatisticsInfoQuery.getStages();
        queryWrapper.eq(StringUtils.isNotNull(stages), "stages", stages);

        Date createTime = poStatisticsInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<PoStatisticsInfoVo> convertVoList(List<PoStatisticsInfo> poStatisticsInfoList) {
        if (StringUtils.isEmpty(poStatisticsInfoList)) {
            return Collections.emptyList();
        }
        return poStatisticsInfoList.stream().map(PoStatisticsInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public List<PoStatisticsInfo> getPoStatisticsInfosByDateAndKeyType(String startDate, String end, String type, String commonKey) {
        return this.list(new LambdaQueryWrapper<PoStatisticsInfo>()
                .eq(PoStatisticsInfo::getType, type)
                .eq(PoStatisticsInfo::getCommonKey, commonKey)
                .apply("date_format(create_time,'%Y-%m-%d') between {0} and {1}", startDate, end)
        );
    }

    //region 统计
    @Override
    public List<StatisticsVo> pointsUsageTypeStatistics(PointsUsageLogStatisticsRequest request) {
        //拿到开始结束时间
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        Date nowDate = DateVerifyUtils.checkDateIsStartAfter(startDate, endDate);
        List<String> dateRanges = DateUtils.getDateRanges(startDate, endDate);
        //如果为空查询全部
        if (StringUtils.isEmpty(dateRanges) || dateRanges == null) {
            return new ArrayList<>();
        }
        //灵活判断最近天数
        String end = dateRanges.getLast();
        //如果包含的日期有今天，则查询今天
        //是否包含今天
        String today = DateUtils.dateTime(nowDate);
        boolean containsToday = dateRanges.contains(today);

        //获取结果
        List<PointsUsageLogStatisticsRo> userRegisterStatisticsResult = getPointsUsageTypeStatistics(request);
        if (containsToday) {
            request.setStartDate(today);
            request.setEndDate(end);
            //查询今天
            List<PointsUsageLogStatisticsRo> userRegisterStatistics = getPointsUsageTypeStatistics(request);
            userRegisterStatisticsResult.addAll(userRegisterStatistics);
            //如果包含了且范围只有1，就表示统计今天
            if (dateRanges.size() == 1) {
                return builderPointsUsageTypeStatisticsResult(userRegisterStatisticsResult);
            }
            //删除今天,添加倒数第二天为最后一天,今天就是最后一天
            dateRanges.removeLast();
            end = dateRanges.getLast();
        }
        List<PoStatisticsInfo> statisticsInfos = getPoStatisticsInfosByDateAndKeyType(startDate, end, PoStatisticsTypeEnum.STATISTICS_TYPE_1.getValue(), POINTS_STATISTICS_POINTS_USAGE_TYPE);
        for (PoStatisticsInfo statisticsInfo : statisticsInfos) {
            String currentDate = DateUtils.dateTime(statisticsInfo.getCreateTime());
            dateRanges.remove(currentDate);
            List<PointsUsageLogStatisticsRo> pointsUsageLogStatisticsRos = JSONArray.parseArray(statisticsInfo.getContent(), PointsUsageLogStatisticsRo.class);
            userRegisterStatisticsResult.addAll(pointsUsageLogStatisticsRos);
        }
        if (StringUtils.isNotEmpty(dateRanges)) {
            ArrayList<PoStatisticsInfo> noStaitsticss = new ArrayList<>();
            for (String currentDate : dateRanges) {
                request.setStartDate(currentDate);
                request.setEndDate(currentDate);
                List<PointsUsageLogStatisticsRo> pointsUsageLogStatisticsRos = getPointsUsageTypeStatistics(request);
                userRegisterStatisticsResult.addAll(pointsUsageLogStatisticsRos);
                PoStatisticsInfo statisticsInfo = getPoStatisticsInfo(currentDate, pointsUsageLogStatisticsRos, PoStatisticsTypeEnum.STATISTICS_TYPE_1.getValue(),
                        POINTS_STATISTICS_POINTS_USAGE_TYPE_NAME, POINTS_STATISTICS_POINTS_USAGE_TYPE, 1L);
                noStaitsticss.add(statisticsInfo);
            }
            poStatisticsInfoMapper.insert(noStaitsticss);
        }
        return builderPointsUsageTypeStatisticsResult(userRegisterStatisticsResult);
    }

    private List<StatisticsVo> builderPointsUsageTypeStatisticsResult(List<PointsUsageLogStatisticsRo> userRegisterStatisticsResult) {
        if (StringUtils.isEmpty(userRegisterStatisticsResult)) {
            return new ArrayList<>();
        }
        //统计返回map，存放结果
        HashMap<String, Long> resultMap = new HashMap<>();
        for (PointsUsageLogStatisticsRo userRegisterStatistics : userRegisterStatisticsResult) {
            if (resultMap.containsKey(userRegisterStatistics.getUsageType())) {
                resultMap.put(userRegisterStatistics.getUsageType(), resultMap.get(userRegisterStatistics.getUsageType()) + userRegisterStatistics.getValue());
            } else {
                resultMap.put(userRegisterStatistics.getUsageType(), userRegisterStatistics.getValue());
            }
        }
        //构建结果
        ArrayList<StatisticsVo> statisticsVos = new ArrayList<>();
        for (Map.Entry<String, Long> stringLongEntry : resultMap.entrySet()) {
            StatisticsVo statisticsVo = new StatisticsVo();
            Optional<PoPointsUsageTypeEnum> enumByValue = PoPointsUsageTypeEnum.getEnumByValue(stringLongEntry.getKey());
            if (enumByValue.isPresent()) {
                PoPointsUsageTypeEnum poPointsUsageTypeEnum = enumByValue.get();
                statisticsVo.setName(poPointsUsageTypeEnum.getLabel());
            } else {
                statisticsVo.setName(stringLongEntry.getKey());
            }
            statisticsVo.setValue(stringLongEntry.getValue());
            statisticsVos.add(statisticsVo);
        }
        return statisticsVos;
    }

    private List<PointsUsageLogStatisticsRo> getPointsUsageTypeStatistics(PointsUsageLogStatisticsRequest request) {
        return poStatisticsInfoMapper.pointsUsageTypeStatistics(request);
    }

    private static PoStatisticsInfo getPoStatisticsInfo(String date, Object content,
                                                        String type, String statisticsName,
                                                        String commonKey, Long stages) {
        PoStatisticsInfo statisticsInfo = new PoStatisticsInfo();
        statisticsInfo.setStatisticsId(IdUtils.snowflakeId().toString());
        statisticsInfo.setType(type);
        statisticsInfo.setStatisticsName(statisticsName + COMMON_SEPARATOR_CACHE + date);
        statisticsInfo.setCommonKey(commonKey);
        statisticsInfo.setStatisticsKey(commonKey + COMMON_SEPARATOR_CACHE + date);
        statisticsInfo.setStages(stages);
        statisticsInfo.setContent(JSONObject.toJSONString(content));
        statisticsInfo.setCreateTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD, date));
        return statisticsInfo;
    }

    //endregion

}
