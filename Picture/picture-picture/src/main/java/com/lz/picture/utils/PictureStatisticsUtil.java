package com.lz.picture.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.StatisticsInfo;
import com.lz.picture.model.dto.statisticsInfo.PictureStatisticsDto;
import com.lz.picture.model.enums.PPictureStatusEnum;
import com.lz.picture.model.enums.PStatisticsTypeEnum;
import com.lz.picture.model.vo.pictureInfo.PictureInfoStatisticsVo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.picture.service.IStatisticsInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.config.utils.ConfigInfoUtils.*;

/**
 * 图片统计工具类
 * 云想衣裳花想容，春风拂槛露华浓。代码写好看一点啦
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-07-17  23:53
 * @Version: 1.0
 */
@Slf4j
@Component
public class PictureStatisticsUtil {
    @Resource
    private IStatisticsInfoService statisticsInfoService;

    @Resource
    @Lazy
    private IPictureInfoService pictureInfoService;

    @Resource
    private RedisCache redisCache;

    private static final String PICTURE_STATISTICS_PICTURE_HOT_KEY = "picture:statistics:picture:hot:minute";
    private static final Integer PICTURE_STATISTICS_PICTURE_HOT_EXPIRE_TIME = 60 * 60 * 24;
    //图片热门统计缓存key，日
    public static final String PICTURE_STATISTICS_PICTURE_HOT = "picture:statistics:picture:hot";
    private static final String PICTURE_STATISTICS_PICTURE_HOT_DAY_NAME = "图片热门统计日排行";
    //图片热门统计缓存key，周
    private static final String PICTURE_STATISTICS_PICTURE_HOT_WEEK_NAME = "图片热门统计周排行";
    //图片热门统计缓存key，月
    private static final String PICTURE_STATISTICS_PICTURE_HOT_MONTH_NAME = "图片热门统计月排行";
    //图片热门统计缓存key，年
    private static final String PICTURE_STATISTICS_PICTURE_HOT_YEAR_NAME = "图片热门统计年排行";
    //图片热门统计缓存key，总
    public static final String PICTURE_STATISTICS_PICTURE_HOT_TOTAL_KEY = "picture:statistics:picture:hot:total";
    private static final String PICTURE_STATISTICS_PICTURE_HOT_TOTAL_NAME = "图片热门统计总排行";
    //当前key,需要存入缓存的
    private static String currentKey = PICTURE_STATISTICS_PICTURE_HOT_KEY + COMMON_SEPARATOR_CACHE + System.currentTimeMillis();
    private static String oldKey = "";

    //多线程
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void pictureHotStatisticsIncrementScore(String pictureId, Double score) {
        redisCache.zSetIncrementScore(currentKey, pictureId, score, PICTURE_STATISTICS_PICTURE_HOT_EXPIRE_TIME);
    }

    //region 自动统计图片热门信息
    public void autoStatisticsPictureByDay() {
        LinkedHashMap<String, PictureInfoStatisticsVo> resultMap = getCacheData();
        if (resultMap == null) return;
        Date nowDate = DateUtils.getNowDate();
        //统计日
        PictureStatisticsDto statisticsDay = pictureStatisticsDay(resultMap, nowDate);
        if (StringUtils.isNotNull(statisticsDay)) {
            //统计到的数据不为空，统计总、周、月、年
            PictureStatisticsDto statisticsTotal = pictureStatisticsTotal(statisticsDay.getStatisticsMap(), nowDate);
            PictureStatisticsDto statisticsWeek = pictureStatisticsWeek(statisticsDay.getStatisticsMap(), nowDate);
            PictureStatisticsDto statisticsMonth = pictureStatisticsMonth(statisticsDay.getStatisticsMap(), nowDate);
            PictureStatisticsDto statisticsYear = pictureStatisticsYear(statisticsDay.getStatisticsMap(), nowDate);
        }
    }

