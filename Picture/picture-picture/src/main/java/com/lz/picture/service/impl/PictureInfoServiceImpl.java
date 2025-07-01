package com.lz.picture.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.config.OssConfig;
import com.lz.common.constant.HttpStatus;
import com.lz.common.constant.redis.PictureRedisConstants;
import com.lz.common.core.domain.DeviceInfo;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.enums.CommonHasStatisticsEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.ParamUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanUtils;
import com.lz.common.utils.ip.IpUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.model.enmus.CTemplateTypeEnum;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureFileLogAsyncFactory;
import com.lz.picture.manager.factory.PictureRecommendAsyncFactory;
import com.lz.picture.mapper.PictureInfoMapper;
import com.lz.picture.model.domain.*;
import com.lz.picture.model.dto.pictureDownloadLogInfo.PictureDownloadLogInfoRequest;
import com.lz.picture.model.dto.pictureInfo.PictureInfoDetailRecommendRequest;
import com.lz.picture.model.dto.pictureInfo.PictureMoreInfo;
import com.lz.picture.model.dto.pictureInfo.UserPictureInfoQuery;
import com.lz.picture.model.dto.pictureRecommend.PictureRecommendRequest;
import com.lz.picture.model.enums.*;
import com.lz.picture.model.vo.pictureInfo.*;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoCache;
import com.lz.picture.model.vo.userBehaviorInfo.UserBehaviorInfoStaticVo;
import com.lz.picture.service.*;
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
import static com.lz.common.constant.config.UserConfigKeyConstants.*;
import static com.lz.common.constant.picture.PictureInfoConstants.PICTURE_RECOMMEND_MODEL_DOWNLOAD_TYPE;
import static com.lz.common.constant.redis.PictureRedisConstants.*;
import static com.lz.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;

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

    @Resource
    private IAccountInfoService accountInfoService;

    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;

    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    @Resource
    @Lazy
    private ISpaceMemberInfoService spaceMemberInfoService;

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
        String pictureUrl = ossConfig.builderUrl(pictureInfo.getPictureUrl(), pictureInfo.getDnsUrl());
        pictureInfo.setPictureUrl(pictureUrl);
        String thumbnailUrl = ossConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl());
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
            String pictureUrl = ossConfig.builderUrl(info.getPictureUrl(), info.getDnsUrl());
            info.setPictureUrl(pictureUrl);
            String thumbnailUrl = ossConfig.builderUrl(info.getThumbnailUrl(), info.getDnsUrl());
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
        SpaceInfo spaceInfo = checkPictureAndSpace(pictureInfo);
        //查询分类是否存在
        PictureCategoryInfo categoryInfo = new PictureCategoryInfo();
        if (StringUtils.isNotEmpty(pictureInfo.getCategoryId())) {
            //查询分类是否存在
            categoryInfo = pictureCategoryInfoService.selectPictureCategoryInfoByCategoryId(pictureInfo.getCategoryId());
            ThrowUtils.throwIf(StringUtils.isNull(categoryInfo), HttpStatus.NO_CONTENT, "分类不存在");
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
        }
        //查询空间是否存在
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(pictureInfo.getSpaceId());
        if (StringUtils.isNull(spaceInfo) || !spaceInfo.getIsDelete().equals(CommonDeleteEnum.NORMAL.getValue())) {
            throw new ServiceException("空间不存在");
        }
        //如果空间为个人空间并且不是官方空间
        if (!spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_2.getValue())
                && !spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_0.getValue())) {
            //如果传过来图片ID并且自己不是图片作者
            ThrowUtils.throwIf(pictureInfo.getPictureId() != null && !pictureInfo.getUserId().equals(UserInfoSecurityUtils.getUserId()), "您不是该图片所有者，无法上传图片");
            //如果用户不是自己
            ThrowUtils.throwIf(!spaceInfo.getUserId().equals(pictureInfo.getUserId()), "您不是该空间所有者，无法上传图片");
            //判断文件夹是否存在且文件夹作者是自己
            if (StringUtils.isNotEmpty(pictureInfo.getFolderId())) {
                SpaceFolderInfo spaceFolderInfo = spaceFolderInfoService.selectSpaceFolderInfoByFolderId(pictureInfo.getFolderId());
                ThrowUtils.throwIf(StringUtils.isNull(spaceFolderInfo), HttpStatus.NO_CONTENT, "文件夹不存在");
                ThrowUtils.throwIf(!spaceFolderInfo.getUserId().equals(pictureInfo.getUserId()), HttpStatus.NO_CONTENT, "您不是该文件夹所有者，无法上传图片");
                //判断该空间是否有此文件夹
                ThrowUtils.throwIf(!spaceFolderInfo.getSpaceId().equals(pictureInfo.getSpaceId()), HttpStatus.NO_CONTENT, "该空间没有此文件夹，无法上传图片");
            }
        }
        //如果空间是团队空间
        if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_1.getValue())) {
            //TODO 团队空间判断
        }
        //判断空间是否是公共
        if (spaceInfo.getOssType().equals(PSpaceOssTypeEnum.SPACE_OSS_TYPE_0.getValue())) {
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
        String key = PictureRedisConstants.PICTURE_PICTURE_DETAIL + pictureId;
        UserPictureDetailInfoVo userPictureDetailInfoVo = null;
        if (redisCache.hasKey(key)) {
            userPictureDetailInfoVo = redisCache.getCacheObject(key);
        } else {
            userPictureDetailInfoVo = getUserPictureDetailInfoVo(pictureId);
            userPictureDetailInfoVo.setPictureUrl(null);
        }
        //存入缓存 五分钟即可
        redisCache.setCacheObject(key, userPictureDetailInfoVo, PICTURE_PICTURE_DETAIL_EXPIRE_TIME, TimeUnit.SECONDS);
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
            redisCache.setCacheObject(behaviorKey, userBehaviorInfoCaches, 5, TimeUnit.MINUTES);
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
        if (!pictureInfo.getPictureStatus().equals(PPictureStatusEnum.PICTURE_STATUS_0.getValue())
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
        String pictureUrl = ossConfig.builderUrl(pictureInfo.getPictureUrl(), pictureInfo.getDnsUrl());
        userPictureDetailInfoVo.setPictureUrl(pictureUrl);
        String thumbnailUrl = ossConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl());
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
        String key = PictureRedisConstants.PICTURE_PICTURE_DETAIL + pictureId;
        UserPictureDetailInfoVo userPictureDetailInfoVo = null;
        if (redisCache.hasKey(key)) {
            userPictureDetailInfoVo = redisCache.getCacheObject(key);
        } else {
            userPictureDetailInfoVo = getUserPictureDetailInfoVo(pictureId);
            userPictureDetailInfoVo.setPictureUrl(null);
        }
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
        //存入缓存 五分钟即可
        redisCache.setCacheObject(key, userPictureDetailInfoVo, PICTURE_PICTURE_DETAIL_EXPIRE_TIME, TimeUnit.SECONDS);
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
        String key = PictureRedisConstants.PICTURE_PICTURE_DETAIL + pictureId;
        if (redisCache.hasKey(key)) {
            redisCache.deleteObject(key);
        }
        return userPictureDetailInfoVo;
    }

    @Override
    public UserPictureDetailInfoVo userUpdatePictureInfo(PictureInfo pictureInfo) {
        SpaceInfo spaceInfo = checkPictureAndSpace(pictureInfo);
        PictureCategoryInfo categoryInfo = new PictureCategoryInfo();
        if (StringUtils.isNotEmpty(pictureInfo.getCategoryId())) {
            //查询分类是否存在
            categoryInfo = pictureCategoryInfoService.selectPictureCategoryInfoByCategoryId(pictureInfo.getCategoryId());
            ThrowUtils.throwIf(StringUtils.isNull(categoryInfo), HttpStatus.NO_CONTENT, "分类不存在");
        }
        //查询在数据库内内容
        PictureInfo pictureInfoDb = pictureInfoMapper.selectPictureInfoByPictureId(pictureInfo.getPictureId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfoDb), HttpStatus.NO_CONTENT, "图片不存在");
        //更新空间信息
        spaceInfo.setTotalSize(spaceInfo.getTotalSize() + pictureInfo.getPicSize() - pictureInfoDb.getPicSize());
        spaceInfo.setLastUpdateTime(new Date());
        //判断当前空间是否到达最大值 官方空间没有限制
        if (spaceInfo.getTotalCount() > spaceInfo.getMaxCount() && !spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_0.getValue())
                || spaceInfo.getTotalSize() > spaceInfo.getMaxSize() && !spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_0.getValue())) {
            throw new ServiceException("空间已满，无法上传图片", HttpStatus.NO_CONTENT);
        }
        //根据图片域名信息去除域名
        //判断空间是否是自定义存储
        if (spaceInfo.getOssType().equals(PSpaceOssTypeEnum.SPACE_OSS_TYPE_0.getValue())) {
            //不是判断图片域名是否正确
            if (pictureInfo.getPictureUrl().startsWith(ossConfig.getDnsUrl())) {
                //删除图片传过来的域名前缀
                pictureInfo.setPictureUrl(pictureInfo.getPictureUrl().replace(ossConfig.getDnsUrl(), ""));
            }
            if (pictureInfo.getThumbnailUrl().startsWith(ossConfig.getDnsUrl())) {
                //删除图片传过来的域名前缀
                pictureInfo.setThumbnailUrl(pictureInfo.getThumbnailUrl().replace(ossConfig.getDnsUrl(), ""));
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

    @Override
    public PictureInfo verifyPictureInfo(String pictureId, String userId) {
        //校验用户当前是否输入密码
        ThrowUtils.throwIf(accountInfoService.getVerifyPassword(userId) != 1, "请输入密码");
        //查询图片信息
        PictureInfo pictureInfo = pictureInfoMapper.selectPictureInfoByPictureId(pictureId);
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo)
                        || (!pictureInfo.getUserId().equals(userId) && pictureInfo.getPictureStatus().equals(PSpaceStatusEnum.SPACE_STATUS_1.getValue())
                        || pictureInfo.getIsDelete().equals(CommonDeleteEnum.DELETED.getValue())),
                "图片不存在");

        //下载记录
        PictureDownloadLogInfo pictureDownloadLogInfo = new PictureDownloadLogInfo();
        pictureDownloadLogInfo.setUserId(userId);
        pictureDownloadLogInfo.setPictureId(pictureId);
        pictureDownloadLogInfo.setCategoryId(pictureInfo.getCategoryId());
        DeviceInfo deviceInfo = IpUtils.getDeviceInfo();

        //查询标签信息
        pictureDownloadLogInfo.setTags(pictureTagRelInfoService.getPictureTagNamesStr(pictureId));
        pictureDownloadLogInfo.setSpaceId(pictureInfo.getSpaceId());
        //所需总积分
        String moreInfo = pictureInfo.getMoreInfo();
        PictureMoreInfo pictureMoreInfo = new PictureMoreInfo();
        if (StringUtils.isNotEmpty(moreInfo)) {
            pictureMoreInfo = JSON.parseObject(moreInfo, PictureMoreInfo.class);
        }
        Long totalPoints = 0L;
        if (StringUtils.isNotNull(pictureMoreInfo) && StringUtils.isNotNull(pictureMoreInfo.getPointsNeed())) {
            totalPoints = pictureMoreInfo.getPointsNeed();
        }
        pictureDownloadLogInfo.setPictureName(pictureInfo.getName() + "." + pictureInfo.getPicFormat());
        if (StringUtils.isEmpty(pictureInfo.getDnsUrl())) {
            pictureDownloadLogInfo.setThumbnailUrl(ossConfig.getDnsUrl() + pictureInfo.getThumbnailUrl());
        } else {
            pictureDownloadLogInfo.setThumbnailUrl(pictureInfo.getDnsUrl() + pictureInfo.getThumbnailUrl());
        }
        pictureDownloadLogInfo.setPointsCost(totalPoints);
        pictureDownloadLogInfo.setCreateTime(DateUtils.getNowDate());
        pictureDownloadLogInfo.setDownloadStatus(PDownloadStatusEnum.DOWNLOAD_STATUS_0.getValue());
        pictureDownloadLogInfo.setDownloadType(PDownloadTypeEnum.DOWNLOAD_TYPE_0.getValue());
        pictureDownloadLogInfo.setReferSource(PDownloadReferSourceEnum.DOWNLOAD_REFER_SOURCE_1.getValue());
        pictureDownloadLogInfo.setHasStatistics(CommonHasStatisticsEnum.HAS_STATISTICS_0.getValue());
        BeanUtils.copyProperties(deviceInfo, pictureDownloadLogInfo);
        //判断本人是否是作者
        if (pictureInfo.getUserId().equals(userId)) {
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
            pictureDownloadLogInfoService.save(pictureDownloadLogInfo);
            return pictureInfo;
        }
        //不是作者 需要更新账号积分、积分使用记录、下载记录、如果不是免费需要给作者充值
        //首先判断图片状态,如果图片状态为公开，且没有删除，则可以下载
        ThrowUtils.throwIf(!PPictureStatusEnum.PICTURE_STATUS_0.getValue().equals(pictureInfo.getPictureStatus())
                        || !CommonDeleteEnum.NORMAL.getValue().equals(pictureInfo.getIsDelete()),
                "图片不存在");
        //判断用户积分是否足够 查询用户账户是否存在，存在判断积分
        AccountInfo accountInfo = accountInfoService.selectAccountInfoByUserId(userId);

        ThrowUtils.throwIf(StringUtils.isNull(accountInfo)
                || accountInfo.getPointsBalance() < totalPoints, "积分不足");

        if (totalPoints != 0) {
            //获取官方、空间比例
            String officialProPortionStr = configInfoService.getConfigInfoInCache(PICTURE_DOWNLOAD_OFFICIAL_PROPORTION);
            double officialProportion = Double.parseDouble(officialProPortionStr);
            String spaceProportionStr = configInfoService.getConfigInfoInCache(PICTURE_DOWNLOAD_SPACE_PROPORTION);
            double spaceProportion = Double.parseDouble(spaceProportionStr);
            double authorProportion = 1 - officialProportion - spaceProportion;
            pictureDownloadLogInfo.setPointsAuthorGain((long) (totalPoints * authorProportion));
            pictureDownloadLogInfo.setPointsOfficialGain((long) (totalPoints * officialProportion));
            pictureDownloadLogInfo.setPointsSpaceGain((long) (totalPoints * spaceProportion));
            pictureDownloadLogInfo.setAuthorProportion(BigDecimal.valueOf(authorProportion));
            pictureDownloadLogInfo.setOfficialProportion(BigDecimal.valueOf(officialProportion));
            pictureDownloadLogInfo.setSpaceProportion(BigDecimal.valueOf(spaceProportion));
            pointsUsageLogInfoService.updateAccountByPointsRechargeInfo(
                    userId,
                    null,
                    PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1.getValue(),
                    PoPointsUsageTypeEnum.POINTS_USAGE_TYPE_0.getValue(),
                    pictureId,
                    -totalPoints,
                    deviceInfo);
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
        pictureDownloadLogInfo.setDownloadId(IdUtils.fastUUID());
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
    public PictureInfo verifyPictureInfoByLog(PictureDownloadLogInfoRequest pictureDownloadLogInfoRequest) {
        //查询到图片记录
        PictureDownloadLogInfo pictureDownloadLogInfo = pictureDownloadLogInfoService.selectPictureDownloadLogInfoByDownloadId(pictureDownloadLogInfoRequest.getDownloadId());
        //是否不存在
        ThrowUtils.throwIf(StringUtils.isNull(pictureDownloadLogInfo), "此下载记录不属于您！！！");
        //查询图片是否存在
        PictureInfo pictureInfo = this.selectPictureInfoByPictureId(pictureDownloadLogInfo.getPictureId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfo) || !CommonDeleteEnum.NORMAL.getValue().equals(pictureInfo.getIsDelete()), "此图片不存在！！！");
        return pictureInfo;
    }

    @Override
    public List<PictureInfoSearchRecommendVo> getSearchRecommend() {
        List<PictureInfoSearchRecommendVo> cache = redisCache.getCacheObject(PICTURE_SEARCH_RECOMMEND);
        if (StringUtils.isNotEmpty(cache)) {
            return cache;
        }
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
        String p = configInfoService.getConfigInfoInCache(PICTURE_INDEX_P);
        for (PictureInfo pictureInfo : pictureInfos) {
            pictureInfo.setThumbnailUrl(ossConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl()) + "?x-oss-process=image/resize,p_" + p);
        }
        //转换为vo
        List<PictureInfoSearchRecommendVo> pictureInfoSearchRecommendVos = PictureInfoSearchRecommendVo.objToVo(pictureInfos);
        redisCache.setCacheObject(PICTURE_SEARCH_RECOMMEND, pictureInfoSearchRecommendVos, PICTURE_SEARCH_RECOMMEND_EXPIRE_TIME, TimeUnit.SECONDS);
        return pictureInfoSearchRecommendVos;
    }

    @Override
    public List<PictureInfoSearchSuggestionVo> getSearchSuggestion(String name) {
        String key = PICTURE_SEARCH_SUGGESTION + COMMON_SEPARATOR_CACHE + name;
        List<PictureInfoSearchSuggestionVo> cache = redisCache.getCacheObject(key);
        if (StringUtils.isNotEmpty(cache)) {
            return cache;
        }
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
        List<PictureInfoSearchSuggestionVo> pictureInfoSearchSuggestionVos = PictureInfoSearchSuggestionVo.objToVo(pictureInfos);
        redisCache.setCacheObject(key, pictureInfoSearchSuggestionVos, PICTURE_SEARCH_SUGGESTION_EXPIRE_TIME, TimeUnit.SECONDS);
        return pictureInfoSearchSuggestionVos;
    }

    @Override
    public List<UserPictureInfoVo> getPictureInfoDetailRecommend(PictureInfoDetailRecommendRequest pictureInfoDetailRecommendRequest) {
        //查询缓存是否存在
        String key = PICTURE_RECOMMEND_DETAIL + pictureInfoDetailRecommendRequest.getPictureId() + COMMON_SEPARATOR_CACHE + pictureInfoDetailRecommendRequest.getPageSize() + COMMON_SEPARATOR_CACHE + pictureInfoDetailRecommendRequest.getCurrentPage();
        if (redisCache.hasKey(key)) {
            return redisCache.getCacheObject(key);
        }
        pictureInfoDetailRecommendRequest.setOffset((pictureInfoDetailRecommendRequest.getCurrentPage() - 1) * pictureInfoDetailRecommendRequest.getPageSize());
        // 在查询前清除分页设置
//        PageHelper.clearPage();
        //        System.out.println("pictureInfoRecommendRequest = " + pictureInfoRecommendRequest);
        List<PictureInfo> list = pictureInfoMapper.getPictureInfoDetailRecommend(pictureInfoDetailRecommendRequest);
        list.forEach(pictureInfo -> {
            pictureInfo.setThumbnailUrl(ossConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl()));
        });
        List<UserPictureInfoVo> userPictureInfoVos = UserPictureInfoVo.objToVo(list);
        redisCache.setCacheObject(key, userPictureInfoVos, PICTURE_RECOMMEND_DETAIL_EXPIRE_TIME, TimeUnit.SECONDS);
        return userPictureInfoVos;
    }

    @Override
    public List<UserRecommendPictureInfoVo> getRecommentHotPictureInfoList(PictureRecommendRequest pictureRecommendRequest) {
        //先查询是否有缓存，如果有直接走缓存
        String key = PICTURE_RECOMMEND_HOT + pictureRecommendRequest.getCurrentPage() + COMMON_SEPARATOR_CACHE + pictureRecommendRequest.getPageSize();
        List<UserRecommendPictureInfoVo> cacheObject = redisCache.getCacheObject(key);
        if (StringUtils.isNotEmpty(cacheObject)) {
            return cacheObject;
        }
        Page<PictureInfo> pictureInfoPage = new Page<>();
        pictureInfoPage.setCurrent(pictureRecommendRequest.getCurrentPage());
        pictureInfoPage.setSize(pictureRecommendRequest.getPageSize());
        Page<PictureInfo> pictureInfoList = this.page(pictureInfoPage, new LambdaQueryWrapper<PictureInfo>()
                .eq(PictureInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .eq(PictureInfo::getPictureStatus, PPictureStatusEnum.PICTURE_STATUS_0.getValue())
                .orderByDesc(PictureInfo::getDownloadCount, PictureInfo::getShareCount, PictureInfo::getCollectCount, PictureInfo::getLikeCount, PictureInfo::getLookCount)
        );
        //构造url
        pictureInfoList.getRecords().forEach(pictureInfo -> {
            pictureInfo.setThumbnailUrl(ossConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl()));
        });
        List<UserRecommendPictureInfoVo> userRecommendPictureInfoVos = UserRecommendPictureInfoVo.objToVo(pictureInfoList.getRecords());
        //防止空指针异常
        if (StringUtils.isEmpty(userRecommendPictureInfoVos)) {
            userRecommendPictureInfoVos = new ArrayList<>();
        }
        redisCache.setCacheObject(key, userRecommendPictureInfoVos, PICTURE_RECOMMEND_HOT_EXPIRE_TIME, TimeUnit.SECONDS);
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
        String inCache = configInfoService.getConfigInfoInCache(PICTURE_SPACE_AVATAR_P);
        List<PictureInfoTableVo> pictureInfoTableVos =
                pictureInfoPage.getRecords().stream()
                        .map(pictureInfo -> {
                            pictureInfo.setThumbnailUrl(ossConfig.builderUrl(pictureInfo.getThumbnailUrl(), pictureInfo.getDnsUrl()) + "?x-oss-process=image/resize,p_" + inCache);
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

    @Override
    public int userUpdatePictureInfoName(PictureInfo pictureInfo) {
        //判断图片是否存在是否是作者
        PictureInfo pictureInfoDb = this.selectNormalPictureInfoByPictureId(pictureInfo.getPictureId());
        ThrowUtils.throwIf(StringUtils.isNull(pictureInfoDb)
                || !pictureInfoDb.getUserId().equals(pictureInfo.getUserId()), "图片不存在或您不是作者");
        pictureInfoDb.setName(pictureInfo.getName());
        pictureInfoDb.setEditTime(DateUtils.getNowDate());
        this.deletePictureTableCacheByUserId(pictureInfoDb.getUserId());
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
        //获取当前用户
        String userId = UserInfoSecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        //判断是否图片是自己的
        pictureInfos.forEach(pictureInfo -> {
            ThrowUtils.throwIf(!pictureInfo.getUserId().equals(userId), StringUtils.format("图片不存在或您不是作者:{}", pictureInfo.getName()));
            pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_1.getValue());
            pictureInfo.setIsDelete(CommonDeleteEnum.DELETED.getValue());
            pictureInfo.setDeletedTime(nowDate);
        });
        this.deletePictureTableCacheByUserId(userId);
        return this.updateBatchById(pictureInfos) ? 1 : 0;
    }

    @Override
    public TableDataInfo listPictureInfoTeamTable(UserPictureInfoQuery userPictureInfoQuery) {
        if (StringUtils.isEmpty(userPictureInfoQuery.getSpaceId())) {
            throw new ServiceException("空间编号不能为空");
        }
        SpaceMemberInfo spaceMemberInfo = spaceMemberInfoService.userIsJoinSpace(userPictureInfoQuery.getSpaceId(), UserInfoSecurityUtils.getUserId());

        String jsonStr = JSON.toJSONString(userPictureInfoQuery);
        //查询缓存是否存在
        String keyData = PICTURE_PICTURE_TABLE_PERSON + userPictureInfoQuery.getSpaceId() + COMMON_SEPARATOR_CACHE +
                jsonStr;
        if (StringUtils.isNull(spaceMemberInfo)) {
            throw new ServiceException("您没有加入此空间");
        }
        return getTableDataInfo(userPictureInfoQuery, keyData);
    }

    public void deletePictureTableCacheByUserId(String userId) {
        redisCache.deleteObjectsByPattern(PICTURE_PICTURE_TABLE_PERSON + userId + "*");
    }
}
