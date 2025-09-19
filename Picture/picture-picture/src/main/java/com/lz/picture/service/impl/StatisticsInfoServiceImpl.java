package com.lz.picture.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
import com.lz.common.config.OssConfig;
import com.lz.common.config.RuoYiConfig;
import com.lz.common.core.domain.statistics.ro.StatisticsRo;
import com.lz.common.core.domain.statistics.vo.BarStatisticsVo;
import com.lz.common.core.domain.statistics.vo.LineManyStatisticsVo;
import com.lz.common.core.domain.statistics.vo.StatisticsVo;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.manager.file.PictureDownloadManager;
import com.lz.common.manager.file.model.BatchDownloadFileDto;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.file.FileUtils;
import com.lz.common.utils.reflect.ReflectUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.common.utils.verify.DateVerifyUtils;
import com.lz.picture.mapper.StatisticsInfoMapper;
import com.lz.picture.model.domain.StatisticsInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoHotRequest;
import com.lz.picture.model.dto.statistics.BasePictureStatisticsRequest;
import com.lz.picture.model.dto.statistics.KeywordStatisticsRequest;
import com.lz.picture.model.dto.statistics.PictureStatisticsRequest;
import com.lz.picture.model.dto.statistics.PictureStatisticsRo;
import com.lz.picture.model.dto.statisticsInfo.StatisticsFileDto;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoQuery;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoRequest;
import com.lz.picture.model.enums.*;
import com.lz.picture.model.vo.pictureInfo.PictureInfoStatisticsVo;
import com.lz.picture.model.vo.statisticsInfo.StatisticsInfoVo;
import com.lz.picture.service.IStatisticsInfoService;
import com.lz.picture.utils.MarkdownBuilder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.picture.PictureStatisticsConstants.*;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_STATISTICS_STAGES;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_STATISTICS_STAGES_EXPIRE_TIME;
import static com.lz.config.utils.ConfigInfoUtils.PICTURE_INDEX_P_VALUE;

/**
 * 统计信息Service业务层处理
 *
 * @author YY
 * @date 2025-07-17
 */
@Slf4j
@Service
public class StatisticsInfoServiceImpl extends ServiceImpl<StatisticsInfoMapper, StatisticsInfo> implements IStatisticsInfoService {
    @Resource
    private StatisticsInfoMapper statisticsInfoMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private PictureDownloadManager pictureDownloadManager;
    //region mybatis代码

    /**
     * 查询统计信息
     *
     * @param statisticsId 统计信息主键
     * @return 统计信息
     */
    @Override
    public StatisticsInfo selectStatisticsInfoByStatisticsId(String statisticsId) {
        return statisticsInfoMapper.selectStatisticsInfoByStatisticsId(statisticsId);
    }

    /**
     * 查询统计信息列表
     *
     * @param statisticsInfo 统计信息
     * @return 统计信息
     */
    @CustomSort(sortFields = {"statisticsName", "commonKey", "statisticsKey", "createTime"},
            sortMappingFields = {"statistics_name", "common_key", "statistics_key", "create_time"})
    @Override
    public List<StatisticsInfo> selectStatisticsInfoList(StatisticsInfo statisticsInfo) {
        return statisticsInfoMapper.selectStatisticsInfoList(statisticsInfo);
    }

    /**
     * 新增统计信息
     *
     * @param statisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int insertStatisticsInfo(StatisticsInfo statisticsInfo) {
        statisticsInfo.setCreateTime(DateUtils.getNowDate());
        return statisticsInfoMapper.insertStatisticsInfo(statisticsInfo);
    }

    /**
     * 修改统计信息
     *
     * @param statisticsInfo 统计信息
     * @return 结果
     */
    @Override
    public int updateStatisticsInfo(StatisticsInfo statisticsInfo) {
        return statisticsInfoMapper.updateStatisticsInfo(statisticsInfo);
    }

    /**
     * 批量删除统计信息
     *
     * @param statisticsIds 需要删除的统计信息主键
     * @return 结果
     */
    @Override
    public int deleteStatisticsInfoByStatisticsIds(String[] statisticsIds) {
        return statisticsInfoMapper.deleteStatisticsInfoByStatisticsIds(statisticsIds);
    }

    /**
     * 删除统计信息信息
     *
     * @param statisticsId 统计信息主键
     * @return 结果
     */
    @Override
    public int deleteStatisticsInfoByStatisticsId(String statisticsId) {
        return statisticsInfoMapper.deleteStatisticsInfoByStatisticsId(statisticsId);
    }