    /**
     * 从缓存内获取数据
     *
     * @param
     * @return LinkedHashMap<String, PictureInfoStatisticsVo>
     * @author: YY
     * @method: getCacheData
     * @date: 2025/7/20 17:26
     **/
    private LinkedHashMap<String, PictureInfoStatisticsVo> getCacheData() {
        //获取当前的时间戳
        long now = System.currentTimeMillis();
        //更新当前key,把当前时间戳赋给oldKey，构建当前key
        oldKey = currentKey;
//        System.out.println("old = " + oldKey);
        currentKey = PICTURE_STATISTICS_PICTURE_HOT_KEY + COMMON_SEPARATOR_CACHE + now;
//        System.out.println("currentKey = " + currentKey);
        //从旧key中获取数据
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisCache.zSetRangeWithScores(oldKey, 0, -1);
        //如果没有数据，直接返回不统计
        if (StringUtils.isEmpty(typedTuples)) {
            return null;
        }
        //获取图片 id集合
        ArrayList<String> pictureIds = new ArrayList<>(typedTuples.size());
        //给对象转换为map，并有序排行,key-value，拥有当前从缓存拿的统计分数
        LinkedHashMap<String, PictureInfoStatisticsVo> resultMap = new LinkedHashMap<String, PictureInfoStatisticsVo>(typedTuples.size());
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            PictureInfoStatisticsVo value = new PictureInfoStatisticsVo();
            value.setPictureId(typedTuple.getValue());
            value.setScore(typedTuple.getScore());
            resultMap.put(typedTuple.getValue(), value);
            pictureIds.add(typedTuple.getValue());
//            System.out.println("typedTuple = " + JSONObject.toJSONString(typedTuple));
        }
        //批量查询图片信息，多线程分批查询，1000条为一批次
        List<Future<List<PictureInfo>>> futures = new ArrayList<>();
        for (int i = 0; i < pictureIds.size(); i += 1000) {
            int end = Math.min(i + 1000, pictureIds.size());
            List<String> subList = pictureIds.subList(i, end);
            futures.add(threadPoolTaskExecutor.submit(() -> {
                return queryPictureInfoList(subList);
            }));
        }
        //等待所有进程完成并处理结果
        List<PictureInfo> pictureInfos = new ArrayList<>();
        for (Future<List<PictureInfo>> future : futures) {
            try {
                pictureInfos.addAll(future.get());
            } catch (Exception e) {
                log.error("获取图片信息失败:{}", e.getMessage());
            }
        }
        //遍历结果,为resultMap赋值
        for (PictureInfo pictureInfo : pictureInfos) {
            PictureInfoStatisticsVo pictureInfoStatisticsVo = resultMap.get(pictureInfo.getPictureId());
            //这里就是从缓存中拿到的图片信息，所以肯定是有的，无需判断是否为空
            PictureInfoStatisticsVo statisticsVo = PictureInfoStatisticsVo.objToVo(pictureInfo);
            statisticsVo.setScore(pictureInfoStatisticsVo.getScore());
            resultMap.put(pictureInfo.getPictureId(), statisticsVo);
        }
        return resultMap;
    }

    /**
     * 图片统计信息-日
     *
     * @param statisticsMap 统计结果map
     * @param nowDate       当前时间
     * @return PictureStatisticsDto
     * @author: YY
     * @method: pictureStatistics
     * @date: 2025/7/20 17:27
     **/
    private PictureStatisticsDto pictureStatisticsDay(LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap, Date nowDate) {
        String statisticsKey = getCurrentStatisticsDayKey(PICTURE_STATISTICS_PICTURE_HOT, nowDate);
        return pictureStatistics(
                statisticsMap,
                PStatisticsTypeEnum.STATISTICS_TYPE_2.getValue(),
                PICTURE_STATISTICS_PICTURE_HOT,
                statisticsKey,
                PICTURE_STATISTICS_PICTURE_HOT_DAY_NAME,
                true,
                PICTURE_STATISTICS_HOT_DAY_RANK_VALUE
        );
    }

    public String getLastStatisticsDayKey(String key, Date nowDate) {
        return key + COMMON_SEPARATOR_CACHE + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(nowDate, -1));
    }

    public String getCurrentStatisticsDayKey(String key, Date nowDate) {
        return key + COMMON_SEPARATOR_CACHE + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, nowDate);
    }

    private PictureStatisticsDto pictureStatisticsTotal(LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap, Date nowDate) {
        return pictureStatistics(
                statisticsMap,
                PStatisticsTypeEnum.STATISTICS_TYPE_6.getValue(),
                PICTURE_STATISTICS_PICTURE_HOT_TOTAL_KEY,
                PICTURE_STATISTICS_PICTURE_HOT_TOTAL_KEY,
                PICTURE_STATISTICS_PICTURE_HOT_TOTAL_NAME,
                false,
                PICTURE_STATISTICS_HOT_TOTAL_RANK_VALUE
        );
    }

    private PictureStatisticsDto pictureStatisticsWeek(LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap, Date nowDate) {
        String statisticsKey = getCurrentStatisticsWeekKey(PICTURE_STATISTICS_PICTURE_HOT, nowDate);
        return pictureStatistics(
                statisticsMap,
                PStatisticsTypeEnum.STATISTICS_TYPE_3.getValue(),
                PICTURE_STATISTICS_PICTURE_HOT,
                statisticsKey,
                PICTURE_STATISTICS_PICTURE_HOT_WEEK_NAME,
                true,
                PICTURE_STATISTICS_HOT_WEEK_RANK_VALUE
        );
    }

    public String getLastStatisticsWeekKey(String key, Date nowDate) {
        //获取上周一周的开始时间，周日时间
        Date weekDay = DateUtils.getWeekDay(nowDate, -1, 1);
        Long weekDayNumber = DateUtils.getWeekDayNumber(weekDay, nowDate);
        return key + COMMON_SEPARATOR_CACHE + weekDayNumber;
    }

    public String getCurrentStatisticsWeekKey(String key, Date nowDate) {
        //获取到本周一的时间，本周日的时间
        Long weekDayNumber = DateUtils.getWeekDayNumber(nowDate, nowDate);
        return key + COMMON_SEPARATOR_CACHE + weekDayNumber;
    }

    private PictureStatisticsDto pictureStatisticsMonth(LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap, Date nowDate) {
        String statisticsKey = getCurrentStatisticsMonthKey(PICTURE_STATISTICS_PICTURE_HOT, nowDate);
        return pictureStatistics(
                statisticsMap,
                PStatisticsTypeEnum.STATISTICS_TYPE_4.getValue(),
                PICTURE_STATISTICS_PICTURE_HOT,
                statisticsKey,
                PICTURE_STATISTICS_PICTURE_HOT_MONTH_NAME,
                true,
                PICTURE_STATISTICS_HOT_MONTH_RANK_VALUE
        );
    }

    public static String getLsatStatisticsMonthKey(String key, Date nowDate) {
        //获取上个月的开始结束时间
        String lastMonthStart = DateUtils.getMonthDay(nowDate, -1, 1, DateUtils.YYYY_MM);
        return key + COMMON_SEPARATOR_CACHE + lastMonthStart;
    }

    public String getCurrentStatisticsMonthKey(String key, Date nowDate) {
        //获取本月的开始时间
        String monthStr = DateUtils.getMonthDay(nowDate, 1, DateUtils.YYYY_MM);
        return key + COMMON_SEPARATOR_CACHE + monthStr;
    }

    private PictureStatisticsDto pictureStatisticsYear(LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap, Date nowDate) {
        String statisticsKey = getCurrentStatisticsYearKey(PICTURE_STATISTICS_PICTURE_HOT, nowDate);
        return pictureStatistics(
                statisticsMap,
                PStatisticsTypeEnum.STATISTICS_TYPE_5.getValue(),
                PICTURE_STATISTICS_PICTURE_HOT,
                statisticsKey,
                PICTURE_STATISTICS_PICTURE_HOT_YEAR_NAME,
                true,
                PICTURE_STATISTICS_HOT_YEAR_RANK_VALUE
        );
    }

    public static String getLastStatisticsYearKey(String key, Date nowDate) {
        //上一年
        String lastYearStart = DateUtils.getYearDay(nowDate, -1, 1, DateUtils.YYYY);
        return key + COMMON_SEPARATOR_CACHE + lastYearStart;
    }

    public String getCurrentStatisticsYearKey(String key, Date nowDate) {
        //获取今年的开始时间
        String yearStart = DateUtils.getYearDay(nowDate, 1, DateUtils.YYYY);
        return key + COMMON_SEPARATOR_CACHE + yearStart;
    }

    /**
     * 图片信息统计
     *
     * @param statisticsMap  缓存数据-已经统计好的数据
     *                       commonKey   通用key
     * @param statisticsType 统计类型
     * @param statisticsKey  统计key
     * @param statisticsName 统计名称
     * @param isQueryLast    是否查询上一次
     * @param rank           统计数量排行
     * @return PictureStatisticsDto
     * @author: YY
     * @method: pictureStatistics
     * @date: 2025/7/20 17:30
     **/
    private PictureStatisticsDto pictureStatistics(LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap,
                                                   String statisticsType,
                                                   String commonKey,
                                                   String statisticsKey,
                                                   String statisticsName,
                                                   boolean isQueryLast,
                                                   Integer rank) {
        //查询到对应的统计信息
        StatisticsInfo statisticsInfo = statisticsInfoService.selectStatisticsInfoByStatisticsKey(statisticsKey);
        //判断对象是否存在，存在是更新，不存在是插入
        if (StringUtils.isNull(statisticsInfo)) {
            return insertPictureStatistics(
                    statisticsMap,
                    statisticsType,
                    commonKey,
                    statisticsKey,
                    statisticsName,
                    isQueryLast,
                    rank);
        } else {
            PictureStatisticsDto pictureStatisticsDto = statisticsPictureUpdate(statisticsInfo, statisticsMap, rank);
            if (StringUtils.isNotNull(pictureStatisticsDto)) {
                //  保存完整结果到数据库
                List<PictureInfoStatisticsVo> statisticsList = pictureStatisticsDto.getStatisticsList();
                statisticsInfo.setContent(JSONObject.toJSONString(statisticsList));
                statisticsInfo.setExtendContent(JSONObject.toJSONString(pictureStatisticsDto.getAllList()));
                statisticsInfo.setRemark("本次统计一共统计（" + statisticsName + "）：" + statisticsList.size() + "张进入热门图片，一共：" + pictureStatisticsDto.getAllList().size() + "张图片");
                statisticsInfoService.updateById(statisticsInfo);
                //删除原有缓存
                redisCache.deleteObject(statisticsInfo.getStatisticsKey());
                return pictureStatisticsDto;
            } else {
                return null;
            }
        }
    }

    /**
     * 更新缓存信息
     *
     * @param statisticsInfo 统计对象
     * @param statisticsMap  统计好的数据
     * @param rank           排行
     * @return void
     * @author: YY
     * @method: statisticsPictureUpdate
     * @date: 2025/7/20 00:21
     **/
    private PictureStatisticsDto statisticsPictureUpdate(StatisticsInfo statisticsInfo,
                                                         LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap,
                                                         Integer rank
    ) {
        // 1. 解析历史数据，获取到所有信息，包括为私有的图片
        List<PictureInfoStatisticsVo> dbResult = Optional.ofNullable(
                JSONObject.parseArray(statisticsInfo.getExtendContent(), PictureInfoStatisticsVo.class)
        ).orElseGet(ArrayList::new);

        // 2. 从数据库内获取结果
        Map<String, PictureInfoStatisticsVo> dbScoreMap = dbResult.stream()
                .filter(vo -> StringUtils.isNotEmpty(vo.getPictureId()))
                .collect(Collectors.toMap(
                        PictureInfoStatisticsVo::getPictureId,
                        //需要获取数据库内对象完整信息
                        vo -> vo,
                        (v1, v2) -> v1
                ));


        // 3. 合并所有数据（不区分状态）
        LinkedHashMap<String, PictureInfoStatisticsVo> mergedMap = new LinkedHashMap<>();

        // 3.1 合并当前窗口数据
        //新建已处理的map，便于快速合并
        LinkedHashMap<String, PictureInfoStatisticsVo> processedMap = new LinkedHashMap<>();
        for (Map.Entry<String, PictureInfoStatisticsVo> entry : statisticsMap.entrySet()) {
            String pictureId = entry.getKey();
            PictureInfoStatisticsVo newVo = new PictureInfoStatisticsVo();
            BeanUtils.copyProperties(entry.getValue(), newVo);

            // 更新分数（包含历史分数）
            double currentScore = entry.getValue().getScore();
            PictureInfoStatisticsVo pictureInfoStatisticsVo = dbScoreMap.get(pictureId);
            //如果数据库里面有，说明之前统计过，则更新分数
            if (StringUtils.isNotNull(pictureInfoStatisticsVo)) {
                newVo.setScore(currentScore + pictureInfoStatisticsVo.getScore());
                processedMap.put(pictureId, newVo);
            } else {
                newVo.setScore(currentScore);
            }
            mergedMap.put(pictureId, newVo);
        }
        //判断是否有数据，如果没有数据则直接返回
        if (StringUtils.isEmpty(mergedMap)) {
            return null;
        }
        // 4  添加历史数据中未更新的图片
        for (PictureInfoStatisticsVo dbVo : dbResult) {
            if (StringUtils.isNotEmpty(dbVo.getPictureId()) &&
                    !processedMap.containsKey(dbVo.getPictureId())) {
                mergedMap.put(dbVo.getPictureId(), dbVo);
            }
        }
        // 4. 排序并保存完整结果
        return updateCacheWithStatus(mergedMap, statisticsMap, rank);
    }

    /**
     * 缓存更新
     *
     * @param allMap        合并的数据-所有数据
     * @param statisticsMap 统计好的数据
     * @param rank          排行
     * @return
     */
    private PictureStatisticsDto updateCacheWithStatus(
            LinkedHashMap<String, PictureInfoStatisticsVo> allMap,
            LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap, Integer rank) {
        //拿到所有的分数结果
        List<PictureInfoStatisticsVo> allResults = allMap.values().stream()
                .filter(vo -> StringUtils.isNotEmpty(vo.getPictureId()) && StringUtils.isNotNull(vo.getScore()))
                .sorted(Comparator.comparingDouble(PictureInfoStatisticsVo::getScore).reversed())
                .collect(Collectors.toList());
        //所有结果缓存到额外信息
        LinkedHashMap<String, PictureInfoStatisticsVo> sortedMap = new LinkedHashMap<>();
        for (PictureInfoStatisticsVo vo : allResults) {
            sortedMap.put(vo.getPictureId(), vo);
        }
        // 1. 创建最终缓存列表
        int needSize = Math.min(rank, allResults.size());
        List<PictureInfoStatisticsVo> topNList = allResults.subList(0, needSize);
        List<PictureInfoStatisticsVo> cacheList = new ArrayList<>(needSize);
        //未处理的ids
        List<String> noDisposePictureIds = new ArrayList<>();
        // 2. 查询这些图片信息，是否都为正常状态，如果不正常则不缓存且需要补齐缓存数量
        // 2.1 首先从pictureMap里面查询图片信息，因为这里图片信息都是最新数据,然后再为这些数据重新赋值
        for (PictureInfoStatisticsVo statisticsVo : topNList) {
            PictureInfoStatisticsVo pictureInfo = statisticsMap.get(statisticsVo.getPictureId());
            if (StringUtils.isNotNull(pictureInfo) && pictureInfo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue())) {
                cacheList.add(statisticsVo);
                sortedMap.put(statisticsVo.getPictureId(), statisticsVo);
            } else {
                noDisposePictureIds.add(statisticsVo.getPictureId());
            }
        }
        // 2.2 判断如果要的缓存等于subList的长度，则说明缓存数据已经全部更新完毕且数据完全正确，则将pictureMap的数据全部插入缓存中
        if (cacheList.size() >= needSize) {
            return new PictureStatisticsDto(sortedMap, allMap, cacheList, allResults);
        }
        // 3 反之缓存数据未全部更新完毕，未完成需要缓存的最大值，需要从所有信息里面再次获取数据且判断是否为正常，也需要更新合并后的map
        // 为什么从这里直接取呢？因为前面的数据未查询的已经获取到了，直接使用未查询的ids还是有序的
        noDisposePictureIds.addAll(allResults.subList(needSize, allResults.size()).stream().map(PictureInfoStatisticsVo::getPictureId).toList());
        List<PictureInfo> pictureInfoList = new ArrayList<>();
        // 4从这些id中查询图片信息，分批查询
        for (int i = 0; i < noDisposePictureIds.size(); i += 1000) {
            int end = Math.min(i + 1000, noDisposePictureIds.size());
            List<String> subList = noDisposePictureIds.subList(i, end);
            List<PictureInfo> pictureInfos = queryPictureInfoList(subList);
            pictureInfoList.addAll(pictureInfos);
            //4.1如果图片数量达到要求，则退出查询
            if (pictureInfoList.size() + cacheList.size() >= needSize) {
                break;
            }
        }
        //5.查询到的图片更新至排序后的map
        for (PictureInfo pictureInfo : pictureInfoList) {
            PictureInfoStatisticsVo value = PictureInfoStatisticsVo.objToVo(pictureInfo);
            //拿到分数
            value.setScore(allMap.get(pictureInfo.getPictureId()).getScore());
            sortedMap.put(pictureInfo.getPictureId(), value);
        }
        // 6. 构建返回结果
        List<PictureInfoStatisticsVo> resultCache = new ArrayList<>(needSize);
        //转换为List
        for (Map.Entry<String, PictureInfoStatisticsVo> entry : sortedMap.entrySet()) {
            PictureInfoStatisticsVo value = entry.getValue();
            if (StringUtils.isNotEmpty(value.getPictureStatus()) && value.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue()) && resultCache.size() < needSize) {
                resultCache.add(value);
            }
        }
        return new PictureStatisticsDto(sortedMap, allMap, resultCache, allResults);
    }

    private List<PictureInfo> queryPictureInfoList(List<String> pictureIds) {
        return pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>()
                .select(PictureInfo::getPictureId, PictureInfo::getDnsUrl, PictureInfo::getName, PictureInfo::getPicScale, PictureInfo::getPicHeight, PictureInfo::getPicWidth, PictureInfo::getThumbnailUrl, PictureInfo::getPictureStatus)
                .in(PictureInfo::getPictureId, pictureIds)
                .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue()));
    }

    /**
     * 插图片数据
     * 为什么不做其他操作？？因为日只统计当天正常的
     *
     * @param statisticsMap  排序结果MAP
     * @param statisticsType 统计类型
     * @param commonKey      公共key
     * @param statisticsKey  统计key
     * @param statisticsName 统计名称
     * @param isQueryLast
     * @param rank           排序
     * @return void
     * @author: YY
     * @method: insertPictureStatistics
     * @date: 2025/7/19 17:05
     **/
    private PictureStatisticsDto insertPictureStatistics(
            LinkedHashMap<String, PictureInfoStatisticsVo> statisticsMap,
            String statisticsType,
            String commonKey,
            String statisticsKey,
            String statisticsName,
            boolean isQueryLast,
            Integer rank
    ) {
        //构建结果 因为查询到的Ids是有序的，所以直接从resultMap中获取
        List<PictureInfoStatisticsVo> resultList = statisticsMap.values().stream()
                .sorted(Comparator.comparing(PictureInfoStatisticsVo::getScore).reversed()).toList();
        //查询到他的上一期
        Date nowDate = DateUtils.getNowDate();
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        if (!isQueryLast) {
            statisticsInfo.setStages(1L);
        } else {
            StatisticsInfo lastStatisticsInfo = statisticsInfoService.selectNewStatisticsInfoByCommonKey(commonKey);
            if (StringUtils.isNull(lastStatisticsInfo)) {
                statisticsInfo.setStages(1L);
            } else {
                statisticsInfo.setStages(lastStatisticsInfo.getStages() + 1);
            }
        }
        statisticsInfo.setType(statisticsType);
        statisticsInfo.setCommonKey(commonKey);
        statisticsInfo.setStatisticsKey(statisticsKey);
        statisticsInfo.setStatisticsName(statisticsName);
        List<PictureInfoStatisticsVo> statisticsVoList = resultList.subList(0, Math.min(rank, resultList.size()));
        String contentStr = JSONObject.toJSONString(statisticsVoList);
        statisticsInfo.setContent(contentStr);
        statisticsInfo.setExtendContent(contentStr);
        statisticsInfo.setRemark("本次统计一共统计（" + statisticsName + "）：" + statisticsVoList.size() + "张进入热门图片");
        statisticsInfo.setCreateTime(nowDate);
        statisticsInfoService.save(statisticsInfo);
        //删除缓存
        redisCache.deleteObject(statisticsKey);
        //因为这里的结果都是计算完后的，所以这里返回的map是相同的
        return new PictureStatisticsDto(statisticsMap, statisticsMap, statisticsVoList, resultList);
    }
    //endregion
}
