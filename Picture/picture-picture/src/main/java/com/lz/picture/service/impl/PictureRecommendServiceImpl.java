package com.lz.picture.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.model.domain.*;
import com.lz.picture.model.dto.pictureRecommend.PictureRecommendRequest;
import com.lz.picture.model.dto.pictureRecommend.UserInterestModel;
import com.lz.picture.model.enums.PUserBehaviorTargetTypeEnum;
import com.lz.picture.model.enums.PViewLogTargetTypeEnum;
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

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;
import static com.lz.common.constant.config.UserConfigKeyConstants.*;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_RECOMMEND_CATEGORY_TAG;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_RECOMMEND_CATEGORY_TAG_EXPIRE_TIME;

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
    private IConfigInfoService configInfoService;

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    @Resource
    private IUserViewLogInfoService userViewLogInfoService;

    @Resource
    private IPictureCategoryInfoService pictureCategoryInfoService;

    //region 计算各个分数
    @Override
    public UserInterestModel getUserViewInterest(String userId, String targetType, Integer limit, Integer categoryWeight, Integer tagWeight, Double timeDecay) {
        //1.查询到用户行为记录
        Page<UserViewLogInfo> page = new Page<>();
        page.setSize(limit);
        page.setCurrent(1);
        List<UserViewLogInfo> userViewLogInfos = userViewLogInfoService.page(page, new LambdaQueryWrapper<UserViewLogInfo>()
                        .eq(UserViewLogInfo::getUserId, userId)
                        .eq(UserViewLogInfo::getTargetType, targetType)
                        .orderByDesc(UserViewLogInfo::getCreateTime))
                .getRecords();
        if (StringUtils.isEmpty(userViewLogInfos)) {
            return null;
        }

        // 2. 计算分类和标签分数（含时间衰减）
        Map<String, Double> categoryScores = new HashMap<>();
        Map<String, Double> tagScores = new HashMap<>();
        Date now = DateUtils.getNowDate();

        for (UserViewLogInfo info : userViewLogInfos) {
            double calculateTimeDecay = calculateTimeDecay(info.getCreateTime(), now, timeDecay); // 时间衰减因子
            double baseScore = info.getScore(); // 行为记录中的原始分数
            //处理分数
            calculateScore(categoryScores, tagScores, baseScore, calculateTimeDecay, info.getCategoryId(), categoryWeight, info.getTags(), tagWeight);
        }

        // 3. 返回结果
        UserInterestModel userInterestModel = new UserInterestModel();
        userInterestModel.setCategoryScores(categoryScores);
        userInterestModel.setTagScores(tagScores);
        userInterestModel.setUpdateTime(now);
        return userInterestModel;
    }

    @Override
    public UserInterestModel getUserBehaviorInterest(String userId, String targetType, Integer limit, Integer categoryWeight, Integer tagWeight, Double timeDecay) {
        //1.查询到用户行为记录
        Page<UserBehaviorInfo> page = new Page<>();
        page.setSize(limit);
        page.setCurrent(1);
        List<UserBehaviorInfo> userBehaviorInfos = userBehaviorInfoService.page(page, new LambdaQueryWrapper<UserBehaviorInfo>()
                        .eq(UserBehaviorInfo::getUserId, userId)
                        .eq(UserBehaviorInfo::getTargetType, targetType)
                        .orderByDesc(UserBehaviorInfo::getCreateTime))
                .getRecords();
        if (StringUtils.isEmpty(userBehaviorInfos)) {
            return null;
        }

        // 2. 计算分类和标签分数（含时间衰减）
        Map<String, Double> categoryScores = new HashMap<>();
        Map<String, Double> tagScores = new HashMap<>();
        Date now = DateUtils.getNowDate();

        for (UserBehaviorInfo info : userBehaviorInfos) {
            double calculateTimeDecay = calculateTimeDecay(info.getCreateTime(), now, timeDecay); // 时间衰减因子
            double baseScore = info.getScore(); // 行为记录中的原始分数
            //处理分数
            calculateScore(categoryScores, tagScores, baseScore, calculateTimeDecay, info.getCategoryId(), categoryWeight, info.getTags(), tagWeight);
        }

        // 3. 返回结果
        UserInterestModel userInterestModel = new UserInterestModel();
        userInterestModel.setCategoryScores(categoryScores);
        userInterestModel.setTagScores(tagScores);
        userInterestModel.setUpdateTime(now);
        return userInterestModel;
    }

    @Override
    public UserInterestModel getPictureDownloadInterest(String userId, Integer limit, Integer categoryWeight, Integer tagWeight, Double timeDecay) {
        //1.查询到用户行为记录
        Page<PictureDownloadLogInfo> page = new Page<>();
        page.setSize(limit);
        page.setCurrent(1);
        List<PictureDownloadLogInfo> pictureDownloadLogInfos = pictureDownloadLogInfoService.page(page, new LambdaQueryWrapper<PictureDownloadLogInfo>()
                        .eq(PictureDownloadLogInfo::getUserId, userId)
                        .orderByDesc(PictureDownloadLogInfo::getCreateTime))
                .getRecords();
        if (StringUtils.isEmpty(pictureDownloadLogInfos)) {
            return null;
        }

        // 2. 计算分类和标签分数（含时间衰减）
        Map<String, Double> categoryScores = new HashMap<>();
        Map<String, Double> tagScores = new HashMap<>();
        Date now = DateUtils.getNowDate();

        for (PictureDownloadLogInfo info : pictureDownloadLogInfos) {
            double calculateTimeDecay = calculateTimeDecay(info.getCreateTime(), now, timeDecay); // 时间衰减因子
            double baseScore = info.getScore(); // 行为记录中的原始分数
            //处理分数
            calculateScore(categoryScores, tagScores, baseScore, calculateTimeDecay, info.getCategoryId(), categoryWeight, info.getTags(), tagWeight);
        }

        // 3. 返回结果
        UserInterestModel userInterestModel = new UserInterestModel();
        userInterestModel.setCategoryScores(categoryScores);
        userInterestModel.setTagScores(tagScores);
        userInterestModel.setUpdateTime(now);
        return userInterestModel;
    }

    @Override
    public UserInterestModel getUserInterest(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        //查询到分数占比
        String viewCategoryScoreStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_VIEW_CATEGORY);
        int viewCategoryWeight = Integer.parseInt(viewCategoryScoreStr);
        String viewTagScoreStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_VIEW_TAG);
        int viewTagWeight = Integer.parseInt(viewTagScoreStr);
        String timeDecayStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_VIEW_TIME_DECAY);
        double timeDecay = Double.parseDouble(timeDecayStr);
        String viewLimitStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_VIEW_LIMIT);
        int viewLimit = Integer.parseInt(viewLimitStr);
        UserInterestModel viewModel = this.getUserViewInterest(userId, PViewLogTargetTypeEnum.VIEW_LOG_TARGET_TYPE_0.getValue(), viewLimit, viewCategoryWeight, viewTagWeight, timeDecay);

        //查询下载兴趣
        String downloadCategoryStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_DOWNLOAD_CATEGORY);
        int downloadCategoryWeight = Integer.parseInt(downloadCategoryStr);
        String downloadTagScoreStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_DOWNLOAD_TAG);
        int downloadTagWeight = Integer.parseInt(downloadTagScoreStr);
        String downloadTimeDecayStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_TIME_DOWNLOAD_DECAY);
        double downloadTimeDecay = Double.parseDouble(downloadTimeDecayStr);
        String downloadLimitStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_DOWNLOAD_LIMIT);
        int downloadLimit = Integer.parseInt(downloadLimitStr);
        UserInterestModel downloadModel = this.getPictureDownloadInterest(userId, downloadLimit, downloadCategoryWeight, downloadTagWeight, downloadTimeDecay);

        //查询行为兴趣
        String behaviorCategoryStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_BEHAVIOR_CATEGORY);
        int behaviorCategoryWeight = Integer.parseInt(behaviorCategoryStr);
        String behaviorTagScoreStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_BEHAVIOR_TAG);
        int behaviorTagWeight = Integer.parseInt(behaviorTagScoreStr);
        String behaviorTimeDecayStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_TIME_BEHAVIOR_DECAY);
        double behaviorTimeDecay = Double.parseDouble(behaviorTimeDecayStr);
        String behaviorLimitStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_BEHAVIOR_LIMIT);
        int behaviorLimit = Integer.parseInt(behaviorLimitStr);
        UserInterestModel behaviorModel = this.getUserBehaviorInterest(userId, PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0.getValue(), behaviorLimit, behaviorCategoryWeight, behaviorTagWeight, behaviorTimeDecay);
        //计算总兴趣总数
        UserInterestModel userInterestModel = new UserInterestModel();
        HashMap<String, Double> tagScores = new HashMap<>();
        userInterestModel.setTagScores(tagScores);
        HashMap<String, Double> categoryScores = new HashMap<>();
        userInterestModel.setCategoryScores(categoryScores);
        userInterestModel.setUpdateTime(DateUtils.getNowDate());
        //合并
        mergeModel(viewModel, userInterestModel);
        mergeModel(behaviorModel, userInterestModel);
        mergeModel(downloadModel, userInterestModel);
        // 对 tagScores 排序并保持顺序
        Map<String, Double> sortedTagScores = tagScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        // 对 categoryScores 排序并保持顺序
        Map<String, Double> sortedCategoryScores = categoryScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
        userInterestModel.setTagScores(sortedTagScores);
        userInterestModel.setCategoryScores(sortedCategoryScores);
        return userInterestModel;
    }

    // 辅助方法：时间衰减计算（每天衰减10%）
    private double calculateTimeDecay(Date createTime, Date now, Double timeDecay) {
        long daysDiff = DateUtil.between(createTime, now, DateUnit.DAY);
        return Math.pow(timeDecay, daysDiff); // 每天衰减10%
    }

    //计算分数
    private void calculateScore(Map<String, Double> categoryScores, Map<String, Double> tagScores,
                                Double baseScore, Double calculateTimeDecay,
                                String categoryId, Integer categoryWeight,
                                String tags, Integer tagWeight) {
        // 处理分类分数
        if (StringUtils.isNotEmpty(categoryId)) {
            double categoryScore = baseScore * categoryWeight * calculateTimeDecay;
            categoryScores.merge(categoryId, categoryScore, Double::sum);
        }

        // 处理标签分数（按分号拆分标签）
        if (StringUtils.isNotEmpty(tags)) {
            Arrays.stream(tags.split(COMMON_SEPARATOR))
                    .filter(tag -> !tag.isEmpty())
                    .forEach(tag -> {
                        double tagScore = baseScore * tagWeight * calculateTimeDecay;
                        tagScores.merge(tag, tagScore, Double::sum);
                    });
        }
    }


    /**
     * 合并模型
     *
     * @param model      模型
     * @param totalModel 总的模型需要合并的
     */
    private void mergeModel(UserInterestModel model, UserInterestModel totalModel) {
        if (StringUtils.isNotNull(model)) {
            Map<String, Double> categoryScores = model.getCategoryScores();
            Map<String, Double> totalModelCategoryScores = totalModel.getCategoryScores();
            if (StringUtils.isNotEmpty(categoryScores)) {
                categoryScores.forEach((key, value) -> {
                    totalModelCategoryScores.merge(key, value, Double::sum);
                });
            }
            if (StringUtils.isNotEmpty(model.getTagScores())) {
                Map<String, Double> tagScores = model.getTagScores();
                Map<String, Double> totalModelTagScores = totalModel.getTagScores();
                tagScores.forEach((key, value) -> {
                    totalModelTagScores.merge(key, value, Double::sum);
                });
            }
        }
    }
    // endregion

    //region 图片推荐核心实现
    // 在类定义中添加以下字段和初始化方法
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
            log.info("ScheduledExecutorService 已关闭");
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
            log.info("开始刷新分类-标签关系缓存");
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
                redisCache.setCacheObject(PICTURE_RECOMMEND_CATEGORY_TAG, newCache, PICTURE_RECOMMEND_CATEGORY_TAG_EXPIRE_TIME, TimeUnit.SECONDS);
                log.info("成功刷新分类-标签关系缓存到Redis，共{}个分类，有效期{}秒",
                        newCache.size(), PICTURE_RECOMMEND_CATEGORY_TAG_EXPIRE_TIME);
            }
        } catch (Exception e) {
            log.error("刷新分类-标签关系缓存失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取分类-标签关系（带Redis缓存）
     */
    private Map<String, Set<String>> getCategoryTagRelation() {
        try {
            // 1. 尝试从Redis获取
            Map<String, Set<String>> cache = redisCache.getCacheObject(PICTURE_RECOMMEND_CATEGORY_TAG);
            if (cache != null && !cache.isEmpty()) {
                return cache;
            }

            // 2. Redis中没有则同步刷新
            log.warn("未找到分类-标签缓存，同步刷新中...");
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
     * 完整的推荐算法实现
     */
    @Override
    public List<UserRecommendPictureInfoVo> getPictureInfoRecommend(PictureRecommendRequest req) {
        // 1. 用户ID验证
        String userId = req.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return getFallbackRecommendation(req);
        }

        // 2. 获取用户兴趣模型（使用您现有的方法）
        UserInterestModel userModel = getUserInterest(userId);
        if (userModel == null) {
            return getFallbackRecommendation(req);
        }

        // 3. 确保归一化分数（使用您现有的方法）
        normalizeScores(userModel);

        // 4. 获取高兴趣分类和标签（使用您现有的方法）
        List<String> topCategories = getTopCategories(userModel);
        List<String> topTags = getTopTags(userModel);

        // 5. 日志记录（调试用）
        log.debug("用户{}推荐 - 高兴趣分类: {}", userId, topCategories);
        log.debug("用户{}推荐 - 高兴趣标签: {}", userId, topTags);

        // 6. 获取候选图片ID集（优化点：分批查询避免大结果集）
        Set<String> candidatePictureIds = new HashSet<>();

        // 分类匹配图片（分批查询）
        if (!CollectionUtils.isEmpty(topCategories)) {
            // 每次最多查询3个分类
            int batchSize = 3;
            for (int i = 0; i < topCategories.size(); i += batchSize) {
                List<String> batchCategories = topCategories.subList(i, Math.min(i + batchSize, topCategories.size()));
                candidatePictureIds.addAll(getCategoryPictureIds(batchCategories, 1000));
            }
        }

        // 标签匹配图片（分批查询）
        if (!CollectionUtils.isEmpty(topTags)) {
            // 每次最多查询5个标签
            int batchSize = 5;
            for (int i = 0; i < topTags.size(); i += batchSize) {
                List<String> batchTags = topTags.subList(i, Math.min(i + batchSize, topTags.size()));
                candidatePictureIds.addAll(getTagPictureIds(batchTags, 2000));
            }
        }

        log.debug("用户{}候选图片数量: {}", userId, candidatePictureIds.size());

        // 7. 无候选图片时返回热门推荐
        if (candidatePictureIds.isEmpty()) {
            log.warn("用户{}无候选图片，返回热门推荐", userId);
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
                            .eq(PictureInfo::getPictureStatus, "0")
                            .eq(PictureInfo::getReviewStatus, 1)
                            .eq(PictureInfo::getIsDelete, "0")
            );
            candidatePics.addAll(batchPics);
        }

        // 9. 批量为所有候选图片注入标签
        injectTags(candidatePics);

        // 10. 计算动态权重
        double categoryWeight = 0.3;
        double tagWeight = 0.7;

        // 11. 计算协同评分
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
            double totalScore = (normCategoryScore * categoryWeight) +
                    (maxTagScore * tagWeight) +
                    synergyBonus;

            // 11.5 存储结果
            scoredItems.add(Pair.of(pic, totalScore));
        }

        // 12. 按得分降序排序
        Collections.sort(scoredItems, (a, b) -> Double.compare(b.getValue(), a.getValue()));

        // 调试：打印前5个得分
        if (!scoredItems.isEmpty() && log.isDebugEnabled()) {
            List<String> topScores = scoredItems.subList(0, Math.min(5, scoredItems.size())).stream()
                    .map(pair -> String.format("%s:%.2f", pair.getKey().getPictureId(), pair.getValue()))
                    .collect(Collectors.toList());
            log.debug("用户{}推荐结果TOP5: {}", userId, topScores);
        }

        // 13. 分页处理
        int pageNum = req.getCurrentPage();
        int pageSize = req.getPageSize();
        int start = Math.max(0, (pageNum - 1) * pageSize);
        int end = Math.min(start + pageSize, scoredItems.size());

        if (start >= scoredItems.size()) {
            return Collections.emptyList();
        }

        // 14. 转换为VO对象返回
        return scoredItems.subList(start, end).stream()
                .map(pair -> {
                    PictureInfo pic = pair.getKey();
                    // 确保标签已注入
                    if (pic.getTags() == null) {
                        injectTags(Collections.singletonList(pic));
                    }
                    pic.setThumbnailUrl(pictureInfoService.builderPictureUrl(pic.getThumbnailUrl(), pic.getDnsUrl()));
                    return UserRecommendPictureInfoVo.objToVo(pic);
                })
                .collect(Collectors.toList());
    }

    // 保持您原有的getFallbackRecommendation方法
    private List<UserRecommendPictureInfoVo> getFallbackRecommendation(PictureRecommendRequest req) {
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
    private Set<String> getCategoryPictureIds(List<String> categories, int limit) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptySet();
        }

        // 使用分页控制返回数量
        Page<PictureInfo> page = new Page<>(1, limit); // 第一页，每页limit条记录

        List<PictureInfo> result = pictureInfoService.page(page,
                        new LambdaQueryWrapper<PictureInfo>()
                                .select(PictureInfo::getPictureId)
                                .in(PictureInfo::getCategoryId, categories)
                                .eq(PictureInfo::getPictureStatus, "0")
                                .eq(PictureInfo::getReviewStatus, 1)
                                .eq(PictureInfo::getIsDelete, "0"))
                .getRecords();

        return result.stream()
                .map(PictureInfo::getPictureId)
                .collect(Collectors.toSet());
    }


    // 获取标签匹配的图片ID
    private Set<String> getTagPictureIds(List<String> tags, int limit) {
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptySet();
        }

        // 使用分页来限制返回的记录数
        Page<PictureTagRelInfo> page = new Page<>(1, limit); // 第一页，每页limit条记录

        List<PictureTagRelInfo> result = pictureTagRelInfoService.page(page,
                        new LambdaQueryWrapper<PictureTagRelInfo>()
                                .select(PictureTagRelInfo::getPictureId)
                                .in(PictureTagRelInfo::getTagName, tags))
                .getRecords();

        return result.stream()
                .map(PictureTagRelInfo::getPictureId)
                .collect(Collectors.toSet());
    }

    // 归一化处理
    public void normalizeScores(UserInterestModel model) {
        // 确保模型不为空
        if (model == null) return;

        // 1. 确保分类归一化分数不为null
        if (model.getNormalizedCategoryScores() == null) {
            model.setNormalizedCategoryScores(new HashMap<>());
        }

        // 2. 确保标签归一化分数不为null
        if (model.getNormalizedTagScores() == null) {
            model.setNormalizedTagScores(new HashMap<>());
        }

        // 3. 分类归一化计算
        if (model.getCategoryScores() != null && !model.getCategoryScores().isEmpty()) {
            double maxCategoryScore = Collections.max(model.getCategoryScores().values());
            for (Map.Entry<String, Double> entry : model.getCategoryScores().entrySet()) {
                model.getNormalizedCategoryScores().put(entry.getKey(), entry.getValue() / maxCategoryScore);
            }
        }

        // 4. 标签归一化计算
        if (model.getTagScores() != null && !model.getTagScores().isEmpty()) {
            double maxTagScore = Collections.max(model.getTagScores().values());
            for (Map.Entry<String, Double> entry : model.getTagScores().entrySet()) {
                model.getNormalizedTagScores().put(entry.getKey(), entry.getValue() / maxTagScore);
            }
        }
    }

    // 获取高兴趣分类
    private List<String> getTopCategories(UserInterestModel interest) {
        if (interest.getCategoryScores() == null || interest.getCategoryScores().isEmpty()) {
            return new ArrayList<>();
        }
        //获取分类数量
        int limit = interest.getCategoryScores().size() / 3;
        return interest.getCategoryScores().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 获取高兴趣标签
    private List<String> getTopTags(UserInterestModel interest) {
        if (interest.getTagScores() == null || interest.getTagScores().isEmpty()) {
            return new ArrayList<>();
        }
        int limit = interest.getTagScores().size() / 6;
        return interest.getTagScores().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
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
