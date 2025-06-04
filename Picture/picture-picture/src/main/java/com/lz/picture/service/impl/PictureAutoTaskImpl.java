package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.enums.CommonHasStatisticsEnum;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.domain.*;
import com.lz.picture.model.enums.PUserBehaviorTargetTypeEnum;
import com.lz.picture.model.enums.PUserBehaviorTypeEnum;
import com.lz.picture.model.enums.PViewLogTargetTypeEnum;
import com.lz.picture.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR;


/**
 * 图片模块自动任务
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-26  00:11
 * @Version: 1.0
 */
@Service
public class PictureAutoTaskImpl implements IPictureAutoTask {
    @Resource
    private IUserViewLogInfoService userViewLogInfoService;

    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

    @Resource
    private IPictureCategoryInfoService pictureCategoryInfoService;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private TransactionTemplate transactionTemplate;

    //region  自动更新浏览记录信息
    @Override
    public void autoUpdateUserViewLogInfo() {
        //查询到所有未统计信息
        List<UserViewLogInfo> userViewLogInfos = userViewLogInfoService.list(new LambdaQueryWrapper<UserViewLogInfo>()
                .eq(UserViewLogInfo::getHasStatistics, CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue())
                .eq(UserViewLogInfo::getTargetType, PViewLogTargetTypeEnum.VIEW_LOG_TARGET_TYPE_0.getValue()));
        if (StringUtils.isEmpty(userViewLogInfos)) {
            return;
        }
        List<UserViewLogInfo> viewLogInfos = userViewLogInfos.stream().map(userViewLogInfo -> {
            UserViewLogInfo info = new UserViewLogInfo();
            info.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_1.getValue());
            info.setViewId(userViewLogInfo.getViewId());
            return info;
        }).toList();
        //需要图片、分类、标签
        //图片
        List<PictureInfo> pictureInfos = getAutoPictureInfByView(userViewLogInfos);
        //分类
        List<PictureCategoryInfo> categoryInfos = getAutoCategoryInfByView(userViewLogInfos);
        //标签
        List<PictureTagInfo> tagInfos = getAutoPictureTagInfByView(userViewLogInfos);
        //标签关联
        List<PictureTagRelInfo> pictureTagRelInfos = getAutoPictureTagRelInfByView(userViewLogInfos);
        transactionTemplate.executeWithoutResult(status -> {
            //更新图片信息
            if (StringUtils.isNotEmpty(pictureInfos)) {
                pictureInfoService.updateBatchById(pictureInfos);
            }
            //更新分类信息
            if (StringUtils.isNotEmpty(categoryInfos)) {
                pictureCategoryInfoService.updateBatchById(categoryInfos);
            }
            //更新标签信息
            if (StringUtils.isNotEmpty(tagInfos)) {
                pictureTagInfoService.updateBatchById(tagInfos);
            }
            //更新标签关联信息
            if (StringUtils.isNotEmpty(pictureTagRelInfos)) {
                pictureTagRelInfoService.updateBatchById(pictureTagRelInfos);
            }
            //更新浏览记录信息
            userViewLogInfoService.updateBatchById(viewLogInfos);
        });
    }

    private List<PictureTagRelInfo> getAutoPictureTagRelInfByView(List<UserViewLogInfo> userViewLogInfos) {
        //去重图片编号，并判断每个图片编号有多少个浏览记录，且分类编号不为空
        // 按 targetId 分组，并统计每组的数量
        Map<String, Long> viewCountMap = userViewLogInfos.stream()
                .filter(userViewLogInfo -> StringUtils.isNotEmpty(userViewLogInfo.getTargetId()))
                .collect(Collectors.groupingBy(UserViewLogInfo::getTargetId, Collectors.counting()));
        //获取到所有的图片编号去重之后的
        List<String> pictureIds = viewCountMap.keySet().stream().toList();
        List<PictureTagRelInfo> tagRelInfos = new ArrayList<>(pictureIds.size());
        if (StringUtils.isNotEmpty(pictureIds)) {
            tagRelInfos = pictureTagRelInfoService.list(new LambdaQueryWrapper<PictureTagRelInfo>()
                    .in(PictureTagRelInfo::getPictureId, pictureIds));
        } else {
            return null;
        }
        //只更新需要内容
        return tagRelInfos.stream().map(pictureTagRelInfo -> {
            PictureTagRelInfo info = new PictureTagRelInfo();
            info.setRelId(pictureTagRelInfo.getRelId());
            info.setLookCount(pictureTagRelInfo.getLookCount() + viewCountMap.get(pictureTagRelInfo.getPictureId()));
            return info;
        }).toList();
    }

    /**
     * 获取自动标签信息,需要更新浏览记录的
     *
     * @param userViewLogInfos 浏览记录
     * @return List<PictureInfo>
     * @author: YY
     * @method: getAutoTagInfByView
     * @date: 2025/5/26 00:32
     **/
    private List<PictureTagInfo> getAutoPictureTagInfByView(List<UserViewLogInfo> userViewLogInfos) {
        //从浏览记录中获取到所有的标签编号，并判断每个标签编号有多少个浏览记录，且标签编号不为空的
        //并且标签存储的是字符串，需要使用;分割，每个标签总数
        List<String> tagTotalNames = userViewLogInfos.stream()
                .filter(userViewLogInfo -> StringUtils.isNotEmpty(userViewLogInfo.getTags()))
                .map(userViewLogInfo -> userViewLogInfo.getTags().split(COMMON_SEPARATOR))
                .flatMap(Arrays::stream)
                .filter(StringUtils::isNotEmpty)
                .toList();
        //统计每个标签的浏览数量
        Map<String, Long> viewCountMap = tagTotalNames.stream()
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.groupingBy(tagName -> tagName, Collectors.counting()));
        List<String> tagNames = viewCountMap.keySet().stream().toList();
        List<PictureTagInfo> tagInfos = new ArrayList<>(viewCountMap.size());
        if (StringUtils.isNotEmpty(tagNames)) {
            tagInfos = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>()
                    .in(PictureTagInfo::getName, tagNames));
        } else {
            return null;
        }
        if (StringUtils.isEmpty(tagInfos)) {
            return null;
        }
        return tagInfos.stream()
                .map(pictureTagInfo -> {
                    PictureTagInfo info = new PictureTagInfo();
                    info.setLookCount(pictureTagInfo.getLookCount() + viewCountMap.get(pictureTagInfo.getName()));
                    info.setTagId(pictureTagInfo.getTagId());
                    return info;
                }).toList();
    }

    /**
     * 获取自动分类信息,需要更新浏览记录的
     *
     * @param userViewLogInfos 浏览记录
     * @return List<PictureInfo>
     * @author: YY
     * @method: getAutoCategoryInfByView
     * @date: 2025/5/26 00:32
     **/
    private List<PictureCategoryInfo> getAutoCategoryInfByView(List<UserViewLogInfo> userViewLogInfos) {
        //去重分类编号，并判断每个分类编号有多少个浏览记录，且分类编号不为空的
        //按照categoryId分组，并统计每组的数量
        Map<String, Long> viewCountMap = userViewLogInfos.stream()
                .filter(userViewLogInfo -> StringUtils.isNotEmpty(userViewLogInfo.getCategoryId()))
                .collect(Collectors.groupingBy(UserViewLogInfo::getCategoryId, Collectors.counting()));
        List<String> categoryIds = viewCountMap.keySet().stream().toList();
        List<PictureCategoryInfo> categoryInfos = new ArrayList<>(viewCountMap.size());
        if (StringUtils.isNotEmpty(categoryIds)) {
            categoryInfos = pictureCategoryInfoService.list(new LambdaQueryWrapper<PictureCategoryInfo>()
                    .in(PictureCategoryInfo::getCategoryId, categoryIds));
        } else {
            return null;
        }
        if (StringUtils.isEmpty(categoryInfos)) {
            return null;
        }
        return categoryInfos.stream()
                .map(categoryInfo -> {
                    PictureCategoryInfo info = new PictureCategoryInfo();
                    info.setCategoryId(categoryInfo.getCategoryId());
                    info.setLookCount(categoryInfo.getLookCount() + viewCountMap.get(categoryInfo.getCategoryId()));
                    return info;
                }).toList();
    }

    /**
     * 获取自动图片信息,需要更新浏览记录的
     *
     * @param userViewLogInfos 浏览记录
     * @return
     */
    private List<PictureInfo> getAutoPictureInfByView(List<UserViewLogInfo> userViewLogInfos) {
        //去重图片编号，并判断每个图片编号有多少个浏览记录，且分类编号不为空
        // 按 targetId 分组，并统计每组的数量
        Map<String, Long> viewCountMap = userViewLogInfos.stream()
                .filter(userViewLogInfo -> StringUtils.isNotEmpty(userViewLogInfo.getTargetId()))
                .collect(Collectors.groupingBy(UserViewLogInfo::getTargetId, Collectors.counting()));
        //获取到所有的图片编号去重之后的
        List<String> pictureIds = viewCountMap.keySet().stream().toList();
        List<PictureInfo> pictureInfos = new ArrayList<>(pictureIds.size());
        if (StringUtils.isNotEmpty(pictureIds)) {
            pictureInfos = pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>()
                    .in(PictureInfo::getPictureId, pictureIds));
        } else {
            return null;
        }
        if (StringUtils.isEmpty(pictureInfos)) {
            return null;
        }
        //只更新需要的字段
        return pictureInfos.stream().map(pictureInfo -> {
            PictureInfo info = new PictureInfo();
            info.setPictureId(pictureInfo.getPictureId());
            info.setLookCount(pictureInfo.getLookCount() + viewCountMap.get(pictureInfo.getPictureId()));
            return info;
        }).toList();
    }
    //endregion

    //region 下载记录自动更新
    @Override
    public void autoUpdatePictureDownloadLogInfo() {
        //查询到所有的尚未统计的下载记录
        List<PictureDownloadLogInfo> pictureDownloadLogInfos =
                pictureDownloadLogInfoService.list(new LambdaQueryWrapper<PictureDownloadLogInfo>()
                        .eq(PictureDownloadLogInfo::getHasStatistics, CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue()));
        if (StringUtils.isEmpty(pictureDownloadLogInfos)) {
            return;
        }
        List<PictureDownloadLogInfo> downloadLogInfos = pictureDownloadLogInfos.stream().map(pictureDownloadLogInfo -> {
            PictureDownloadLogInfo info = new PictureDownloadLogInfo();
            info.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_1.getValue());
            info.setDownloadId(pictureDownloadLogInfo.getDownloadId());
            return info;
        }).toList();
        //需要更新分类、空间、图片、标签
        List<PictureCategoryInfo> categoryInfos = getAutoCategoryInfByDownload(pictureDownloadLogInfos);
        List<SpaceInfo> spaceInfos = getAutoSpaceInfByDownload(pictureDownloadLogInfos);
        List<PictureInfo> pictureInfos = getAutoPictureInfByDownload(pictureDownloadLogInfos);
        List<PictureTagInfo> tagInfos = getAutoPictureTagInfByDownload(pictureDownloadLogInfos);
        List<PictureTagRelInfo> tagRelInfos = getAutoPictureTagRelInfByDownload(pictureDownloadLogInfos);
        transactionTemplate.execute(result -> {
            //更新分类
            if (StringUtils.isNotEmpty(categoryInfos)) {
                pictureCategoryInfoService.updateBatchById(categoryInfos);
            }
            //更新空间
            if (StringUtils.isNotEmpty(spaceInfos)) {
                spaceInfoService.updateBatchById(spaceInfos);
            }
            //更新图片
            if (StringUtils.isNotEmpty(pictureInfos)) {
                pictureInfoService.updateBatchById(pictureInfos);
            }
            //更新标签
            if (StringUtils.isNotEmpty(tagInfos)) {
                pictureTagInfoService.updateBatchById(tagInfos);
            }
            //更新标签关联
            if (StringUtils.isNotEmpty(tagRelInfos)) {
                pictureTagRelInfoService.updateBatchById(tagRelInfos);
            }
            //更新下载记录
            return pictureDownloadLogInfoService.updateBatchById(downloadLogInfos);
        });
    }

    private List<PictureTagRelInfo> getAutoPictureTagRelInfByDownload(List<PictureDownloadLogInfo> pictureDownloadLogInfos) {
        //去重图片编号，并判断每个图片编号有多少个下载记录，且分类编号不为空
        Map<String, Long> downloadCountMap = pictureDownloadLogInfos.stream()
                .filter(pictureDownloadLogInfo -> StringUtils.isNotEmpty(pictureDownloadLogInfo.getPictureId()))
                .collect(Collectors.groupingBy(PictureDownloadLogInfo::getPictureId, Collectors.counting()));
        List<String> pictureIds = downloadCountMap.keySet().stream().toList();
        List<PictureTagRelInfo> pictureTagRelInfos = new ArrayList<>(pictureIds.size());
        if (StringUtils.isNotEmpty(pictureIds)) {
            pictureTagRelInfos = pictureTagRelInfoService.list(new LambdaQueryWrapper<PictureTagRelInfo>()
                    .in(PictureTagRelInfo::getPictureId, pictureIds));
        } else {
            return null;
        }
        return pictureTagRelInfos.stream()
                .map(pictureTagRelInfo -> {
                    PictureTagRelInfo info = new PictureTagRelInfo();
                    info.setRelId(pictureTagRelInfo.getRelId());
                    info.setDownloadCount(pictureTagRelInfo.getDownloadCount() + downloadCountMap.get(pictureTagRelInfo.getPictureId()));
                    return info;
                }).toList();
    }

    private List<PictureTagInfo> getAutoPictureTagInfByDownload(List<PictureDownloadLogInfo> pictureDownloadLogInfos) {
        //去重标签名称，并判断每个标签名称有多少个下载记录，且标签名称不为空
        //标签存储是字符串，需要使用;分割，每个标签总数
        List<String> tagTotalNames = pictureDownloadLogInfos.stream()
                .filter(pictureDownloadLogInfo -> StringUtils.isNotEmpty(pictureDownloadLogInfo.getTags()))
                .map(pictureDownloadLogInfo -> pictureDownloadLogInfo.getTags().split(COMMON_SEPARATOR))
                .flatMap(Arrays::stream)
                .filter(StringUtils::isNotEmpty)
                .toList();
        Map<String, Long> downloadCountMap = tagTotalNames.stream()
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.groupingBy(tagName -> tagName, Collectors.counting()));
        List<PictureTagInfo> tagInfos = new ArrayList<>(downloadCountMap.size());
        if (StringUtils.isNotEmpty(tagTotalNames)) {
            tagInfos = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>()
                    .in(PictureTagInfo::getName, tagTotalNames));
        } else {
            return null;
        }
        if (StringUtils.isEmpty(tagInfos)) {
            return null;
        }
        return tagInfos.stream()
                .map(tagInfo -> {
                    PictureTagInfo info = new PictureTagInfo();
                    info.setTagId(tagInfo.getTagId());
                    info.setDownloadCount(tagInfo.getDownloadCount() + downloadCountMap.get(tagInfo.getName()));
                    return info;
                }).toList();
    }

    private List<PictureInfo> getAutoPictureInfByDownload(List<PictureDownloadLogInfo> pictureDownloadLogInfos) {
        //去重图片编号，并判断每个图片编号有多少个下载记录，且分类编号不为空
        Map<String, Long> downloadCountMap = pictureDownloadLogInfos.stream()
                .filter(pictureDownloadLogInfo -> StringUtils.isNotEmpty(pictureDownloadLogInfo.getPictureId()))
                .collect(Collectors.groupingBy(PictureDownloadLogInfo::getPictureId, Collectors.counting()));
        List<String> pictureIds = downloadCountMap.keySet().stream().toList();
        List<PictureInfo> pictureInfos = new ArrayList<>(pictureIds.size());
        if (StringUtils.isNotEmpty(pictureIds)) {
            pictureInfos = pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>()
                    .in(PictureInfo::getPictureId, pictureIds));
        } else {
            return null;
        }
        if (StringUtils.isEmpty(pictureInfos)) {
            return null;
        }
        return pictureInfos.stream().map(pictureInfo -> {
            PictureInfo info = new PictureInfo();
            info.setPictureId(pictureInfo.getPictureId());
            info.setDownloadCount(pictureInfo.getDownloadCount() + downloadCountMap.get(pictureInfo.getPictureId()));
            return info;
        }).toList();
    }

    private List<SpaceInfo> getAutoSpaceInfByDownload(List<PictureDownloadLogInfo> pictureDownloadLogInfos) {
        //去重空间编号，并判断每个空间编号有多少个下载记录，且空间编号不为空的
        Map<String, Long> downloadCountMap = pictureDownloadLogInfos.stream()
                .filter(pictureDownloadLogInfo -> StringUtils.isNotEmpty(pictureDownloadLogInfo.getSpaceId()))
                .collect(Collectors.groupingBy(PictureDownloadLogInfo::getSpaceId, Collectors.counting()));
        List<String> spaceIds = downloadCountMap.keySet().stream().toList();
        List<SpaceInfo> spaceInfos = new ArrayList<>(spaceIds.size());
        if (StringUtils.isNotEmpty(spaceIds)) {
            spaceInfos = spaceInfoService.list(new LambdaQueryWrapper<SpaceInfo>()
                    .in(SpaceInfo::getSpaceId, spaceIds));
        } else {
            return null;
        }
        if (StringUtils.isEmpty(spaceInfos)) {
            return null;
        }
        return spaceInfos.stream()
                .map(spaceInfo -> {
                    SpaceInfo info = new SpaceInfo();
                    info.setSpaceId(spaceInfo.getSpaceId());
                    info.setDownloadCount(spaceInfo.getDownloadCount() + downloadCountMap.get(spaceInfo.getSpaceId()));
                    return info;
                }).toList();
    }

    private List<PictureCategoryInfo> getAutoCategoryInfByDownload(List<PictureDownloadLogInfo> pictureDownloadLogInfos) {
        //去重分类编号，并判断每个分类编号有多少个下载记录，且分类编号不为空的
        //按照categoryId分组，并统计每组的数量
        Map<String, Long> downloadCountMap = pictureDownloadLogInfos.stream()
                .filter(pictureDownloadLogInfo -> StringUtils.isNotEmpty(pictureDownloadLogInfo.getCategoryId()))
                .collect(Collectors.groupingBy(PictureDownloadLogInfo::getCategoryId, Collectors.counting()));
        List<String> categoryIds = downloadCountMap.keySet().stream().toList();
        List<PictureCategoryInfo> categoryInfos = new ArrayList<>(downloadCountMap.size());
        if (StringUtils.isNotEmpty(categoryIds)) {
            categoryInfos = pictureCategoryInfoService.list(new LambdaQueryWrapper<PictureCategoryInfo>()
                    .in(PictureCategoryInfo::getCategoryId, categoryIds));
        } else {
            return null;
        }
        if (StringUtils.isEmpty(categoryInfos)) {
            return null;
        }
        return categoryInfos.stream()
                .map(categoryInfo -> {
                    PictureCategoryInfo info = new PictureCategoryInfo();
                    info.setCategoryId(categoryInfo.getCategoryId());
                    info.setDownloadCount(categoryInfo.getDownloadCount() + downloadCountMap.get(categoryInfo.getCategoryId()));
                    return info;
                }).toList();
    }
    //endregion

    //region 更新用户行为
    @Override
    public void autoUpdateUserBehaviorInfo() {
        //首先更新空间，空间只有收藏
        //获取到需要更新的空间列表
        List<UserBehaviorInfo> spaceBehaviorList = userBehaviorInfoService.list(new LambdaQueryWrapper<UserBehaviorInfo>()
                .eq(UserBehaviorInfo::getBehaviorType, PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_1.getValue())
                .eq(UserBehaviorInfo::getTargetType, PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_1.getValue())
                .eq(UserBehaviorInfo::getHasStatistics, CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue()));
        if (StringUtils.isNotEmpty(spaceBehaviorList)) {
            spaceBehaviorList.forEach(spaceBehavior -> {
                spaceBehavior.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_1.getValue());
            });
        }
        List<SpaceInfo> spaceInfos = getAutoSpaceInfByBehavior(spaceBehaviorList);
        //其次是图片，图片有点赞、分享、收藏
        //查询到所有要有未统计、且目标类型是图片的用户行为列表
        List<UserBehaviorInfo> pictureBehaviorList = userBehaviorInfoService.list(new LambdaQueryWrapper<UserBehaviorInfo>()
                .eq(UserBehaviorInfo::getTargetType, PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0.getValue())
                .eq(UserBehaviorInfo::getHasStatistics, CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue()));
        if (StringUtils.isEmpty(pictureBehaviorList)) {
            pictureBehaviorList.forEach(pictureBehavior -> {
                pictureBehavior.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_1.getValue());
            });
        }
        List<PictureInfo> pictureInfos = getAutoPictureInfByBehavior(pictureBehaviorList);
        List<PictureTagRelInfo> pictureTagRelInfos = getAutoPictureTagRelInfByBehavior(pictureBehaviorList);
        transactionTemplate.execute(result -> {
            if (StringUtils.isNotEmpty(spaceInfos)) {
                spaceInfoService.updateBatchById(spaceInfos);
            }
            if (StringUtils.isNotEmpty(pictureInfos)) {
                pictureInfoService.updateBatchById(pictureInfos);
            }
            if (StringUtils.isEmpty(spaceBehaviorList)) {
                userBehaviorInfoService.updateBatchById(spaceBehaviorList);
            }
            if (StringUtils.isEmpty(pictureBehaviorList)) {
                userBehaviorInfoService.updateBatchById(pictureBehaviorList);
            }
            if (StringUtils.isNotEmpty(pictureTagRelInfos)) {
                pictureTagRelInfoService.updateBatchById(pictureTagRelInfos);
            }
            return true;
        });
    }

    private List<PictureTagRelInfo> getAutoPictureTagRelInfByBehavior(List<UserBehaviorInfo> pictureBehaviorList) {
        //图片有点赞、分享、收藏
        if (StringUtils.isEmpty(pictureBehaviorList)) {
            return null;
        }
        List<String> pictureIds = pictureBehaviorList.stream()
                .map(UserBehaviorInfo::getTargetId)
                .distinct()
                .toList();
        List<PictureTagRelInfo> pictureTagRelInfos = pictureTagRelInfoService.list(new LambdaQueryWrapper<PictureTagRelInfo>()
                .in(PictureTagRelInfo::getPictureId, pictureIds));
        if (StringUtils.isEmpty(pictureTagRelInfos)) {
            return null;
        }
        //根据用户行为类型+目标编号分组，统计每种行为出现的次数 例如"1": { "1001": 5, "1002": 3 },
        Map<String, Map<String, Long>> behaviorCountMap = pictureBehaviorList.stream()
                .filter(userBehaviorInfo -> PUserBehaviorTypeEnum.getEnumByValue(userBehaviorInfo.getBehaviorType()).isPresent())
                .collect(Collectors.groupingBy(UserBehaviorInfo::getBehaviorType, Collectors.groupingBy(UserBehaviorInfo::getTargetId, Collectors.counting())));
        return pictureTagRelInfos.stream()
                .map(pictureTagRelInfo -> {
                    PictureTagRelInfo info = new PictureTagRelInfo();
                    info.setRelId(pictureTagRelInfo.getRelId());
                    info.setLikeCount(pictureTagRelInfo.getLikeCount() + behaviorCountMap.getOrDefault(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_0.getValue(), new HashMap<>()).getOrDefault(pictureTagRelInfo.getPictureId(), 0L));
                    info.setCollectCount(pictureTagRelInfo.getCollectCount() + behaviorCountMap.getOrDefault(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_1.getValue(), new HashMap<>()).getOrDefault(pictureTagRelInfo.getPictureId(), 0L));
                    info.setShareCount(pictureTagRelInfo.getShareCount() + behaviorCountMap.getOrDefault(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_2.getValue(), new HashMap<>()).getOrDefault(pictureTagRelInfo.getPictureId(), 0L));
                    return info;
                }).toList();
    }

    private List<PictureInfo> getAutoPictureInfByBehavior(List<UserBehaviorInfo> pictureBehaviorList) {
        //图片有点赞、分享、收藏
        if (StringUtils.isEmpty(pictureBehaviorList)) {
            return null;
        }
        //查询到所有的图片
        List<String> pictureIds = pictureBehaviorList.stream()
                .map(UserBehaviorInfo::getTargetId)
                .distinct()
                .toList();
        List<PictureInfo> pictureInfos = pictureInfoService.list(new LambdaQueryWrapper<PictureInfo>()
                .in(PictureInfo::getPictureId, pictureIds));
        if (StringUtils.isEmpty(pictureInfos)) {
            return null;
        }
        //根据用户行为类型+目标编号分组，统计每种行为出现的次数 例如"1": { "1001": 5, "1002": 3 },
        Map<String, Map<String, Long>> behaviorCountMap = pictureBehaviorList.stream()
                .filter(userBehaviorInfo -> PUserBehaviorTypeEnum.getEnumByValue(userBehaviorInfo.getBehaviorType()).isPresent())
                .collect(Collectors.groupingBy(UserBehaviorInfo::getBehaviorType, Collectors.groupingBy(UserBehaviorInfo::getTargetId, Collectors.counting())));
        return pictureInfos.stream()
                .map(pictureInfo -> {
                    PictureInfo info = new PictureInfo();
                    info.setPictureId(pictureInfo.getPictureId());
                    info.setLikeCount(pictureInfo.getLikeCount() + behaviorCountMap.getOrDefault(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_0.getValue(), new HashMap<>()).getOrDefault(pictureInfo.getPictureId(), 0L));
                    info.setCollectCount(pictureInfo.getCollectCount() + behaviorCountMap.getOrDefault(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_1.getValue(), new HashMap<>()).getOrDefault(pictureInfo.getPictureId(), 0L));
                    info.setShareCount(pictureInfo.getShareCount() + behaviorCountMap.getOrDefault(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_2.getValue(), new HashMap<>()).getOrDefault(pictureInfo.getPictureId(), 0L));
                    return info;
                }).toList();
    }

    /**
     * 查询到所有的需要更新的空间列表
     *
     * @return
     */
    private List<SpaceInfo> getAutoSpaceInfByBehavior(List<UserBehaviorInfo> spaceBehaviorList) {
        if (StringUtils.isEmpty(spaceBehaviorList)) {
            return null;
        }
        //去重空间编号，并判断每个空间编号有多少个收藏记录，且空间编号不为空的 targetId
        Map<String, Long> collectCountMap = spaceBehaviorList.stream()
                .filter(userBehaviorInfo -> StringUtils.isNotEmpty(userBehaviorInfo.getTargetId()))
                .collect(Collectors.groupingBy(UserBehaviorInfo::getTargetId, Collectors.counting()));
        if (StringUtils.isEmpty(collectCountMap)) {
            return null;
        }
        List<String> spaceIds = collectCountMap.keySet().stream().toList();
        List<SpaceInfo> spaceInfos = new ArrayList<>(spaceIds.size());
        if (StringUtils.isNotEmpty(spaceIds)) {
            spaceInfos = spaceInfoService.list(new LambdaQueryWrapper<SpaceInfo>()
                    .in(SpaceInfo::getSpaceId, spaceIds));
        } else {
            return null;
        }
        if (StringUtils.isEmpty(spaceInfos)) {
            return null;
        }
        return spaceInfos.stream()
                .map(spaceInfo -> {
                    SpaceInfo info = new SpaceInfo();
                    info.setSpaceId(spaceInfo.getSpaceId());
                    info.setCollectCount(spaceInfo.getCollectCount() + collectCountMap.get(spaceInfo.getSpaceId()));
                    return info;
                }).toList();
    }
}
