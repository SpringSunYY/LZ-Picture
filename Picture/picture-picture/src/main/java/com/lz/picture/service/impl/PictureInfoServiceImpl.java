package com.lz.picture.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheEvict;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.annotation.CustomSort;
import com.lz.common.config.OssConfig;
import com.lz.common.constant.HttpStatus;
import com.lz.common.constant.redis.PictureRedisConstants;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.enums.CommonHasStatisticsEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.manager.file.PictureDownloadManager;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.ParamUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.model.enmus.CTemplateTypeEnum;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureFileLogAsyncFactory;
import com.lz.picture.manager.factory.PictureRecommendAsyncFactory;
import com.lz.picture.mapper.PictureInfoMapper;
import com.lz.picture.model.domain.*;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoRequest;
import com.lz.picture.model.dto.pictureInfo.*;
import com.lz.picture.model.enums.*;
import com.lz.picture.model.vo.pictureInfo.*;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoCache;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoStaticVo;
import com.lz.picture.service.*;
import com.lz.picture.utils.PictureStatisticsUtil;
import com.lz.picture.utils.SpaceAuthUtils;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.enums.PoPointsUsageLogTypeEnum;
import com.lz.points.model.enums.PoPointsUsageTypeEnum;
import com.lz.points.service.IAccountInfoService;
import com.lz.points.service.IPointsUsageLogInfoService;
import com.lz.user.manager.UserAsyncManager;
import com.lz.user.manager.factory.InformInfoAsyncFactory;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.enums.UInformTypeEnum;
import com.lz.user.model.vo.userInfo.UserVo;
import com.lz.user.service.IUserInfoService;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.config.TemplateInfoKeyConstants.DOWNLOAD_PICTURE;
import static com.lz.common.constant.config.TemplateInfoKeyConstants.DOWNLOAD_PICTURE_AUTHOR_PROPORTION;
import static com.lz.common.constant.picture.PictureInfoConstants.*;
import static com.lz.common.constant.redis.PictureRedisConstants.*;
import static com.lz.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;
import static com.lz.config.utils.ConfigInfoUtils.*;
import static com.lz.picture.utils.PictureStatisticsUtil.PICTURE_STATISTICS_PICTURE_HOT;
import static com.lz.picture.utils.PictureStatisticsUtil.PICTURE_STATISTICS_PICTURE_HOT_TOTAL_KEY;

