package com.lz.picture.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.mapper.PictureRecommendInfoMapper;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import com.lz.picture.model.domain.PictureRecommendInfo;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.model.domain.UserViewLogInfo;
import com.lz.picture.model.dto.pictureRecommend.UserInterestModel;
import com.lz.picture.model.dto.pictureRecommendInfo.PictureRecommendInfoQuery;
import com.lz.picture.model.enums.PUserBehaviorTargetTypeEnum;
import com.lz.picture.model.enums.PViewLogTargetTypeEnum;
import com.lz.picture.model.vo.pictureRecommendInfo.PictureRecommendInfoVo;
import com.lz.picture.service.IPictureDownloadLogInfoService;
import com.lz.picture.service.IPictureRecommendInfoService;
import com.lz.picture.service.IUserBehaviorInfoService;
import com.lz.picture.service.IUserViewLogInfoService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;
import static com.lz.common.constant.config.UserConfigKeyConstants.*;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_RECOMMEND_PICTURE_MODEL;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_RECOMMEND_PICTURE_MODEL_EXPIRE_TIME;

/**
 * 用户图片推荐模型Service业务层处理
 *
 * @author YY
 * @date 2025-06-10
 */
@Slf4j
@Service
public class PictureRecommendInfoServiceImpl extends ServiceImpl<PictureRecommendInfoMapper, PictureRecommendInfo> implements IPictureRecommendInfoService {
    @Resource
    private PictureRecommendInfoMapper pictureRecommendInfoMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private IConfigInfoService configInfoService;

    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    @Resource
    private IUserViewLogInfoService userViewLogInfoService;

    private static final long lastCacheRefreshTime = 0;
    private static final long CACHE_REFRESH_INTERVAL = 3600 * 1000; // 1小时刷新一次
    private ScheduledExecutorService scheduler = null;
    //浏览分数相关
    private static int VIEW_CATEGORY_WEIGHT = 1;
    private static int VIEW_TAG_WEIGHT = 2;
    private static double VIEW_TIME_DECAY = 0.8;
    private static int VIEW_LIMIT = 500;
    //行为分数相关
    private static int BEHAVIOR_CATEGORY_WEIGHT = 1;
    private static int BEHAVIOR_TAG_WEIGHT = 2;
    private static double BEHAVIOR_TIME_DECAY = 0.8;
    private static int BEHAVIOR_LIMIT = 200;
    //下载分数相关
    private static int DOWNLOAD_CATEGORY_WEIGHT = 1;
    private static int DOWNLOAD_TAG_WEIGHT = 2;
    private static double DOWNLOAD_TIME_DECAY = 0.8;
    private static int DOWNLOAD_LIMIT = 100;


    @PostConstruct
    public void init() {
        startRefreshTask(); // 启动定时刷新任务
        refreshConfig();    //刷新配置
    }

