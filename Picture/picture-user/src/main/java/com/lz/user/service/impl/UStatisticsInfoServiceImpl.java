package com.lz.user.service.impl;

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
import com.lz.common.core.domain.statistics.vo.PieStatisticsVo;
import com.lz.common.core.domain.statistics.vo.RadarStatisticsVo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.model.domain.InformTemplateInfo;
import com.lz.config.service.IInformTemplateInfoService;
import com.lz.system.service.ISysConfigService;
import com.lz.user.mapper.UStatisticsInfoMapper;
import com.lz.user.model.domain.InformInfo;
import com.lz.user.model.domain.UStatisticsInfo;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.dto.statistics.UserInformStatisticsRequest;
import com.lz.user.model.dto.statistics.UserInformTypeStatisticsRo;
import com.lz.user.model.dto.statistics.UserLoginStatisticsRequest;
import com.lz.user.model.dto.statistics.UserStatisticsRequest;
import com.lz.user.model.dto.uStatisticsInfo.UStatisticsInfoQuery;
import com.lz.user.model.enums.UStatisticsTypeEnum;
import com.lz.user.model.enums.UUserSexEnum;
import com.lz.user.model.vo.statistics.UserInformStatisticsVo;
import com.lz.user.model.vo.uStatisticsInfo.UStatisticsInfoVo;
import com.lz.user.service.IInformInfoService;
import com.lz.user.service.IUStatisticsInfoService;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.lz.common.constant.ConfigConstants.USER_STATISTICS_AGE_KEY;
import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.user.UserStatisticsConstants.*;
import static com.lz.framework.web.service.UserInfoTokenService.LOGIN_USER_KEY;

/**
 * 统计信息Service业务层处理
 *
 * @author YY
 * @date 2025-09-09
 */
@Slf4j
@Service
public class UStatisticsInfoServiceImpl extends ServiceImpl<UStatisticsInfoMapper, UStatisticsInfo> implements IUStatisticsInfoService {
    @Resource
    private UStatisticsInfoMapper uStatisticsInfoMapper;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private ISysConfigService sysConfigService;

    @Resource
    private IInformTemplateInfoService informTemplateInfoService;

    @Resource
    private IInformInfoService informInfoService;

    @Resource
    private RedisCache redisCache;

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

    //region 用户统计
    @CustomCacheable(keyPrefix = USER_STATISTICS_REGISTER_DAY, expireTime = USER_STATISTICS_REGISTER_DAY_EXPIRE_TIME, useQueryParamsAsKey = true)
    @Override
    public LineStatisticsVo userRegisterStatistics(UserStatisticsRequest request) {
        //拿到开始结束时间
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        Date nowDate = checkDate(startDate, endDate);
        List<String> dateRanges = DateUtils.getDateRanges(startDate, endDate);
        //如果为空查询全部
        if (StringUtils.isEmpty(dateRanges) || dateRanges == null) {
            return new LineStatisticsVo();
        }
        //灵活判断最近天数
        String end = dateRanges.getLast();
        LineStatisticsVo lineStatisticsVo = new LineStatisticsVo();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Long> totals = new ArrayList<>();
        //如果包含的日期有今天，则查询今天
        //是否包含今天
        String today = DateUtils.dateTime(nowDate);
        boolean containsToday = dateRanges.contains(today);
        if (containsToday) {
            //查询今天
            List<StatisticsRo> userRegisterStatistics = getUserRegisterStatistics(today, today);
            //构建名称和数量
            for (StatisticsRo userRegisterStatistic : userRegisterStatistics) {
                builderNamesAndTotals(names, totals, userRegisterStatistic);
            }
            //如果包含了且范围只有1，就表示统计今天
            if (dateRanges.size() == 1) {
                lineStatisticsVo.setNames(names);
                lineStatisticsVo.setTotals(totals);
                return lineStatisticsVo;
            }
            //删除今天,添加倒数第二天为最后一天,今天就是最后一天
            dateRanges.removeLast();
            end = dateRanges.getLast();
        }
        //首先查询开始时间和结束时间-1这个时间范围内是否有数据，因为当天数据是会更新的，所以要新的查询
        List<UStatisticsInfo> uStatisticsInfoList = getUStatisticsInfosByDateAndKeyType(startDate, end, UStatisticsTypeEnum.STATISTICS_TYPE_1.getValue(), USER_STATISTICS_REGISTER_DAY);
        //获取没有统计的日期
        List<String> noStatisticsDate = getNoStatisticsDate(dateRanges, uStatisticsInfoList, names, totals);
        if (StringUtils.isNotEmpty(noStatisticsDate)) {
            ArrayList<UStatisticsInfo> uStatisticsInfos = new ArrayList<>();
            for (String date : noStatisticsDate) {
                List<StatisticsRo> userRegisterStatistics = getUserRegisterStatistics(date, date);
                for (StatisticsRo userRegisterStatistic : userRegisterStatistics) {
                    builderNamesAndTotals(names, totals, userRegisterStatistic);
                    //统计数据
                    UStatisticsInfo uStatisticsInfo = getUStatisticsInfo(date, userRegisterStatistic, UStatisticsTypeEnum.STATISTICS_TYPE_1.getValue(), USER_STATISTICS_REGISTER_DAY_NAME, USER_STATISTICS_REGISTER_DAY, 1L);
                    uStatisticsInfos.add(uStatisticsInfo);
                }
            }
            uStatisticsInfoMapper.insertOrUpdate(uStatisticsInfos);
        }
        //构建结果，因为当前时间和数据是一一对应的，但是时间排序不一样，所以要排序
        sortNamesAndTotals(names, totals);
        //排序，根据时间排序
        lineStatisticsVo.setNames(names);
        lineStatisticsVo.setTotals(totals);
        return lineStatisticsVo;
    }

