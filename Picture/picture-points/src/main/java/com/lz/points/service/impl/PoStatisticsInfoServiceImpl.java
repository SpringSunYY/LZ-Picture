package com.lz.points.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.core.domain.statistics.ro.StatisticsRo;
import com.lz.common.core.domain.statistics.vo.BarStatisticsVo;
import com.lz.common.core.domain.statistics.vo.LineStatisticsVo;
import com.lz.common.core.domain.statistics.vo.StatisticsVo;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.reflect.ReflectUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.common.utils.verify.DateVerifyUtils;
import com.lz.points.mapper.PoStatisticsInfoMapper;
import com.lz.points.model.domain.PoStatisticsInfo;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.lz.points.model.dto.poStatisticsInfo.PoStatisticsInfoQuery;
import com.lz.points.model.dto.statistics.*;
import com.lz.points.model.enums.PoPointsUsageTypeEnum;
import com.lz.points.model.enums.PoStatisticsTypeEnum;
import com.lz.points.model.vo.poStatisticsInfo.PoStatisticsInfoVo;
import com.lz.points.service.IPoStatisticsInfoService;
import com.lz.points.service.IPointsRechargePackageInfoService;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.points.PointsStatisticsConstants.*;

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

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private IPointsRechargePackageInfoService pointsRechargePackageInfoService;

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

    /**
     * 构建统计
     *
     * @param commonKey      统计的通用key
     * @param typeEnum       统计类型枚举
     * @param statisticsName 统计名称
     * @param request        请求参数
     * @param roClass        返回结果对象
     * @param queryFunction  查询方法
     * @param resultBuilder  构建结果方法
     * @return R
     * @author: YY
     * @method: buildRangeStatistics
     * @date: 2025/9/23 23:30
     **/
    private <REQ, RO, R> R buildRangeStatistics(
            String commonKey,
            PoStatisticsTypeEnum typeEnum,
            String statisticsName,
            REQ request,
            Class<RO> roClass,
            BiFunction<REQ, String, List<RO>> queryFunction,
            BiFunction<List<RO>, List<String>, R> resultBuilder
    ) {
        // 拿到开始结束时间
        String startDate = (String) ReflectUtils.getFieldValue(request, "startDate");
        String endDate = (String) ReflectUtils.getFieldValue(request, "endDate");
        Date nowDate = DateVerifyUtils.checkDateIsStartAfter(startDate, endDate);
        List<String> dateRanges = DateUtils.getDateRanges(startDate, endDate);
        if (dateRanges == null || dateRanges.isEmpty()) {
            return resultBuilder.apply(new ArrayList<>(), dateRanges);
        }

        String end = dateRanges.getLast();
        String today = DateUtils.dateTime(nowDate);
        boolean containsToday = dateRanges.contains(today);

        // 所有结果
        List<RO> resultList = new ArrayList<>();

        // 1. 今天的数据
        if (containsToday) {
            ReflectUtils.setFieldValue(request, "startDate", today);
            ReflectUtils.setFieldValue(request, "endDate", today);
            List<RO> todayList = queryFunction.apply(request, today);
            resultList.addAll(todayList);
            if (dateRanges.size() == 1) {
                return resultBuilder.apply(resultList, dateRanges);
            }
            dateRanges.removeLast();
            end = dateRanges.getLast();
        }

        // 2. 历史数据（从统计表里取）
        List<PoStatisticsInfo> statisticsInfos =
                getPoStatisticsInfosByDateAndKeyType(startDate, end, typeEnum.getValue(), commonKey);
        List<String> noStatisticsDates = new ArrayList<>(dateRanges);
        for (PoStatisticsInfo info : statisticsInfos) {
            String currentDate = DateUtils.dateTime(info.getCreateTime());
            noStatisticsDates.remove(currentDate);
            String content = info.getContent();
            if (StringUtils.isNotEmpty(content)) {
                // 获取泛型类型参数
                // 由于泛型擦除，我们需要通过其他方式获取类型信息
                List<RO> list = JSONArray.parseArray(info.getContent(), roClass);
                resultList.addAll(list);
            }
        }

        // 3. 没有统计过的 → 现查 DB + 插入统计表
        if (!noStatisticsDates.isEmpty()) {
            List<PoStatisticsInfo> newInfos = new ArrayList<>();
            for (String currentDate : noStatisticsDates) {
                ReflectUtils.setFieldValue(request, "startDate", currentDate);
                ReflectUtils.setFieldValue(request, "endDate", currentDate);
                List<RO> list = queryFunction.apply(request, currentDate);
                resultList.addAll(list);

                PoStatisticsInfo newInfo = getPoStatisticsInfo(
                        currentDate,
                        list,
                        typeEnum.getValue(),
                        statisticsName,
                        commonKey,
                        1L
                );
                newInfos.add(newInfo);
            }
            poStatisticsInfoMapper.insert(newInfos);
        }

        // 4. 统一构建结果
        return resultBuilder.apply(resultList, dateRanges);
    }

    @Override
    @CustomCacheable(keyPrefix = POINTS_STATISTICS_POINTS_USAGE_TYPE,
            expireTime = POINTS_STATISTICS_POINTS_USAGE_TYPE_EXPIRE_TIME,
            useQueryParamsAsKey = true)
    public List<StatisticsVo> pointsUsageTypeStatistics(PointsUsageLogStatisticsRequest request) {
        return buildRangeStatistics(
                POINTS_STATISTICS_POINTS_USAGE_TYPE,
                PoStatisticsTypeEnum.STATISTICS_TYPE_1,
                POINTS_STATISTICS_POINTS_USAGE_TYPE_NAME,
                request,
                PointsUsageLogStatisticsRo.class,
                (req, date) -> getPointsUsageTypeStatistics(req),
                (resultList, dateRanges) -> builderPointsUsageTypeStatisticsResult(resultList)
        );
    }


    /**
     * 构建积分使用类型统计结果
     *
     * @param userRegisterStatisticsResult 结果
     * @return
     */
    private List<StatisticsVo> builderPointsUsageTypeStatisticsResult(List<PointsUsageLogStatisticsRo> userRegisterStatisticsResult) {
        if (StringUtils.isEmpty(userRegisterStatisticsResult)) {
            return new ArrayList<>();
        }
        //统计返回map，存放结果
        HashMap<String, Long> resultMap = new HashMap<>();
//        System.out.println("userRegisterStatisticsResult = " + userRegisterStatisticsResult);
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

    @CustomCacheable(keyPrefix = POINTS_STATISTICS_POINTS_USAGE,
            expireTime = POINTS_STATISTICS_POINTS_USAGE_EXPIRE_TIME,
            useQueryParamsAsKey = true)
    @Override
    public LineStatisticsVo pointsUsageStatistics(PointsUsageLogStatisticsRequest request) {
        return buildRangeStatistics(
                POINTS_STATISTICS_POINTS_USAGE,
                PoStatisticsTypeEnum.STATISTICS_TYPE_3,
                POINTS_STATISTICS_POINTS_USAGE_NAME,
                request,
                StatisticsRo.class,
                (req, date) -> {
                    List<StatisticsRo> statisticsRos = poStatisticsInfoMapper.pointsUsageStatistics(req);
                    //因为消费是负数，所以这里取负数
                    return statisticsRos.stream().peek(statisticsRo -> {
                        //因为消费是负数，所以这里取负数
                        statisticsRo.setTotal(-statisticsRo.getTotal());
                    }).collect(Collectors.toList());
                },
                this::builderLineStatisticsVoResult
        );
    }

    /**
     * 构建积分使用统计结果
     *
     * @param statisticsRos 统计结果
     * @param dateRanges    日期范围
     * @return LineStatisticsVo
     */
    private LineStatisticsVo builderLineStatisticsVoResult(List<StatisticsRo> statisticsRos, List<String> dateRanges) {
        //根据时间升序
        statisticsRos.sort(Comparator.comparing(StatisticsRo::getName));
        LinkedHashMap<String, Long> resultMap = new LinkedHashMap<>();
        for (String dateRange : dateRanges) {
            resultMap.put(dateRange, 0L);
        }
        for (StatisticsRo statisticsRo : statisticsRos) {
            resultMap.put(statisticsRo.getName(), statisticsRo.getTotal());
        }
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Long> values = new ArrayList<>();
        resultMap.forEach((dateRange, total) -> {
            names.add(dateRange);
            values.add(total);
        });
        return new LineStatisticsVo(names, values);
    }

    @Override
    @CustomCacheable(keyPrefix = POINTS_STATISTICS_USER_CHARGE_RANKING,
            expireTime = POINTS_STATISTICS_USER_CHARGE_RANKING_EXPIRE_TIME,
            useQueryParamsAsKey = true)
    public BarStatisticsVo pointsOrderRankStatistics(PaymentOrderStatisticsRequest request) {
        return buildRangeStatistics(
                POINTS_STATISTICS_USER_CHARGE_RANKING,
                PoStatisticsTypeEnum.STATISTICS_TYPE_2,
                POINTS_STATISTICS_USER_CHARGE_RANKING_NAME,
                request,
                PaymentOrderStatisticsRo.class,
                (req, date) -> {
                    List<PaymentOrderStatisticsRo> list = poStatisticsInfoMapper.pointsOrderRankStatistics(req);
                    processPointsOrderRankRo(list);
                    return list;
                },
                (resultList, dateRanges) -> builderPointsOrderRankResult(resultList)
        );
    }


    /**
     * 处理查询到的充值排名结果
     *
     * @param pointsUsageLogStatisticsRos 统计结果
     */
    private void processPointsOrderRankRo(List<PaymentOrderStatisticsRo> pointsUsageLogStatisticsRos) {
        if (StringUtils.isEmpty(pointsUsageLogStatisticsRos)) {
            return;
        }
        for (PaymentOrderStatisticsRo pointsUsageLogStatisticsRo : pointsUsageLogStatisticsRos) {
            if (StringUtils.isNotEmpty(pointsUsageLogStatisticsRo.getName())) {
                continue;
            }
            UserInfo userInfo = userInfoService.selectUserInfoByUserId(pointsUsageLogStatisticsRo.getData());
            if (StringUtils.isNotNull(userInfo)) {
                pointsUsageLogStatisticsRo.setName(userInfo.getUserName());
            }
        }
    }

    /**
     * 构建用户充值排名统计结果
     *
     * @param statisticsRos 统计结果
     * @return BarStatisticsVo
     */
    private BarStatisticsVo builderPointsOrderRankResult(List<PaymentOrderStatisticsRo> statisticsRos) {
        if (StringUtils.isEmpty(statisticsRos)) {
            return new BarStatisticsVo();
        }
        List<String> names = new ArrayList<>();
        List<Long> totals = new ArrayList<>();
        for (PaymentOrderStatisticsRo statisticsRo : statisticsRos) {
            names.add(statisticsRo.getName());
            totals.add(statisticsRo.getValue());
        }
        return new BarStatisticsVo(names, totals);
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

    @Override
    public BarStatisticsVo pointsRechargePackageRankStatistics(PointsRechargeStatisticsRequest request) {
        return buildRangeStatistics(
                POINTS_STATISTICS_USER_CHARGE_PACKAGE_RANKING,
                PoStatisticsTypeEnum.STATISTICS_TYPE_6,
                POINTS_STATISTICS_USER_CHARGE_PACKAGE_RANKING_NAME,
                request,
                PointsRechargeStatisticsRo.class,
                (req, date) -> {
                    List<PointsRechargeStatisticsRo> list = poStatisticsInfoMapper.pointsRechargePackageRankStatistics(req);
                    processPointsRechargePackageRo(list);
                    return list;
                },
                (resultList, dateRanges) -> builderPointsRechargePackageRankResult(resultList)
        );
    }

    /**
     * 构建用户充值排名统计结果
     *
     * @param resultList 统计结果
     * @return BarStatisticsVo
     */
    private BarStatisticsVo builderPointsRechargePackageRankResult(List<PointsRechargeStatisticsRo> resultList) {
        if (StringUtils.isEmpty(resultList)) {
            return new BarStatisticsVo();
        }
        List<String> names = new ArrayList<>();
        List<Long> totals = new ArrayList<>();
        for (PointsRechargeStatisticsRo statisticsRo : resultList) {
            names.add(statisticsRo.getName());
            totals.add(statisticsRo.getValue());
        }
        return new BarStatisticsVo(names, totals);
    }

    /**
     * 处理查询到的充值排名结果
     *
     * @param list 统计结果
     */
    private void processPointsRechargePackageRo(List<PointsRechargeStatisticsRo> list) {
        if (StringUtils.isEmpty(list)) {
            return;
        }
        for (PointsRechargeStatisticsRo pointsRechargeStatisticsRo : list) {
            if (StringUtils.isNotEmpty(pointsRechargeStatisticsRo.getName())) {
                continue;
            }
            PointsRechargePackageInfo packageInfo = pointsRechargePackageInfoService.selectPointsRechargePackageInfoByPackageId(pointsRechargeStatisticsRo.getData());
            if (StringUtils.isNotNull(packageInfo)) {
                pointsRechargeStatisticsRo.setName(packageInfo.getPackageName());
            }
        }
    }
    //endregion

}