/**
 * 图片信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class PictureInfoServiceImpl extends ServiceImpl<PictureInfoMapper, PictureInfo> implements IPictureInfoService {
    @Resource
    private PictureInfoMapper pictureInfoMapper;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    @Lazy
    private ISpaceFolderInfoService spaceFolderInfoService;

    @Resource
    private IPictureTagInfoService pictureTagInfoService;

    @Resource
    private IPictureTagRelInfoService pictureTagRelInfoService;

    @Resource
    private ExecutorService executorService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private IPictureCategoryInfoService pictureCategoryInfoService;

    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    @Resource
    private PictureDownloadManager pictureDownloadManager;

    @Resource
    private IAccountInfoService accountInfoService;

    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;

    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    @Resource
    private SpaceAuthUtils spaceAuthUtils;

    @Resource
    private PictureStatisticsUtil pictureStatisticsUtil;

    @Resource
    private IStatisticsInfoService statisticsInfoService;

    //region mybatis代码

    /**
     * 查询图片信息
     *
     * @param pictureId 图片信息主键
     * @return 图片信息
     */
    @Override
    public PictureInfo selectPictureInfoByPictureId(String pictureId) {
        PictureInfo pictureInfo = pictureInfoMapper.selectPictureInfoByPictureId(pictureId);
        if (StringUtils.isNotNull(pictureInfo)) {
            String pictureUrl = OssConfig.builderUrl(pictureInfo.getPictureUrl(), pictureInfo.getDnsUrl());
            pictureInfo.setPictureUrl(pictureUrl);
            String thumbnailUrl = OssConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl());
            pictureInfo.setThumbnailUrl(thumbnailUrl);
        }
        return pictureInfo;
    }

    /**
     * 查询图片信息列表
     *
     * @param pictureInfo 图片信息
     * @return 图片信息
     */
    @CustomSort(
            sortFields = {"createTime", "updateTime", "publishTime", "deletedTime",
                    "lookCount", "collectCount", "likeCount", "shareCount", "downloadCount",
                    "picSize", "picWidth", "picHeight"},
            sortMappingFields = {
                    "create_time", "update_time", "publish_time", "deleted_time",
                    "look_count", "collect_count", "like_count", "share_count", "download_count",
                    "pic_size", "pic_width", "pic_height"})
    @Override
    public List<PictureInfo> selectPictureInfoList(PictureInfo pictureInfo) {
        List<PictureInfo> pictureInfos = pictureInfoMapper.selectPictureInfoList(pictureInfo);
        if (StringUtils.isEmpty(pictureInfos)) {
            return Collections.emptyList();
        }
        //获取到所有的分类编号
        List<String> categoryIds = pictureInfos.stream().map(PictureInfo::getCategoryId).distinct().collect(Collectors.toList());
        Map<String, String> categoryIdNameMap = pictureCategoryInfoService.list(new LambdaQueryWrapper<PictureCategoryInfo>()
                        .select(PictureCategoryInfo::getCategoryId, PictureCategoryInfo::getName)
                        .in(PictureCategoryInfo::getCategoryId, categoryIds))
                .stream().collect(Collectors.toMap(PictureCategoryInfo::getCategoryId, PictureCategoryInfo::getName));
        for (PictureInfo info : pictureInfos) {
            String pictureUrl = OssConfig.builderUrl(info.getPictureUrl(), info.getDnsUrl());
            info.setPictureUrl(pictureUrl);
            String thumbnailUrl = OssConfig.builderUrl(info.getThumbnailUrl(), info.getDnsUrl());
            info.setThumbnailUrl(thumbnailUrl);
            info.setCategoryName(categoryIdNameMap.get(info.getCategoryId()));
        }
        return pictureInfos;
    }

    /**
     * 新增图片信息
     *
     * @param pictureInfo 图片信息
     * @return 结果
     */
    @Override
    public int insertPictureInfo(PictureInfo pictureInfo) {
        pictureInfo.setCreateTime(DateUtils.getNowDate());
        return pictureInfoMapper.insertPictureInfo(pictureInfo);
    }

    /**
     * 修改图片信息
     *
     * @param pictureInfo 图片信息
     * @return 结果
     */
    @Override
    public int updatePictureInfo(PictureInfo pictureInfo) {
        pictureInfo.setUpdateTime(DateUtils.getNowDate());
        //删除缓存
        redisCache.deleteObject(PictureRedisConstants.PICTURE_PICTURE_DETAIL + pictureInfo.getPictureId());
        return pictureInfoMapper.updatePictureInfo(pictureInfo);
    }

    /**
     * 批量删除图片信息
     *
     * @param pictureIds 需要删除的图片信息主键
     * @return 结果
     */
    @Override
    public int deletePictureInfoByPictureIds(String[] pictureIds) {
        pictureTagRelInfoService.remove(new LambdaQueryWrapper<PictureTagRelInfo>().in(PictureTagRelInfo::getPictureId, (Object) pictureIds));
        return pictureInfoMapper.deletePictureInfoByPictureIds(pictureIds);
    }

    /**
     * 删除图片信息信息
     *
     * @param pictureId 图片信息主键
     * @return 结果
     */
    @Override
    public int deletePictureInfoByPictureId(String pictureId) {
        return pictureInfoMapper.deletePictureInfoByPictureId(pictureId);
    }

    //endregion
    @Override
    public QueryWrapper<PictureInfo> getQueryWrapper(PictureInfo pictureInfo) {
        QueryWrapper<PictureInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pictureInfo.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String pictureId = pictureInfo.getPictureId();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureId), "picture_id", pictureId);

        String name = pictureInfo.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name);

        String categoryId = pictureInfo.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId), "category_id", categoryId);

        Long picSize = pictureInfo.getPicSize();
        queryWrapper.eq(StringUtils.isNotNull(picSize), "pic_size", picSize);

        Long picWidth = pictureInfo.getPicWidth();
        queryWrapper.eq(StringUtils.isNotNull(picWidth), "pic_width", picWidth);

        Long picHeight = pictureInfo.getPicHeight();
        queryWrapper.eq(StringUtils.isNotNull(picHeight), "pic_height", picHeight);

        Double picScale = pictureInfo.getPicScale();
        queryWrapper.eq(StringUtils.isNotNull(picScale), "pic_scale", picScale);

        String picFormat = pictureInfo.getPicFormat();
        queryWrapper.eq(StringUtils.isNotEmpty(picFormat), "pic_format", picFormat);

        String userId = pictureInfo.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = pictureInfo.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date publishTime = pictureInfo.getPublishTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginPublishTime")) && StringUtils.isNotNull(params.get("endPublishTime")), "publish_time", params.get("beginpublishTime"), params.get("endpublishTime"));

        Date updateTime = pictureInfo.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String pictureStatus = pictureInfo.getPictureStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureStatus), "picture_status", pictureStatus);

        String spaceId = pictureInfo.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String folderId = pictureInfo.getFolderId();
        queryWrapper.eq(StringUtils.isNotEmpty(folderId), "folder_id", folderId);

        String isDelete = pictureInfo.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        Date deletedTime = pictureInfo.getDeletedTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginDeletedTime")) && StringUtils.isNotNull(params.get("endDeletedTime")), "deleted_time", params.get("beginDeletedTime"), params.get("endDeletedTime"));
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }


    @Override
    public List<PictureInfoVo> convertVoList(List<PictureInfo> pictureInfoList) {
        if (StringUtils.isEmpty(pictureInfoList)) {
            return Collections.emptyList();
        }
        return pictureInfoList.stream().map(PictureInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public PictureInfo selectPictureInfoNormalByPictureId(String pictureId) {
        return this.getOne(new LambdaQueryWrapper<PictureInfo>().eq(PictureInfo::getPictureId, pictureId).eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue()));
    }

    @Override
    public int userInsertPictureInfo(PictureInfo pictureInfo) {
        SpaceInfo spaceInfo = checkPictureAndSpace(pictureInfo);
        //查询分类是否存在
        PictureCategoryInfo categoryInfo = new PictureCategoryInfo();
        if (StringUtils.isNotEmpty(pictureInfo.getCategoryId())) {
            //查询分类是否存在
            categoryInfo = pictureCategoryInfoService.selectPictureCategoryInfoByCategoryId(pictureInfo.getCategoryId());
            ThrowUtils.throwIf(StringUtils.isNull(categoryInfo)
                            || !categoryInfo.getCategoryStatus().equals(PCategoryStatusEnum.CATEGORY_STATUS_0.getValue()),
                    HttpStatus.MOVED_PERM, "分类不存在或不可选");
            categoryInfo.setUsageCount(categoryInfo.getUsageCount() + 1);
        }
        //更新空间信息
        spaceInfo.setTotalCount(spaceInfo.getTotalCount() + 1);
        spaceInfo.setTotalSize(spaceInfo.getTotalSize() + pictureInfo.getPicSize());
        spaceInfo.setLastUpdateTime(new Date());
        //判断当前空间是否到达最大值 官方空间没有限制
        if (spaceInfo.getTotalCount() > spaceInfo.getMaxCount() && !spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_0.getValue())
                || spaceInfo.getTotalSize() > spaceInfo.getMaxSize() && !spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_0.getValue())) {
            throw new ServiceException("空间已满，无法上传图片", HttpStatus.NO_CONTENT);
        }
        pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_1.getValue());
        // 计算宽高比例
        double picScale = (double) pictureInfo.getPicWidth() / (double) pictureInfo.getPicHeight();
        //保留小数点后1位
        picScale = Double.parseDouble(String.format("%.1f", picScale));
        pictureInfo.setPicScale(picScale);
        pictureInfo.setPictureId(IdUtils.snowflakeId().toString());
        int i = pictureInfoMapper.insertPictureInfo(pictureInfo);

        //异步更新图片空间、标签、标签关联、分类
        PictureCategoryInfo finalCategoryInfo = categoryInfo;
        executorService.execute(() -> {
            implementPictureAdd(pictureInfo, spaceInfo, finalCategoryInfo);
        });
        //异步更新文件日志
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.updateNormalFileLog(pictureInfo));
        return i;
    }


    /**
     * description: 校验空间
     * author: YY
     * method: checkPictureAndSpace
     * date: 2025/4/11 15:32
     * param:
     * param: pictureInfo
     * return: com.lz.picture.model.domain.SpaceInfo
     **/
    private SpaceInfo checkPictureAndSpace(PictureInfo pictureInfo) {
        //如果传过来有图片id则校验图片
        if (StringUtils.isNotEmpty(pictureInfo.getPictureId())) {
            PictureInfo pictureInfoById = selectPictureInfoByPictureId(pictureInfo.getPictureId());
            ThrowUtils.throwIf(StringUtils.isNull(pictureInfoById), HttpStatus.NO_CONTENT, "图片不存在");
            pictureInfo.setUserId(pictureInfoById.getUserId());
            //如果图片已经发布不可以修改
            ThrowUtils.throwIf(pictureInfoById.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue()), "图片已发布，请勿重复操作");
        }
        //查询空间是否存在
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(pictureInfo.getSpaceId());
        if (StringUtils.isNull(spaceInfo) || !spaceInfo.getIsDelete().equals(CommonDeleteEnum.NORMAL.getValue())) {
            throw new ServiceException("空间不存在");
        }
        //如果空间是团队空间
        if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_1.getValue())) {
            //判断此用户是否对此空间有权限
            ThrowUtils.throwIf(!spaceAuthUtils.checkSpaceEditPerm(spaceInfo.getSpaceId()),
                    "您对此空间没有权限修改！！！");

            //判断文件夹是否存在且文件夹作者是自己
            if (StringUtils.isNotEmpty(pictureInfo.getFolderId()) && !pictureInfo.getFolderId().equals("0")) {
                SpaceFolderInfo spaceFolderInfo = spaceFolderInfoService.selectSpaceFolderInfoByFolderId(pictureInfo.getFolderId());
                //判断该空间是否有此文件夹
                ThrowUtils.throwIf(StringUtils.isNotNull(spaceFolderInfo) && !spaceFolderInfo.getSpaceId().equals(pictureInfo.getSpaceId()), HttpStatus.NO_CONTENT, "该空间没有此文件夹，无法上传图片");
            }
        } else if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_2.getValue())) {
            //如果空间个人空间，校验用户是否是作者
            //如果传过来图片ID并且自己不是图片作者
            ThrowUtils.throwIf(pictureInfo.getPictureId() != null && !pictureInfo.getUserId().equals(UserInfoSecurityUtils.getUserId()), "您不是该图片所有者，无法上传图片");
            //如果用户不是自己
            ThrowUtils.throwIf(!spaceInfo.getUserId().equals(pictureInfo.getUserId()), "您不是该空间所有者，无法上传图片");
            //判断文件夹是否存在且文件夹作者是自己
            if (StringUtils.isNotEmpty(pictureInfo.getFolderId()) && !pictureInfo.getFolderId().equals("0")) {
                SpaceFolderInfo spaceFolderInfo = spaceFolderInfoService.selectSpaceFolderInfoByFolderId(pictureInfo.getFolderId());
                ThrowUtils.throwIf(StringUtils.isNull(spaceFolderInfo), HttpStatus.NO_CONTENT, "文件夹不存在");
                ThrowUtils.throwIf(!spaceFolderInfo.getUserId().equals(pictureInfo.getUserId()), HttpStatus.NO_CONTENT, "您不是该文件夹所有者，无法上传图片");
                //判断该空间是否有此文件夹
                ThrowUtils.throwIf(!spaceFolderInfo.getSpaceId().equals(pictureInfo.getSpaceId()), HttpStatus.NO_CONTENT, "该空间没有此文件夹，无法上传图片");
            }
        } else if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_0.getValue())) {
            //判断用户是否是作者
            ThrowUtils.throwIf(pictureInfo.getPictureId() != null && !pictureInfo.getUserId().equals(UserInfoSecurityUtils.getUserId()), "您不是该图片所有者，无法上传图片");
            //判断是否添加文件夹
            ThrowUtils.throwIf(StringUtils.isNotEmpty(pictureInfo.getFolderId()), HttpStatus.NO_CONTENT, "官方空间图片不可添加文件夹！！！");
        } else {
            throw new ServiceException("空间类型错误");
        }
        if (spaceInfo.getOssType().equals(PSpaceOssTypeEnum.SPACE_OSS_TYPE_0.getValue())) {
            //是公共空间，使用官方域名,官方不指定域名，根据配置文件获取域名
            pictureInfo.setDnsUrl(null);
        } else {
            pictureInfo.setDnsUrl(pictureInfo.getDnsUrl());
        }
        return spaceInfo;
    }

    /**
     * description: 异步更新图片信息
     * author: YY
     * method: implementPictureAdd
     * date: 2025/4/11 15:14
     * param:
     * param: pictureInfo
     * param: spaceInfo
     * return: void
     **/
    private void implementPictureAdd(PictureInfo pictureInfo, SpaceInfo spaceInfo, PictureCategoryInfo categoryInfo) {
        //查询标签是否存在
        List<String> tags = pictureInfo.getTags();
        if (StringUtils.isEmpty(tags)) {
            tags = new ArrayList<>();
        }
        //校验标签长度，如果超过16，则截取
        tags.forEach(tag -> {
            if (tag.length() > 16) {
                tag = tag.substring(0, 16);
            }
        });
        List<PictureTagInfo> tagInfoList;
        if (StringUtils.isEmpty(tags)) {
            tagInfoList = new ArrayList<>();
        } else {
            tagInfoList = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>().in(PictureTagInfo::getName, tags));
        }
        //遍历两个标签，如果查询到的标签并且此标签为禁止状态，删除tags的标签
        for (PictureTagInfo tagInfo : tagInfoList) {
            if (tagInfo.getTagsStatus().equals(PTagStatusEnum.TAG_STATUS_1.getValue())) {
                tags.remove(tagInfo.getName());
                tagInfoList.remove(tagInfo);
            }
        }
        //图片新增的标签
        List<PictureTagInfo> addTagInfoList = new ArrayList<>();
        //遍历剩下的tagInfoList，如果标签不存在，则添加新的标签
        for (PictureTagInfo info : tagInfoList) {
            //如果包含说明数据库已经有此标签
            if (tags.contains(info.getName())) {
                PictureTagInfo pictureTagInfo = new PictureTagInfo();
                BeanUtils.copyBeanProp(pictureTagInfo, info);
                pictureTagInfo.setUsageCount(info.getUsageCount() + 1);
                addTagInfoList.add(pictureTagInfo);
                tags.remove(info.getName());
            }
        }
        for (String tag : tags) {
            PictureTagInfo pictureTagInfo = new PictureTagInfo();
            pictureTagInfo.setLookCount(0L);
            pictureTagInfo.setDownloadCount(0L);
            pictureTagInfo.setUserId(pictureInfo.getUserId());
            pictureTagInfo.setCreateTime(new Date());
            pictureTagInfo.setName(tag);
            pictureTagInfo.setTagsStatus(PTagStatusEnum.TAG_STATUS_0.getValue());
            pictureTagInfo.setUsageCount(1L);
            addTagInfoList.add(pictureTagInfo);
        }
        ArrayList<PictureTagRelInfo> pictureTagRelInfos = new ArrayList<>();
        Boolean execute = transactionTemplate.execute(result -> {
            if (StringUtils.isNotEmpty(addTagInfoList)) {
                pictureTagInfoService.saveOrUpdateBatch(addTagInfoList);
            }
            //插入关联信息
            String pictureName = pictureInfo.getName();
            addTagInfoList.forEach(tagInfo -> {
                PictureTagRelInfo rel = new PictureTagRelInfo();
                rel.setPictureId(pictureInfo.getPictureId());
                rel.setPictureName(pictureName);
                rel.setTagName(tagInfo.getName());
                rel.setTagId(tagInfo.getTagId()); // 这里使用回填的ID
                rel.setUserId(pictureInfo.getUserId());
                pictureTagRelInfos.add(rel);
            });
            pictureTagRelInfoService.saveBatch(pictureTagRelInfos);
            if (StringUtils.isNotEmpty(categoryInfo.getCategoryId())) {
                pictureCategoryInfoService.updateById(categoryInfo);
            }
//            System.out.println("spaceInfo = " + spaceInfo);
            return spaceInfoService.updateById(spaceInfo);
        });
    }

    @Override
    public UserPictureDetailInfoVo userSelectPictureInfoByPictureId(String pictureId, String userId) {
        //先查询缓存是否存在
        UserPictureDetailInfoVo userPictureDetailInfoVo = getUserPictureDetailInfoVo(pictureId);
        userPictureDetailInfoVo.setPictureUrl(null);

        //查询是否有行为，点赞、收藏
        isBehavior(pictureId, userId, userPictureDetailInfoVo);
        //如果图片不是公共且图片审核状态不是通过，且当前用户不是作者
        if (!userPictureDetailInfoVo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                && !userPictureDetailInfoVo.getUserId().equals(UserInfoSecurityUtils.getUserId())
                && !spaceAuthUtils.checkUserJoinSpace(userPictureDetailInfoVo.getSpaceId())) {
            throw new ServiceException("图片审核不通过，无法查看");
        }
        return userPictureDetailInfoVo;
    }

    /**
     * 是否有收藏
     *
     * @param pictureId
     * @param userId
     * @param userPictureDetailInfoVo
     */
    private void isBehavior(String pictureId, String userId, UserPictureDetailInfoVo userPictureDetailInfoVo) {
        String behaviorKey = PICTURE_USER_BEHAVIOR + userId + ":" + PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0.getValue() + ":" + pictureId;
        List<UserBehaviorInfoCache> userBehaviorInfoCaches = new ArrayList<>();
        if (redisCache.hasKey(behaviorKey)) {
            userBehaviorInfoCaches = redisCache.getCacheObject(behaviorKey);
        } else {
            List<UserBehaviorInfo> list = userBehaviorInfoService.list(new LambdaQueryWrapper<UserBehaviorInfo>()
                    .eq(UserBehaviorInfo::getUserId, userId)
                    .eq(UserBehaviorInfo::getTargetId, pictureId)
                    .eq(UserBehaviorInfo::getTargetType, PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0.getValue())
                    .in(UserBehaviorInfo::getBehaviorType, Arrays.asList(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_0.getValue(), PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_1.getValue())));
            if (StringUtils.isNotEmpty(list)) {
                userBehaviorInfoCaches = UserBehaviorInfoCache.objToVo(list);
            }
        }

        if (StringUtils.isNotEmpty(userBehaviorInfoCaches)) {
            for (UserBehaviorInfoCache info : userBehaviorInfoCaches) {
                if (info.getBehaviorType().equals(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_0.getValue())) {
                    userPictureDetailInfoVo.setIsLike(true);
                }
                if (info.getBehaviorType().equals(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_1.getValue())) {
                    userPictureDetailInfoVo.setIsCollect(true);
                }
            }
            redisCache.setCacheObject(behaviorKey, userBehaviorInfoCaches, PICTURE_USER_BEHAVIOR_EXPIRE_TIME, TimeUnit.MINUTES);
        }
    }

    /**
     * description: 获取用户图片详情
     * author: YY
     * method: getUserPictureDetailInfoVo
     * date: 2025/4/15 00:32
     * param:
     * param: pictureId
     * param: key
     * return: com.lz.picture.model.vo.pictureInfo.UserPictureDetailInfoVo
     **/
    private UserPictureDetailInfoVo getUserPictureDetailInfoVo(String pictureId) {
        //手动设置缓存，这里基本内部调用，spring没有托管，所以需要手动设置缓存
        String key = PICTURE_PICTURE_DETAIL + COMMON_SEPARATOR_CACHE + pictureId;
        if (redisCache.hasKey(key)) {
            return redisCache.getCacheObject(key);
        }
        UserPictureDetailInfoVo userPictureDetailInfoVo = new UserPictureDetailInfoVo();
        PictureInfo pictureInfo = this.getById(pictureId);
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo), HttpStatus.NO_CONTENT, "图片不存在");
        //查询分类
        PictureCategoryInfo categoryInfo = pictureCategoryInfoService.selectPictureCategoryInfoByCategoryId(pictureInfo.getCategoryId());
        BeanUtils.copyProperties(pictureInfo, userPictureDetailInfoVo);
        if (StringUtils.isNotNull(categoryInfo)) {
            userPictureDetailInfoVo.setCategoryName(categoryInfo.getName());
        }
        //构建图片url
        String pictureUrl = OssConfig.builderUrl(pictureInfo.getPictureUrl(), pictureInfo.getDnsUrl());
        userPictureDetailInfoVo.setPictureUrl(pictureUrl);
        String thumbnailUrl = OssConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl());
        userPictureDetailInfoVo.setThumbnailUrl(thumbnailUrl);
        //查询空间
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(pictureInfo.getSpaceId());
        //判断空间是否存在且是否是公共空间
        if (StringUtils.isNotNull(spaceInfo) && spaceInfo.getSpaceStatus().equals(PSpaceStatusEnum.SPACE_STATUS_0.getValue())) {
            //如果是则加入空间的信息
            userPictureDetailInfoVo.setSpaceId(spaceInfo.getSpaceId());
            userPictureDetailInfoVo.setSpaceName(spaceInfo.getSpaceName());
        } else {
            userPictureDetailInfoVo.setSpaceId(null);
        }
        //查询用户信息
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(pictureInfo.getUserId());
        if (StringUtils.isNotNull(userInfo)) {
            userPictureDetailInfoVo.setUserId(userInfo.getUserId());
            userPictureDetailInfoVo.setUserName(userInfo.getNickName());
            UserVo userVo = new UserVo();
            if (StringUtils.isNotEmpty(userInfo.getAvatarUrl())) {
                userInfo.setAvatarUrl(userInfo.getAvatarUrl() + "?x-oss-process=image/resize,p_10");
            }
            BeanUtils.copyProperties(userInfo, userVo);
            userPictureDetailInfoVo.setUserInfoVo(userVo);
        }
        //查询标签信息
        userPictureDetailInfoVo.setPictureTags(pictureTagRelInfoService.getPictureTagNames(pictureId));
        //转换更多信息
        if (StringUtils.isNotEmpty(pictureInfo.getMoreInfo())) {
            userPictureDetailInfoVo.setMoreInfo(JSON.parseObject(pictureInfo.getMoreInfo(), PictureMoreInfo.class));
        } else {
            userPictureDetailInfoVo.setMoreInfo(new PictureMoreInfo());
        }
        redisCache.setCacheObject(key, userPictureDetailInfoVo, PictureRedisConstants.PICTURE_PICTURE_DETAIL_EXPIRE_TIME, TimeUnit.SECONDS);
        return userPictureDetailInfoVo;
    }

    /**
     * description: 获取统计信息
     * author: YY
     * method: getPictureStatics
     * date: 2025/4/15 00:32
     * param:
     * param: pictureId
     * param: userPictureDetailInfoVo
     * return: void
     **/
    private void getPictureStatics(String pictureId, UserPictureDetailInfoVo userPictureDetailInfoVo) {
        UserBehaviorInfo behaviorInfo = new UserBehaviorInfo();
        behaviorInfo.setTargetId(pictureId);
        behaviorInfo.setTargetType(PUserBehaviorTargetTypeEnum.USER_BEHAVIOR_TARGET_TYPE_0.getValue());
        List<UserBehaviorInfoStaticVo> staticBehaviorInfo = userBehaviorInfoService.staticBehaviorInfo(behaviorInfo);
        if (StringUtils.isNotEmpty(staticBehaviorInfo)) {
            staticBehaviorInfo.forEach(behaviorInfoStaticVo -> {
                if (behaviorInfoStaticVo.getBehaviorType().equals(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_0.getValue())) {
                    userPictureDetailInfoVo.setLikeCount(behaviorInfoStaticVo.getTargetTypeCount());
                }
                if (behaviorInfoStaticVo.getBehaviorType().equals(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_1.getValue())) {
                    userPictureDetailInfoVo.setCollectCount(behaviorInfoStaticVo.getTargetTypeCount());
                }
                if (behaviorInfoStaticVo.getBehaviorType().equals(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_2.getValue())) {
                    userPictureDetailInfoVo.setShareCount(behaviorInfoStaticVo.getTargetTypeCount());
                }
            });
        }
        //如果没有就是0
        userPictureDetailInfoVo.setLikeCount(StringUtils.isNull(userPictureDetailInfoVo.getLikeCount()) ? 0 : userPictureDetailInfoVo.getLikeCount());
        userPictureDetailInfoVo.setCollectCount(StringUtils.isNull(userPictureDetailInfoVo.getCollectCount()) ? 0 : userPictureDetailInfoVo.getCollectCount());
        userPictureDetailInfoVo.setShareCount(StringUtils.isNull(userPictureDetailInfoVo.getShareCount()) ? 0 : userPictureDetailInfoVo.getShareCount());
    }

    @Override
    public void resetPictureInfoCacheByBehavior(String pictureId, String behaviorType, Boolean exist) {
        UserPictureDetailInfoVo userPictureDetailInfoVo = getUserPictureDetailInfoVo(pictureId);

        if (behaviorType.equals(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_0.getValue())) {
            if (exist) {
                userPictureDetailInfoVo.setLikeCount(userPictureDetailInfoVo.getLikeCount() - 1);
            } else {
                userPictureDetailInfoVo.setLikeCount(userPictureDetailInfoVo.getLikeCount() + 1);
            }
        } else if (behaviorType.equals(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_1.getValue())) {
            if (exist) {
                userPictureDetailInfoVo.setCollectCount(userPictureDetailInfoVo.getCollectCount() - 1);
            } else {
                userPictureDetailInfoVo.setCollectCount(userPictureDetailInfoVo.getCollectCount() + 1);
            }
        } else if (behaviorType.equals(PUserBehaviorTypeEnum.USER_BEHAVIOR_TYPE_2.getValue())) {
            userPictureDetailInfoVo.setShareCount(userPictureDetailInfoVo.getShareCount() + 1);
        }
        //这里不是更新数据，所以要重新手动更新缓存
        redisCache.setCacheObject(PICTURE_PICTURE_DETAIL + COMMON_SEPARATOR_CACHE + pictureId, userPictureDetailInfoVo, PICTURE_PICTURE_DETAIL_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public UserPictureDetailInfoVo userMySelectPictureInfoByPictureId(String pictureId, String userId) {
        UserPictureDetailInfoVo userPictureDetailInfoVo = getUserPictureDetailInfoVo(pictureId);
        //如果图片不是公共且图片审核状态不是通过，且当前用户不是作者，且不是空间的成员
        if (!userPictureDetailInfoVo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                && !userPictureDetailInfoVo.getUserId().equals(UserInfoSecurityUtils.getUserId())
                && !spaceAuthUtils.checkUserJoinSpace(userPictureDetailInfoVo.getSpaceId())) {
            throw new ServiceException("图片审核不通过，无法查看");
        }
        //说明是自己，则获取修改图片权限，并且授权密钥让用户可以访问图片
        String url = pictureDownloadManager.generateDownloadUrl(userPictureDetailInfoVo.getPictureUrl(), PICTURE_LOOK_ORIGINAL_TIMEOUT_VALUE);
        userPictureDetailInfoVo.setPictureUrl(url);
        return userPictureDetailInfoVo;
    }

    @CustomCacheEvict(keyPrefixes = {PICTURE_PICTURE_TABLE_PERSON, PICTURE_PICTURE_DETAIL},
            keyFields = {"picture.userId", "picture.pictureId"})
    @Override
    public UserPictureDetailInfoVo userUpdatePictureInfo(PictureInfo pictureInfo) {
        SpaceInfo spaceInfo = checkPictureAndSpace(pictureInfo);
        PictureCategoryInfo categoryInfo = new PictureCategoryInfo();
        //查询在数据库内内容
        PictureInfo pictureInfoDb = pictureInfoMapper.selectPictureInfoByPictureId(pictureInfo.getPictureId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfoDb), HttpStatus.MOVED_PERM, "图片不存在");
        if (StringUtils.isNotEmpty(pictureInfo.getCategoryId())) {
            //查询分类是否存在
            categoryInfo = pictureCategoryInfoService.selectPictureCategoryInfoByCategoryId(pictureInfo.getCategoryId());
            ThrowUtils.throwIf(StringUtils.isNull(categoryInfo), HttpStatus.MOVED_PERM, "分类不存在");
            //如果分类状态不是正常，且不等于在数据库内的分类
            ThrowUtils.throwIf(!categoryInfo.getCategoryStatus().equals(PCategoryStatusEnum.CATEGORY_STATUS_0.getValue())
                            && !categoryInfo.getCategoryId().equals(pictureInfoDb.getCategoryId()),
                    HttpStatus.MOVED_PERM, "当前分类不可选择");
        }
        //更新空间信息
        spaceInfo.setTotalSize(spaceInfo.getTotalSize() + pictureInfo.getPicSize() - pictureInfoDb.getPicSize());
        Date updateTime = new Date();
        spaceInfo.setLastUpdateTime(updateTime);
        //判断当前空间是否到达最大值 官方空间没有限制
        if (spaceInfo.getTotalCount() > spaceInfo.getMaxCount() && !spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_0.getValue())
                || spaceInfo.getTotalSize() > spaceInfo.getMaxSize() && !spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_0.getValue())) {
            throw new ServiceException("空间已满，无法上传图片", HttpStatus.MOVED_PERM);
        }
        //根据图片域名信息去除域名
        //判断空间是否是自定义存储
        if (spaceInfo.getOssType().equals(PSpaceOssTypeEnum.SPACE_OSS_TYPE_0.getValue())) {
            //不是判断图片域名是否正确
            if (pictureInfo.getPictureUrl().startsWith(OssConfig.getDnsUrl())) {
                //删除图片传过来的域名前缀
                pictureInfo.setPictureUrl(pictureInfo.getPictureUrl().replace(OssConfig.getDnsUrl(), ""));
            }
            if (pictureInfo.getThumbnailUrl().startsWith(OssConfig.getDnsUrl())) {
                //删除图片传过来的域名前缀
                pictureInfo.setThumbnailUrl(pictureInfo.getThumbnailUrl().replace(OssConfig.getDnsUrl(), ""));
            }
            pictureInfo.setDnsUrl(null);
        } else {
            //反之是用户自定义域名
            //TODO 判断、删除域名前缀、添加域名
            //删除路径参数
            pictureInfo.setPictureUrl(pictureInfo.getPictureUrl().split("\\?")[0]);
        }
        //删除路径参数
        pictureInfo.setPictureUrl(pictureInfo.getPictureUrl().split("\\?")[0]);
        //删除路径参数
        pictureInfo.setThumbnailUrl(pictureInfo.getThumbnailUrl().split("\\?")[0]);
        // 计算宽高比例
        double picScale = (double) pictureInfo.getPicWidth() / (double) pictureInfo.getPicHeight();
        //保留小数点后1位
        picScale = Double.parseDouble(String.format("%.1f", picScale));
        pictureInfo.setPicScale(picScale);
        pictureInfo.setUpdateTime(updateTime);
        pictureInfoMapper.updatePictureInfo(pictureInfo);
        //同步更新图片空间、标签、标签关联
        implementPictureUpdate(pictureInfo, spaceInfo);
        //更新分类信息
        if (StringUtils.isNotEmpty(categoryInfo.getCategoryId())) {
            pictureCategoryInfoService.updatePictureCategoryInfo(categoryInfo.getCategoryId(), pictureInfoDb.getCategoryId());
        }
        //异步更新文件日志的信息