    //endregion
    @Override
    public QueryWrapper<StatisticsInfo> getQueryWrapper(StatisticsInfoQuery statisticsInfoQuery) {
        QueryWrapper<StatisticsInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = statisticsInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String statisticsId = statisticsInfoQuery.getStatisticsId();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsId), "statistics_id", statisticsId);

        String type = statisticsInfoQuery.getType();
        queryWrapper.eq(StringUtils.isNotEmpty(type), "type", type);

        String statisticsName = statisticsInfoQuery.getStatisticsName();
        queryWrapper.like(StringUtils.isNotEmpty(statisticsName), "statistics_name", statisticsName);

        String commonKey = statisticsInfoQuery.getCommonKey();
        queryWrapper.like(StringUtils.isNotEmpty(commonKey), "common_key", commonKey);

        String statisticsKey = statisticsInfoQuery.getStatisticsKey();
        queryWrapper.eq(StringUtils.isNotEmpty(statisticsKey), "statistics_key", statisticsKey);

        Long stages = statisticsInfoQuery.getStages();
        queryWrapper.eq(StringUtils.isNotNull(stages), "stages", stages);


        Date createTime = statisticsInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<StatisticsInfoVo> convertVoList(List<StatisticsInfo> statisticsInfoList) {
        if (StringUtils.isEmpty(statisticsInfoList)) {
            return Collections.emptyList();
        }
        return statisticsInfoList.stream().map(StatisticsInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public StatisticsInfo selectStatisticsInfoByStatisticsKey(String key) {
        return this.getOne(new LambdaQueryWrapper<StatisticsInfo>().eq(StatisticsInfo::getStatisticsKey, key)
                .orderBy(true, false, StatisticsInfo::getCreateTime).last("limit 1"));
    }

    @Override
    public StatisticsInfo selectNewStatisticsInfoByCommonKeyAndType(String key, String type) {
        return this.getOne(new LambdaQueryWrapper<StatisticsInfo>().eq(StatisticsInfo::getCommonKey, key)
                .eq(StatisticsInfo::getType, type)
                .orderBy(true, false, StatisticsInfo::getCreateTime).last("limit 1"));
    }

    @Override
    public TableDataInfo getStatisticsPictureInfo(String statisticKey, PictureInfoHotRequest request) {
        //构建查询条件
        int start = (request.getPageNum() - 1) * request.getPageSize();
        int end = start + request.getPageSize() - 1;
        //首先判断缓存是否存在
        if (redisCache.hasKey(statisticKey)) {
            Long size = redisCache.getCacheListSize(statisticKey);
            List<PictureInfoStatisticsVo> cacheList = redisCache.getCacheList(statisticKey, start, end);
            return new TableDataInfo(cacheList, Math.toIntExact(size));
        }
        //如果不存在则查询数据库
        StatisticsInfo statisticsInfo = this.selectStatisticsInfoByStatisticsKey(statisticKey);
        if (StringUtils.isNotNull(statisticsInfo) && StringUtils.isNotNull(statisticsInfo.getContent())) {
            try {
                //转换为List
                List<PictureInfoStatisticsVo> statisticsVoList = JSONObject.parseArray(statisticsInfo.getContent(), PictureInfoStatisticsVo.class);
                for (PictureInfoStatisticsVo vo : statisticsVoList) {
                    vo.setThumbnailUrl(OssConfig.builderPictureUrl(vo.getThumbnailUrl(), PICTURE_INDEX_P_VALUE));
                }
                //缓存key，返回值
                redisCache.deleteObject(statisticKey);
                //这里直接指定，因为十分钟更新一次统计缓存
                redisCache.setCacheListRightPushAll(statisticKey, statisticsVoList, 15 * 60, TimeUnit.SECONDS);
                List<PictureInfoStatisticsVo> resultList = statisticsVoList.subList(Math.min(start, statisticsVoList.size()), Math.min(end, statisticsVoList.size()));
                return new TableDataInfo(resultList, statisticsVoList.size());
            } catch (Exception e) {
                log.error("获取图片统计信息失败:{}", e.getMessage());
                return new TableDataInfo(new ArrayList<>(), 0);
            }
        }
        return new TableDataInfo(new ArrayList<>(), 0);
    }

    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_STAGES, useQueryParamsAsKey = true, expireTime = PICTURE_STATISTICS_STAGES_EXPIRE_TIME)
    @Override
    public Long getStatisticsInfoStages(StatisticsInfoRequest request) {
        StatisticsInfo statisticsInfo = this.getOne(new LambdaQueryWrapper<StatisticsInfo>().eq(StatisticsInfo::getCommonKey, request.getCommonKey())
                .eq(StringUtils.isNotEmpty(request.getType()), StatisticsInfo::getType, request.getType())
                .orderBy(true, false, StatisticsInfo::getCreateTime).last("limit 1"));
        if (StringUtils.isNull(statisticsInfo)) {
            return 0L;
        }
        return statisticsInfo.getStages();
    }

    //region 获取统计文件
    @Override
    public StatisticsFileDto getStatisticsPictureHotFilePath(String type, String commonKey, String statisticsKey, Long stage, int number) {
        ThrowUtils.throwIf(StringUtils.isEmpty(statisticsKey) && StringUtils.isNull(stage) && StringUtils.isEmpty(commonKey) && StringUtils.isEmpty(type),
                "参数错误");
        //如果有统计key，则优先使用
        if (StringUtils.isNotEmpty(statisticsKey)) {
            stage = null;
        }
        StatisticsInfo statisticsInfo = this.getOne(
                new LambdaQueryWrapper<StatisticsInfo>().eq(StatisticsInfo::getCommonKey, commonKey)
                        .eq(StringUtils.isNotEmpty(type), StatisticsInfo::getType, type)
                        .eq(StringUtils.isNotEmpty(statisticsKey), StatisticsInfo::getStatisticsKey, statisticsKey)
                        .eq(StringUtils.isNotNull(stage), StatisticsInfo::getStages, stage)
                        .orderBy(true, false, StatisticsInfo::getCreateTime).last("limit 1"));
        ThrowUtils.throwIf(StringUtils.isNull(statisticsInfo) || StringUtils.isEmpty(statisticsInfo.getContent()), "未找到统计信息");
        //转换为 List
        List<PictureInfoStatisticsVo> statisticsVoList = JSONObject.parseArray(statisticsInfo.getContent(), PictureInfoStatisticsVo.class);
        List<PictureInfoStatisticsVo> vos = statisticsVoList.subList(0, Math.min(number, statisticsVoList.size()));
        //文件夹路径,替换statisticsKey里的: 为/
        String keyPath = StringUtils.replace(statisticsInfo.getStatisticsKey(), ":", File.separator);
        String bastPath = RuoYiConfig.getPicturePath() + keyPath;
        String imagesPath = bastPath + File.separator + "images";
        downloadHotPictureImage(vos, imagesPath);
        String mdPath = downloadHotPictureMd(statisticsInfo, bastPath, vos);
        //给文件压缩成 zip
        String zipPath = bastPath + File.separator + statisticsInfo.getStatisticsName() + "_" + System.currentTimeMillis() + ".zip";
        String compressZip = FileUtils.compressZip(List.of(imagesPath, mdPath), zipPath);
        //压缩成功删除md文件
        FileUtils.deleteFile(mdPath);
        return new StatisticsFileDto(FileUtils.getName(compressZip), compressZip);
    }

    /**
     * 生成md文件
     *
     * @param bastPath 基础路径
     * @param vos      vos
     */
    private String downloadHotPictureMd(StatisticsInfo statisticsInfo, String bastPath, List<PictureInfoStatisticsVo> vos) {
        //判断是否存在describe.md,如果存在直接删除
        String describePath = bastPath + File.separator + "图片热门统计_" + statisticsInfo.getStatisticsName() + "_" + System.currentTimeMillis() + ".md";
        if (FileUtils.isFileExists(describePath)) {
            FileUtils.deleteFile(describePath);
        }
        //开始写入md文件
        MarkdownBuilder builder = new MarkdownBuilder();
        builder.addHeading(statisticsInfo.getStatisticsName(), 1);
        builder.addParagraph(statisticsInfo.getRemark());
        builder.addParagraph("本次统计KEY:" + statisticsInfo.getStatisticsKey());
        builder.addHeading("图片列表", 2);
        //图片列表
        ArrayList<String> headers = new ArrayList<>(List.of("排名", "图片编号", "图片名称", "图片分数"));
        ArrayList<List<String>> rows = new ArrayList<>();
        //图片列表
        List<StatisticsFileDto> imageList = new ArrayList<>();
        for (PictureInfoStatisticsVo vo : vos) {
            //获取文件名
            if (StringUtils.isEmpty(vo.getThumbnailUrl())) {
                continue;
            }
            String fileName = FileUtils.getName(vo.getThumbnailUrl());
            rows.add(List.of(
                    String.valueOf(rows.size() + 1),
                    vo.getPictureId(),
                    vo.getName(),
                    String.valueOf(vo.getScore())
            ));
            StatisticsFileDto statisticsFileDto = new StatisticsFileDto(vo.getName(), "./images/" + fileName);
            imageList.add(statisticsFileDto);
        }
        builder.addTable(headers, rows);
        //图片列表
        builder.addHeading("图片列表", 2);
        for (int i = 0; i < imageList.size(); i++) {
            StatisticsFileDto file = imageList.get(i);
            builder.addHeading("排名:" + (i + 1), 3);
            builder.addParagraph(file.getFileName());
            builder.addImage(file.getFileName(), file.getFilePath());
        }
        try {
            builder.writeToFile(describePath);
        } catch (IOException e) {
            log.error("写入md文件失败:{}", e.getMessage());
            throw new RuntimeException("写入md文件失败");
        }
        return describePath;
    }

    /**
     * 下载图片
     *
     * @param vos        vos
     * @param imagesPath 图片保存路径
     * @return
     */
    private void downloadHotPictureImage(List<PictureInfoStatisticsVo> vos, String imagesPath) {
        List<BatchDownloadFileDto> batchDownloadFileDtos = new ArrayList<>();
        for (PictureInfoStatisticsVo vo : vos) {
            //获取文件名
            if (StringUtils.isEmpty(vo.getThumbnailUrl())) {
                continue;
            }
            String fileName = FileUtils.getName(vo.getThumbnailUrl());
            String localPath = imagesPath + File.separator + fileName;
            if (FileUtils.isFileExists(localPath)) {
                //存在则跳过
                continue;
            }
            BatchDownloadFileDto batchDownloadFileDto = new BatchDownloadFileDto();
            batchDownloadFileDto.setLocalPath(localPath);
            batchDownloadFileDto.setOssFilePath(vo.getThumbnailUrl());
            batchDownloadFileDtos.add(batchDownloadFileDto);
        }
        if (!batchDownloadFileDtos.isEmpty()) {
            //下载文件
            pictureDownloadManager.downloadFile(batchDownloadFileDtos);
        }
    }
    //endregion

    @Override
    public List<StatisticsInfo> getStatisticsInfosByDateAndKeyType(String startDate, String endDate, String type, String commonKey) {
        return this.list(new LambdaQueryWrapper<StatisticsInfo>()
                .eq(StatisticsInfo::getType, type)
                .eq(StatisticsInfo::getCommonKey, commonKey)
                //大于开始时间
                .ge(StatisticsInfo::getCreateTime, startDate)
                //小于结束时间
                .apply("create_time < DATE_ADD({0},INTERVAL 1 day)", endDate)
        );
    }

    @Override
    public StatisticsInfo getStatisticsInfoByCommonKey(String commonKey, String type) {
        return this.getOne(new LambdaQueryWrapper<StatisticsInfo>()
                .eq(StatisticsInfo::getType, type)
                .eq(StatisticsInfo::getCommonKey, commonKey)
                .orderByDesc(StatisticsInfo::getCreateTime)
                .last("limit 1"));
    }

    //region 统计
    @Override
    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_SEARCHER_KEYWORD,
            expireTime = PICTURE_STATISTICS_SEARCHER_KEYWORD_EXPIRE_TIME,
            useQueryParamsAsKey = true)
    public List<StatisticsVo> searchKeywordStatistics(KeywordStatisticsRequest request) {
        return buildRangeStatistics(
                PICTURE_STATISTICS_SEARCHER_KEYWORD,
                PStatisticsTypeEnum.STATISTICS_TYPE_7,
                PICTURE_STATISTICS_SEARCHER_KEYWORD_NAME,
                request,
                (req, date) -> {
                    KeywordStatisticsRequest tmp = new KeywordStatisticsRequest();
                    tmp.setStartDate(date);
                    tmp.setEndDate(date);
                    tmp.setSize(req.getSize());
                    return statisticsInfoMapper.keywordSearchStatistics(tmp);
                },
                this::builderKeywordStatisticsResult
        );
    }

    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_TAG_KEYWORD,
            expireTime = PICTURE_STATISTICS_TAG_KEYWORD_EXPIRE_TIME,
            useQueryParamsAsKey = true)
    @Override
    public List<StatisticsVo> tagKeywordStatistics(KeywordStatisticsRequest request) {
        return buildRangeStatistics(
                PICTURE_STATISTICS_TAG_KEYWORD,
                PStatisticsTypeEnum.STATISTICS_TYPE_8,
                PICTURE_STATISTICS_TAG_KEYWORD_NAME,
                request,
                (req, date) -> {
                    KeywordStatisticsRequest tmp = new KeywordStatisticsRequest();
                    tmp.setStartDate(date);
                    tmp.setEndDate(date);
                    tmp.setSize(req.getSize());
                    return statisticsInfoMapper.tagKeywordStatistics(tmp);
                },
                this::builderKeywordStatisticsResult
        );
    }

    @Override
    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_USER_BEHAVIOR,
            expireTime = PICTURE_STATISTICS_USER_BEHAVIOR_EXPIRE_TIME,
            useQueryParamsAsKey = true)
    public List<StatisticsVo> userBehaviorStatistics(BasePictureStatisticsRequest request) {
        return buildRangeStatistics(
                PICTURE_STATISTICS_USER_BEHAVIOR,
                PStatisticsTypeEnum.STATISTICS_TYPE_13,
                PICTURE_STATISTICS_USER_BEHAVIOR_NAME,
                request,
                (req, date) -> {
                    BasePictureStatisticsRequest tmp = new BasePictureStatisticsRequest();
                    tmp.setStartDate(date);
                    tmp.setEndDate(date);
                    return statisticsInfoMapper.userBehaviorStatistics(tmp);
                },
                this::builderUserBehaviorStatisticsResult
        );
    }


    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_PICTURE_DOWNLOAD, expireTime = PICTURE_STATISTICS_PICTURE_DOWNLOAD_EXPIRE_TIME, useQueryParamsAsKey = true)
    @Override
    public BarStatisticsVo pictureDownloadStatistics(BasePictureStatisticsRequest request) {
        return buildRangeStatistics(
                PICTURE_STATISTICS_PICTURE_DOWNLOAD,
                PStatisticsTypeEnum.STATISTICS_TYPE_14,
                PICTURE_STATISTICS_PICTURE_DOWNLOAD_NAME,
                request,
                (req, date) -> {
                    BasePictureStatisticsRequest tmp = new BasePictureStatisticsRequest();
                    tmp.setStartDate(date);
                    tmp.setEndDate(date);
                    return statisticsInfoMapper.pictureDownloadStatistics(tmp);
                },
                this::builderBarStatisticsResult
        );
    }

    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_SPACE_ADD,
            expireTime = PICTURE_STATISTICS_SPACE_ADD_EXPIRE_TIME, useQueryParamsAsKey = true)
    @Override
    public BarStatisticsVo spaceStatistics(BasePictureStatisticsRequest request) {
        return buildRangeStatistics(
                PICTURE_STATISTICS_SPACE_ADD,
                PStatisticsTypeEnum.STATISTICS_TYPE_15,
                PICTURE_STATISTICS_SPACE_ADD_NAME,
                request,
                (req, date) -> {
                    BasePictureStatisticsRequest tmp = new BasePictureStatisticsRequest();
                    tmp.setStartDate(date);
                    tmp.setEndDate(date);
                    return statisticsInfoMapper.spaceStatistics(tmp);
                },
                this::builderBarStatisticsResult
        );
    }


    /**
     * 构建范围查询统计
     *
     * @param commonKey          公共key
     * @param statisticsTypeEnum 统计类型枚举
     * @param statisticsName     统计名称
     * @param request            请求参数
     * @param queryFunction      查询方法
     * @param resultBuilder      结果构建方法
     * @return List<StatisticsVo>
     * @author: YY
     * @method: buildRangeStatistics
     * @date: 2025/9/19 16:31
     **/
    private <REQ, R> R buildRangeStatistics(
            String commonKey,
            PStatisticsTypeEnum statisticsTypeEnum,
            String statisticsName,
            REQ request,
            BiFunction<REQ, String, List<StatisticsRo>> queryFunction, // queryFunction(request, date)
            Function<Map<String, Long>, R> resultBuilder
    ) {
        // 拿到开始结束时间
        String startDate = (String) ReflectUtils.getFieldValue(request, "startDate");
        String endDate = (String) ReflectUtils.getFieldValue(request, "endDate");
        Date nowDate = DateVerifyUtils.checkDateIsStartAfter(startDate, endDate);

        List<String> dateRanges = DateUtils.getDateRanges(startDate, endDate);
        if (StringUtils.isEmpty(dateRanges) || dateRanges == null) {
            // 创建一个空的Map并使用resultBuilder来生成默认结果
            return resultBuilder.apply(new HashMap<>());
        }

        String end = dateRanges.getLast();
        Map<String, Long> resultMap = new HashMap<>();
        String today = DateUtils.dateTime(nowDate);

        // 今天的数据必须实时查
        if (dateRanges.contains(today)) {
            List<StatisticsRo> todayList = queryFunction.apply(request, today);
            builderStatisticsRoNamesAndValue(resultMap, todayList);

            if (dateRanges.size() == 1) {
                return resultBuilder.apply(resultMap);
            }
            dateRanges.removeLast();
            end = dateRanges.getLast();
        }

        // 历史数据查统计表
        List<StatisticsInfo> statisticsInfoList = getStatisticsInfosByDateAndKeyType(startDate, end, statisticsTypeEnum.getValue(), commonKey);

        // 找到没统计过的日期
        List<String> noStatisticsDate = getNoStatisticsDate(dateRanges, statisticsInfoList, resultMap);
        if (!noStatisticsDate.isEmpty()) {
            List<StatisticsInfo> newInfoList = new ArrayList<>();
            for (String date : noStatisticsDate) {
                List<StatisticsRo> statisticsRos = queryFunction.apply(request, date);
                builderStatisticsRoNamesAndValue(resultMap, statisticsRos);
                StatisticsInfo info = builderStatisticsInfo(date, statisticsRos,
                        statisticsTypeEnum.getValue(), commonKey, statisticsName, 1L);
                newInfoList.add(info);
            }
            statisticsInfoMapper.insert(newInfoList);
        }
        return resultBuilder.apply(resultMap);
    }

    /**
     * 构建柱形图结果
     *
     * @param resultMap 返回结果
     * @return
     */
    private BarStatisticsVo builderBarStatisticsResult(Map<String, Long> resultMap) {
        //key是时间，value是数量
        //根据时间排序
        LinkedHashMap<String, Long> sortMap = resultMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        BarStatisticsVo barStatisticsVo = new BarStatisticsVo();
        barStatisticsVo.setNames(sortMap.keySet().stream().map(date -> date).toList());
        barStatisticsVo.setTotals(sortMap.values().stream().map(total -> total).toList());
        return barStatisticsVo;
    }

    /**
     * 构建用户行为统计结果
     *
     * @param resultMap 统计结果
     * @return
     */
    private List<StatisticsVo> builderUserBehaviorStatisticsResult(Map<String, Long> resultMap) {
        List<StatisticsVo> statisticsVoList = new ArrayList<>();
        resultMap
                .forEach((key, value) -> {
                            Optional<PUserBehaviorTypeEnum> enumByValue = PUserBehaviorTypeEnum.getEnumByValue(key);
                            StatisticsVo statisticsVo = new StatisticsVo();
                            statisticsVo.setValue(value);
                            if (enumByValue.isPresent()) {
                                PUserBehaviorTypeEnum behaviorTypeEnum = enumByValue.get();
                                statisticsVo.setName(behaviorTypeEnum.getLabel());
                            } else {
                                statisticsVo.setName(key);
                            }
                            statisticsVoList.add(statisticsVo);
                        }
                );
        return statisticsVoList;
    }

    /**
     * 获取没有统计的日期
     *
     * @param dateRanges         时间范围
     * @param statisticsInfoList 统计信息
     * @param resultMap          结果
     * @return List<String>
     * @author: YY
     * @method: getNoStatisticsDate
     * @date: 2025/9/18 18:28
     **/
    @NotNull
    private List<String> getNoStatisticsDate(List<String> dateRanges, List<StatisticsInfo> statisticsInfoList, Map<String, Long> resultMap) {
        List<String> noStatisticsDate = new ArrayList<>(dateRanges);
        if (!statisticsInfoList.isEmpty()) {
            for (StatisticsInfo statisticsInfo : statisticsInfoList) {
                //删除统计的日期
                noStatisticsDate.remove(DateUtils.dateTime(statisticsInfo.getCreateTime()));
                if (StringUtils.isNotEmpty(statisticsInfo.getContent())) {
                    //拿到内容
                    String content = statisticsInfo.getContent();
                    //拿到内容中的关键词
                    List<StatisticsRo> keywordList = JSONObject.parseArray(content, StatisticsRo.class);
                    //添加到结果中
                    builderStatisticsRoNamesAndValue(resultMap, keywordList);
                }
            }
        }
        return noStatisticsDate;
    }

    /**
     * 构建关键词统计结果
     *
     * @param resultMap        resultMap
     * @param statisticsRoList statisticsRoList
     * @author: YY
     * @method: builderKeywordStatisticsRoNamesAndValue
     * @date: 2025/9/18 16:39
     **/
    private void builderStatisticsRoNamesAndValue(Map<String, Long> resultMap, List<StatisticsRo> statisticsRoList) {
        if (StringUtils.isEmpty(statisticsRoList)) {
            return;
        }
        for (StatisticsRo statisticsRo : statisticsRoList) {
            if (StringUtils.isEmpty(statisticsRo.getName()) || StringUtils.isNull(statisticsRo.getTotal())) {
                continue;
            }
            if (resultMap.containsKey(statisticsRo.getName())) {
                resultMap.put(statisticsRo.getName(), resultMap.get(statisticsRo.getName()) + statisticsRo.getTotal());
            } else {
                resultMap.put(statisticsRo.getName(), statisticsRo.getTotal());
            }
        }
    }

    /**
     * 构建统计信息
     *
     * @param date           date
     * @param content        content
     * @param type           type
     * @param commonKey      commonKey
     * @param statisticsName statisticsName
     * @param stage          stage
     * @return StatisticsInfo
     * @author: YY
     * @method: builderStatisticsInfo
     * @date: 2025/9/18 16:39
     **/
    private StatisticsInfo builderStatisticsInfo(String date, Object content, String type, String commonKey, String statisticsName, Long stage) {
        StatisticsInfo statisticsInfo = new StatisticsInfo();
        statisticsInfo.setStatisticsId(IdUtils.snowflakeId().toString());
        statisticsInfo.setType(type);
        statisticsInfo.setStatisticsName(statisticsName);
        statisticsInfo.setCommonKey(commonKey);
        statisticsInfo.setStatisticsKey(commonKey + COMMON_SEPARATOR_CACHE + date);
        statisticsInfo.setStages(stage);
        statisticsInfo.setContent(JSONObject.toJSONString(content));
        statisticsInfo.setCreateTime(DateUtils.dateTime(DateUtils.YYYY_MM_DD, date));
        return statisticsInfo;
    }


    /**
     * 构建关键词统计结果
     *
     * @param resultMap resultMap
     * @return List<KeywordStatisticsVo>
     * @author: YY
     * @method: builderKeywordStatisticsResult
     * @date: 2025/9/18 16:39
     **/
    private List<StatisticsVo> builderKeywordStatisticsResult(Map<String, Long> resultMap) {
        return resultMap.entrySet().stream().map(entry -> {
            StatisticsVo vo = new StatisticsVo();
            vo.setName(entry.getKey());
            vo.setValue(entry.getValue());
            return vo;
        }).toList();
    }


    @Override
    public LineManyStatisticsVo pictureStatistics(PictureStatisticsRequest request) {
        // 拿到开始结束时间
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        Date nowDate = DateVerifyUtils.checkDateIsStartAfter(startDate, endDate);
        List<String> dateRanges = DateUtils.getDateRanges(startDate, endDate);
        if (StringUtils.isEmpty(dateRanges) || dateRanges == null) {
            // 创建一个空的Map并使用resultBuilder来生成默认结果
            return new LineManyStatisticsVo();
        }

        String end = dateRanges.getLast();
        String today = DateUtils.dateTime(nowDate);
        //初始化数据 key-时间，key-类型，value-数量
        HashMap<String, Map<String, Long>> resultMap = new HashMap<>();
        for (String date : dateRanges) {
            HashMap<String, Long> value = new HashMap<>();
            Arrays.stream(PPictureUploadTypeEnum.values()).forEach(enumItem -> value.put(enumItem.getValue(), 0L));
            resultMap.put(date, value);
        }
        ArrayList<PictureStatisticsRo> pictureStatisticsRos = new ArrayList<>();
        // 今天的数据必须实时查
        if (dateRanges.contains(today)) {
            request.setEndDate(today);
            request.setStartDate(today);
            List<PictureStatisticsRo> todayList = statisticsInfoMapper.pictureStatistics(request);
            pictureStatisticsRos.addAll(todayList);
            //如果只有一个
            if (dateRanges.size() == 1) {
                return builderPictureStatisticsResult(resultMap, pictureStatisticsRos);
            }
            dateRanges.removeLast();
            end = dateRanges.getLast();
        }
        // 历史数据查统计表
        List<StatisticsInfo> statisticsInfoList = getStatisticsInfosByDateAndKeyType(startDate, end, PStatisticsTypeEnum.STATISTICS_TYPE_16.getValue(), PICTURE_STATISTICS_PICTURE_ADD);
        List<String> noStatisticsDate = new ArrayList<>(dateRanges);
        if (!statisticsInfoList.isEmpty()) {
            for (StatisticsInfo statisticsInfo : statisticsInfoList) {
                //删除统计的日期
                noStatisticsDate.remove(DateUtils.dateTime(statisticsInfo.getCreateTime()));
                if (StringUtils.isNotEmpty(statisticsInfo.getContent())) {
                    //拿到内容
                    String content = statisticsInfo.getContent();
                    //拿到内容中的关键词
                    List<PictureStatisticsRo> currentList = JSONObject.parseArray(content, PictureStatisticsRo.class);
                    pictureStatisticsRos.addAll(currentList);
                }
            }
        }
        if (StringUtils.isNotEmpty(noStatisticsDate)) {
            List<StatisticsInfo> newInfoList = new ArrayList<>();
            for (String date : noStatisticsDate) {
                request.setStartDate(date);
                request.setEndDate(date);
                List<PictureStatisticsRo> currentList = statisticsInfoMapper.pictureStatistics(request);
                pictureStatisticsRos.addAll(currentList);
                StatisticsInfo info = builderStatisticsInfo(date, currentList,
                        PStatisticsTypeEnum.STATISTICS_TYPE_16.getValue(), PICTURE_STATISTICS_PICTURE_ADD, PICTURE_STATISTICS_PICTURE_ADD_NAME, 1L);
                newInfoList.add(info);
            }
            if (StringUtils.isNotEmpty(newInfoList)) {
                statisticsInfoMapper.insert(newInfoList);
            }
        }
        return builderPictureStatisticsResult(resultMap, pictureStatisticsRos);
    }

    private LineManyStatisticsVo builderPictureStatisticsResult(HashMap<String, Map<String, Long>> resultMap, List<PictureStatisticsRo> pictureStatisticsRoList) {
        if (StringUtils.isEmpty(pictureStatisticsRoList)) {
            return new LineManyStatisticsVo();
        }
        for (PictureStatisticsRo pictureStatisticsRo : pictureStatisticsRoList) {
            String createTime = pictureStatisticsRo.getCreateTime();
            if (!resultMap.containsKey(createTime)) {
                resultMap.put(createTime, new HashMap<>());
            }
            //当前map
            Map<String, Long> currentMap = resultMap.get(createTime);
            if (currentMap.containsKey(pictureStatisticsRo.getUploadType())) {
                currentMap.put(pictureStatisticsRo.getUploadType(), currentMap.get(pictureStatisticsRo.getUploadType()) + pictureStatisticsRo.getTotal());
            } else {
                currentMap.put(pictureStatisticsRo.getUploadType(), pictureStatisticsRo.getTotal());
            }
        }
        //按照时间排序
        LinkedHashMap<String, Map<String, Long>> sortMap = resultMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        //新建value map，key-上传类型，value-数量
        Map<String, List<Long>> resultValueMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Long>> entry : sortMap.entrySet()) {
            Map<String, Long> value = entry.getValue();
            value.forEach((key1, value1) -> {
                if (!resultValueMap.containsKey(key1)) {
                    resultValueMap.put(key1, new ArrayList<>());
                }
                resultValueMap.get(key1).add(value1);
            });
        }
        //拿到所有的时间，也就是sortMap的key
        List<String> timeList = sortMap.keySet().stream().toList();
        List<LineManyStatisticsVo.Data> dataList = new ArrayList<>();
        resultValueMap.forEach((key, value) -> {
            LineManyStatisticsVo.Data data = new LineManyStatisticsVo.Data();
            Optional<PPictureUploadTypeEnum> enumByValue = PPictureUploadTypeEnum.getEnumByValue(key);
            if (enumByValue.isPresent()) {
                data.setName(enumByValue.get().getLabel());
            } else {
                data.setName(key);
            }
            data.setValue(value);
            dataList.add(data);
        });
        LineManyStatisticsVo lineManyStatisticsVo = new LineManyStatisticsVo();
        lineManyStatisticsVo.setNames(timeList);
        lineManyStatisticsVo.setValues(dataList);
        return lineManyStatisticsVo;
    }


    @Override
    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_PICTURE_STATUS, expireTime = PICTURE_STATISTICS_PICTURE_STATUS_EXPIRE_TIME)
    public List<StatisticsVo> pictureStatusStatistics() {
        return buildStatistics(
                PICTURE_STATISTICS_PICTURE_STATUS,
                PStatisticsTypeEnum.STATISTICS_TYPE_9,
                PICTURE_STATISTICS_PICTURE_STATUS_NAME,
                () -> statisticsInfoMapper.pictureStatusStatistics("", ""),
                PPictureStatusEnum::getEnumByValue,
                PPictureStatusEnum::getLabel
        );
    }

    @Override
    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_PICTURE_UPLOAD_TYPE, expireTime = PICTURE_STATISTICS_PICTURE_UPLOAD_TYPE_EXPIRE_TIME)
    public List<StatisticsVo> pictureUploadTypeStatistics() {
        return buildStatistics(
                PICTURE_STATISTICS_PICTURE_UPLOAD_TYPE,
                PStatisticsTypeEnum.STATISTICS_TYPE_10,
                PICTURE_STATISTICS_PICTURE_UPLOAD_TYPE_NAME,
                () -> statisticsInfoMapper.pictureUploadTypeStatistics("", ""),
                PPictureUploadTypeEnum::getEnumByValue,
                PPictureUploadTypeEnum::getLabel
        );
    }

    @Override
    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_SPACE_FILE_TOTAL, expireTime = PICTURE_STATISTICS_SPACE_FILE_TOTAL_EXPIRE_TIME)
    public List<StatisticsVo> spaceFileTotalStatistics() {
        return buildStatistics(
                PICTURE_STATISTICS_SPACE_FILE_TOTAL,
                PStatisticsTypeEnum.STATISTICS_TYPE_11,
                PICTURE_STATISTICS_SPACE_FILE_TOTAL_NAME,
                () -> statisticsInfoMapper.spaceFileTotalStatistics("", ""),
                PSpaceTypeEnum::getEnumByValue,
                PSpaceTypeEnum::getLabel
        );
    }

    @CustomCacheable(keyPrefix = PICTURE_STATISTICS_SPACE_FILE_SIZE, expireTime = PICTURE_STATISTICS_SPACE_FILE_SIZE_EXPIRE_TIME)
    @Override
    public List<StatisticsVo> spaceFileSizeStatistics() {
        return buildStatistics(
                PICTURE_STATISTICS_SPACE_FILE_SIZE,
                PStatisticsTypeEnum.STATISTICS_TYPE_12,
                PICTURE_STATISTICS_SPACE_FILE_SIZE_NAME,
                () -> statisticsInfoMapper.spaceFileSizeStatistics("", ""),
                PSpaceTypeEnum::getEnumByValue,
                PSpaceTypeEnum::getLabel
        );
    }

    /**
     * 通用的统计方法抽取
     *
     * @param commonKey          公共key
     * @param statisticsTypeEnum 统计类型
     * @param statisticsName     统计名称
     * @param querySupplier      查询方法
     * @param enumResolver       枚举解析函数：根据数据库返回的 name，尝试解析成对应的枚举
     * @param labelExtractor     枚举标签提取函数：从枚举中获取展示用的 label
     * @param <E>                枚举泛型，用于支持不同的枚举类型
     * @return 统计结果列表
     */
    private <E> List<StatisticsVo> buildStatistics(
            String commonKey,
            PStatisticsTypeEnum statisticsTypeEnum,
            String statisticsName,
            Supplier<List<StatisticsRo>> querySupplier,
            Function<String, Optional<E>> enumResolver,
            Function<E, String> labelExtractor
    ) {
        //获取当前日期
        String nowDate = DateUtils.dateTime(DateUtils.getNowDate());

        //获取今天的数据
        StatisticsInfo statisticsInfo = getStatisticsInfoByCommonKey(commonKey, statisticsTypeEnum.getValue());

        // 如果有今天数据直接返回
        if (StringUtils.isNotNull(statisticsInfo)
                && DateUtils.dateTime(statisticsInfo.getCreateTime()).equals(nowDate)) {
            return JSONObject.parseArray(statisticsInfo.getContent(), StatisticsVo.class);
        }

        //如果没有数据，查询数据库
        List<StatisticsVo> statisticsVoList = new ArrayList<>();
        List<StatisticsRo> statisticsRoList = querySupplier.get();

        // 遍历数据库返回结果
        for (StatisticsRo ro : statisticsRoList) {
            // 将数据库的 name 转换为枚举，匹配则取枚举的 label
            String name = enumResolver.apply(ro.getName())
                    .map(labelExtractor)
                    .orElse(ro.getName());
            StatisticsVo vo = new StatisticsVo();
            vo.setName(name);
            vo.setValue(ro.getTotal());
            statisticsVoList.add(vo);
        }

        // 构建新的 StatisticsInfo
        StatisticsInfo newStatisticsInfo = builderStatisticsInfo(
                nowDate,
                statisticsVoList,
                statisticsTypeEnum.getValue(),
                commonKey,
                statisticsName,
                1L
        );
        statisticsInfoMapper.insert(newStatisticsInfo);
        return statisticsVoList;
    }
    //endregion
}
