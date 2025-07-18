package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lz.common.config.OssConfig;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.domain.PictureCategoryInfo;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.domain.PictureTagInfo;
import com.lz.picture.model.domain.PictureTagRelInfo;
import com.lz.picture.model.dto.pictureRecommend.PictureRecommendRequest;
import com.lz.picture.model.dto.pictureRecommend.UserInterestModel;
import com.lz.picture.model.enums.PPictureStatusEnum;
import com.lz.picture.model.vo.pictureInfo.UserRecommendPictureInfoVo;
import com.lz.picture.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.redis.PictureRedisConstants.*;
import static com.lz.config.utils.ConfigInfoUtils.*;

/**
 * 用户推荐服务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-06  20:42
 * @Version: 1.0
 */
@Slf4j
@Service
public class PictureRecommendServiceImpl implements IPictureRecommendService {
    @Resource
    private RedisCache redisCache;

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;


    @Resource
    private IPictureCategoryInfoService pictureCategoryInfoService;

    @Resource
    private IPictureRecommendInfoService pictureRecommendInfoService;

    @Resource
    private OssConfig ossConfig;

    //region 图片推荐核心实现
    private static final long lastCacheRefreshTime = 0;
    private static final long CACHE_REFRESH_INTERVAL = 3600 * 1000; // 1小时刷新一次
    private ScheduledExecutorService scheduler = null;

    @PostConstruct
    public void init() {
        refreshCategoryTagCache(); // 服务启动时初始化
        startRefreshTask(); // 启动定时刷新任务
    }