    private void refreshConfig() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheRefreshTime < CACHE_REFRESH_INTERVAL) {
            return;
        }
        VIEW_CATEGORY_WEIGHT = getIntConfig(PICTURE_RECOMMEND_VIEW_CATEGORY);
        VIEW_TAG_WEIGHT = getIntConfig(PICTURE_RECOMMEND_VIEW_TAG);
        VIEW_TIME_DECAY = getDoubleConfig(PICTURE_RECOMMEND_VIEW_TIME_DECAY);
        VIEW_LIMIT = getIntConfig(PICTURE_RECOMMEND_VIEW_LIMIT);

        BEHAVIOR_CATEGORY_WEIGHT = getIntConfig(PICTURE_RECOMMEND_BEHAVIOR_CATEGORY);
        BEHAVIOR_TAG_WEIGHT = getIntConfig(PICTURE_RECOMMEND_BEHAVIOR_TAG);
        BEHAVIOR_TIME_DECAY = getDoubleConfig(PICTURE_RECOMMEND_TIME_BEHAVIOR_DECAY);
        BEHAVIOR_LIMIT = getIntConfig(PICTURE_RECOMMEND_BEHAVIOR_LIMIT);

        DOWNLOAD_CATEGORY_WEIGHT = getIntConfig(PICTURE_RECOMMEND_DOWNLOAD_CATEGORY);
        DOWNLOAD_TAG_WEIGHT = getIntConfig(PICTURE_RECOMMEND_DOWNLOAD_TAG);
        DOWNLOAD_TIME_DECAY = getDoubleConfig(PICTURE_RECOMMEND_TIME_DOWNLOAD_DECAY);
        DOWNLOAD_LIMIT = getIntConfig(PICTURE_RECOMMEND_DOWNLOAD_LIMIT);

    }

    private int getIntConfig(String key) {
        return Integer.parseInt(configInfoService.getConfigInfoInCache(key));
    }

    private double getDoubleConfig(String key) {
        return Double.parseDouble(configInfoService.getConfigInfoInCache(key));
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
        scheduler.scheduleAtFixedRate(this::refreshConfig, 1, 1, TimeUnit.HOURS);
    }

    //region mybatis代码

    /**
     * 查询用户图片推荐模型
     *
     * @param recommendId 用户图片推荐模型主键
     * @return 用户图片推荐模型
     */
    @Override
    public PictureRecommendInfo selectPictureRecommendInfoByRecommendId(String recommendId) {
        return pictureRecommendInfoMapper.selectPictureRecommendInfoByRecommendId(recommendId);
    }

    /**
     * 查询用户图片推荐模型列表
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 用户图片推荐模型
     */
    @Override
    public List<PictureRecommendInfo> selectPictureRecommendInfoList(PictureRecommendInfo pictureRecommendInfo) {
        return pictureRecommendInfoMapper.selectPictureRecommendInfoList(pictureRecommendInfo);
    }

    /**
     * 新增用户图片推荐模型
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 结果
     */
    @Override
    public int insertPictureRecommendInfo(PictureRecommendInfo pictureRecommendInfo) {
        pictureRecommendInfo.setCreateTime(DateUtils.getNowDate());
        return pictureRecommendInfoMapper.insertPictureRecommendInfo(pictureRecommendInfo);
    }

    /**
     * 修改用户图片推荐模型
     *
     * @param pictureRecommendInfo 用户图片推荐模型
     * @return 结果
     */
    @Override
    public int updatePictureRecommendInfo(PictureRecommendInfo pictureRecommendInfo) {
        return pictureRecommendInfoMapper.updatePictureRecommendInfo(pictureRecommendInfo);
    }

    /**
     * 批量删除用户图片推荐模型
     *
     * @param recommendIds 需要删除的用户图片推荐模型主键
     * @return 结果
     */
    @Override
    public int deletePictureRecommendInfoByRecommendIds(String[] recommendIds) {
        return pictureRecommendInfoMapper.deletePictureRecommendInfoByRecommendIds(recommendIds);
    }

    /**
     * 删除用户图片推荐模型信息
     *
     * @param recommendId 用户图片推荐模型主键
     * @return 结果
     */
    @Override
    public int deletePictureRecommendInfoByRecommendId(String recommendId) {
        return pictureRecommendInfoMapper.deletePictureRecommendInfoByRecommendId(recommendId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureRecommendInfo> getQueryWrapper(PictureRecommendInfoQuery pictureRecommendInfoQuery) {
        QueryWrapper<PictureRecommendInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureRecommendInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String recommendId = pictureRecommendInfoQuery.getRecommendId();
        queryWrapper.eq(StringUtils.isNotEmpty(recommendId), "recommend_id", recommendId);

        String userId = pictureRecommendInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = pictureRecommendInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<PictureRecommendInfoVo> convertVoList(List<PictureRecommendInfo> pictureRecommendInfoList) {
        if (StringUtils.isEmpty(pictureRecommendInfoList)) {
            return Collections.emptyList();
        }
        return pictureRecommendInfoList.stream().map(PictureRecommendInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public UserInterestModel getUserInterrestModelByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        //查询缓存是否存在
        String key = PICTURE_RECOMMEND_PICTURE_MODEL + userId;
        if (redisCache.hasKey(key)) {
            String json = redisCache.getCacheObject(key);
            return JSONObject.parseObject(json, UserInterestModel.class);
        }
        //不存在，查询数据库最新的一条数据库
        PictureRecommendInfo pictureRecommendInfo = this.getOne(new LambdaQueryWrapper<PictureRecommendInfo>()
                .eq(PictureRecommendInfo::getUserId, userId)
                .orderBy(true, false, PictureRecommendInfo::getCreateTime)
                .last("limit 1"));
        //如果不为空，直接返回
        if (StringUtils.isNotNull(pictureRecommendInfo)) {
            UserInterestModel userInterestModel = new UserInterestModel();
            userInterestModel.setTagScores(
                    JSONObject.parseObject(pictureRecommendInfo.getTagScores(), new TypeReference<Map<String, Double>>() {
                    }));
            userInterestModel.setCategoryScores(
                    JSONObject.parseObject(pictureRecommendInfo.getCategoryScores(), new TypeReference<Map<String, Double>>() {
                    }));
            userInterestModel.setTopTags(
                    JSONObject.parseObject(pictureRecommendInfo.getTopTags(), new TypeReference<List<String>>() {
                    }));
            userInterestModel.setTopCategories(
                    JSONObject.parseObject(pictureRecommendInfo.getTopCategories(), new TypeReference<List<String>>() {
                    }));
            userInterestModel.setNormalizedTagScores(
                    JSONObject.parseObject(pictureRecommendInfo.getNormalizedTagScores(), new TypeReference<Map<String, Double>>() {
                    }));
            userInterestModel.setNormalizedCategoryScores(
                    JSONObject.parseObject(pictureRecommendInfo.getNormalizedCategoryScores(), new TypeReference<Map<String, Double>>() {
                    }));

            String json = JSON.toJSONString(userInterestModel);
            redisCache.setCacheObject(key, json, PICTURE_RECOMMEND_PICTURE_MODEL_EXPIRE_TIME, TimeUnit.SECONDS);
            return userInterestModel;
        }
        UserInterestModel userInterestModel = getUserInterestModel(userId);
        //如果为空，返回空，但也要缓存
        if (StringUtils.isNull(userInterestModel)) {
            userInterestModel = new UserInterestModel();
            String json = JSON.toJSONString(userInterestModel);
            redisCache.setCacheObject(key, json, PICTURE_RECOMMEND_PICTURE_MODEL_EXPIRE_TIME, TimeUnit.SECONDS);
            return null;
        }
        pictureRecommendInfo = new PictureRecommendInfo();
        pictureRecommendInfo.setUserId(userId);
        pictureRecommendInfo.setTagScores(JSONObject.toJSONString(userInterestModel.getTagScores()));
        pictureRecommendInfo.setCategoryScores(JSONObject.toJSONString(userInterestModel.getCategoryScores()));
        pictureRecommendInfo.setTopTags(JSONObject.toJSONString(userInterestModel.getTopTags()));
        pictureRecommendInfo.setTopCategories(JSONObject.toJSONString(userInterestModel.getTopCategories()));
        pictureRecommendInfo.setNormalizedTagScores(JSONObject.toJSONString(userInterestModel.getNormalizedTagScores()));
        pictureRecommendInfo.setNormalizedCategoryScores(JSONObject.toJSONString(userInterestModel.getNormalizedCategoryScores()));
        this.save(pictureRecommendInfo);
        String json = JSON.toJSONString(userInterestModel);
        redisCache.setCacheObject(key, json, PICTURE_RECOMMEND_PICTURE_MODEL_EXPIRE_TIME, TimeUnit.SECONDS);
        return userInterestModel;
    }

    private UserInterestModel getUserInterestModel(String userId) {
        UserInterestModel userInterestModel = getUserInterest(userId);
        if (StringUtils.isNull(userInterestModel)
                || (userInterestModel.getTagScores().isEmpty() && userInterestModel.getCategoryScores().isEmpty())) {
            return null;
        }
        normalizeScores(userInterestModel);
        List<String> topCategories = getTopScores(userInterestModel.getCategoryScores(), 3, 10);
        List<String> topTags = getTopScores(userInterestModel.getTagScores(), 6, 5);
        userInterestModel.setTopCategories(topCategories);
        userInterestModel.setTopTags(topTags);
        return userInterestModel;
    }


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
        return userInterestModel;
    }

    @Override
    public UserInterestModel getUserInterest(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        //查询到浏览分数占比
        UserInterestModel viewModel = this.getUserViewInterest(userId, PViewLogTargetTypeEnum.VIEW_LOG_TARGET_TYPE_0.getValue(), VIEW_LIMIT, VIEW_CATEGORY_WEIGHT, VIEW_TAG_WEIGHT, VIEW_TIME_DECAY);

        //查询下载兴趣
        UserInterestModel downloadModel = this.getPictureDownloadInterest(userId, DOWNLOAD_LIMIT, DOWNLOAD_CATEGORY_WEIGHT, DOWNLOAD_TAG_WEIGHT, DOWNLOAD_TIME_DECAY);

        //查询行为兴趣
        UserInterestModel behaviorModel = this.getUserBehaviorInterest(userId, PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0.getValue(), BEHAVIOR_LIMIT, BEHAVIOR_CATEGORY_WEIGHT, BEHAVIOR_TAG_WEIGHT, BEHAVIOR_TIME_DECAY);
        //计算总兴趣总数
        UserInterestModel userInterestModel = new UserInterestModel();
        HashMap<String, Double> tagScores = new HashMap<>();
        userInterestModel.setTagScores(tagScores);
        HashMap<String, Double> categoryScores = new HashMap<>();
        userInterestModel.setCategoryScores(categoryScores);
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

    // 获取高兴趣的分数
    private List<String> getTopScores(Map<String, Double> scores, int rate, int min) {
        if (StringUtils.isEmpty(scores)) {
            return new ArrayList<>();
        }
        //获取分类数量
        int limit = scores.size() / rate;
        if (limit < min) {
            limit = scores.size();
        }
        return scores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 归一化处理
    public void normalizeScores(UserInterestModel model) {
        // 确保模型不为空
        if (StringUtils.isNull(model)) return;

        // 1. 确保分类归一化分数不为null
        if (StringUtils.isEmpty(model.getNormalizedCategoryScores())) {
            model.setNormalizedCategoryScores(new HashMap<>());
        }

        // 2. 确保标签归一化分数不为null
        if (StringUtils.isEmpty(model.getNormalizedTagScores())) {
            model.setNormalizedTagScores(new HashMap<>());
        }

        // 3. 分类归一化计算
        if (StringUtils.isNotEmpty(model.getCategoryScores())) {
            double maxCategoryScore = Collections.max(model.getCategoryScores().values());
            for (Map.Entry<String, Double> entry : model.getCategoryScores().entrySet()) {
                model.getNormalizedCategoryScores().put(entry.getKey(), entry.getValue() / maxCategoryScore);
            }
        }

        // 4. 标签归一化计算
        if (StringUtils.isNotEmpty(model.getTagScores())) {
            double maxTagScore = Collections.max(model.getTagScores().values());
            for (Map.Entry<String, Double> entry : model.getTagScores().entrySet()) {
                model.getNormalizedTagScores().put(entry.getKey(), entry.getValue() / maxTagScore);
            }
        }
    }
    // endregion
}