    private static UStatisticsInfo getUStatisticsInfo(String date, Object content,
                                                      String type, String statisticsName,
                                                      String commonKey, Long stages) {
        UStatisticsInfo uStatisticsInfo = new UStatisticsInfo();
        uStatisticsInfo.setStatisticsId(IdUtils.snowflakeId().toString());
        uStatisticsInfo.setType(type);
        uStatisticsInfo.setStatisticsName(statisticsName + COMMON_SEPARATOR_CACHE + date);
        uStatisticsInfo.setCommonKey(commonKey);
        uStatisticsInfo.setStatisticsKey(commonKey + COMMON_SEPARATOR_CACHE + date);
        uStatisticsInfo.setStages(stages);
        uStatisticsInfo.setContent(JSONObject.toJSONString(content));
        uStatisticsInfo.setCreateTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD, date));
        return uStatisticsInfo;
    }

    /**
     * 校验时间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    private static Date checkDate(String startDate, String endDate) {
        //如果开始时间大于结束时间
        Date startTime = DateUtils.parseDate(startDate);
        Date endTime = DateUtils.parseDate(endDate);
        if (startTime.getTime() > endTime.getTime()) {
            throw new ServiceException("开始时间不能大于结束时间");
        }
        //如果结束时间大于当前时间
        Date nowDate = DateUtils.getNowDate();
        if (endTime.getTime() > nowDate.getTime()) {
            throw new ServiceException("结束时间不能大于当前时间");
        }
        return nowDate;
    }

    /**
     * 获取用户注册数据
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    private List<StatisticsRo> getUserRegisterStatistics(String start, String end) {
        return getUserStatistics(
                start, end,
                UserStatisticsRequest::new,
                uStatisticsInfoMapper::userRegisterStatistics
        );
    }

    @Override
    @CustomCacheable(keyPrefix = USER_STATISTICS_SEX, expireTime = USER_STATISTICS_SEX_EXPIRE_TIME)
    public PieStatisticsVo userSexStatistics() {
        //统计默认今天，表示最新
        String nowData = DateUtils.dateTime(DateUtils.getNowDate());
        UStatisticsInfo uStatisticsInfo = getUStatisticsInfoByCommonKey(USER_STATISTICS_SEX, UStatisticsTypeEnum.STATISTICS_TYPE_5.getValue());
        //如果有数据且就是今天的
        if (StringUtils.isNotNull(uStatisticsInfo) && DateUtils.dateTime(uStatisticsInfo.getCreateTime()).equals(nowData)) {
            return JSONObject.parseObject(uStatisticsInfo.getContent(), PieStatisticsVo.class);
        }
        PieStatisticsVo pieStatisticsVo = new PieStatisticsVo();
        ArrayList<PieStatisticsVo.Data> datas = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        //没有数据，查询数据库
        List<StatisticsRo> userSexStatistics = uStatisticsInfoMapper.userSexStatistics(CommonDeleteEnum.NORMAL.getValue());
        for (StatisticsRo userSexStatistic : userSexStatistics) {
            Optional<UUserSexEnum> enumByValue = UUserSexEnum.getEnumByValue(userSexStatistic.getName());
            String name = enumByValue.isPresent() ? enumByValue.get().getLabel() : userSexStatistic.getName();
            PieStatisticsVo.Data data = new PieStatisticsVo.Data();
            data.setName(name);
            data.setValue(userSexStatistic.getTotal());
            datas.add(data);
            names.add(name);
        }
        pieStatisticsVo.setNames(names);
        pieStatisticsVo.setDatas(datas);
        UStatisticsInfo newUStatisticsInfo = getUStatisticsInfo(nowData, pieStatisticsVo,
                UStatisticsTypeEnum.STATISTICS_TYPE_5.getValue(), USER_STATISTICS_SEX_NAME, USER_STATISTICS_SEX,
                StringUtils.isNull(uStatisticsInfo) ? 1L : uStatisticsInfo.getStages() + 1);
        uStatisticsInfoMapper.insertOrUpdate(newUStatisticsInfo);
        return pieStatisticsVo;
    }

    @Override
    public UStatisticsInfo getUStatisticsInfoByCommonKey(String commonKey, String type) {
        //首先查询是否有存在
        return this.getOne(new LambdaQueryWrapper<UStatisticsInfo>()
                .eq(UStatisticsInfo::getType, type)
                .eq(UStatisticsInfo::getCommonKey, commonKey)
                .orderByDesc(UStatisticsInfo::getCreateTime)
                .last("limit 1"));
    }

    @Override
    @CustomCacheable(keyPrefix = USER_STATISTICS_AGE, expireTime = USER_STATISTICS_AGE_EXPIRE_TIME)
    public RadarStatisticsVo userAgeStatistics() {
        //统计默认今天，表示最新
        String nowData = DateUtils.dateTime(DateUtils.getNowDate());
        UStatisticsInfo uStatisticsInfo = getUStatisticsInfoByCommonKey(USER_STATISTICS_AGE, UStatisticsTypeEnum.STATISTICS_TYPE_6.getValue());
        //如果有数据且就是今天的
        if (StringUtils.isNotNull(uStatisticsInfo) && DateUtils.dateTime(uStatisticsInfo.getCreateTime()).equals(nowData)) {
            return JSONObject.parseObject(uStatisticsInfo.getContent(), RadarStatisticsVo.class);
        }
        //查询到所有的用户，分批查询
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        int pageNum = 0;
        int pageSize = 1000;
        List<UserInfo> userInfoList;
        do {
            userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                    .select(UserInfo::getSex, UserInfo::getBirthday)
                    .last("limit " + (pageNum * pageSize) + "," + pageSize));
            userInfos.addAll(userInfoList);
            pageNum++;
        } while (userInfoList.size() == pageSize);
        List<Integer> rangeBounds = builderAgeBounds();
        // 为不同性别创建年龄统计Map，key为性别值，value为年龄范围统计
        Map<String, Map<String, Integer>> genderAgeStats = new LinkedHashMap<>();
        //总人数
        //不能拿同一个数据源，否则会重复计算
        Map<String, Integer> ageStats = initAgeStats(rangeBounds);
        genderAgeStats.put("-1", ageStats);
        // 初始化各性别的年龄区间统计
        for (UUserSexEnum sex : UUserSexEnum.values()) {
            // 以性别值为key创建map
            genderAgeStats.put(sex.getValue(), initAgeStats(rangeBounds));
        }
        Date nowDate = DateUtils.getNowDate();
        //构建用户年龄
        for (UserInfo userInfo : userInfos) {
            builderUserAge(userInfo, nowDate, genderAgeStats, rangeBounds);
        }
        //构建返回数据
        RadarStatisticsVo radarStatisticsVo = builderUserAgeResult(ageStats, genderAgeStats);
        //保存结果
        UStatisticsInfo newUStatisticsInfo = getUStatisticsInfo(nowData, radarStatisticsVo,
                UStatisticsTypeEnum.STATISTICS_TYPE_6.getValue(), USER_STATISTICS_AGE_NAME, USER_STATISTICS_AGE,
                StringUtils.isNull(uStatisticsInfo) ? 1L : uStatisticsInfo.getStages() + 1);
        uStatisticsInfoMapper.insertOrUpdate(newUStatisticsInfo);
        //构建返回数据
        return radarStatisticsVo;
    }

    /**
     * 构建年龄段
     */
    private List<Integer> builderAgeBounds() {
        String ageRange = sysConfigService.selectConfigByKey(USER_STATISTICS_AGE_KEY);
        if (StringUtils.isEmpty(ageRange)) {
            ageRange = "18;30;40;50;60";
        }
        // 解析年龄区间配置
        List<Integer> rangeBounds = new ArrayList<>();
        for (String str : ageRange.split(";")) {
            rangeBounds.add(Integer.parseInt(str));
        }
        return rangeBounds;
    }

    /**
     * 构建用户年龄统计结果
     *
     * @param ageStats       年龄段
     * @param genderAgeStats 各性别的统计
     */
    private static RadarStatisticsVo builderUserAgeResult(Map<String, Integer> ageStats, Map<String, Map<String, Integer>> genderAgeStats) {
        RadarStatisticsVo radarStatisticsVo = new RadarStatisticsVo();
        ArrayList<RadarStatisticsVo.Indicator> indicators = new ArrayList<>();
        for (Map.Entry<String, Integer> stringIntegerEntry : ageStats.entrySet()) {
            RadarStatisticsVo.Indicator indicator = new RadarStatisticsVo.Indicator();
            indicator.setText(stringIntegerEntry.getKey());
            indicator.setMax(0L);
            indicators.add(indicator);
        }
        radarStatisticsVo.setIndicators(indicators);
        ArrayList<RadarStatisticsVo.Data> datas = new ArrayList<>();
        genderAgeStats.forEach((sex, ageStatMap) -> {
            RadarStatisticsVo.Data data = new RadarStatisticsVo.Data();
            if (sex.equals("-1")) {
                data.setName("总计");
            } else {
                Optional<UUserSexEnum> sexEnumOpt = UUserSexEnum.getEnumByValue(sex);
                if (sexEnumOpt.isPresent()) {
                    data.setName(sexEnumOpt.get().getLabel());
                } else {
                    data.setName("未知");
                }
            }
            List<Long> values = ageStatMap.values().stream()
                    .map(Integer::longValue)
                    .toList();
            data.setValues(new ArrayList<>(values));
            datas.add(data);
        });
        radarStatisticsVo.setDatas(datas);
        return radarStatisticsVo;
    }

    /**
     * 构建用户年龄
     *
     * @param userInfo       用户信息
     * @param nowDate        当前时间
     * @param genderAgeStats 性别年龄统计
     * @param rangeBounds    年龄区间
     */
    private static void builderUserAge(UserInfo userInfo, Date nowDate, Map<String, Map<String, Integer>> genderAgeStats, List<Integer> rangeBounds) {
        Integer age = DateUtils.getAgeByData(nowDate, userInfo.getBirthday());
        String sex = userInfo.getSex();
        //如果性别为空
        if (StringUtils.isEmpty(sex)) {
            sex = UUserSexEnum.USER_SEX_0.getValue();
        }
        Map<String, Integer> currentAgeStats = genderAgeStats.get(sex);
        Map<String, Integer> totalAgeStats = genderAgeStats.get("-1");
        // 分配到对应区间
        if (age == null || age < 0) {
            currentAgeStats.put("未知", currentAgeStats.get("未知") + 1);
            totalAgeStats.put("未知", totalAgeStats.get("未知") + 1);
        } else if (age < rangeBounds.getFirst()) {
            currentAgeStats.put(rangeBounds.getFirst() + "以下", currentAgeStats.get(rangeBounds.getFirst() + "以下") + 1);
            totalAgeStats.put(rangeBounds.getFirst() + "以下", totalAgeStats.get(rangeBounds.getFirst() + "以下") + 1);
        } else if (age >= rangeBounds.getLast()) {
            currentAgeStats.put(rangeBounds.getLast() + "以上",
                    currentAgeStats.get(rangeBounds.getLast() + "以上") + 1);
            totalAgeStats.put(rangeBounds.getLast() + "以上", totalAgeStats.get(rangeBounds.getLast() + "以上") + 1);
        } else {
            // 在中间区间
            for (int i = 0; i < rangeBounds.size() - 1; i++) {
                int current = rangeBounds.get(i);
                int next = rangeBounds.get(i + 1);
                if (age >= current && age < next) {
                    String rangeKey = current + "-" + (next - 1);
                    currentAgeStats.put(rangeKey, currentAgeStats.get(rangeKey) + 1);
                    totalAgeStats.put(rangeKey, totalAgeStats.get(rangeKey) + 1);
                    break;
                }
            }
        }
    }

    private Map<String, Integer> initAgeStats(List<Integer> rangeBounds) {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("未知", 0);
        map.put(rangeBounds.getFirst() + "以下", 0);
        for (int i = 0; i < rangeBounds.size() - 1; i++) {
            int current = rangeBounds.get(i);
            int next = rangeBounds.get(i + 1);
            map.put(current + "-" + (next - 1), 0);
        }
        map.put(rangeBounds.getLast() + "以上", 0);
        return map;
    }


    @Override
    @CustomCacheable(keyPrefix = USER_STATISTICS_LOGIN_DAY, expireTime = USER_STATISTICS_LOGIN_DAY_EXPIRE_TIME, useQueryParamsAsKey = true)
    public BarStatisticsVo userLoginStatistics(UserLoginStatisticsRequest request) {
        //拿到开始结束时间
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        Date nowDate = checkDate(startDate, endDate);
        List<String> dateRanges = DateUtils.getDateRanges(startDate, endDate);
        //如果为空查询全部
        if (StringUtils.isEmpty(dateRanges) || dateRanges == null) {
            return new BarStatisticsVo();
        }
        //灵活判断最近天数
        String end = dateRanges.getLast();
        BarStatisticsVo barStatisticsVo = new BarStatisticsVo();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Long> totals = new ArrayList<>();
        //如果包含的日期有今天，则查询今天
        //是否包含今天
        String today = DateUtils.dateTime(nowDate);
        boolean containsToday = dateRanges.contains(today);
        if (containsToday) {
            List<StatisticsRo> userLoginStatistics = getUserLoginStatistics(today, today);
            for (StatisticsRo userLoginStatistic : userLoginStatistics) {
                builderNamesAndTotals(names, totals, userLoginStatistic);
            }
            //如果包含了且范围只有1，就表示统计今天
            if (dateRanges.size() == 1) {
                barStatisticsVo.setNames(names);
                barStatisticsVo.setTotals(totals);
                return barStatisticsVo;
            }
            //删除今天,添加倒数第二天为最后一天,今天就是最后一天
            dateRanges.removeLast();
            end = dateRanges.getLast();
        }
        //首先查询开始时间和结束时间-1这个时间范围内是否有数据，因为当天数据是会更新的，所以要新的查询
        List<UStatisticsInfo> uStatisticsInfoList = getUStatisticsInfosByDateAndKeyType(startDate, end, UStatisticsTypeEnum.STATISTICS_TYPE_2.getValue(), USER_STATISTICS_LOGIN_DAY);
        //获取没有统计的日期
        List<String> noStatisticsDate = getNoStatisticsDate(dateRanges, uStatisticsInfoList, names, totals);
        if (StringUtils.isNotEmpty(noStatisticsDate)) {
            ArrayList<UStatisticsInfo> uStatisticsInfos = new ArrayList<>();
            for (String date : noStatisticsDate) {
                List<StatisticsRo> userRegisterStatistics = getUserLoginStatistics(date, date);
                for (StatisticsRo userRegisterStatistic : userRegisterStatistics) {
                    builderNamesAndTotals(names, totals, userRegisterStatistic);
                    //统计数据
                    UStatisticsInfo uStatisticsInfo = getUStatisticsInfo(date, userRegisterStatistic, UStatisticsTypeEnum.STATISTICS_TYPE_2.getValue(), USER_STATISTICS_LOGIN_DAY_NAME, USER_STATISTICS_LOGIN_DAY, 1L);
                    uStatisticsInfos.add(uStatisticsInfo);
                }
            }
            uStatisticsInfoMapper.insertOrUpdate(uStatisticsInfos);
        }
        //构建结果，因为当前时间和数据是一一对应的，但是时间排序不一样，所以要排序
        sortNamesAndTotals(names, totals);
        //排序，根据时间排序
        barStatisticsVo.setNames(names);
        barStatisticsVo.setTotals(totals);
        return barStatisticsVo;
    }

    private void sortNamesAndTotals(ArrayList<String> names, ArrayList<Long> totals) {
        //把两个list绑定成一个临时集合
        List<Map.Entry<String, Long>> combined = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            combined.add(new AbstractMap.SimpleEntry<>(names.get(i), totals.get(i)));
        }
        //时间升序排序
        combined.sort(Comparator.comparing(entry -> LocalDate.parse(entry.getKey(), DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD))));
        //清空数据重新填充
        names.clear();
        totals.clear();
        for (Map.Entry<String, Long> entry : combined) {
            names.add(entry.getKey());
            totals.add(entry.getValue());
        }
    }


    /**
     * 构建names和totals
     *
     * @param names        names
     * @param totals       totals
     * @param statisticsRo 统计
     */
    private void builderNamesAndTotals(ArrayList<String> names, ArrayList<Long> totals, StatisticsRo statisticsRo) {
        names.add(statisticsRo.getName());
        totals.add(statisticsRo.getTotal());
    }

    @Override
    public List<UStatisticsInfo> getUStatisticsInfosByDateAndKeyType(String startDate, String end, String type, String commonKey) {
        return this.list(new LambdaQueryWrapper<UStatisticsInfo>()
                .eq(UStatisticsInfo::getType, type)
                .eq(UStatisticsInfo::getCommonKey, commonKey)
                .apply("date_format(create_time,'%Y-%m-%d') between {0} and {1}", startDate, end)
        );
    }

    /**
     * 获取没有统计的日期
     *
     * @param dateRanges          时间范围
     * @param uStatisticsInfoList 统计数据
     * @param names               名称
     * @param totals              总数
     * @return
     */
    private static List<String> getNoStatisticsDate(List<String> dateRanges, List<UStatisticsInfo> uStatisticsInfoList, ArrayList<String> names, ArrayList<Long> totals) {
        List<String> noStatisticsDate = new ArrayList<>(dateRanges);
        if (StringUtils.isNotEmpty(uStatisticsInfoList)) {
            //添加所有统计到的数据
            for (UStatisticsInfo uStatisticsInfo : uStatisticsInfoList) {
                String dateToStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, uStatisticsInfo.getCreateTime());
                names.add(dateToStr);
                String statisticsStr = uStatisticsInfo.getContent();
                StatisticsRo statisticsRo = JSONObject.parseObject(statisticsStr, StatisticsRo.class);
                totals.add(statisticsRo.getTotal());
                //如果没有统计，则添加
                noStatisticsDate.remove(dateToStr);
            }
        }
        return noStatisticsDate;
    }

    /**
     * 获取用户登录统计
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    private List<StatisticsRo> getUserLoginStatistics(String startDate, String endDate) {
        return getUserStatistics(
                startDate, endDate,
                UserLoginStatisticsRequest::new,
                uStatisticsInfoMapper::userLoginStatistics
        );
    }


    /**
     * 获取用户统计
     *
     * @param startDate       开始时间
     * @param endDate         结束时间
     * @param requestSupplier 构造request
     * @param queryFunction   查询方法
     */
    private <R, T> List<T> getUserStatistics(
            String startDate,
            String endDate,
            Supplier<R> requestSupplier,
            Function<R, List<T>> queryFunction
    ) {
        try {
            // 1. 构造 request
            R request = requestSupplier.get();
            request.getClass().getMethod("setStartDate", String.class).invoke(request, startDate);
            request.getClass().getMethod("setEndDate", String.class).invoke(request, endDate);

            // 2. 执行 mapper 方法（方法引用传进来）
            List<T> list = queryFunction.apply(request);
            if (list == null || list.isEmpty()) {
                return Collections.emptyList();
            }
            return list;
        } catch (Exception e) {
            log.error("统计失败", e);
            throw new ServiceException("统计失败");
        }
    }


    @Override
    @CustomCacheable(keyPrefix = USER_STATISTICS_INFORM_DAY, expireTime = USER_STATISTICS_INFORM_DAY_EXPIRE_TIME, useQueryParamsAsKey = true)
    public BarStatisticsVo userInformTypeStatistics(UserStatisticsRequest request) {
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        Date nowDate = checkDate(startDate, endDate);
        List<String> dateRanges = DateUtils.getDateRanges(startDate, endDate);
        //如果为空查询全部
        if (StringUtils.isEmpty(dateRanges) || dateRanges == null) {
            return new BarStatisticsVo();
        }
        //灵活判断最近天数
        String end = dateRanges.getLast();
        BarStatisticsVo barStatisticsVo = new BarStatisticsVo();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Long> totals = new ArrayList<>();
        //如果包含的日期有今天，则查询今天
        //是否包含今天
        String today = DateUtils.dateTime(nowDate);
        boolean containsToday = dateRanges.contains(today);
        Map<String, InformTemplateInfo> templateInfoMap = new HashMap<>();
        if (containsToday) {
            List<UserInformTypeStatisticsRo> userLoginStatistics = getInformTypeStatistics(today, today);
            builderUserInformTypeRoNamesAndTotals(names, totals, userLoginStatistics, templateInfoMap);
            //如果包含了且范围只有1，就表示统计今天
            if (dateRanges.size() == 1) {
                builderUserInformTypeRoNamesAndTotals(names, totals, userLoginStatistics, templateInfoMap);
                barStatisticsVo.setNames(names);
                barStatisticsVo.setTotals(totals);
                return barStatisticsVo;
            }
            //删除今天,添加倒数第二天为最后一天,今天就是最后一天
            dateRanges.removeLast();
            end = dateRanges.getLast();
        }
        //首先查询开始时间和结束时间-1这个时间范围内是否有数据，因为当天数据是会更新的，所以要新的查询
        List<UStatisticsInfo> uStatisticsInfoList = getUStatisticsInfosByDateAndKeyType(startDate, end, UStatisticsTypeEnum.STATISTICS_TYPE_7.getValue(), USER_STATISTICS_INFORM_DAY);
        //获取没有统计的日期
        List<String> noStatisticsDate = getInformTypeNoStatisticsDate(dateRanges, uStatisticsInfoList, names, totals, templateInfoMap);
        if (StringUtils.isNotEmpty(noStatisticsDate)) {
            ArrayList<UStatisticsInfo> uStatisticsInfos = new ArrayList<>();
            for (String date : noStatisticsDate) {
                List<UserInformTypeStatisticsRo> userRegisterStatistics = getInformTypeStatistics(date, date);
                builderUserInformTypeRoNamesAndTotals(names, totals, userRegisterStatistics, templateInfoMap);
                //统计数据
                UStatisticsInfo uStatisticsInfo = getUStatisticsInfo(date, userRegisterStatistics, UStatisticsTypeEnum.STATISTICS_TYPE_7.getValue(), USER_STATISTICS_INFORM_DAY_NAME, USER_STATISTICS_INFORM_DAY, 1L);
                uStatisticsInfos.add(uStatisticsInfo);
            }
            uStatisticsInfoMapper.insertOrUpdate(uStatisticsInfos);
        }
        //构建结果，因为当前时间和数据是一一对应的，但是时间排序不一样，所以要排序
        sortUserInformTypeNamesAndTotals(names, totals);
        //排序，根据时间排序
        barStatisticsVo.setNames(names);
        barStatisticsVo.setTotals(totals);
        return barStatisticsVo;
    }

    /**
     * 获取没有统计的日期 消息类型
     *
     * @param dateRanges          日期范围
     * @param uStatisticsInfoList 统计数据
     * @param names               名称
     * @param totals              总数
     */
    private List<String> getInformTypeNoStatisticsDate(List<String> dateRanges, List<UStatisticsInfo> uStatisticsInfoList, ArrayList<String> names, ArrayList<Long> totals, Map<String, InformTemplateInfo> templateInfoMap) {
        List<String> noStatisticsDate = new ArrayList<>(dateRanges);
        if (StringUtils.isNotEmpty(uStatisticsInfoList)) {
            //添加所有统计到的数据
            for (UStatisticsInfo uStatisticsInfo : uStatisticsInfoList) {
                String dateToStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, uStatisticsInfo.getCreateTime());
                String statisticsStr = uStatisticsInfo.getContent();
                List<UserInformTypeStatisticsRo> statisticsRo = JSONArray.parseArray(statisticsStr, UserInformTypeStatisticsRo.class);
                builderUserInformTypeRoNamesAndTotals(names, totals, statisticsRo, templateInfoMap);
                //如果没有统计，则添加
                noStatisticsDate.remove(dateToStr);
            }
        }
        return noStatisticsDate;
    }

    /**
     * 构建消息结果，根据total排序
     *
     * @param names  名称
     * @param totals 总数
     */
    private void sortUserInformTypeNamesAndTotals(ArrayList<String> names, ArrayList<Long> totals) {
        //创建一个临时的map，存储name和total
        HashMap<String, Long> tempMap = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (tempMap.containsKey(name)) {
                tempMap.put(name, tempMap.get(name) + totals.get(i));
            } else {
                tempMap.put(name, totals.get(i));
            }
        }
        //根据总数排序,map的value进行排序
        // 按 value 降序排序
        LinkedHashMap<String, Long> sortedMap = tempMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        //清空数据集并重新填充数据
        names.clear();
        totals.clear();
        sortedMap.forEach((name, total) -> {
            names.add(name);
            totals.add(total);
        });
    }

    private void builderUserInformTypeRoNamesAndTotals(ArrayList<String> names, ArrayList<Long> totals, List<UserInformTypeStatisticsRo> statisticsRos, Map<String, InformTemplateInfo> templateInfoMap) {
        for (UserInformTypeStatisticsRo statisticsRo : statisticsRos) {
            String templateName;
            if (templateInfoMap.containsKey(statisticsRo.getTemplateKey() + statisticsRo.getLocale() + statisticsRo.getTemplateType())) {
                templateName = templateInfoMap.get(statisticsRo.getTemplateKey() + statisticsRo.getLocale() + statisticsRo.getTemplateType()).getTemplateName();
            } else {
                InformTemplateInfo informTemplateInfoByKeyLocaleType = informTemplateInfoService.getInformTemplateInfoByKeyLocaleType(statisticsRo.getTemplateKey(), statisticsRo.getLocale(), statisticsRo.getTemplateType());
                templateName = informTemplateInfoByKeyLocaleType.getTemplateName();
            }
            names.add(templateName);
            totals.add(statisticsRo.getTotal());
        }
    }

    private List<UserInformTypeStatisticsRo> getInformTypeStatistics(String startDate, String endDate) {
        return getUserStatistics(
                startDate, endDate,
                UserStatisticsRequest::new,
                uStatisticsInfoMapper::userInformTypeStatistics
        );
    }


    @Override
    public List<UserInformStatisticsVo> userInformStatistics(UserInformStatisticsRequest request) {
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        checkDate(startDate, endDate);
        startDate = startDate + " 00:00:00";
        endDate = endDate + " 23:59:59";
        Long pageNum = request.getPageNum();
        Long pageSize = request.getPageSize();
        List<InformInfo> informInfos = informInfoService.list(new LambdaQueryWrapper<InformInfo>()
                .select(InformInfo::getUserId, InformInfo::getIsRead, InformInfo::getInformTitle, InformInfo::getSendTime)
                .ge(InformInfo::getSendTime, startDate)
                .le(InformInfo::getSendTime, endDate)
                .last("limit " + ((pageNum - 1) * pageSize) + "," + pageSize)
        );
        if (StringUtils.isEmpty(informInfos)) {
            return List.of();
        }
        //获取到所有的用户id
        List<String> userIds = informInfos.stream().map(InformInfo::getUserId).toList();
        List<UserInfo> userInfos = userInfoService.list(new LambdaQueryWrapper<UserInfo>().in(UserInfo::getUserId, userIds));
        //转换为map方便查询userId-userName
        Map<String, String> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getUserId, UserInfo::getUserName));
        return informInfos.stream().map(informInfo -> {
            UserInformStatisticsVo userInformStatisticsVo = UserInformStatisticsVo.objToVo(informInfo);
            userInformStatisticsVo.setUserName(userInfoMap.get(informInfo.getUserId()));
            return userInformStatisticsVo;
        }).toList();
    }

    @Override
    @CustomCacheable(keyPrefix = USER_STATISTICS_COUNT, expireTime = USER_STATISTICS_COUNT_EXPIRE_TIME)
    public Long userTotalStatistics() {
        //统计默认今天，表示最新
        String nowData = DateUtils.dateTime(DateUtils.getNowDate());
        UStatisticsInfo uStatisticsInfo = getUStatisticsInfoByCommonKey(USER_STATISTICS_COUNT, UStatisticsTypeEnum.STATISTICS_TYPE_8.getValue());
        //如果有数据且就是今天的
        if (StringUtils.isNotNull(uStatisticsInfo) && DateUtils.dateTime(uStatisticsInfo.getCreateTime()).equals(nowData)) {
            return JSONObject.parseObject(uStatisticsInfo.getContent(), Long.class);
        }
        long count = userInfoService.count();
        //保存结果
        UStatisticsInfo newUStatisticsInfo = getUStatisticsInfo(nowData, count,
                UStatisticsTypeEnum.STATISTICS_TYPE_8.getValue(), USER_STATISTICS_COUNT_NAME, USER_STATISTICS_COUNT,
                StringUtils.isNull(uStatisticsInfo) ? 1L : uStatisticsInfo.getStages() + 1);
        uStatisticsInfoMapper.insertOrUpdate(newUStatisticsInfo);
        return count;
    }

    @Override
    @CustomCacheable(keyPrefix = USER_STATISTICS_ONLINE_COUNT, expireTime = USER_STATISTICS_ONLINE_COUNT_EXPIRE_TIME)
    public Long userOnlineTotalStatistics() {
        return redisCache.getPatternSize(LOGIN_USER_KEY);
    }
    //endregion

}
