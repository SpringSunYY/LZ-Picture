package com.lz.picture.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.model.domain.UserViewLogInfo;
import com.lz.picture.model.dto.pictureRecommend.UserInterestModel;
import com.lz.picture.model.enums.PUserBehaviorTargetTypeEnum;
import com.lz.picture.model.enums.PViewLogTargetTypeEnum;
import com.lz.picture.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;
import static com.lz.common.constant.config.UserConfigKeyConstants.*;

/**
 * 用户推荐服务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-06  20:42
 * @Version: 1.0
 */
@Service
public class PictureRecommendServiceImpl implements IPictureRecommendService {
    @Resource
    private RedisCache redisCache;

    @Resource
    private IConfigInfoService configInfoService;

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    @Resource
    private IUserViewLogInfoService userViewLogInfoService;

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

        String downloadCategoryStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_DOWNLOAD_CATEGORY);
        int downloadCategoryWeight = Integer.parseInt(downloadCategoryStr);
        String downloadTagScoreStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_DOWNLOAD_TAG);
        int downloadTagWeight = Integer.parseInt(downloadTagScoreStr);
        String downloadTimeDecayStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_TIME_DOWNLOAD_DECAY);
        double downloadTimeDecay = Double.parseDouble(downloadTimeDecayStr);
        String downloadLimitStr = configInfoService.getConfigInfoInCache(PICTURE_RECOMMEND_DOWNLOAD_LIMIT);
        int downloadLimit = Integer.parseInt(downloadLimitStr);
        UserInterestModel downloadModel = this.getPictureDownloadInterest(userId, downloadLimit, downloadCategoryWeight, downloadTagWeight, downloadTimeDecay);

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
        if (StringUtils.isNotNull(viewModel)) {
            if (StringUtils.isNotEmpty(viewModel.getCategoryScores())) {
                viewModel.getCategoryScores().forEach((key, value) -> {
                    categoryScores.merge(key, value, Double::sum);
                });
            }
            if (StringUtils.isNotEmpty(viewModel.getTagScores())) {
                viewModel.getTagScores().forEach((key, value) -> {
                    tagScores.merge(key, value, Double::sum);
                });
            }
        }
        if (StringUtils.isNotNull(downloadModel)) {
            if (StringUtils.isNotEmpty(downloadModel.getCategoryScores())) {
                downloadModel.getCategoryScores().forEach((key, value) -> {
                    categoryScores.merge(key, value, Double::sum);
                });
            }
            if (StringUtils.isNotEmpty(downloadModel.getTagScores())) {
                downloadModel.getTagScores().forEach((key, value) -> {
                    tagScores.merge(key, value, Double::sum);
                });
            }
        }
        if (StringUtils.isNotNull(behaviorModel)) {
            if (StringUtils.isNotEmpty(behaviorModel.getCategoryScores())) {
                behaviorModel.getCategoryScores().forEach((key, value) -> {
                    categoryScores.merge(key, value, Double::sum);
                });
            }
            if (StringUtils.isNotEmpty(behaviorModel.getTagScores())) {
                behaviorModel.getTagScores().forEach((key, value) -> {
                    tagScores.merge(key, value, Double::sum);
                });
            }
        }
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
            double categoryScore = baseScore * categoryWeight / calculateTimeDecay;
            categoryScores.merge(categoryId, categoryScore, Double::sum);
        }

        // 处理标签分数（按分号拆分标签）
        if (StringUtils.isNotEmpty(tags)) {
            Arrays.stream(tags.split(COMMON_SEPARATOR))
                    .filter(tag -> !tag.isEmpty())
                    .forEach(tag -> {
                        double tagScore = baseScore * tagWeight / calculateTimeDecay;
                        tagScores.merge(tag, tagScore, Double::sum);
                    });
        }
    }
    // endregion
}
