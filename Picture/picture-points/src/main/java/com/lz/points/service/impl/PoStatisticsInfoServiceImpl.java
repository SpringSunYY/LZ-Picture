package com.lz.points.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
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
import com.lz.points.model.enums.PoPaymentTypeEnum;
import com.lz.points.model.enums.PoPointsUsageTypeEnum;
import com.lz.points.model.enums.PoStatisticsTypeEnum;
import com.lz.points.model.vo.poStatisticsInfo.PoStatisticsInfoVo;
import com.lz.points.model.vo.statistics.PaymentOrderMapStatisticsVo;
import com.lz.points.service.IPoStatisticsInfoService;
import com.lz.points.service.IPointsRechargePackageInfoService;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @CustomSort(sortFields = {"statisticsName", "commonKey", "statisticsKey", "createTime"},
            sortMappingFields = {"statistics_name", "common_key", "statistics_key", "create_time"})
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
        //统计的时间范围
        List<String> statisticsDateRanges = DateUtils.getDateRanges(startDate, endDate);
        if (statisticsDateRanges == null || statisticsDateRanges.isEmpty()) {
            return resultBuilder.apply(new ArrayList<>(), statisticsDateRanges);
        }
        //完整日期范围，为初始化数据做准备
        List<String> dateRanges=new ArrayList<>(statisticsDateRanges);

        String end = statisticsDateRanges.getLast();
        String today = DateUtils.dateTime(nowDate);
        boolean containsToday = statisticsDateRanges.contains(today);

        // 所有结果
        List<RO> resultList = new ArrayList<>();

        // 1. 今天的数据
        if (containsToday) {
            ReflectUtils.setFieldValue(request, "startDate", today);
            ReflectUtils.setFieldValue(request, "endDate", today);
            List<RO> todayList = queryFunction.apply(request, today);
            resultList.addAll(todayList);
            if (statisticsDateRanges.size() == 1) {
                return resultBuilder.apply(resultList, statisticsDateRanges);
            }
            statisticsDateRanges.removeLast();
            end = statisticsDateRanges.getLast();
        }

        // 2. 历史数据（从统计表里取）
        List<PoStatisticsInfo> statisticsInfos =
                getPoStatisticsInfosByDateAndKeyType(startDate, end, typeEnum.getValue(), commonKey);
        List<String> noStatisticsDates = new ArrayList<>(statisticsDateRanges);
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
                        statisticsRo.setValue(-statisticsRo.getValue());
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
            resultMap.put(statisticsRo.getName(), statisticsRo.getValue());
        }
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Long> values = new ArrayList<>();
        resultMap.forEach((dateRange, total) -> {
            names.add(dateRange);
            values.add(-total);
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
        LinkedHashMap<String, Long> resultMap = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        List<Long> totals = new ArrayList<>();
        for (PaymentOrderStatisticsRo statisticsRo : statisticsRos) {
            if (resultMap.containsKey(statisticsRo.getName())) {
                resultMap.put(statisticsRo.getName(), resultMap.get(statisticsRo.getName()) + statisticsRo.getValue());
            } else {
                resultMap.put(statisticsRo.getName(), statisticsRo.getValue());
            }
        }
        for (Map.Entry<String, Long> stringLongEntry : resultMap.entrySet()) {
            totals.add(stringLongEntry.getValue());
            names.add(stringLongEntry.getKey());
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

    @CustomCacheable(keyPrefix = POINTS_STATISTICS_USER_CHARGE_PACKAGE_RANKING,
            expireTime = POINTS_STATISTICS_USER_CHARGE_PACKAGE_RANKING_EXPIRE_TIME,
            useQueryParamsAsKey = true)
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
        LinkedHashMap<String, Long> resultMap = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        List<Long> totals = new ArrayList<>();
        for (PointsRechargeStatisticsRo statisticsRo : resultList) {
            if (resultMap.containsKey(statisticsRo.getName())) {
                resultMap.put(statisticsRo.getName(), resultMap.get(statisticsRo.getName()) + statisticsRo.getValue());
            } else {
                resultMap.put(statisticsRo.getName(), statisticsRo.getValue());
            }
        }
        for (Map.Entry<String, Long> stringLongEntry : resultMap.entrySet()) {
            totals.add(stringLongEntry.getValue());
            names.add(stringLongEntry.getKey());
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

    @CustomCacheable(keyPrefix = POINTS_STATISTICS_USER_CHARGE_PAYMENT,
            expireTime = POINTS_STATISTICS_USER_CHARGE_PAYMENT_EXPIRE_TIME,
            useQueryParamsAsKey = true)
    @Override
    public List<StatisticsVo> pointsPaymentTypeStatistics(PaymentOrderStatisticsRequest request) {
        return buildRangeStatistics(
                POINTS_STATISTICS_USER_CHARGE_PAYMENT,
                PoStatisticsTypeEnum.STATISTICS_TYPE_7,
                POINTS_STATISTICS_USER_CHARGE_PAYMENT_NAME,
                request,
                PointsRechargeStatisticsRo.class,
                (req, date) -> {
                    return poStatisticsInfoMapper.pointsPaymentTypeStatistics(req);
                },
                (resultList, dateRanges) -> builderPointPaymentResult(resultList)
        );
    }

    private List<StatisticsVo> builderPointPaymentResult(List<PointsRechargeStatisticsRo> resultList) {
        if (StringUtils.isEmpty(resultList)) {
            return new ArrayList<>();
        }
        HashMap<String, Long> resultMap = new HashMap<>();
        for (PointsRechargeStatisticsRo statisticsRo : resultList) {
            if (resultMap.containsKey(statisticsRo.getData())) {
                resultMap.put(statisticsRo.getData(), resultMap.get(statisticsRo.getData()) + statisticsRo.getValue());
            } else {
                resultMap.put(statisticsRo.getData(), statisticsRo.getValue());
            }
        }
        //遍历结果获取label、value
        List<StatisticsVo> statisticsVos = new ArrayList<>();
        for (Map.Entry<String, Long> entry : resultMap.entrySet()) {
            StatisticsVo statisticsVo = new StatisticsVo();
            Optional<PoPaymentTypeEnum> enumByValue = PoPaymentTypeEnum.getEnumByValue(entry.getKey());
            if (enumByValue.isPresent()) {
                statisticsVo.setName(enumByValue.get().getLabel());
            } else {
                statisticsVo.setName(entry.getKey());
            }
            statisticsVo.setValue(entry.getValue());
            statisticsVos.add(statisticsVo);
        }
        return statisticsVos;
    }

    @Override
    @CustomCacheable(keyPrefix = POINTS_STATISTICS_USER_PAYMENT_MAP,
            expireTime = POINTS_STATISTICS_USER_PAYMENT_MAP_EXPIRE_TIME,
            useQueryParamsAsKey = true)
    public List<PaymentOrderMapStatisticsVo> pointsOrderIpAddressStatistics(PaymentOrderMapStatisticsRequest request) {
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        String location = request.getIpAddress();
        Date nowDate = DateVerifyUtils.checkDateIsStartAfter(startDate, endDate);
        List<String> dateRanges = DateUtils.getDateRanges(startDate, endDate);
        //如果为空查询全部
        if (StringUtils.isEmpty(dateRanges) || dateRanges == null) {
            return new ArrayList<>();
        }
        String commonKey = "";
        boolean isChina = StringUtils.isEmpty(location) || location.equals("中国") || location.equals("中华人民共和国");
        if (isChina) {
            location = "";
            request.setIpAddress("");
            commonKey = POINTS_STATISTICS_USER_PAYMENT_MAP + COMMON_SEPARATOR_CACHE + "中国";
        } else {
            commonKey = POINTS_STATISTICS_USER_PAYMENT_MAP + COMMON_SEPARATOR_CACHE + location;
        }
        //此处不是准确的地理等级，而是空格级，空格前1级，空格后2级，因为一般是省份 城市，所以这样标注，
        // 存入数据库只是统计数据只是城市或者省份，默认1级
        //key-时间，value：key-省份
        Map<String, Map<String, PaymentOrderMapStatisticsRo>> countryMap = new HashMap<>();
        //key-时间，value：key-省份，value：key-城市
        Map<String, Map<String, Map<String, PaymentOrderMapStatisticsRo>>> provinceMap = new HashMap<>();
        //初始化数据
        for (String date : dateRanges) {
            countryMap.put(date, new HashMap<>());
            provinceMap.put(date, new HashMap<>());
        }
        //灵活判断最近天数
        String end = dateRanges.getLast();
        //是否包含今天
        String today = DateUtils.dateTime(nowDate);
        if (dateRanges.contains(today)) {
            List<PaymentOrderMapStatisticsRo> currentStatistics = getPointsOrderIpAddressStatistics(today, today, request);
            builderPointsOrderIpAddressStatistics(countryMap, provinceMap, currentStatistics);
            //如果包含了且范围只有1，就表示统计今天
            if (dateRanges.size() == 1) {
                return builderPointsOrderIpAddressResult(countryMap, provinceMap, isChina, location);
            }
            //删除今天,添加倒数第二天为最后一天,今天就是最后一天
            dateRanges.removeLast();
            end = dateRanges.getLast();
        }
        //查询昨天及以前的数据
        List<PoStatisticsInfo> statistics = getPoStatisticsInfosByDateAndKeyType(startDate, end,
                PoStatisticsTypeEnum.STATISTICS_TYPE_4.getValue(),
                commonKey);
        //没有统计的数据
        List<String> noStatistics = new ArrayList<>(dateRanges);
        for (PoStatisticsInfo poStatisticsInfo : statistics) {
            List<PaymentOrderMapStatisticsRo> statisticsRos = JSONArray.parseArray(poStatisticsInfo.getContent(), PaymentOrderMapStatisticsRo.class);
            builderPointsOrderIpAddressStatistics(countryMap, provinceMap, statisticsRos);
            noStatistics.remove(DateUtils.dateTime(poStatisticsInfo.getCreateTime()));
        }

        //如果有没有统计数据
        if (StringUtils.isNotEmpty(noStatistics)) {
            //key-时间，value：key-省份
            Map<String, Map<String, PaymentOrderMapStatisticsRo>> noCountryMap = new HashMap<>();
            //key-时间，value：key-省份，value：key-城市
            Map<String, Map<String, Map<String, PaymentOrderMapStatisticsRo>>> noProvinceMap = new HashMap<>();
            for (String currentDate : noStatistics) {
                noCountryMap.put(currentDate, new HashMap<>());
                noProvinceMap.put(currentDate, new HashMap<>());
                List<PaymentOrderMapStatisticsRo> pointsOrderIpAddressStatistics = getPointsOrderIpAddressStatistics(currentDate, currentDate, request);
                builderPointsOrderIpAddressStatistics(noCountryMap, noProvinceMap, pointsOrderIpAddressStatistics);
            }
            //遍历没有统计结果插入数据库
            ArrayList<PoStatisticsInfo> statisticsInfos = new ArrayList<>();
            for (Map.Entry<String, Map<String, PaymentOrderMapStatisticsRo>> entry : noCountryMap.entrySet()) {
                List<PaymentOrderMapStatisticsRo> list = entry.getValue().values().stream().toList();
                statisticsInfos.add(
                        getPoStatisticsInfo(entry.getKey(), list, PoStatisticsTypeEnum.STATISTICS_TYPE_4.getValue(),
                                POINTS_STATISTICS_USER_PAYMENT_MAP_NAME + COMMON_SEPARATOR_CACHE + "中国",
                                POINTS_STATISTICS_USER_PAYMENT_MAP + COMMON_SEPARATOR_CACHE + "中国", 1L)
                );
            }
            for (Map.Entry<String, Map<String, Map<String, PaymentOrderMapStatisticsRo>>> currentMap : noProvinceMap.entrySet()) {
                //key省份
                for (Map.Entry<String, Map<String, PaymentOrderMapStatisticsRo>> entry : currentMap.getValue().entrySet()) {
                    List<PaymentOrderMapStatisticsRo> list = entry.getValue().values().stream().toList();
                    statisticsInfos.add(
                            getPoStatisticsInfo(currentMap.getKey(), list, PoStatisticsTypeEnum.STATISTICS_TYPE_4.getValue(),
                                    POINTS_STATISTICS_USER_PAYMENT_MAP_NAME + COMMON_SEPARATOR_CACHE + entry.getKey(),
                                    POINTS_STATISTICS_USER_PAYMENT_MAP + COMMON_SEPARATOR_CACHE + entry.getKey(), 1L)
                    );
                }
            }
            poStatisticsInfoMapper.insert(statisticsInfos);
            //合并统计数据和没有统计数据
            countryMap.putAll(noCountryMap);
            provinceMap.putAll(noProvinceMap);
        }
        return builderPointsOrderIpAddressResult(countryMap, provinceMap, isChina, location);
    }

    private List<PaymentOrderMapStatisticsVo> builderPointsOrderIpAddressResult(Map<String, Map<String, PaymentOrderMapStatisticsRo>> countryMap,
                                                                                Map<String, Map<String, Map<String, PaymentOrderMapStatisticsRo>>> provinceMap,
                                                                                boolean isChina, String ipAddress) {
        //key-时间，key-省份，value，为什么只拿省份的，因为第一次国家统计是包含到了城市，
        // 当你统计了省份的时候，就只统计了省份，所以此处相当于是国家级别
        Map<String, Map<String, PaymentOrderMapStatisticsRo>> resultMap = new HashMap<>();
        resultMap = countryMap;

        //构建结果
        //根据时间排序
        LinkedHashMap<String, Map<String, PaymentOrderMapStatisticsRo>> sortMap = resultMap.entrySet()
                .stream().sorted(
                        Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2)
                                -> e1, LinkedHashMap::new));

        return sortMap.entrySet().stream().map(entry -> {
            PaymentOrderMapStatisticsVo vo = new PaymentOrderMapStatisticsVo();
            vo.setDate(entry.getKey());
            List<PaymentOrderMapStatisticsVo.Data> dataList = entry.getValue().entrySet().stream().map(entry1 -> {
                PaymentOrderMapStatisticsVo.Data data = new PaymentOrderMapStatisticsVo.Data();
                data.setIpAddress(entry1.getKey());
                data.setValue(entry1.getValue().getValue());
                data.setAmount(entry1.getValue().getAmount());
                return data;
            }).toList();
            vo.setDatas(dataList);
            return vo;
        }).collect(Collectors.toList());
    }

    private void builderPointsOrderIpAddressStatistics(Map<String, Map<String, PaymentOrderMapStatisticsRo>> countryMap,
                                                       Map<String, Map<String, Map<String, PaymentOrderMapStatisticsRo>>> provinceMap,
                                                       List<PaymentOrderMapStatisticsRo> statisticsRos) {
        if (StringUtils.isEmpty(statisticsRos)) {
            return;
        }
        //遍历结果
        for (PaymentOrderMapStatisticsRo statisticsRo : statisticsRos) {
            String ipAddress = statisticsRo.getIpAddress();
            String date = statisticsRo.getDate();
            BigDecimal amount = statisticsRo.getAmount();
            Long value = statisticsRo.getValue();
            Map<String, PaymentOrderMapStatisticsRo> currentCountryMap = countryMap.get(date);
            Map<String, Map<String, PaymentOrderMapStatisticsRo>> currentProvinceMap = provinceMap.get(date);
            builderIpAddressMap(date, ipAddress, amount, value, currentCountryMap, currentProvinceMap);
        }
    }

    /**
     * 构建结果
     *
     * @param date               时间
     * @param ipAddress          ip地址
     * @param amount             金额
     * @param value              数量
     * @param currentCountryMap  国家
     * @param currentProvinceMap 省份
     * @return void
     * @author: YY
     * @method: builderIpAddressMap
     * @date: 2025/9/25 19:28
     **/
    private void builderIpAddressMap(String date, String ipAddress, BigDecimal amount, Long value, Map<String, PaymentOrderMapStatisticsRo> currentCountryMap, Map<String, Map<String, PaymentOrderMapStatisticsRo>> currentProvinceMap) {
        if (StringUtils.isEmpty(ipAddress)) {
            return;
        }
        String[] split = ipAddress.split(" ");
        if (split.length < 1) {
            return;
        }
        String province = split[0];
        //国家
        builderAddress(date, amount, value, currentCountryMap, province);
        if (split.length < 2) {
            return;
        }
        String city = split[1];
        //省份
        if (currentProvinceMap.containsKey(province)) {
            Map<String, PaymentOrderMapStatisticsRo> roMap = currentProvinceMap.get(province);
            builderAddress(date, amount, value, roMap, city);
        } else {
            Map<String, PaymentOrderMapStatisticsRo> roMap = new HashMap<>();
            PaymentOrderMapStatisticsRo ro = new PaymentOrderMapStatisticsRo();
            ro.setIpAddress(city);
            ro.setAmount(amount);
            ro.setValue(value);
            ro.setDate(date);
            roMap.put(city, ro);
            currentProvinceMap.put(province, roMap);
        }
    }

    private static void builderAddress(String date, BigDecimal amount, Long value, Map<String, PaymentOrderMapStatisticsRo> map, String address) {
        if (map.containsKey(address)) {
            PaymentOrderMapStatisticsRo ro = map.get(address);
            ro.setAmount(ro.getAmount().add(amount));
            ro.setValue(ro.getValue() + value);
            map.put(address, ro);
        } else {
            PaymentOrderMapStatisticsRo ro = new PaymentOrderMapStatisticsRo();
            ro.setIpAddress(address);
            ro.setAmount(amount);
            ro.setValue(value);
            ro.setDate(date);
            map.put(address, ro);
        }
    }

    private List<PaymentOrderMapStatisticsRo> getPointsOrderIpAddressStatistics(String start, String end, PaymentOrderMapStatisticsRequest request) {
        request.setStartDate(start);
        request.setEndDate(end);
        return poStatisticsInfoMapper.pointsOrderIpAddressStatistics(request);
    }
    //endregion

}