//        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.updateFileLogInfo(pictureInfoDb, pictureInfo));
        //查询用户现在所拥有的信息
        return userMySelectPictureInfoByPictureId(pictureInfo.getPictureId(), pictureInfo.getUserId());
    }

    /**
     * 更新标签、空间
     *
     * @param pictureInfo
     * @param spaceInfo
     * @return void
     * @author YY
     * @method implementPictureUpdate
     * @date 2025/4/26 21:07
     **/
    private void implementPictureUpdate(PictureInfo pictureInfo, SpaceInfo spaceInfo) {
        //获取图片原有关联
        List<PictureTagRelInfo> tagRelInfos = pictureTagRelInfoService.list(new LambdaQueryWrapper<PictureTagRelInfo>()
                .eq(PictureTagRelInfo::getPictureId, pictureInfo.getPictureId()));
        //根据名称转换为map
        Map<String, PictureTagRelInfo> tagRelInfoMap = tagRelInfos.stream()
                .collect(Collectors.toMap(PictureTagRelInfo::getTagId, Function.identity()));
        List<String> tagRelTagIds = tagRelInfos
                .stream()
                .map(PictureTagRelInfo::getTagId)
                .toList();
        //原来拥有的标签
        List<String> tagRelTagNames = new ArrayList<>();
        //判断是否有关联
        if (StringUtils.isNotEmpty(tagRelTagIds)) {
            List<PictureTagInfo> tagInfos = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>()
                    .in(PictureTagInfo::getTagId, tagRelTagIds));
            tagRelTagNames = tagInfos.stream()
                    .map(PictureTagInfo::getName)
                    .toList();
        }
        //删除图片原有标签关联
        pictureTagRelInfoService.deletePictureTagRelInfoByPictureId(pictureInfo.getPictureId());
        //查询标签是否存在
        List<String> tags = pictureInfo.getTags();
        if (StringUtils.isEmpty(tags)) {
            tags = new ArrayList<>();
        }
        //校验标签长度，如果超过16，则截取
        tags.forEach(tag -> {
            if (tag.length() > 16) {
                tag = tag.substring(0, 16);
            }
        });
        //查询数据库内标签
        List<PictureTagInfo> tagInfoList;
        if (!StringUtils.isEmpty(tags)) {
            tagInfoList = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>().in(PictureTagInfo::getName, tags));
        } else {
            tagInfoList = new ArrayList<>();
        }
        //遍历两个标签，如果查询到的标签并且此标签为禁止状态，删除tags的标签
        for (PictureTagInfo tagInfo : tagInfoList) {
            if (tagInfo.getTagsStatus().equals(PTagStatusEnum.TAG_STATUS_1.getValue())) {
                tags.remove(tagInfo.getName());
                tagInfoList.remove(tagInfo);
            }
        }
        List<PictureTagInfo> addTagInfoList = new ArrayList<>();
        //遍历剩下的tagInfoList，如果标签不存在，则添加新的标签
        for (PictureTagInfo info : tagInfoList) {
            //如果包含说明数据库已经有此标签
            if (tags.contains(info.getName())) {
                PictureTagInfo pictureTagInfo = new PictureTagInfo();
                BeanUtils.copyBeanProp(pictureTagInfo, info);
                //如果标签之前有过关联这不需要给使用次数加1
                if (!tagRelTagNames.contains(info.getName())) {
                    pictureTagInfo.setUsageCount(info.getUsageCount() + 1);
                }
                addTagInfoList.add(pictureTagInfo);
                tags.remove(info.getName());
            }
        }
        for (String tag : tags) {
            PictureTagInfo pictureTagInfo = new PictureTagInfo();
            pictureTagInfo.setLookCount(0L);
            pictureTagInfo.setDownloadCount(0L);
            pictureTagInfo.setUserId(pictureInfo.getUserId());
            pictureTagInfo.setCreateTime(new Date());
            pictureTagInfo.setName(tag);
            pictureTagInfo.setTagsStatus(PTagStatusEnum.TAG_STATUS_0.getValue());
            pictureTagInfo.setUsageCount(1L);
            addTagInfoList.add(pictureTagInfo);
        }
        ArrayList<PictureTagRelInfo> pictureTagRelInfos = new ArrayList<>();
        Boolean execute = transactionTemplate.execute(result -> {
            if (StringUtils.isNotEmpty(addTagInfoList)) {
                pictureTagInfoService.saveOrUpdateBatch(addTagInfoList);
            }
            String pictureName = pictureInfo.getName();
            //插入关联信息
            addTagInfoList.forEach(tagInfo -> {
                PictureTagRelInfo rel = new PictureTagRelInfo();
                rel.setPictureName(pictureName);
                rel.setPictureId(pictureInfo.getPictureId());
                rel.setTagName(tagInfo.getName());
                rel.setTagId(tagInfo.getTagId()); // 这里使用回填的ID
                rel.setUserId(pictureInfo.getUserId());
                //判断是否之前有关联标签，如果有设置默认记录值下载、浏览、分享、点赞、收藏次数
                if (tagRelInfoMap.containsKey(tagInfo.getTagId())) {
                    PictureTagRelInfo tagRelInfo = tagRelInfoMap.get(tagInfo.getTagId());
                    rel.setLookCount(tagRelInfo.getLookCount());
                    rel.setCollectCount(tagRelInfo.getCollectCount());
                    rel.setLikeCount(tagRelInfo.getLikeCount());
                    rel.setShareCount(tagRelInfo.getShareCount());
                    rel.setDownloadCount(tagRelInfo.getDownloadCount());
                }
                pictureTagRelInfos.add(rel);
            });
            pictureTagRelInfoService.saveBatch(pictureTagRelInfos);
            return spaceInfoService.updateById(spaceInfo);
        });
    }

    @CustomCacheable(
            keyPrefix = PICTURE_SEARCH_RECOMMEND,
            expireTime = PICTURE_SEARCH_RECOMMEND_EXPIRE_TIME
    )
    @Override
    public List<PictureInfoSearchRecommendVo> getSearchRecommend() {
        Page<PictureInfo> page = new Page<>(1, 30);
        LambdaQueryWrapper<PictureInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                .orderByDesc(PictureInfo::getDownloadCount)
                .orderByDesc(PictureInfo::getShareCount)
                .orderByDesc(PictureInfo::getLikeCount)
                .orderByDesc(PictureInfo::getCollectCount)
                .orderByDesc(PictureInfo::getLookCount);

        List<PictureInfo> pictureInfos = this.page(page, queryWrapper).getRecords();
        for (PictureInfo pictureInfo : pictureInfos) {
            pictureInfo.setThumbnailUrl(OssConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl()) + "?x-oss-process=image/resize,p_" + PICTURE_INDEX_P_VALUE);
        }
        //转换为vo
        return PictureInfoSearchRecommendVo.objToVo(pictureInfos);
    }

    @CustomCacheable(keyPrefix = PICTURE_SEARCH_SUGGESTION,
            keyField = "name",
            expireTime = PICTURE_SEARCH_SUGGESTION_EXPIRE_TIME)
    @Override
    public List<PictureInfoSearchSuggestionVo> getSearchSuggestion(String name) {
        Page<PictureInfo> page = new Page<>(1, 15);
        LambdaQueryWrapper<PictureInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                .like(StringUtils.isNotEmpty(name), PictureInfo::getName, name)
                .orderByDesc(PictureInfo::getDownloadCount)
                .orderByDesc(PictureInfo::getShareCount)
                .orderByDesc(PictureInfo::getLikeCount)
                .orderByDesc(PictureInfo::getCollectCount)
                .orderByDesc(PictureInfo::getLookCount);

        List<PictureInfo> pictureInfos = this.page(page, queryWrapper).getRecords();
        return PictureInfoSearchSuggestionVo.objToVo(pictureInfos);
    }

    @CustomCacheable(keyPrefix = PICTURE_RECOMMEND_DETAIL,
            keyField = "request.pictureId",
            paginate = true,
            pageNumberField = "request.currentPage",
            pageSizeField = "request.pageSize",
            expireTime = PICTURE_RECOMMEND_DETAIL_EXPIRE_TIME)
    @Override
    public List<UserPictureInfoVo> getPictureInfoDetailRecommend(PictureInfoDetailRecommendRequest request) {
        request.setOffset(request.getCurrentPage() * request.getPageSize());
        // 在查询前清除分页设置
//        PageHelper.clearPage();
        //        System.out.println("pictureInfoRecommendRequest = " + pictureInfoRecommendRequest);
        List<PictureInfo> list = pictureInfoMapper.getPictureInfoDetailRecommend(request);
        list.forEach(pictureInfo -> {
            pictureInfo.setThumbnailUrl(OssConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl()));
        });
        return UserPictureInfoVo.objToVo(list);
    }

    @CustomCacheable(
            keyPrefix = PICTURE_QUERY_LIST,
            expireTime = PICTURE_QUERY_LIST_EXPIRE_TIME,
            paginate = true,
            useQueryParamsAsKey = true,
            pageNumberField = "request.pageNum",
            pageSizeField = "request.pageSize")
    @Override
    public List<UserRecommendPictureInfoVo> queryPictureInfoList(PictureQueryRequest request) {
        //如果分类ID不为空，需要查询他自己的分类下的图片
        ArrayList<String> categoryIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(request.getCategoryId())) {
            PictureCategoryInfo pictureCategoryInfo = new PictureCategoryInfo();
            pictureCategoryInfo.setParentId(request.getCategoryId());
            List<PictureCategoryInfo> pictureCategoryInfos = pictureCategoryInfoService.userSelectPictureCategoryInfoList(pictureCategoryInfo);
            categoryIds.add(request.getCategoryId());
            if (StringUtils.isNotEmpty(pictureCategoryInfos)) {
                pictureCategoryInfos.forEach(pictureCategoryInfo1 -> {
                    categoryIds.add(pictureCategoryInfo1.getCategoryId());
                });
            }
        }
        Page<PictureInfo> pictureInfoPage = new Page<>();
        //!!!currentPage一定要大于等于1
        pictureInfoPage.setCurrent(request.getPageNum());
        pictureInfoPage.setSize(request.getPageSize());
        LambdaQueryWrapper<PictureInfo> query = new LambdaQueryWrapper<PictureInfo>()
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                .eq(StringUtils.isNotEmpty(request.getSpaceId()), PictureInfo::getSpaceId, request.getSpaceId())
                .in(StringUtils.isNotEmpty(categoryIds), PictureInfo::getCategoryId, categoryIds)
                .like(StringUtils.isNotEmpty(request.getName()), PictureInfo::getName, request.getName());
        //构造排序
        if (StringUtils.isNotEmpty(request.getOrderByColumn())
                && Arrays.asList("publishTime", "shareCount", "collectCount", "likeCount", "lookCount").contains(request.getOrderByColumn())) {
            switch (request.getOrderByColumn()) {
                case "publishTime" -> query.orderBy(true, false, PictureInfo::getPublishTime);
                case "shareCount" -> query.orderBy(true, false, PictureInfo::getShareCount);
                case "collectCount" -> query.orderBy(true, false, PictureInfo::getCollectCount);
                case "likeCount" -> query.orderBy(true, false, PictureInfo::getLikeCount);
                case "lookCount" -> query.orderBy(true, false, PictureInfo::getLookCount);
            }
        } else {
            query.orderByDesc(PictureInfo::getDownloadCount, PictureInfo::getShareCount, PictureInfo::getCollectCount, PictureInfo::getLikeCount, PictureInfo::getLookCount);
        }
        Page<PictureInfo> pictureInfoList = this.page(pictureInfoPage, query);
        //构造url
        pictureInfoList.getRecords().forEach(pictureInfo -> {
            pictureInfo.setThumbnailUrl(OssConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl()) + "?x-oss-process=image/resize,p_" + PICTURE_INDEX_P_VALUE);
        });
        List<UserRecommendPictureInfoVo> userRecommendPictureInfoVos = UserRecommendPictureInfoVo.objToVo(pictureInfoList.getRecords());
        //防止空指针异常
        if (StringUtils.isEmpty(userRecommendPictureInfoVos)) {
            userRecommendPictureInfoVos = new ArrayList<>();
        }
        return userRecommendPictureInfoVos;
    }

    @Override
    public TableDataInfo listPictureInfoTable(UserPictureInfoQuery userPictureInfoQuery) {
        String jsonStr = JSON.toJSONString(userPictureInfoQuery);
        //查询缓存是否存在
        String keyData = PICTURE_PICTURE_TABLE_PERSON + userPictureInfoQuery.getUserId() + COMMON_SEPARATOR_CACHE +
                jsonStr;
        return getTableDataInfo(userPictureInfoQuery, keyData);
    }

    private TableDataInfo getTableDataInfo(UserPictureInfoQuery userPictureInfoQuery, String keyData) {
        //如果都存在，直接返回
        if (redisCache.hasKey(keyData)) {
            return redisCache.getCacheObject(keyData);
        }
        //构造查询条件
        Page<PictureInfo> pictureInfoPage = new Page<>();

        pictureInfoPage.setCurrent(userPictureInfoQuery.getPageNum());
        pictureInfoPage.setSize(userPictureInfoQuery.getPageSize());
        String beginCreateTime = ParamUtils.getSafeString(userPictureInfoQuery, ParamUtils.BEGIN_CREATE_TIME);
        String endCreateTime = ParamUtils.getSafeString(userPictureInfoQuery, ParamUtils.END_CREATE_TIME);
        //构造查询条件
        LambdaQueryWrapper<PictureInfo> lambdaQueryWrapper = new LambdaQueryWrapper<PictureInfo>()
                .like(StringUtils.isNotEmpty(userPictureInfoQuery.getName()), PictureInfo::getName, userPictureInfoQuery.getName())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getCategoryId()), PictureInfo::getCategoryId, userPictureInfoQuery.getCategoryId())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getPictureStatus()), PictureInfo::getPictureStatus, userPictureInfoQuery.getPictureStatus())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getUserId()), PictureInfo::getUserId, userPictureInfoQuery.getUserId())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getSpaceId()), PictureInfo::getSpaceId, userPictureInfoQuery.getSpaceId())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getFolderId()), PictureInfo::getFolderId, userPictureInfoQuery.getFolderId())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getPictureId()), PictureInfo::getPictureId, userPictureInfoQuery.getPictureId())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getIsDelete()), PictureInfo::getIsDelete, userPictureInfoQuery.getIsDelete())
                .apply(
                        StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                        "create_time between {0} and {1}",
                        beginCreateTime, endCreateTime);
        if (StringUtils.isNotEmpty(userPictureInfoQuery.getIsAsc())
                && StringUtils.isNotEmpty(userPictureInfoQuery.getOrderByColumn())
                && Arrays.asList("createTime", "picSize").contains(userPictureInfoQuery.getOrderByColumn())) {
            if (userPictureInfoQuery.getOrderByColumn().equals("picSize")) {
                lambdaQueryWrapper
                        .orderBy(true, userPictureInfoQuery.getIsAsc().equals("asc"), PictureInfo::getPicSize);
            }
            if (userPictureInfoQuery.getOrderByColumn().equals("createTime")) {
                lambdaQueryWrapper
                        .orderBy(true, userPictureInfoQuery.getIsAsc().equals("asc"), PictureInfo::getCreateTime);
            }
        } else {
            lambdaQueryWrapper
                    .orderBy(true, false, PictureInfo::getCreateTime);
        }
        Page<PictureInfo> page = this.page(pictureInfoPage, lambdaQueryWrapper);
        //如果为空，直接缓存，返回
        List<PictureInfo> records = page.getRecords();
        if (StringUtils.isEmpty(records)) {
            TableDataInfo tableDataInfo = new TableDataInfo(records, (int) page.getTotal());
            redisCache.setCacheObject(keyData, tableDataInfo, PICTURE_PICTURE_TABLE_DATE_EXPIRE_TIME, TimeUnit.SECONDS);
            return tableDataInfo;
        }
        //转换为 vo 并且转换地址
        //压缩图片
        List<PictureInfoTableVo> pictureInfoTableVos =
                pictureInfoPage.getRecords().stream()
                        .map(pictureInfo -> {
                            pictureInfo.setThumbnailUrl(OssConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl()) + "?x-oss-process=image/resize,p_" + PICTURE_COVER_P_VALUE);
                            return PictureInfoTableVo.objToVo(pictureInfo);
                        }).toList();

        //查询标签
        List<PictureTagRelInfo> tagRelInfos = pictureTagRelInfoService.list(new LambdaQueryWrapper<PictureTagRelInfo>()
                .in(PictureTagRelInfo::getPictureId, records.stream().map(PictureInfo::getPictureId).collect(Collectors.toList())));
        //标签关联转换为map pictureId-tagName
        Map<String, List<String>> tagRelMap = tagRelInfos.stream().filter(tagRelInfo -> StringUtils.isNotEmpty(tagRelInfo.getTagName())).collect(Collectors.groupingBy(PictureTagRelInfo::getPictureId, Collectors.mapping(PictureTagRelInfo::getTagName, Collectors.toList())));
        pictureInfoTableVos.forEach(pictureInfoTableVo -> {
            List<String> tags = tagRelMap.get(pictureInfoTableVo.getPictureId());
            if (StringUtils.isNotEmpty(tags)) {
                pictureInfoTableVo.setTags(tags);
            }
        });
        //查询分类
        //获取分类id
        List<String> categoryIds = records.stream().map(PictureInfo::getCategoryId).collect(Collectors.toList());
        List<PictureCategoryInfo> categoryInfos = pictureCategoryInfoService.list(new LambdaQueryWrapper<PictureCategoryInfo>()
                .in(PictureCategoryInfo::getCategoryId, categoryIds));
        Map<String, PictureCategoryInfo> categoryInfoMap = categoryInfos.stream().collect(Collectors.toMap(PictureCategoryInfo::getCategoryId, Function.identity()));
        pictureInfoTableVos.forEach(pictureInfoTableVo -> {
            PictureCategoryInfo pictureCategoryInfo = categoryInfoMap.get(pictureInfoTableVo.getCategoryId());
            if (StringUtils.isNotNull(pictureCategoryInfo)) {
                pictureInfoTableVo.setCategoryName(pictureCategoryInfo.getName());
            }
        });
        //查询空间
        List<SpaceInfo> spaceInfos = spaceInfoService.list(new LambdaQueryWrapper<SpaceInfo>()
                .in(SpaceInfo::getSpaceId, records.stream().map(PictureInfo::getSpaceId).collect(Collectors.toList())));
        Map<String, SpaceInfo> spaceInfoMap = spaceInfos.stream().collect(Collectors.toMap(SpaceInfo::getSpaceId, Function.identity()));
        pictureInfoTableVos.forEach(pictureInfoTableVo -> {
            SpaceInfo spaceInfo = spaceInfoMap.get(pictureInfoTableVo.getSpaceId());
            if (StringUtils.isNotNull(spaceInfo)) {
                pictureInfoTableVo.setSpaceName(spaceInfo.getSpaceName());
            }
        });
        //存入缓存并返回信息
        TableDataInfo tableDataInfo = new TableDataInfo(pictureInfoTableVos, (int) page.getTotal());
        redisCache.setCacheObject(keyData, tableDataInfo, PICTURE_PICTURE_TABLE_DATE_EXPIRE_TIME, TimeUnit.SECONDS);
        return tableDataInfo;
    }

    @CustomCacheEvict(keyPrefixes = {PICTURE_PICTURE_TABLE_PERSON, PICTURE_PICTURE_DETAIL},
            keyFields = {"picture.userId", "picture.pictureId"})
    @Override
    public int userUpdatePictureInfoName(PictureInfo pictureInfo) {
        //判断图片是否存在是否是作者
        PictureInfo pictureInfoDb = this.selectNormalPictureInfoByPictureId(pictureInfo.getPictureId());
        checkPictureAndSpace(pictureInfoDb);
        pictureInfoDb.setName(pictureInfo.getName());
        pictureInfoDb.setUpdateTime(DateUtils.getNowDate());
        this.deletePictureTableCacheBySpaceId(pictureInfoDb.getSpaceId());
        return this.updateById(pictureInfoDb) ? 1 : 0;
    }

    @Override
    public PictureInfo selectNormalPictureInfoByPictureId(String pictureId) {
        return this.getOne(new LambdaQueryWrapper<PictureInfo>()
                .eq(PictureInfo::getPictureId, pictureId)
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue()));
    }

    @Override
    public int userDeletePictureInfoByIds(String[] pictureIds) {
        ThrowUtils.throwIf(StringUtils.isEmpty(pictureIds), HttpStatus.BAD_REQUEST, "请选择要删除的图片");
        //查询到所有的图片，是正常的
        List<PictureInfo> pictureInfos = this.list(new LambdaQueryWrapper<PictureInfo>()
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .in(PictureInfo::getPictureId, List.of(pictureIds)));
        if (StringUtils.isEmpty(pictureInfos)) {
            return 0;
        }
        //获取当前用户
        String userId = UserInfoSecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        //新建一个map，spaceId-SpaceInfo
        Map<String, SpaceInfo> spaceInfoMap = new HashMap<>();
        //判断是否图片是自己的
        pictureInfos.forEach(pictureInfo -> {
            ThrowUtils.throwIf(!pictureInfo.getUserId().equals(userId), StringUtils.format("图片不存在或您不是作者:{}", pictureInfo.getName()));
            pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_1.getValue());
            pictureInfo.setIsDelete(CommonDeleteEnum.DELETED.getValue());
            pictureInfo.setDeletedTime(nowDate);
            this.deletePictureTableCacheBySpaceId(pictureInfo.getSpaceId());
            this.deletePictureTableCacheByUserId(pictureInfo.getUserId());
            if (!spaceInfoMap.containsKey(pictureInfo.getSpaceId())) {
                SpaceInfo spaceInfo = new SpaceInfo();
                spaceInfo.setTotalCount(1L);
                spaceInfo.setTotalSize(pictureInfo.getPicSize());
                spaceInfo.setSpaceId(pictureInfo.getSpaceId());
                spaceInfoMap.put(pictureInfo.getSpaceId(), spaceInfo);
            } else {
                spaceInfoMap.get(pictureInfo.getSpaceId()).setTotalCount(spaceInfoMap.get(pictureInfo.getSpaceId()).getTotalCount() + 1);
            }
        });
        //如果没有空间直接返回
        if (StringUtils.isEmpty(spaceInfoMap)) {
            List<BatchResult> batchResults = pictureInfoMapper.insertOrUpdate(pictureInfos);
            return StringUtils.isNotEmpty(batchResults) ? 1 : 0;
        }
        //从map中获取所有的空间编号
        List<String> spaceIds = spaceInfoMap.values().stream().map(SpaceInfo::getSpaceId).toList();
        List<SpaceInfo> spaceInfos = spaceInfoService.list(new LambdaQueryWrapper<SpaceInfo>().in(SpaceInfo::getSpaceId, spaceIds));
        //批量更新空间信息，从map里面拿出来
        spaceInfos.forEach(spaceInfo -> {
            SpaceInfo info = spaceInfoMap.get(spaceInfo.getSpaceId());
            spaceInfo.setTotalSize(spaceInfo.getTotalSize() - info.getTotalSize());
            spaceInfo.setTotalCount(spaceInfo.getTotalCount() - info.getTotalCount());
        });
        Integer execute = transactionTemplate.execute(res -> {
            spaceInfoService.updateBatchById(spaceInfos);
            return StringUtils.isNotEmpty(pictureInfoMapper.insertOrUpdate(pictureInfos)) ? 1 : 0;
        });
        return execute != null ? execute : 0;
    }

    @Override
    public TableDataInfo listPictureInfoTeamTable(UserPictureInfoQuery userPictureInfoQuery) {
        if (StringUtils.isEmpty(userPictureInfoQuery.getSpaceId())) {
            throw new ServiceException("空间编号不能为空");
        }
/*        SpaceMemberInfo spaceMemberInfo = spaceMemberInfoService.userIsJoinSpace(userPictureInfoQuery.getSpaceId(), UserInfoSecurityUtils.getUserId());
        if (StringUtils.isNull(spaceMemberInfo)) {
        }*/
        if (!spaceAuthUtils.checkUserJoinSpace(userPictureInfoQuery.getSpaceId())) {
            throw new ServiceException("您没有加入此空间");
        }
        String jsonStr = JSON.toJSONString(userPictureInfoQuery);
        //查询缓存是否存在
        String keyData = PICTURE_PICTURE_TABLE_SPACE + userPictureInfoQuery.getSpaceId() + COMMON_SEPARATOR_CACHE +
                jsonStr;
        return getTableDataInfo(userPictureInfoQuery, keyData);
    }

    @Override
    public TableDataInfo listMy(UserPictureInfoQuery userPictureInfoQuery) {
        //如果传过来spaceId，则查询此空间下的图片，如果没有查询用户自己的图片
        String spaceId = userPictureInfoQuery.getSpaceId();
        if (StringUtils.isEmpty(spaceId)) {
            userPictureInfoQuery.setUserId(UserInfoSecurityUtils.getUserId());
        } else {
            //查询空间是否是团队空间
            SpaceInfo spaceInfo = spaceInfoService.selectNormalSpaceInfoBySpaceId(spaceId);
            if (StringUtils.isNull(spaceInfo)) {
                throw new ServiceException("空间不存在");
            }
            if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_1.getValue())) {
                ThrowUtils.throwIf(!spaceAuthUtils.checkUserJoinSpace(spaceId) && !spaceInfo.getSpaceStatus().equals(PSpaceStatusEnum.SPACE_STATUS_0.getValue()), "您没有加入此空间");
            } else if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_2.getValue())) {
                ThrowUtils.throwIf(!spaceInfo.getUserId().equals(UserInfoSecurityUtils.getUserId()) && !spaceInfo.getSpaceStatus().equals(PSpaceStatusEnum.SPACE_STATUS_0.getValue()), "您没有权限查看此空间");
            } else {
                userPictureInfoQuery.setUserId(UserInfoSecurityUtils.getUserId());
            }
        }
        //构造查询条件
        Page<PictureInfo> pictureInfoPage = new Page<>();

        pictureInfoPage.setCurrent(userPictureInfoQuery.getPageNum());
        pictureInfoPage.setSize(userPictureInfoQuery.getPageSize());
        //构造查询条件
        LambdaQueryWrapper<PictureInfo> lambdaQueryWrapper = new LambdaQueryWrapper<PictureInfo>()
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getCategoryId()), PictureInfo::getCategoryId, userPictureInfoQuery.getCategoryId())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getUserId()), PictureInfo::getUserId, userPictureInfoQuery.getUserId())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getSpaceId()), PictureInfo::getSpaceId, userPictureInfoQuery.getSpaceId())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getFolderId()), PictureInfo::getFolderId, userPictureInfoQuery.getFolderId())
                .eq(StringUtils.isNotEmpty(userPictureInfoQuery.getIsDelete()), PictureInfo::getIsDelete, userPictureInfoQuery.getIsDelete())
                .orderBy(true, false, PictureInfo::getCreateTime);
        Page<PictureInfo> page = this.page(pictureInfoPage, lambdaQueryWrapper);
        List<MyPictureInfoVo> userPictureInfoVos = MyPictureInfoVo.objToVo(page.getRecords());
        //压缩图片
        for (MyPictureInfoVo vo : userPictureInfoVos) {
            vo.setThumbnailUrl(OssConfig.builderUrl(vo.getThumbnailUrl(), vo.getDnsUrl()) + "?x-oss-process=image/resize,p_" + PICTURE_INDEX_P_VALUE);
        }
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setRows(userPictureInfoVos);
        tableDataInfo.setTotal(page.getTotal());
        return tableDataInfo;
    }

    public void deletePictureTableCacheByUserId(String userId) {
        redisCache.deleteObjectsByPattern(PICTURE_PICTURE_TABLE_PERSON + userId + "*");
    }

    public void deletePictureTableCacheBySpaceId(String spaceId) {
        redisCache.deleteObjectsByPattern(PICTURE_PICTURE_TABLE_SPACE + spaceId + "*");
    }

    //region 热门图片 我一点一点猜 猜猜不出你的独白
    @Override
    public TableDataInfo getPictureInfoHot(PictureInfoHotRequest request) {
        switch (request.getType()) {
            case PICTURE_HOT_TOTAL -> {
                return statisticsInfoService.getStatisticsPictureInfo(PICTURE_STATISTICS_PICTURE_HOT_TOTAL_KEY, request);
            }
            case PICTURE_HOT_DAY -> {
                //如果是今天五点之前，获取上一期缓存，保证有数量可以拿
                Date date = DateUtils.getNowDate();
                String key = "";
                if (DateUtils.isAfterToday(date, 60 * 60 * 5)) {
                    key = pictureStatisticsUtil.getCurrentStatisticsDayKey(PICTURE_STATISTICS_PICTURE_HOT, date);
                } else {
                    key = pictureStatisticsUtil.getLastStatisticsDayKey(PICTURE_STATISTICS_PICTURE_HOT, date);
                }
                return statisticsInfoService.getStatisticsPictureInfo(key, request);
            }
            case PICTURE_HOT_WEEK -> {
                return statisticsInfoService.getStatisticsPictureInfo(pictureStatisticsUtil.getCurrentStatisticsWeekKey(PICTURE_STATISTICS_PICTURE_HOT, DateUtils.getNowDate()), request);
            }
            case PICTURE_HOT_MONTH -> {
                return statisticsInfoService.getStatisticsPictureInfo(pictureStatisticsUtil.getCurrentStatisticsMonthKey(PICTURE_STATISTICS_PICTURE_HOT, DateUtils.getNowDate()), request);
            }
            case PICTURE_HOT_YEAR -> {
                return statisticsInfoService.getStatisticsPictureInfo(pictureStatisticsUtil.getCurrentStatisticsYearKey(PICTURE_STATISTICS_PICTURE_HOT, DateUtils.getNowDate()), request);
            }
            default -> {
                return getPictureInfoByNew(request);
            }
        }
    }

    //获取图片总数
    @Override
    public Long getPictureCountByPictureStatus(String pictureStatus, String isDelete) {
        return this.count(new LambdaQueryWrapper<PictureInfo>()
                .eq(StringUtils.isNotEmpty(pictureStatus), PictureInfo::getPictureStatus, pictureStatus)
                .eq(StringUtils.isNotEmpty(isDelete), PictureInfo::getIsDelete, isDelete));
    }

    @CustomCacheable(
            keyPrefix = PICTURE_HOT_PICTURE,
            keyField = "request.type",
            useQueryParamsAsKey = true,
            expireTime = PICTURE_HOT_PICTURE_EXPIRE_TIME
    )
    private TableDataInfo getPictureInfoByNew(PictureInfoHotRequest request) {
        Page<PictureInfo> pictureInfoPage = new Page<>();
        pictureInfoPage.setCurrent(request.getPageNum());
        pictureInfoPage.setSize(request.getPageSize());
        Page<PictureInfo> pictureInfoList = this.page(pictureInfoPage, new LambdaQueryWrapper<PictureInfo>()
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                .orderByDesc(PictureInfo::getPublishTime)
        );
        //构造url
        pictureInfoList.getRecords().forEach(pictureInfo -> {
            pictureInfo.setThumbnailUrl(OssConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl()) + "?x-oss-process=image/resize,p_" + PICTURE_INDEX_P_VALUE);
        });
        List<UserRecommendPictureInfoVo> userRecommendPictureInfoVos = UserRecommendPictureInfoVo.objToVo(pictureInfoList.getRecords());
        //防止空指针异常
        if (StringUtils.isEmpty(userRecommendPictureInfoVos)) {
            userRecommendPictureInfoVos = new ArrayList<>();
        }
        Long pictureCountByPictureStatus = getPictureCountByPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue(), CommonDeleteEnum.NORMAL.getValue());
        return new TableDataInfo(userRecommendPictureInfoVos, Math.toIntExact(pictureCountByPictureStatus));
    }

    /**
     * 获取热门图片
     *
     * @param request 请求参数
     * @return 表格
     */
    private TableDataInfo getPictureInfoByHotTotal(PictureQueryRequest request) {
        List<UserRecommendPictureInfoVo> recommentHotPictureInfoList = queryPictureInfoList(request);
        //遍历图片压缩图片
        for (UserRecommendPictureInfoVo vo : recommentHotPictureInfoList) {
            vo.setThumbnailUrl(vo.getThumbnailUrl() + "?x-oss-process=image/resize,p_" + PICTURE_INDEX_P_VALUE);
        }
        Long pictureCountByPictureStatus = getPictureCountByPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue(), CommonDeleteEnum.NORMAL.getValue());
        return new TableDataInfo(recommentHotPictureInfoList, Math.toIntExact(pictureCountByPictureStatus));
    }
    //endregion

    // region 图片下载操作
    @Override
    public PictureInfoDto verifyPictureInfo(String pictureId, String userId, String downloadType) {
        //查询图片信息
        PictureInfo pictureInfo = pictureInfoMapper.selectPictureInfoByPictureId(pictureId);
        //判断图片1、是否存在，2、是否是作者，如果不是是否是正常，如果也不是判断是否不是空间成员，如果都false判断是否删除
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo)
                        || ((
                        !pictureInfo.getUserId().equals(userId)
                                && pictureInfo.getPictureStatus().equals(PSpaceStatusEnum.SPACE_STATUS_1.getValue())
                                && !spaceAuthUtils.checkUserJoinSpace(pictureInfo.getSpaceId())
                )
                        || pictureInfo.getIsDelete().equals(CommonDeleteEnum.DELETED.getValue())),
                "图片不存在");
        //所需总积分
        String moreInfo = pictureInfo.getMoreInfo();
        PictureMoreInfo pictureMoreInfo = new PictureMoreInfo();
        if (StringUtils.isNotEmpty(moreInfo)) {
            pictureMoreInfo = JSON.parseObject(moreInfo, PictureMoreInfo.class);
        }
        Long totalPoints = 0L;
        if (StringUtils.isNotNull(pictureMoreInfo.getPointsNeed())) {
            totalPoints = pictureMoreInfo.getPointsNeed();
        }
        pictureMoreInfo.setPointsNeed(totalPoints);
        //校验用户当前是否输入密码,如果需要积分就必须输入
        ThrowUtils.throwIf(totalPoints > 0 && accountInfoService.getVerifyPassword(userId) != 1, "请输入密码");
        executeDownloadPicture(pictureId, userId, pictureInfo, totalPoints, downloadType);
        PictureInfoDto pictureInfoDto = PictureInfoDto.objToDto(pictureInfo);
        pictureInfoDto.setPictureMoreInfo(pictureMoreInfo);
        return pictureInfoDto;
    }

    @Override
    public PictureInfoDto verifyPictureInfoByMy(String pictureId, String userId) {
        PictureInfo pictureInfo = this.selectNormalPictureInfoByPictureId(pictureId);
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo), "图片不存在");
        //判断图片1、是否存在，2、是否是作者，如果不是是否是正常，如果也不是判断是否不是空间成员，如果都false判断是否删除
        ThrowUtils.throwIf(((
                        !pictureInfo.getUserId().equals(userId)
                                && pictureInfo.getPictureStatus().equals(PSpaceStatusEnum.SPACE_STATUS_1.getValue())
                                && !spaceAuthUtils.checkUserJoinSpace(pictureInfo.getSpaceId())
                )
                        || pictureInfo.getIsDelete().equals(CommonDeleteEnum.DELETED.getValue())),
                "图片不存在");
        return PictureInfoDto.objToDto(pictureInfo);
    }

    private PictureInfo executeDownloadPicture(String pictureId, String userId, PictureInfo pictureInfo, Long totalPoints, String downloadType) {
        //下载记录
        PictureDownloadLogInfo pictureDownloadLogInfo = new PictureDownloadLogInfo();
        pictureDownloadLogInfo.setUserId(userId);
        pictureDownloadLogInfo.setPictureId(pictureId);
        pictureDownloadLogInfo.setCategoryId(pictureInfo.getCategoryId());
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();

        //查询标签信息
        pictureDownloadLogInfo.setTags(pictureTagRelInfoService.getPictureTagNamesStr(pictureId));
        pictureDownloadLogInfo.setSpaceId(pictureInfo.getSpaceId());
        pictureDownloadLogInfo.setPictureName(pictureInfo.getName() + "." + pictureInfo.getPicFormat());
        if (StringUtils.isEmpty(pictureInfo.getDnsUrl())) {
            pictureDownloadLogInfo.setThumbnailUrl(pictureInfo.getThumbnailUrl());
        } else {
            pictureDownloadLogInfo.setThumbnailUrl(pictureInfo.getDnsUrl() + pictureInfo.getThumbnailUrl());
        }
        pictureDownloadLogInfo.setPointsCost(totalPoints);
        pictureDownloadLogInfo.setCreateTime(DateUtils.getNowDate());
        pictureDownloadLogInfo.setDownloadStatus(PDownloadStatusEnum.DOWNLOAD_STATUS_0.getValue());
        pictureDownloadLogInfo.setDownloadType(downloadType);
        pictureDownloadLogInfo.setReferSource(PDownloadReferSourceEnum.DOWNLOAD_REFER_SOURCE_1.getValue());
        pictureDownloadLogInfo.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue());
        BeanUtils.copyProperties(deviceInfo, pictureDownloadLogInfo);
        //判断本人是否是作者，是否加入空间
        if (pictureInfo.getUserId().equals(userId) || spaceAuthUtils.checkUserJoinSpace(pictureInfo.getSpaceId())) {
            pictureDownloadLogInfo.setPointsCost(0L);
            //是作者
            //如果已经下载过就不再新增下载记录
            List<PictureDownloadLogInfo> list = pictureDownloadLogInfoService.list(new LambdaQueryWrapper<PictureDownloadLogInfo>()
                    .eq(PictureDownloadLogInfo::getUserId, userId)
                    .eq(PictureDownloadLogInfo::getPictureId, pictureId));
            if (StringUtils.isNotEmpty(list)) {
                return pictureInfo;
            }
            pictureDownloadLogInfo.setAuthorProportion(BigDecimal.valueOf(0.0));
            pictureDownloadLogInfo.setOfficialProportion(BigDecimal.valueOf(0.0));
            pictureDownloadLogInfo.setSpaceProportion(BigDecimal.valueOf(0.0));
            pictureDownloadLogInfo.setScore(0.0);
            pictureDownloadLogInfoService.save(pictureDownloadLogInfo);
            return pictureInfo;
        }
        //不是作者 需要更新账号积分、积分使用记录、下载记录、如果不是免费需要给作者充值
        //判断用户积分是否足够 查询用户账户是否存在，存在判断积分
        AccountInfo accountInfo = accountInfoService.selectAccountInfoByUserId(userId);

        ThrowUtils.throwIf(StringUtils.isNull(accountInfo)
                || accountInfo.getPointsBalance() < totalPoints, "积分不足");

        //如果大于等于10才分成积分,且必须是10的倍数
        if (totalPoints > 0) {
            //用户积分消费
            pointsUsageLogInfoService.updateAccountByPointsRechargeInfo(
                    userId,
                    null,
                    PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1.getValue(),
                    PoPointsUsageTypeEnum.POINTS_USAGE_TYPE_0.getValue(),
                    pictureId,
                    -totalPoints,
                    deviceInfo);
            //积分大于等于10、10的倍数才分成
            if (totalPoints >= 10 && totalPoints % 10 == 0) {
                //获取官方、空间比例
                double authorProportion = 1 - PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_VALUE - PICTURE_DOWNLOAD_SPACE_PROPORTION_VALUE;
                pictureDownloadLogInfo.setPointsAuthorGain((long) (totalPoints * authorProportion));
                pictureDownloadLogInfo.setPointsOfficialGain((long) (totalPoints * PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_VALUE));
                pictureDownloadLogInfo.setPointsSpaceGain((long) (totalPoints * PICTURE_DOWNLOAD_SPACE_PROPORTION_VALUE));
                pictureDownloadLogInfo.setAuthorProportion(BigDecimal.valueOf(authorProportion));
                pictureDownloadLogInfo.setOfficialProportion(BigDecimal.valueOf(PICTURE_DOWNLOAD_OFFICIAL_PROPORTION_VALUE));
                pictureDownloadLogInfo.setSpaceProportion(BigDecimal.valueOf(PICTURE_DOWNLOAD_SPACE_PROPORTION_VALUE));
                //作者获取积分记录
                pointsUsageLogInfoService.updateAccountByPointsRechargeInfo(
                        pictureInfo.getUserId(),
                        userId,
                        PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_2.getValue(),
                        PoPointsUsageTypeEnum.POINTS_USAGE_TYPE_0.getValue(),
                        pictureId,
                        pictureDownloadLogInfo.getPointsAuthorGain(),
                        deviceInfo
                );
                //发送消息提醒作者赚取积分
            /*
                {
                   "pictureName":"YY",
                   "points":"10000",
                   "createTime":"2025-05-26 10:11:12"
                }
             */
                HashMap<String, String> params = new HashMap<>();
                params.put("points", String.valueOf(pictureDownloadLogInfo.getPointsAuthorGain()));
                params.put("pictureName", pictureInfo.getName());
                params.put("createTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, pictureDownloadLogInfo.getCreateTime()));
                UserAsyncManager.me().execute(InformInfoAsyncFactory.sendInform(
                        pictureInfo.getUserId(),
                        DOWNLOAD_PICTURE_AUTHOR_PROPORTION,
                        null,
                        CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue(),
                        UInformTypeEnum.INFORM_TYPE_0.getValue(),
                        params
                ));
            }
        }
        //添加分数
        pictureDownloadLogInfo.setScore(PICTURE_STATISTICS_HOT_BEHAVIOR_SCORE_DOWNLOAD_VALUE);
        pictureStatisticsUtil.pictureHotStatisticsIncrementScore(pictureInfo.getPictureId(), pictureDownloadLogInfo.getScore());
        pictureDownloadLogInfoService.save(pictureDownloadLogInfo);
        //发送消息
        HashMap<String, String> params = new HashMap<>();
        /*
         {
             "points":100,
             "pictureName":"懒羊羊呀懒羊羊",
             "createTime":"2025-05-25 10:14:15"
         }
         */
        params.put("points", String.valueOf(totalPoints));
        params.put("pictureName", pictureInfo.getName());
        params.put("createTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, pictureDownloadLogInfo.getCreateTime()));
        UserAsyncManager.me().execute(InformInfoAsyncFactory.sendInform(
                userId,
                DOWNLOAD_PICTURE,
                null,
                CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue(),
                UInformTypeEnum.INFORM_TYPE_0.getValue(),
                params));
        //更新行为
        PictureAsyncManager.me().execute(PictureRecommendAsyncFactory.insertUserInterestModel(userId, PICTURE_RECOMMEND_MODEL_DOWNLOAD_TYPE));
        return pictureInfo;
    }

    @Override
    public PictureInfoDto verifyPictureInfoByLog(PictureDownloadLogInfoRequest pictureDownloadLogInfoRequest) {
        //查询到图片记录
        PictureDownloadLogInfo pictureDownloadLogInfo = pictureDownloadLogInfoService.selectPictureDownloadLogInfoByDownloadId(pictureDownloadLogInfoRequest.getDownloadId());
        //是否不存在
        ThrowUtils.throwIf(StringUtils.isNull(pictureDownloadLogInfo) ||
                !pictureDownloadLogInfo.getUserId().equals(pictureDownloadLogInfoRequest.getUserId()), "此下载记录不属于您！！！");
        //查询图片是否存在
        PictureInfo pictureInfo = this.selectPictureInfoByPictureId(pictureDownloadLogInfo.getPictureId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo) || !CommonDeleteEnum.NORMAL.getValue().equals(pictureInfo.getIsDelete()), "此图片不存在！！！");
        //所需总积分
        String moreInfo = pictureInfo.getMoreInfo();
        PictureMoreInfo pictureMoreInfo = new PictureMoreInfo();
        if (StringUtils.isNotEmpty(moreInfo)) {
            pictureMoreInfo = JSON.parseObject(moreInfo, PictureMoreInfo.class);
        }
        Long totalPoints = 0L;
        if (StringUtils.isNotNull(pictureMoreInfo.getPointsNeed())) {
            totalPoints = pictureMoreInfo.getPointsNeed();
        }
        pictureMoreInfo.setPointsNeed(totalPoints);
 /*       //如果当前之前图片下载是免费，但当前图片需要积分，则抛出异常表示此图片当前需要积分，不可从此记录下载
        ThrowUtils.throwIf(StringUtils.isNotNull(pictureMoreInfo.getPointsNeed())
                        && totalPoints != 0 && pictureDownloadLogInfo.getPointsCost() == 0,
                "当前图片已经设置积分或者金额，需要您重新下载！！！");*/
        PictureInfoDto pictureInfoDto = PictureInfoDto.objToDto(pictureInfo);
        pictureInfoDto.setPictureMoreInfo(pictureMoreInfo);
        return pictureInfoDto;
    }
    //endregion
}
