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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.config.utils.ConfigInfoUtils.PICTURE_STATISTICS_HOT_DAY_RANK_VALUE;

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

    private static final String PICTURE_STATISTICS_HOT_KEY = "picture:statistics:hot:minute";
    private static final Integer PICTURE_STATISTICS_HOT_EXPIRE_TIME = 60 * 60 * 24;
    //图片热门统计缓存key，日
    private static final String PICTURE_STATISTICS_HOT_DAY_KEY = "picture:statistics:hot:day";
    private static final String PICTURE_STATISTICS_HOT_DAY_NAME = "图片热门统计日排行";
    private static final Integer PICTURE_STATISTICS_HOT_DAY_EXPIRE_TIME = 60 * 60 * 24 * 3;
    //当前key,需要存入缓存的
    private static String currentKey = PICTURE_STATISTICS_HOT_KEY + COMMON_SEPARATOR_CACHE + System.currentTimeMillis();
    private static String oldKey = "";

    //多线程
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void pictureHotStatisticsIncrementScore(String pictureId, Double score) {
        redisCache.zSetIncrementScore(currentKey, pictureId, score, PICTURE_STATISTICS_HOT_EXPIRE_TIME);
    }

    public void autoStatisticsPictureByDay() {
        //获取当前的时间戳
        long now = System.currentTimeMillis();
        //更新当前key,把当前时间戳赋给oldKey，构建当前key
        oldKey = currentKey;
//        System.out.println("old = " + oldKey);
        currentKey = PICTURE_STATISTICS_HOT_KEY + COMMON_SEPARATOR_CACHE + now;
//        System.out.println("currentKey = " + currentKey);
        //从旧key中获取数据
        Set<ZSetOperations.TypedTuple<String>> typedTuples = redisCache.zSetRangeWithScores(oldKey, 0, -1);
        //如果没有数据，直接返回不统计
        if (StringUtils.isEmpty(typedTuples)) {
            return;
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
        //查询到对应的统计信息
        StatisticsInfo statisticsInfo = statisticsInfoService.selectStatisticsInfoByStatisticsKey(PICTURE_STATISTICS_HOT_DAY_KEY + COMMON_SEPARATOR_CACHE + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.getNowDate()));
        //判断对象是否存在，存在是更新，不存在是插入
        if (StringUtils.isNull(statisticsInfo)) {
            insertStatisticsByDay(resultMap);
        } else {
            statisticsPictureUpdate(statisticsInfo, resultMap);
        }
    }

    /**
     * 更新缓存信息
     *
     * @param statisticsInfo 统计对象
     * @param resultMap      结果集-缓存拿的
     * @return void
     * @author: YY
     * @method: statisticsPictureUpdate
     * @date: 2025/7/20 00:21
     **/
    private void statisticsPictureUpdate(StatisticsInfo statisticsInfo,
                                         LinkedHashMap<String, PictureInfoStatisticsVo> resultMap
    ) {
        // 1. 解析历史数据，获取到所有信息，包括为私有的图片
        List<PictureInfoStatisticsVo> dbResult = Optional.ofNullable(
                JSONObject.parseArray(statisticsInfo.getContent(), PictureInfoStatisticsVo.class)
        ).orElseGet(ArrayList::new);


        Map<String, PictureInfoStatisticsVo> dbScoreMap = dbResult.stream()
                .filter(vo -> StringUtils.isNotEmpty(vo.getPictureId()))
                .collect(Collectors.toMap(
                        PictureInfoStatisticsVo::getPictureId,
                        //需要获取数据库内对象完整信息
                        vo -> vo,
                        (v1, v2) -> v1
                ));


        // 3. 合并所有数据（不区分状态）
        Map<String, PictureInfoStatisticsVo> mergedMap = new LinkedHashMap<>();

        // 3.1 合并当前窗口数据
        //新建已处理的map，便于快速合并
        Map<String, PictureInfoStatisticsVo> processedMap = new LinkedHashMap<>();
        for (Map.Entry<String, PictureInfoStatisticsVo> entry : resultMap.entrySet()) {
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
            return;
        }
        // 4  添加历史数据中未更新的图片
        for (PictureInfoStatisticsVo dbVo : dbResult) {
            if (StringUtils.isNotEmpty(dbVo.getPictureId()) &&
                    !processedMap.containsKey(dbVo.getPictureId())) {
                mergedMap.put(dbVo.getPictureId(), dbVo);
            }
        }

        // 4. 排序并保存完整结果
        // 5.从完整结果内获取到TopN，并判断TopN的图片状态是否是公开，公开就存缓存，不公开就从所有结果获取后面内容直至补齐TopN
        List<PictureInfoStatisticsVo> cacheList = updateCacheWithStatus(statisticsInfo, mergedMap, resultMap);
        // 6. 保存完整结果到数据库
        statisticsInfo.setContent(JSONObject.toJSONString(cacheList));
        statisticsInfoService.updateById(statisticsInfo);

    }

    /**
     * 缓存更新
     *
     * @param statisticsInfo 统计内容
     * @param mergedMap      合并的数据
     * @param resultMap      结果数据-从缓存拿的
     * @return
     */
    private List<PictureInfoStatisticsVo> updateCacheWithStatus(StatisticsInfo statisticsInfo,
                                                                Map<String, PictureInfoStatisticsVo> mergedMap,
                                                                Map<String, PictureInfoStatisticsVo> resultMap) {
        //拿到所有的分数结果
        List<PictureInfoStatisticsVo> allResults = mergedMap.values().stream()
                .filter(vo -> StringUtils.isNotEmpty(vo.getPictureId()) && StringUtils.isNotNull(vo.getScore()))
                .sorted(Comparator.comparingDouble(PictureInfoStatisticsVo::getScore).reversed())
                .collect(Collectors.toList());
        //所有结果缓存到额外信息
        statisticsInfo.setExtendContent(JSONObject.toJSONString(allResults));
        Map<String, PictureInfoStatisticsVo> sortedMap = new LinkedHashMap<>();
        for (PictureInfoStatisticsVo vo : allResults) {
            sortedMap.put(vo.getPictureId(), vo);
        }
        // 1. 创建最终缓存列表
        int needSize = Math.min(PICTURE_STATISTICS_HOT_DAY_RANK_VALUE, allResults.size());
        List<PictureInfoStatisticsVo> topNList = allResults.subList(0, needSize);
        List<PictureInfoStatisticsVo> cacheList = new ArrayList<>(needSize);
        //未处理的ids
        List<String> noDisposePictureIds = new ArrayList<>();
        // 2. 查询这些图片信息，是否都为正常状态，如果不正常则不缓存且需要补齐缓存数量
        // 2.1 首先从pictureMap里面查询图片信息，因为这里图片信息都是最新数据,然后再为这些数据重新赋值
        for (PictureInfoStatisticsVo statisticsVo : topNList) {
            PictureInfoStatisticsVo pictureInfo = resultMap.get(statisticsVo.getPictureId());
            if (StringUtils.isNotNull(pictureInfo) && pictureInfo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue())) {
                cacheList.add(statisticsVo);
                sortedMap.put(statisticsVo.getPictureId(), statisticsVo);
            } else {
                noDisposePictureIds.add(statisticsVo.getPictureId());
            }
        }
        // 2.2 判断如果要的缓存等于subList的长度，则说明缓存数据已经全部更新完毕且数据完全正确，则将pictureMap的数据全部插入缓存中
        if (cacheList.size() >= needSize) {
            redisCache.setCacheObject(statisticsInfo.getStatisticsKey(), cacheList, PICTURE_STATISTICS_HOT_DAY_EXPIRE_TIME, TimeUnit.SECONDS);
            return allResults;
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
            value.setScore(mergedMap.get(pictureInfo.getPictureId()).getScore());
            sortedMap.put(pictureInfo.getPictureId(), value);
        }
        // 6. 构建返回结果
        List<PictureInfoStatisticsVo> resultList = new ArrayList<>(sortedMap.values());
        List<PictureInfoStatisticsVo> resultCache = new ArrayList<>(needSize);
        //转换为List
        for (Map.Entry<String, PictureInfoStatisticsVo> entry : sortedMap.entrySet()) {
            PictureInfoStatisticsVo value = entry.getValue();
            resultList.add(value);
            if (StringUtils.isNotEmpty(value.getPictureStatus()) && value.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue()) && resultCache.size() < needSize) {
                resultCache.add(value);
            }
        }
        //缓存结果
        redisCache.setCacheObject(statisticsInfo.getStatisticsKey(), resultCache, PICTURE_STATISTICS_HOT_DAY_EXPIRE_TIME, TimeUnit.SECONDS);
        return resultList;
    }

    private List<PictureInfo> queryPictureInfoList(List<String> pitureIds) {
        return pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>()
                .select(PictureInfo::getPictureId, PictureInfo::getDnsUrl, PictureInfo::getName, PictureInfo::getPicScale, PictureInfo::getPicHeight, PictureInfo::getPicWidth, PictureInfo::getThumbnailUrl, PictureInfo::getPictureStatus)
                .in(PictureInfo::getPictureId, pitureIds)
                .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue()));
    }

    /**
     * 插图片数据
     * 为什么不做其他操作？？因为日只统计当天正常的
     *
     * @param resultMap 排序结果MAP
     * @return void
     * @author: YY
     * @method: insertStatisticsByDay
     * @date: 2025/7/19 17:05
     **/
    private List<PictureInfoStatisticsVo> insertStatisticsByDay(LinkedHashMap<String, PictureInfoStatisticsVo> resultMap) {
        //构建结果 因为查询到的Ids是有序的，所以直接从resultMap中获取
        List<PictureInfoStatisticsVo> resultList = resultMap.values().stream()
                .sorted(Comparator.comparing(PictureInfoStatisticsVo::getScore).reversed()).toList();
        //查询到他的上一期
        Date nowDate = DateUtils.getNowDate();
        StatisticsInfo lastStatisticsInfo = statisticsInfoService.selectStatisticsInfoByStatisticsKey(PICTURE_STATISTICS_HOT_DAY_KEY + COMMON_SEPARATOR_CACHE + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.addDays(nowDate, -1)));
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setType(PStatisticsTypeEnum.STATISTICS_TYPE_2.getValue());
        if (StringUtils.isNull(lastStatisticsInfo)) {
            statisticsInfo.setStages(1L);
        } else {
            statisticsInfo.setStages(lastStatisticsInfo.getStages() + 1);
        }
        String statisticsKey = PICTURE_STATISTICS_HOT_DAY_KEY + COMMON_SEPARATOR_CACHE + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, nowDate);
        statisticsInfo.setStatisticsKey(statisticsKey);
        statisticsInfo.setStatisticsName(PICTURE_STATISTICS_HOT_DAY_NAME);
        List<PictureInfoStatisticsVo> statisticsVoList = resultList.subList(0, Math.min(PICTURE_STATISTICS_HOT_DAY_RANK_VALUE, resultList.size()));
        statisticsInfo.setContent(JSONObject.toJSONString(statisticsVoList));
        statisticsInfo.setCreateTime(nowDate);
        statisticsInfoService.save(statisticsInfo);
        //存入缓存
        redisCache.setCacheObject(statisticsKey, statisticsVoList, PICTURE_STATISTICS_HOT_DAY_EXPIRE_TIME, TimeUnit.SECONDS);
        return statisticsVoList;
    }
}
