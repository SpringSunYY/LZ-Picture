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
import com.lz.picture.model.dto.pictureInfo.PictureQueryRequest;
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

    //region 图片推荐核心实现
    private static final long lastCacheRefreshTime = 0;
    private static final long CACHE_REFRESH_INTERVAL = 3600 * 1000; // 1小时刷新一次
    private ScheduledExecutorService scheduler = null;

    @PostConstruct
    public void init() {
        refreshCategoryTagCache(); // 服务启动时初始化
        startRefreshTask(); // 启动定时刷新任务
    }

    @PreDestroy
    public void destroy() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }

    private void startRefreshTask() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::refreshCategoryTagCache, 1, 1, TimeUnit.HOURS);
    }

    /**
     * 定时刷新分类与热门标签的映射缓存。
     * 避免每次推荐时都查询数据库，提高性能。
     */
    private synchronized void refreshCategoryTagCache() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheRefreshTime < CACHE_REFRESH_INTERVAL) {
            return;
        }

        try {
            Map<String, Set<String>> newCache = new HashMap<>();
            List<PictureCategoryInfo> categories = pictureCategoryInfoService.list(
                    new LambdaQueryWrapper<PictureCategoryInfo>()
            );

            for (PictureCategoryInfo category : categories) {
                String categoryId = category.getCategoryId();
                // 查询每个分类下使用频率最高的100个标签
                List<PictureTagInfo> result = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>()
                        .orderBy(true, false, PictureTagInfo::getUsageCount)
                        .last(true, "limit 100"));
                Set<String> tags = result.stream()
                        .map(PictureTagInfo::getName)
                        .collect(Collectors.toSet());
                newCache.put(categoryId, tags);
            }

            if (!newCache.isEmpty()) {
                redisCache.setCacheObject(PICTURE_RECOMMEND_CATEGORY_TAG + COMMON_SEPARATOR_CACHE, newCache, PICTURE_RECOMMEND_CATEGORY_TAG_EXPIRE_TIME, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("刷新分类-标签关系缓存失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取分类与热门标签的映射关系，优先从Redis缓存中获取。
     * 如果缓存不存在，则同步刷新缓存。
     */
    private Map<String, Set<String>> getCategoryTagRelation() {
        try {
            String key = PICTURE_RECOMMEND_CATEGORY_TAG + COMMON_SEPARATOR_CACHE;
            Map<String, Set<String>> cache = redisCache.getCacheObject(key);
            if (cache != null && !cache.isEmpty()) {
                return cache;
            }
            refreshCategoryTagCache();
            cache = redisCache.getCacheObject(key);
            if (cache != null) {
                return cache;
            }
            return Collections.emptyMap();
        } catch (Exception e) {
            log.error("获取分类-标签关系失败: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    /**
     * 核心推荐算法实现。
     * 结合用户兴趣模型、内容特征（分类、标签）进行混合推荐。
     */
    @Override
    public List<UserRecommendPictureInfoVo> getPictureInfoRecommend(PictureRecommendRequest req) {
        String userId = req.getUserId();
        // 如果用户未登录，则使用热门图片作为fallback推荐
        if (StringUtils.isEmpty(userId)) {
            return getFallbackRecommendation(req);
        }

        // 尝试从缓存中获取用户的个性化推荐结果
        String cacheKey = PICTURE_RECOMMEND_USER + COMMON_SEPARATOR_CACHE + userId + (StringUtils.isNotEmpty(req.getUploadType()) ? COMMON_SEPARATOR_CACHE + req.getUploadType() : "");
        if (redisCache.hasKey(cacheKey)) {
            return buildResult(req);
        }

        // 获取用户兴趣模型
        UserInterestModel userModel = pictureRecommendInfoService.getUserInterrestModelByUserId(userId);
        if (StringUtils.isNull(userModel) || (CollectionUtils.isEmpty(userModel.getTagScores()) && CollectionUtils.isEmpty(userModel.getCategoryScores()))) {
            // 如果用户模型为空，说明该用户没有足够的数据，返回热门推荐
            return getFallbackRecommendation(req);
        }

        List<String> topCategories = userModel.getTopCategories();
        List<String> topTags = userModel.getTopTags();

        Set<PictureInfo> candidatePictures = new HashSet<>();

        // 精细化分类候选集获取
        // 获取用户直接感兴趣的分类ID集合，作为推荐的主力军
        Set<String> interestedCategoryIds = new HashSet<>(topCategories);

        // 分类匹配图片
        if (!CollectionUtils.isEmpty(topCategories)) {
            // 首先查询用户直接感兴趣的分类，并获取较多数量的图片
            candidatePictures.addAll(getCategoryPictureList(new ArrayList<>(interestedCategoryIds), req.getCategoryType(), req.getUploadType(), true));

            // 获取所有扩展分类（父类及其兄弟子类）的ID
            List<String> allCategoryIds = getAllExpandedCategoryIds(topCategories, req.getCategoryType());
            // 过滤掉用户直接感兴趣的分类，得到纯粹的扩展分类
            List<String> expandedCategoryIds = allCategoryIds.stream()
                    .filter(id -> !interestedCategoryIds.contains(id))
                    .collect(Collectors.toList());

            // 限制扩展分类图片的查询数量，确保多样性但不过多，避免喧宾夺主
            List<PictureInfo> expandedPics = getCategoryPictureList(expandedCategoryIds, req.getCategoryType(), req.getUploadType(), false);
            candidatePictures.addAll(expandedPics);
        }

        // 标签匹配图片
        if (!CollectionUtils.isEmpty(topTags)) {
            int batchSize = 8;
            for (int i = 0; i < topTags.size(); i += batchSize) {
                List<String> batchTags = topTags.subList(i, Math.min(i + batchSize, topTags.size()));
                candidatePictures.addAll(getTagPictureList(batchTags, req.getUploadType()));
            }
        }

        List<PictureInfo> candidatePictureList = new ArrayList<>(candidatePictures);
        if (candidatePictureList.isEmpty()) {
            return getFallbackRecommendation(req);
        }

        // 批量为所有候选图片注入标签，避免N+1查询问题
        injectTags(candidatePictureList);

        List<Pair<PictureInfo, Double>> scoredItems = new ArrayList<>();
        Map<String, Set<String>> categoryTags = getCategoryTagRelation();

        // 降低扩展分类的图片权重
        for (PictureInfo pic : candidatePictureList) {
            double normCategoryScore = userModel.getNormalizedCategoryScores().getOrDefault(pic.getCategoryId(), 0.0);

            // 如果图片来自非用户直接感兴趣的分类，对其分类得分进行衰减
            if (!interestedCategoryIds.contains(pic.getCategoryId())) {
                normCategoryScore *= 0.5;
            }

            // 计算标签平均分，反映图片的标签相关性
            double averageTagScore = 0.0;
            if (pic.getTags() != null && !pic.getTags().isEmpty()) {
                double totalTagScore = 0.0;
                for (String tag : pic.getTags()) {
                    totalTagScore += userModel.getNormalizedTagScores().getOrDefault(tag, 0.0);
                }
                averageTagScore = totalTagScore / pic.getTags().size();
            }

            // 计算分类-标签匹配度，作为协同增强分数
            double categoryTagMatchScore = 0.0;
            Set<String> categoryStandardTags = categoryTags.getOrDefault(pic.getCategoryId(), Collections.emptySet());
            if (!categoryStandardTags.isEmpty() && pic.getTags() != null && !pic.getTags().isEmpty()) {
                Set<String> pictureTags = new HashSet<>(pic.getTags());
                pictureTags.retainAll(categoryStandardTags);
                if (!pictureTags.isEmpty()) {
                    double matchingTagScoreSum = pictureTags.stream()
                            .mapToDouble(tag -> userModel.getNormalizedTagScores().getOrDefault(tag, 0.0))
                            .sum();
                    categoryTagMatchScore = matchingTagScoreSum / pictureTags.size();
                }
            }

            // 最终综合评分公式
            double totalScore = (normCategoryScore * PICTURE_RECOMMEND_CATEGORY_WEIGHT_VALUE) +
                    (averageTagScore * PICTURE_RECOMMEND_TAG_WEIGHT_VALUE) +
                    (categoryTagMatchScore * 1);

            // 增加微小的随机扰动，打破得分相近图片的排序僵局，增加推荐的随机性
            totalScore += Math.random() * 0.01;

            scoredItems.add(Pair.of(pic, totalScore));
        }

        // 按得分降序排序
        scoredItems.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // 将排序后的推荐结果缓存到Redis，供后续分页请求使用
        cacheResult(scoredItems, cacheKey);

        // 返回当前页的分页结果
        return buildResult(req);
    }

    /**
     * 从Redis缓存中构建分页结果。
     */
    private List<UserRecommendPictureInfoVo> buildResult(PictureRecommendRequest req) {
        int start = req.getCurrentPage() * req.getPageSize();
        int end = start + req.getPageSize();
        String cacheKey = PICTURE_RECOMMEND_USER + COMMON_SEPARATOR_CACHE + req.getUserId() + (StringUtils.isNotEmpty(req.getUploadType()) ? COMMON_SEPARATOR_CACHE + req.getUploadType() : "");
        return redisCache.getCacheList(cacheKey, start, end - 1);
    }

    /**
     * 将推荐结果缓存到Redis，使用用户的ID作为键。
     */
    private void cacheResult(List<Pair<PictureInfo, Double>> scoredItems, String cacheKey) {

        List<UserRecommendPictureInfoVo> vos = scoredItems.stream()
                .map(pair -> {
                    PictureInfo pic = pair.getKey();
                    // 构建缩略图完整URL
                    pic.setThumbnailUrl(OssConfig.builderPictureUrl(pic.getThumbnailUrl(), PICTURE_INDEX_P_VALUE));
                    return UserRecommendPictureInfoVo.objToVo(pic);
                }).toList();

        // 缓存前先删除旧的缓存
        redisCache.deleteObject(cacheKey);
        if (StringUtils.isNotEmpty(vos)) {
            // 将列表全部推入缓存
            redisCache.setCacheListRightPushAll(cacheKey, vos, PICTURE_RECOMMEND_USER_EXPIRE_TIME, TimeUnit.SECONDS);
        }
    }

    /**
     * 当无法进行个性化推荐时的备选方案，返回热门图片。
     */
    private List<UserRecommendPictureInfoVo> getFallbackRecommendation(PictureRecommendRequest req) {
        PictureQueryRequest userPictureInfoQuery = new PictureQueryRequest();
        userPictureInfoQuery.setPageNum(req.getCurrentPage() + 1);
        userPictureInfoQuery.setPageSize(req.getPageSize());
        return pictureInfoService.queryPictureInfoList(userPictureInfoQuery);
    }

    /**
     * 获取用户感兴趣分类的父类及其同级子类的所有分类ID，用于扩展候选集。
     */
    private List<String> getAllExpandedCategoryIds(List<String> categories, String categoryType) {
        Set<String> categoryIds = new HashSet<>(categories);
        List<PictureCategoryInfo> infoList = pictureCategoryInfoService.list(new LambdaQueryWrapper<PictureCategoryInfo>()
                .in(PictureCategoryInfo::getCategoryId, categories));

        // 找到用户感兴趣分类的所有父ID
        Set<String> parentIds = infoList.stream()
                .map(PictureCategoryInfo::getParentId)
                .filter(parentId -> !parentId.equals("0"))
                .collect(Collectors.toSet());

        if (StringUtils.isNotEmpty(parentIds)) {
            // 根据父ID查询所有子分类和父分类本身，确保获取完整的同级分类
            List<PictureCategoryInfo> allChildren = pictureCategoryInfoService.list(new LambdaQueryWrapper<PictureCategoryInfo>()
                    .in(PictureCategoryInfo::getParentId, parentIds)
                    .or().in(PictureCategoryInfo::getCategoryId, parentIds));

            allChildren.forEach(c -> categoryIds.add(c.getCategoryId()));
        }
        return new ArrayList<>(categoryIds);
    }

    /**
     * 根据分类列表查询图片，并根据是否为用户直接感兴趣的分类来动态调整查询数量。
     *
     * @param categories   要查询的分类ID列表
     * @param isInterested 是否为用户直接感兴趣的分类
     */
    private List<PictureInfo> getCategoryPictureList(List<String> categories, String categoryType, String uploadType, boolean isInterested) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        categories.sort(Comparator.naturalOrder());
        // 缓存键中加入'interested'或'expanded'来区分不同类型的查询结果
        String key = PICTURE_RECOMMEND_CATEGORY_PICTURE + COMMON_SEPARATOR_CACHE + (isInterested ? "interested" : "expanded") + COMMON_SEPARATOR_CACHE + (StringUtils.isNotEmpty(categoryType) ? categoryType + COMMON_SEPARATOR_CACHE : "") + String.join(COMMON_SEPARATOR_CACHE, categories);

        if (redisCache.hasKey(key)) {
            return redisCache.getCacheList(key);
        }

        // 根据是否为用户直接感兴趣的分类，设置不同的查询限制
        // 直接感兴趣的分类获取更多图片，扩展分类只获取少量图片
        int limit = isInterested ? PICTURE_RECOMMEND_CATEGORY_MAX_VALUE : (PICTURE_RECOMMEND_CATEGORY_MAX_VALUE / 10);

        List<PictureInfo> resultList = new ArrayList<>(limit);
        int batchSize = 5;

        for (int i = 0; i < categories.size(); i += batchSize) {
            if (resultList.size() >= limit) break;
            List<String> batchCats = categories.subList(i, Math.min(i + batchSize, categories.size()));
            List<PictureInfo> batchResult = pictureInfoService.list(
                    new LambdaQueryWrapper<PictureInfo>()
                            .select(PictureInfo::getPictureId, PictureInfo::getCategoryId, PictureInfo::getPicHeight, PictureInfo::getPicWidth, PictureInfo::getThumbnailUrl, PictureInfo::getName)
                            .in(PictureInfo::getCategoryId, batchCats)
                            .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                            .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                            .eq(StringUtils.isNotEmpty(uploadType), PictureInfo::getUploadType, uploadType)
                            .last("LIMIT " + (limit - resultList.size()))
            );

            resultList.addAll(batchResult);
        }
        if (StringUtils.isNotEmpty(resultList)) {
            redisCache.setCacheListRightPushAll(key, resultList, PICTURE_RECOMMEND_CATEGORY_PICTURE_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        return resultList;
    }


    /**
     * 根据标签列表查询图片，并从数据库获取相关图片ID。
     */
    private List<PictureInfo> getTagPictureList(List<String> tags, String uploadType) {
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptyList();
        }
        tags.sort(Comparator.naturalOrder());
        String key = PICTURE_RECOMMEND_TAG_PICTURE + COMMON_SEPARATOR_CACHE + (StringUtils.isNotEmpty(uploadType) ? uploadType + COMMON_SEPARATOR_CACHE : "") + StringUtils.join(tags, COMMON_SEPARATOR_CACHE);

        if (redisCache.hasKey(key)) {
            return redisCache.getCacheList(key);
        }

        // 先通过标签关系表查询图片ID，限制总数
        List<String> tagPictureIds = pictureTagRelInfoService.list(
                        new LambdaQueryWrapper<PictureTagRelInfo>()
                                .select(PictureTagRelInfo::getPictureId)
                                .in(PictureTagRelInfo::getTagName, tags)
                                .last("LIMIT " + PICTURE_RECOMMEND_TAG_MAX_VALUE))
                .stream()
                .map(PictureTagRelInfo::getPictureId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(tagPictureIds)) {
            return Collections.emptyList();
        }

        // 根据图片ID批量查询图片信息
        List<PictureInfo> resultList = pictureInfoService.list(
                new LambdaQueryWrapper<PictureInfo>()
                        .select(PictureInfo::getPictureId, PictureInfo::getCategoryId, PictureInfo::getPicHeight, PictureInfo::getPicWidth, PictureInfo::getThumbnailUrl, PictureInfo::getName)
                        .in(PictureInfo::getPictureId, tagPictureIds)
                        .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                        .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                        .eq(StringUtils.isNotEmpty(uploadType), PictureInfo::getUploadType, uploadType)
        );
        if (StringUtils.isNotEmpty(resultList)) {
            redisCache.setCacheListRightPushAll(key, resultList, PICTURE_RECOMMEND_TAG_PICTURE_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        return resultList;
    }

    /**
     * 批量为图片对象注入标签信息，避免多次查询。
     */
    private void injectTags(List<PictureInfo> pictures) {
        if (CollectionUtils.isEmpty(pictures)) {
            return;
        }

        Set<String> picIds = pictures.stream()
                .map(PictureInfo::getPictureId)
                .collect(Collectors.toSet());

        List<PictureTagRelInfo> tagRels = pictureTagRelInfoService.list(
                new LambdaQueryWrapper<PictureTagRelInfo>()
                        .select(PictureTagRelInfo::getPictureId, PictureTagRelInfo::getTagName)
                        .in(PictureTagRelInfo::getPictureId, picIds)
        );

        Map<String, List<String>> tagMap = tagRels.stream()
                .collect(Collectors.groupingBy(
                        PictureTagRelInfo::getPictureId,
                        Collectors.mapping(PictureTagRelInfo::getTagName, Collectors.toList())
                ));

        pictures.forEach(pic ->
                pic.setTags(tagMap.getOrDefault(pic.getPictureId(), Collections.emptyList()))
        );
    }
}
