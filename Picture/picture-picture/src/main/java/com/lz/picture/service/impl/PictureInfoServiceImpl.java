package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.config.OssConfig;
import com.lz.common.constant.HttpStatus;
import com.lz.common.constant.redis.PictureRedisConstants;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.mapper.PictureInfoMapper;
import com.lz.picture.model.domain.*;
import com.lz.picture.model.enums.*;
import com.lz.picture.model.vo.pictureInfo.PictureInfoVo;
import com.lz.picture.model.vo.pictureInfo.UserPictureDetailInfoVo;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoStaticVo;
import com.lz.picture.service.*;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.vo.userInfo.UserVo;
import com.lz.user.service.IUserInfoService;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.config.UserConfigKeyConstants.*;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_USER_BEHAVIOR;

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
    private IConfigInfoService configInfoService;

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
    private PictureUploadManager pictureUploadManager;

    @Resource
    private OssConfig ossConfig;

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
        String pictureUrl = builderPictureUrl(pictureInfo.getPictureUrl(), pictureInfo.getDnsUrl());
        pictureInfo.setPictureUrl(pictureUrl);
        String thumbnailUrl = builderPictureUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl());
        pictureInfo.setThumbnailUrl(thumbnailUrl);
        return pictureInfo;
    }

    /**
     * 查询图片信息列表
     *
     * @param pictureInfo 图片信息
     * @return 图片信息
     */
    @Override
    public List<PictureInfo> selectPictureInfoList(PictureInfo pictureInfo) {
        List<PictureInfo> pictureInfos = pictureInfoMapper.selectPictureInfoList(pictureInfo);
        for (PictureInfo info : pictureInfos) {
            String pictureUrl = builderPictureUrl(info.getPictureUrl(), info.getDnsUrl());
            info.setPictureUrl(pictureUrl);
            String thumbnailUrl = builderPictureUrl(info.getThumbnailUrl(), info.getDnsUrl());
            info.setThumbnailUrl(thumbnailUrl);
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

        Long pointsNeed = pictureInfo.getPointsNeed();
        queryWrapper.eq(StringUtils.isNotNull(pointsNeed), "points_need", pointsNeed);

        String userId = pictureInfo.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = pictureInfo.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date editTime = pictureInfo.getEditTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginEditTime")) && StringUtils.isNotNull(params.get("endEditTime")), "edit_time", params.get("beginEditTime"), params.get("endEditTime"));

        Date updateTime = pictureInfo.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String pictureStatus = pictureInfo.getPictureStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(pictureStatus), "picture_status", pictureStatus);

        Long reviewStatus = pictureInfo.getReviewStatus();
        queryWrapper.eq(StringUtils.isNotNull(reviewStatus), "review_status", reviewStatus);

        Long reviewUserId = pictureInfo.getReviewUserId();
        queryWrapper.eq(StringUtils.isNotNull(reviewUserId), "review_user_id", reviewUserId);

        Date reviewTime = pictureInfo.getReviewTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginReviewTime")) && StringUtils.isNotNull(params.get("endReviewTime")), "review_time", params.get("beginReviewTime"), params.get("endReviewTime"));

        String spaceId = pictureInfo.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String folderId = pictureInfo.getFolderId();
        queryWrapper.eq(StringUtils.isNotEmpty(folderId), "folder_id", folderId);

        String isDelete = pictureInfo.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        Date deletedTime = pictureInfo.getDeletedTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginDeletedTime")) && StringUtils.isNotNull(params.get("endDeletedTime")), "deleted_time", params.get("beginDeletedTime"), params.get("endDeletedTime"));

        return queryWrapper;
    }


    @Override
    public List<PictureInfoVo> convertVoList(List<PictureInfo> pictureInfoList) {
        if (StringUtils.isEmpty(pictureInfoList)) {
            return Collections.emptyList();
        }
        return pictureInfoList.stream().map(PictureInfoVo::objToVo).collect(Collectors.toList());
    }

    //TODO 更新图库信息
    @Override
    public int userInsertPictureInfo(PictureInfo pictureInfo) {
        SpaceInfo spaceInfo = checkSpace(pictureInfo);
        //更新空间信息
        spaceInfo.setTotalCount(spaceInfo.getTotalCount() + 1);
        spaceInfo.setTotalSize(spaceInfo.getTotalSize() + pictureInfo.getPicSize());
        spaceInfo.setLastUpdateTime(new Date());
        //判断当前空间是否到达最大值 官方空间没有限制
        if (spaceInfo.getTotalCount() > spaceInfo.getMaxCount() && !spaceInfo.getSpaceType().equals(PSpaceType.SPACE_TYPE_0.getValue())
                || spaceInfo.getTotalSize() > spaceInfo.getMaxSize() && !spaceInfo.getSpaceType().equals(PSpaceType.SPACE_TYPE_0.getValue())) {
            throw new ServiceException("空间已满，无法上传图片");
        }
        //获取积分最大值和最小值
        String pointsNeedMax = configInfoService.getConfigInfoInCache(PICTURE_POINTS_MAX);
        String pointsNeedMin = configInfoService.getConfigInfoInCache(PICTURE_POINTS_MIN);
        //判断积分是否比最小值大最大值小
        if (!(Long.parseLong(pointsNeedMax) >= pictureInfo.getPointsNeed() && pictureInfo.getPointsNeed() >= Long.parseLong(pointsNeedMin))) {
            throw new ServiceException(StringUtils.format("图片所需积分不在范围内，最小值：{}，最大值：{}", pointsNeedMin, pointsNeedMax));
        }
        //判断是否是十的倍数
        if (pictureInfo.getPointsNeed() % 10 != 0) {
            throw new ServiceException("图片体积必须是10的倍数");
        }
        // 计算宽高比例
        double picScale = (double) pictureInfo.getPicWidth() / (double) pictureInfo.getPicHeight();
        //保留小数点后1位
        picScale = Double.parseDouble(String.format("%.1f", picScale));
        pictureInfo.setPicScale(picScale);
        pictureInfo.setReviewStatus(Long.parseLong(PPictureReviewStatus.PICTURE_REVIEW_STATUS_0.getValue()));
        pictureInfo.setPictureId(IdUtils.snowflakeId().toString());
        int i = pictureInfoMapper.insertPictureInfo(pictureInfo);
        //异步更新图片空间、标签、标签关联
        executorService.execute(() -> {
            implementPictureAdd(pictureInfo, spaceInfo);
        });
        return i;
    }


    /**
     * description: 校验空间
     * author: YY
     * method: checkSpace
     * date: 2025/4/11 15:32
     * param:
     * param: pictureInfo
     * return: com.lz.picture.model.domain.SpaceInfo
     **/
    private SpaceInfo checkSpace(PictureInfo pictureInfo) {
        //查询分类是否存在
        PictureCategoryInfo categoryInfo = pictureCategoryInfoService.selectPictureCategoryInfoByCategoryId(pictureInfo.getCategoryId());
        ThrowUtils.throwIf(StringUtils.isNull(categoryInfo), HttpStatus.NO_CONTENT, "分类不存在");
        //查询空间是否存在
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(pictureInfo.getSpaceId());
        if (StringUtils.isNull(spaceInfo) || !spaceInfo.getIsDelete().equals(CommonDeleteEnum.NORMAL.getValue())) {
            throw new ServiceException("空间不存在");
        }
        //如果空间不是公共的，则需要判断用户是否是空间所有者
        //TODO 后续团队空间判断不一样
        if (!spaceInfo.getSpaceType().equals(PSpaceType.SPACE_TYPE_0.getValue()) && !spaceInfo.getUserId().equals(pictureInfo.getUserId())) {
            throw new ServiceException("您不是该空间所有者，无法上传图片");
        }
        //判断文件夹是否存在且文件夹作者是自己
        //TODO 后续团队空间判断不一样
        if (StringUtils.isNotEmpty(pictureInfo.getFolderId())) {
            SpaceFolderInfo spaceFolderInfo = spaceFolderInfoService.selectSpaceFolderInfoByFolderId(pictureInfo.getFolderId());
            if (StringUtils.isNull(spaceFolderInfo)) {
                throw new ServiceException("文件夹不存在");
            }
            if (!spaceFolderInfo.getUserId().equals(pictureInfo.getUserId())) {
                throw new ServiceException("您不是该文件夹所有者，无法上传图片");
            }
        }
        //判断空间是否是公共
        if (spaceInfo.getOssType().equals(PSpaceOssType.SPACE_OSS_TYPE_0.getValue())) {
            //是公共空间，使用官方域名,官方不指定域名，根据配置文件获取域名
            pictureInfo.setDnsUrl(null);
        } else {
            //TODO 从空间配置获取域名
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
    private void implementPictureAdd(PictureInfo pictureInfo, SpaceInfo spaceInfo) {
        //查询标签是否存在
        List<String> tags = pictureInfo.getTags();
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
            if (tagInfo.getTagsStatus().equals(PTagStatus.TAG_STATUS_1.getValue())) {
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
            pictureTagInfo.setTagsStatus(PTagStatus.TAG_STATUS_0.getValue());
            pictureTagInfo.setUsageCount(1L);
            addTagInfoList.add(pictureTagInfo);
        }
        ArrayList<PictureTagRelInfo> pictureTagRelInfos = new ArrayList<>();
        Boolean execute = transactionTemplate.execute(result -> {
            if (StringUtils.isNotEmpty(addTagInfoList)) {
                pictureTagInfoService.saveOrUpdateBatch(addTagInfoList);
            }
            //插入关联信息
            addTagInfoList.forEach(tagInfo -> {
                PictureTagRelInfo rel = new PictureTagRelInfo();
                rel.setPictureId(pictureInfo.getPictureId());
                rel.setTagId(tagInfo.getTagId()); // 这里使用回填的ID
                pictureTagRelInfos.add(rel);
            });
            pictureTagRelInfoService.saveBatch(pictureTagRelInfos);
            return spaceInfoService.updateById(spaceInfo);
        });
    }

    @Override
    public UserPictureDetailInfoVo userSelectPictureInfoByPictureId(String pictureId, String userId) {
        //先查询缓存是否存在
        String key = PictureRedisConstants.PICTURE_PICTURE_DETAIL + pictureId;
        UserPictureDetailInfoVo userPictureDetailInfoVo = null;
        if (redisCache.hasKey(key)) {
            userPictureDetailInfoVo = redisCache.getCacheObject(key);
        } else {
            userPictureDetailInfoVo = getUserPictureDetailInfoVo(pictureId);
            userPictureDetailInfoVo.setPictureUrl(null);
        }
        //存入缓存 五分钟即可
        redisCache.setCacheObject(key, userPictureDetailInfoVo, 5, TimeUnit.MINUTES);
        //查询是否有行为，点赞、收藏
        isBehavior(pictureId, userId, userPictureDetailInfoVo);
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
        String behaviorKey = PICTURE_USER_BEHAVIOR + userId + ":" + PUserBehaviorTargetType.USER_BEHAVIOR_TARGET_TYPE_0.getValue() + ":" + pictureId;
        ArrayList<UserBehaviorInfo> userBehaviorInfos = new ArrayList<>();
        if (redisCache.hasKey(behaviorKey)) {
            userBehaviorInfos = redisCache.getCacheObject(behaviorKey);
        } else {
            List<UserBehaviorInfo> list = userBehaviorInfoService.list(new LambdaQueryWrapper<UserBehaviorInfo>()
                    .eq(UserBehaviorInfo::getUserId, userId)
                    .eq(UserBehaviorInfo::getTargetId, pictureId)
                    .eq(UserBehaviorInfo::getTargetType, PUserBehaviorTargetType.USER_BEHAVIOR_TARGET_TYPE_0.getValue())
                    .in(UserBehaviorInfo::getBehaviorType, Arrays.asList(PUserBehaviorType.USER_BEHAVIOR_TYPE_0.getValue(), PUserBehaviorType.USER_BEHAVIOR_TYPE_1.getValue())));
            if (StringUtils.isNotEmpty(list)) {
                userBehaviorInfos.addAll(list);
            }
        }

        if (StringUtils.isNotEmpty(userBehaviorInfos)) {
            for (UserBehaviorInfo info : userBehaviorInfos) {
                if (info.getBehaviorType().equals(PUserBehaviorType.USER_BEHAVIOR_TYPE_0.getValue())) {
                    userPictureDetailInfoVo.setIsLike(true);
                }
                if (info.getBehaviorType().equals(PUserBehaviorType.USER_BEHAVIOR_TYPE_1.getValue())) {
                    userPictureDetailInfoVo.setIsCollect(true);
                }
            }
            redisCache.setCacheObject(behaviorKey, userBehaviorInfos, 5, TimeUnit.MINUTES);
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
        UserPictureDetailInfoVo userPictureDetailInfoVo = new UserPictureDetailInfoVo();
        PictureInfo pictureInfo = this.getById(pictureId);
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo), HttpStatus.NO_CONTENT, "图片不存在");
        //如果图片不是公共且图片审核状态不是通过，且当前用户不是作者
        //TODO 后续有判断是否是团队空间 团队空间成员还是可以查看的
        if (!pictureInfo.getPictureStatus().equals(PPictureStatus.PICTURE_STATUS_0.getValue())
                && pictureInfo.getReviewStatus().equals(Long.parseLong(PPictureReviewStatus.PICTURE_REVIEW_STATUS_1.getValue()))
                && !pictureInfo.getUserId().equals(UserInfoSecurityUtils.getUserId())) {
            throw new ServiceException("图片审核不通过，无法查看");
        }
        //查询分类
        PictureCategoryInfo categoryInfo = pictureCategoryInfoService.selectPictureCategoryInfoByCategoryId(pictureInfo.getCategoryId());
        if (StringUtils.isNotNull(categoryInfo)) {
            userPictureDetailInfoVo.setCategoryName(categoryInfo.getName());
        }
        BeanUtils.copyProperties(pictureInfo, userPictureDetailInfoVo);
        //构建图片url
        String pictureUrl = builderPictureUrl(pictureInfo.getPictureUrl(), pictureInfo.getDnsUrl());
        userPictureDetailInfoVo.setPictureUrl(pictureUrl);
        String thumbnailUrl = builderPictureUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl());
        userPictureDetailInfoVo.setThumbnailUrl(thumbnailUrl);
        //查询空间
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(pictureInfo.getSpaceId());
        //判断空间是否存在且是否是公共空间
        if (StringUtils.isNotNull(spaceInfo) && spaceInfo.getSpaceStatus().equals(PSpaceStatus.SPACE_STATUS_0.getValue())) {
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
        List<PictureTagRelInfo> pictureTagRelInfoList = pictureTagRelInfoService.list(new LambdaQueryWrapper<PictureTagRelInfo>()
                .eq(PictureTagRelInfo::getPictureId, pictureId));
        if (StringUtils.isNotEmpty(pictureTagRelInfoList)) {
            List<String> tagIds = pictureTagRelInfoList.stream()
                    .map(PictureTagRelInfo::getTagId).collect(Collectors.toList());
            List<PictureTagInfo> pictureTagInfoList = pictureTagInfoService.list(new LambdaQueryWrapper<PictureTagInfo>()
                    .in(PictureTagInfo::getTagId, tagIds));
            List<String> tagNames = pictureTagInfoList.stream()
                    .map(PictureTagInfo::getName).collect(Collectors.toList());
            userPictureDetailInfoVo.setPictureTags(tagNames);
        }
        //查询点赞、收藏、分享数
        getPictureStatics(pictureId, userPictureDetailInfoVo);

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
        behaviorInfo.setTargetType(PUserBehaviorTargetType.USER_BEHAVIOR_TARGET_TYPE_0.getValue());
        List<UserBehaviorInfoStaticVo> staticBehaviorInfo = userBehaviorInfoService.staticBehaviorInfo(behaviorInfo);
        if (StringUtils.isNotEmpty(staticBehaviorInfo)) {
            staticBehaviorInfo.forEach(behaviorInfoStaticVo -> {
                if (behaviorInfoStaticVo.getBehaviorType().equals(PUserBehaviorType.USER_BEHAVIOR_TYPE_0.getValue())) {
                    userPictureDetailInfoVo.setLikeCount(behaviorInfoStaticVo.getTargetTypeCount());
                }
                if (behaviorInfoStaticVo.getBehaviorType().equals(PUserBehaviorType.USER_BEHAVIOR_TYPE_1.getValue())) {
                    userPictureDetailInfoVo.setCollectCount(behaviorInfoStaticVo.getTargetTypeCount());
                }
                if (behaviorInfoStaticVo.getBehaviorType().equals(PUserBehaviorType.USER_BEHAVIOR_TYPE_2.getValue())) {
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
    public void resetPictureInfoCache(String pictureId) {
        String key = PictureRedisConstants.PICTURE_PICTURE_DETAIL + pictureId;
        UserPictureDetailInfoVo userPictureDetailInfoVo = getUserPictureDetailInfoVo(pictureId);
        //存入缓存 五分钟即可
        redisCache.setCacheObject(key, userPictureDetailInfoVo, 5, TimeUnit.MINUTES);
    }

    @Override
    public UserPictureDetailInfoVo userMySelectPictureInfoByPictureId(String pictureId, String userId) {
        UserPictureDetailInfoVo userPictureDetailInfoVo = getUserPictureDetailInfoVo(pictureId);
        ThrowUtils.throwIf(!userId.equals(userPictureDetailInfoVo.getUserId()), "图片不存在");
        //说明是自己，则获取修改图片权限，并且授权密钥让用户可以访问图片
        String time = configInfoService.getConfigInfoInCache(PICTURE_LOOK_ORIGINAL_TIMEOUT);
        Long timeout = Long.valueOf(time);
        String url = pictureUploadManager.generateDownloadUrl(userPictureDetailInfoVo.getPictureUrl(), timeout);
        userPictureDetailInfoVo.setPictureUrl(url);
        return userPictureDetailInfoVo;
    }

    @Override
    public String builderPictureUrl(String pictureUrl, String dnsUrl) {
        if (StringUtils.isNotEmpty(dnsUrl)) {
            return dnsUrl + pictureUrl;
        } else {
            return ossConfig.getDnsUrl() + pictureUrl;
        }
    }
}