    // 添加 @PreDestroy 方法用于关闭线程池
    @PreDestroy
    public void destroy() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow(); // 强制关闭
//            log.info("ScheduledExecutorService 已关闭");
        }
    }

    // 启动缓存刷新定时任务
    private void startRefreshTask() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::refreshCategoryTagCache, 1, 1, TimeUnit.HOURS); // 每小时刷新一次
    }

    // 刷新分类-标签关系缓存
    private synchronized void refreshCategoryTagCache() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheRefreshTime < CACHE_REFRESH_INTERVAL) {
            return;
        }

        try {
//            log.info("开始刷新分类-标签关系缓存");
            Map<String, Set<String>> newCache = new HashMap<>();

            // 1. 查询所有分类
            List<PictureCategoryInfo> categories = pictureCategoryInfoService.list(
                    new LambdaQueryWrapper<PictureCategoryInfo>()
            );

            // 2. 对于每个分类，查询其关联的标签
            for (PictureCategoryInfo category : categories) {
                String categoryId = category.getCategoryId();

                // 查询该分类下的热门标签（按使用频率排序）
                List<PictureTagInfo> result = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>()
                        .orderBy(true, false, PictureTagInfo::getUsageCount)
                        .last(true, "limit 100"));

                // 提取标签名
                Set<String> tags = result.stream()
                        .map(PictureTagInfo::getName)
                        .collect(Collectors.toSet());

                newCache.put(categoryId, tags);
            }

            // 3. 原子更新缓存
            if (!newCache.isEmpty()) {
                redisCache.setCacheObject(PICTURE_RECOMMEND_CATEGORY_TAG + COMMON_SEPARATOR_CACHE, newCache, PICTURE_RECOMMEND_CATEGORY_TAG_EXPIRE_TIME, TimeUnit.SECONDS);
//                log.info("成功刷新分类-标签关系缓存到Redis，共{}个分类，有效期{}秒",
//                        newCache.size(), PICTURE_RECOMMEND_CATEGORY_TAG_EXPIRE_TIME);
            }
        } catch (Exception e) {
            log.error("刷新分类-标签关系缓存失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取分类-标签关系
     */
    private Map<String, Set<String>> getCategoryTagRelation() {
        try {
            // 1. 尝试从Redis获取
            Map<String, Set<String>> cache = redisCache.getCacheObject(PICTURE_RECOMMEND_CATEGORY_TAG);
            if (cache != null && !cache.isEmpty()) {
                return cache;
            }

            // 2. Redis中没有则同步刷新
//            log.warn("未找到分类-标签缓存，同步刷新中...");
            refreshCategoryTagCache();

            // 3. 再次尝试从Redis获取
            cache = redisCache.getCacheObject(PICTURE_RECOMMEND_CATEGORY_TAG);
            if (cache != null) {
                return cache;
            }

            // 4. 最终回退方案：使用空映射
            log.error("无法获取分类-标签关系，使用空映射");
            return Collections.emptyMap();
        } catch (Exception e) {
            log.error("获取分类-标签关系失败: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }


    /**
     * 完推荐算法实现
     */
    @Override
    public List<UserRecommendPictureInfoVo> getPictureInfoRecommend(PictureRecommendRequest req) {
        // 1. 用户ID验证
        String userId = req.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return getFallbackRecommendation(req);
        }
        //尝试从缓存拿数据
        if (redisCache.hasKey(PICTURE_RECOMMEND_USER + COMMON_SEPARATOR_CACHE + userId)) {
            return buildResult(req);
        }
        // 2. 获取用户兴趣模型
        UserInterestModel userModel = pictureRecommendInfoService.getUserInterrestModelByUserId(userId);
        if (StringUtils.isNull(userModel)
                || (StringUtils.isEmpty(userModel.getTagScores()) && StringUtils.isEmpty(userModel.getCategoryScores()))) {
            //如果为空表示没有兴趣模型可以直接返回热门数据
            return getFallbackRecommendation(req);
        }

        // 4. 获取高兴趣分类和标签（使用您现有的方法）
        List<String> topCategories = userModel.getTopCategories();
        List<String> topTags = userModel.getTopTags();

        // 5. 获取候选图片ID集（优化点：分批查询避免大结果集）
        Set<String> candidatePictureIds = new HashSet<>();

        // 6.1分类匹配图片（分批查询）
        if (!CollectionUtils.isEmpty(topCategories)) {
            // 每次最多查询3个分类
            int batchSize = 3;
            for (int i = 0; i < topCategories.size(); i += batchSize) {
                List<String> batchCategories = topCategories.subList(i, Math.min(i + batchSize, topCategories.size()));
                candidatePictureIds.addAll(getCategoryPictureIds(batchCategories));
            }
        }

        // 6.2标签匹配图片（分批查询）
        if (!CollectionUtils.isEmpty(topTags)) {
            // 每次最多查询5个标签
            int batchSize = 5;
            for (int i = 0; i < topTags.size(); i += batchSize) {
                List<String> batchTags = topTags.subList(i, Math.min(i + batchSize, topTags.size()));
                candidatePictureIds.addAll(getTagPictureIds(batchTags));
            }
        }

//        log.debug("用户{}候选图片数量: {}", userId, candidatePictureIds.size());

        // 7. 无候选图片时返回热门推荐
        if (candidatePictureIds.isEmpty()) {
//            log.warn("用户{}无候选图片，返回热门推荐", userId);
            return getFallbackRecommendation(req);
        }

        // 8. 获取候选图片完整信息（分批获取）
        List<PictureInfo> candidatePics = new ArrayList<>();
        List<String> candidateIdList = new ArrayList<>(candidatePictureIds);
        int batchSize = 100; // 每次查询100张图片

        for (int i = 0; i < candidateIdList.size(); i += batchSize) {
            List<String> batchIds = candidateIdList.subList(i, Math.min(i + batchSize, candidateIdList.size()));
            List<PictureInfo> batchPics = pictureInfoService.list(
                    new LambdaQueryWrapper<PictureInfo>()
                            .in(PictureInfo::getPictureId, batchIds)
                            .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                            .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
            );
            candidatePics.addAll(batchPics);
        }

        // 9. 批量为所有候选图片注入标签
        injectTags(candidatePics);

        // 10. 计算协同评分
        List<Pair<PictureInfo, Double>> scoredItems = new ArrayList<>();

        // 提前获取分类-标签关系
        Map<String, Set<String>> categoryTags = getCategoryTagRelation();

        for (PictureInfo pic : candidatePics) {
            // 11.1 获取分类归一化分数
            Double normCategoryScoreObj = userModel.getNormalizedCategoryScores().get(pic.getCategoryId());
            double normCategoryScore = normCategoryScoreObj != null ? normCategoryScoreObj : 0.0;

            // 11.2 计算标签最高分
            double maxTagScore = 0.0;
            if (pic.getTags() != null && !pic.getTags().isEmpty()) {
                for (String tag : pic.getTags()) {
                    Double tagScoreObj = userModel.getNormalizedTagScores().get(tag);
                    double tagScore = tagScoreObj != null ? tagScoreObj : 0.0;
                    if (tagScore > maxTagScore) {
                        maxTagScore = tagScore;
                    }
                }
            }

            // 11.3 核心改进：协同增强（优化计算逻辑）
            double synergyBonus = 0.0;
            if (normCategoryScore > 0.1 && maxTagScore > 0.1) {
                // 优化：使用现有数据避免重复计算
                double categoryWeightValue = userModel.getCategoryScores().getOrDefault(pic.getCategoryId(), 0.0);
                String strongestTag = getStrongestMatchTag(pic, userModel);
                double topTagWeightValue = userModel.getTagScores().getOrDefault(strongestTag, 0.0);

                // 防止除零错误
                double weightSum = categoryWeightValue + topTagWeightValue;
                if (weightSum > 0.0001) {
                    double weightSimilarity = 1 - Math.abs(categoryWeightValue - topTagWeightValue) / weightSum;

                    // 计算标签与分类的关联度（使用分类-标签关系）
                    Set<String> standardTags = categoryTags.getOrDefault(pic.getCategoryId(), Collections.emptySet());
                    int commonCount = 0;
                    if (!standardTags.isEmpty() && pic.getTags() != null) {
                        for (String tag : pic.getTags()) {
                            if (standardTags.contains(tag)) commonCount++;
                        }
                    }
                    double categoryTagMatch = standardTags.isEmpty() ? 0 : (double) commonCount / standardTags.size();

                    // 最终协同奖励
                    synergyBonus = 0.3 * weightSimilarity * categoryTagMatch;
                }
            }

            // 11.4 计算协同得分
            double totalScore = (normCategoryScore * PICTURE_RECOMMEND_CATEGORY_WEIGHT_VALUE) +
                    (maxTagScore * PICTURE_RECOMMEND_TAG_WEIGHT_VALUE) +
                    synergyBonus;

            // 11.5 存储结果
            scoredItems.add(Pair.of(pic, totalScore));
        }

        // 12. 按得分降序排序
        scoredItems.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // 调试：打印前5个得分
        if (!scoredItems.isEmpty() && log.isDebugEnabled()) {
            List<String> topScores = scoredItems.subList(0, Math.min(5, scoredItems.size())).stream()
                    .map(pair -> String.format("%s:%.2f", pair.getKey().getPictureId(), pair.getValue()))
                    .toList();
//            log.debug("用户{}推荐结果TOP5: {}", userId, topScores);
        }
        //13. 缓存结果
        cacheResult(scoredItems, userId);
        // 14. 分页处理
        return buildResult(req);
    }

    private List<UserRecommendPictureInfoVo> buildResult(PictureRecommendRequest req) {
        //查询缓存是否存在
        int start = req.getCurrentPage() * req.getPageSize();
        int end = start + req.getPageSize();
        return redisCache.getCacheList(PICTURE_RECOMMEND_USER + COMMON_SEPARATOR_CACHE + req.getUserId(), start, end - 1);
    }

    private List<UserRecommendPictureInfoVo> cacheResult(List<Pair<PictureInfo, Double>> scoredItems, String userId) {
        //缓存用户推荐结果
        List<UserRecommendPictureInfoVo> vos = scoredItems.stream()
                .map(pair -> {
                    PictureInfo pic = pair.getKey();
                    // 确保标签已注入
                    if (pic.getTags() == null) {
                        injectTags(Collections.singletonList(pic));
                    }
                    pic.setThumbnailUrl(ossConfig.builderUrl(pic.getThumbnailUrl(), pic.getDnsUrl()));
                    return UserRecommendPictureInfoVo.objToVo(pic);
                }).toList();
        //判断是否有缓存如果有先删除
        String key = PICTURE_RECOMMEND_USER + COMMON_SEPARATOR_CACHE + userId;
        redisCache.deleteObject(key);
        long count = redisCache.setCacheListRightPushAll(key, vos, PICTURE_RECOMMEND_USER_EXPIRE_TIME, TimeUnit.SECONDS);
//        if (count > 0) {
//            log.debug("用户{}推荐结果缓存成功，数据数：{}", userId, count);
//        }
        return vos;
    }

    // 保持您原有的getFallbackRecommendation方法
    private List<UserRecommendPictureInfoVo> getFallbackRecommendation(PictureRecommendRequest req) {
        req.setCurrentPage(req.getCurrentPage() + 1);
        return pictureInfoService.getRecommentHotPictureInfoList(req);
    }

    // 获取与图片匹配最强的标签
    private String getStrongestMatchTag(PictureInfo pic, UserInterestModel userModel) {
        if (pic.getTags() == null || pic.getTags().isEmpty()) {
            return null;
        }

        return pic.getTags().stream()
                .max(Comparator.comparingDouble(tag ->
                        userModel.getTagScores().getOrDefault(tag, 0.0)
                ))
                .orElse(null);
    }

    // 获取分类匹配的图片ID，并限制数量
    private Set<String> getCategoryPictureIds(List<String> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptySet();
        }
        //给分类排序
        categories.sort(Comparator.naturalOrder());
        String key = PICTURE_RECOMMEND_CATEGORY_PICTURE + StringUtils.join(categories, COMMON_SEPARATOR_CACHE);
        if (redisCache.hasKey(key)) {
            return redisCache.getCacheSet(key);
        }
        // 1. 分批查询 - 每次最多3个分类
        int batchSize = 3;
        Set<String> resultSet = new HashSet<>(512);
        int processedBatches = 0;
        final int maxBatches = 10; // 最多处理10批（约30个分类）

        for (int i = 0; i < categories.size(); i += batchSize) {
            // 2. 终止条件：达到最大候选数量或最大批次
            if (resultSet.size() >= PICTURE_RECOMMEND_CATEGORY_MAX_VALUE || processedBatches >= maxBatches) {
//                log.info("分类查询终止 - 当前总数: {} | 分类范围: {}/{}",
//                        resultSet.size(), i, categories.size());
                break;
            }

            // 3. 获取当前批次分类
            List<String> batchCats = categories.subList(i, Math.min(i + batchSize, categories.size()));

            // 4. 计算当前批次所需限制（确保不超过总限制）
            int currentLimit = Math.max(0, PICTURE_RECOMMEND_CATEGORY_MAX_VALUE - resultSet.size());

            // 5. 执行查询
            List<PictureInfo> batchResult = pictureInfoService.list(
                    new LambdaQueryWrapper<PictureInfo>()
                            .select(PictureInfo::getPictureId)
                            .in(PictureInfo::getCategoryId, batchCats)
                            .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                            .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                            .last("LIMIT " + currentLimit)
            );

            // 6. 合并结果
            Set<String> batchIds = batchResult.stream()
                    .map(PictureInfo::getPictureId)
                    .collect(Collectors.toSet());
            resultSet.addAll(batchIds);

//            log.debug("分类批处理: {}个分类 -> {}张图片 | 总数: {}",
//                    batchCats.size(), batchIds.size(), resultSet.size());

            processedBatches++;
        }
        redisCache.setCacheSet(key, resultSet, PICTURE_RECOMMEND_CATEGORY_PICTURE_EXPIRE_TIME, TimeUnit.SECONDS);
        return resultSet;
    }


    // 获取标签匹配的图片ID
    private Set<String> getTagPictureIds(List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptySet();
        }
        tags.sort(Comparator.naturalOrder());
        String key = PICTURE_RECOMMEND_TAG_PICTURE + StringUtils.join(tags, COMMON_SEPARATOR_CACHE);
        if (redisCache.hasKey(key)) {
            return redisCache.getCacheSet(key);
        }
        int batchSize = 3; // 标签批处理使用更大批大小
        Set<String> resultSet = new HashSet<>(1024);
        final int maxBatches = 8; // 最大批次减少

        for (int i = 0; i < tags.size(); i += batchSize) {
            if (resultSet.size() >= PICTURE_RECOMMEND_TAG_MAX_VALUE || i / batchSize >= maxBatches) {
//                log.info("标签查询终止 - 当前总数: {} | 标签范围: {}/{}",
//                        resultSet.size(), i, tags.size());
                break;
            }

            List<String> batchTags = tags.subList(i, Math.min(i + batchSize, tags.size()));
            int currentLimit = Math.max(0, PICTURE_RECOMMEND_TAG_MAX_VALUE - resultSet.size());

            List<PictureTagRelInfo> batchResult = pictureTagRelInfoService.list(
                    new LambdaQueryWrapper<PictureTagRelInfo>()
                            .select(PictureTagRelInfo::getPictureId)
                            .in(PictureTagRelInfo::getTagName, batchTags)
                            .last("LIMIT " + currentLimit)
            );

            Set<String> batchIds = batchResult.stream()
                    .map(PictureTagRelInfo::getPictureId)
                    .collect(Collectors.toSet());
            resultSet.addAll(batchIds);

            //            log.debug("标签批处理: {}个标签 -> {}张图片 | 总数: {}",
            //                    batchTags.size(), batchIds.size(), resultSet.size());
        }
        redisCache.setCacheSet(key, resultSet, PICTURE_RECOMMEND_TAG_PICTURE_EXPIRE_TIME, TimeUnit.SECONDS);
        return resultSet;
    }


    // 批量注入标签
    private void injectTags(List<PictureInfo> pictures) {
        if (CollectionUtils.isEmpty(pictures)) {
            return;
        }

        // 1. 提取图片ID集合
        Set<String> picIds = pictures.stream()
                .map(PictureInfo::getPictureId)
                .collect(Collectors.toSet());

        // 2. 批量查询标签关系
        List<PictureTagRelInfo> tagRels = pictureTagRelInfoService.list(
                new LambdaQueryWrapper<PictureTagRelInfo>()
                        .select(PictureTagRelInfo::getPictureId, PictureTagRelInfo::getTagName)
                        .in(PictureTagRelInfo::getPictureId, picIds)
        );

        // 3. 构建图片ID到标签名的映射
        Map<String, List<String>> tagMap = tagRels.stream()
                .collect(Collectors.groupingBy(
                        PictureTagRelInfo::getPictureId,
                        Collectors.mapping(PictureTagRelInfo::getTagName, Collectors.toList())
                ));

        // 4. 为每个图片设置标签
        pictures.forEach(pic ->
                pic.setTags(tagMap.getOrDefault(pic.getPictureId(), Collections.emptyList()))
        );
    }

    //endregion
}
