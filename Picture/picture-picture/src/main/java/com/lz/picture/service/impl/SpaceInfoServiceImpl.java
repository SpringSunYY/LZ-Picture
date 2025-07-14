package com.lz.picture.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.config.OssConfig;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.ParamUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureFileLogAsyncFactory;
import com.lz.picture.mapper.SpaceInfoMapper;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.domain.SpaceMemberInfo;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoQuery;
import com.lz.picture.model.dto.spaceInfo.UserSpaceInfoQuery;
import com.lz.picture.model.dto.spaceInfo.UserTeamSpaceInfoQuery;
import com.lz.picture.model.enums.PSpaceJoinTypeEnum;
import com.lz.picture.model.enums.PSpaceOssTypeEnum;
import com.lz.picture.model.enums.PSpaceRoleEnum;
import com.lz.picture.model.enums.PSpaceTypeEnum;
import com.lz.picture.model.vo.spaceInfo.SpaceInfoVo;
import com.lz.picture.model.vo.spaceInfo.UserPersonalSpaceInfoVo;
import com.lz.picture.model.vo.spaceInfo.UserSpaceInfoVo;
import com.lz.picture.model.vo.spaceInfo.UserTeamSpaceInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.picture.service.ISpaceMemberInfoService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.redis.PictureRedisConstants.*;
import static com.lz.config.utils.ConfigInfoUtils.*;

/**
 * 空间信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class SpaceInfoServiceImpl extends ServiceImpl<SpaceInfoMapper, SpaceInfo> implements ISpaceInfoService {
    @Resource
    private SpaceInfoMapper spaceInfoMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private OssConfig ossConfig;

    @Resource
    @Lazy
    private ISpaceMemberInfoService spaceMemberInfoService;
    //region mybatis代码

    /**
     * 查询空间信息
     *
     * @param spaceId 空间信息主键
     * @return 空间信息
     */
    @Override
    public SpaceInfo selectSpaceInfoBySpaceId(String spaceId) {
        return spaceInfoMapper.selectSpaceInfoBySpaceId(spaceId);
    }

    /**
     * 查询空间信息列表
     *
     * @param spaceInfo 空间信息
     * @return 空间信息
     */
    @Override
    public List<SpaceInfo> selectSpaceInfoList(SpaceInfo spaceInfo) {
        return spaceInfoMapper.selectSpaceInfoList(spaceInfo);
    }

    /**
     * 新增空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    @Override
    public int insertSpaceInfo(SpaceInfo spaceInfo) {
        spaceInfo.setSpaceId(IdUtils.snowflakeId().toString());
        spaceInfo.setCreateTime(DateUtils.getNowDate());
        return spaceInfoMapper.insertSpaceInfo(spaceInfo);
    }

    /**
     * 修改空间信息
     *
     * @param spaceInfo 空间信息
     * @return 结果
     */
    @Override
    public int updateSpaceInfo(SpaceInfo spaceInfo) {
        spaceInfo.setUpdateTime(DateUtils.getNowDate());
        return spaceInfoMapper.updateSpaceInfo(spaceInfo);
    }

    /**
     * 批量删除空间信息
     *
     * @param spaceIds 需要删除的空间信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceInfoBySpaceIds(String[] spaceIds) {
        return spaceInfoMapper.deleteSpaceInfoBySpaceIds(spaceIds);
    }

    /**
     * 删除空间信息信息
     *
     * @param spaceId 空间信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceInfoBySpaceId(String spaceId) {
        return spaceInfoMapper.deleteSpaceInfoBySpaceId(spaceId);
    }

    //endregion
    @Override
    public QueryWrapper<SpaceInfo> getQueryWrapper(SpaceInfoQuery spaceInfoQuery) {
        QueryWrapper<SpaceInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = spaceInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String spaceId = spaceInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String spaceName = spaceInfoQuery.getSpaceName();
        queryWrapper.like(StringUtils.isNotEmpty(spaceName), "space_name", spaceName);

        String ossType = spaceInfoQuery.getOssType();
        queryWrapper.eq(StringUtils.isNotEmpty(ossType), "oss_type", ossType);

        String userId = spaceInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String spaceStatus = spaceInfoQuery.getSpaceStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceStatus), "space_status", spaceStatus);

        String spaceType = spaceInfoQuery.getSpaceType();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceType), "space_type", spaceType);

        Date createTime = spaceInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date lastUpdateTime = spaceInfoQuery.getLastUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginLastUpdateTime")) && StringUtils.isNotNull(params.get("endLastUpdateTime")), "last_update_time", params.get("beginLastUpdateTime"), params.get("endLastUpdateTime"));

        Date updateTime = spaceInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String isDelete = spaceInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        Date deletedTime = spaceInfoQuery.getDeletedTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginDeletedTime")) && StringUtils.isNotNull(params.get("endDeletedTime")), "deleted_time", params.get("beginDeletedTime"), params.get("endDeletedTime"));

        return queryWrapper;
    }

    @Override
    public List<SpaceInfoVo> convertVoList(List<SpaceInfo> spaceInfoList) {
        if (StringUtils.isEmpty(spaceInfoList)) {
            return Collections.emptyList();
        }
        return spaceInfoList.stream().map(SpaceInfoVo::objToVo).collect(Collectors.toList());
    }

    //获取正常的空间信息-未删除
    @Override
    public SpaceInfo selectNormalSpaceInfoBySpaceId(String spaceId) {
        return this.getOne(new LambdaQueryWrapper<SpaceInfo>().eq(SpaceInfo::getSpaceId, spaceId).eq(SpaceInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue()));
    }

    @Override
    public int userInsertSpaceInfo(SpaceInfo spaceInfo) {
        //根据用户查询是否存在此空间
        SpaceInfo old = this.getOne(new LambdaQueryWrapper<SpaceInfo>().eq(SpaceInfo::getUserId, spaceInfo.getUserId()).eq(SpaceInfo::getSpaceName, spaceInfo.getSpaceName()));
        if (StringUtils.isNotNull(old)) {
            throw new ServiceException("空间名称已经存在！！！");
        }
        //判断用户可以创建的类型、个人、团队
        ArrayList<String> spaceTypeList = new ArrayList<>();
        spaceTypeList.add(PSpaceTypeEnum.SPACE_TYPE_1.getValue());
        spaceTypeList.add(PSpaceTypeEnum.SPACE_TYPE_2.getValue());
        if (!spaceTypeList.contains(spaceInfo.getSpaceType())) {
            throw new ServiceException("空间类型错误！！！");
        }
        //获取用户最大空间数量
        Integer maxSpaceCount = 10;
        if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_1.getValue())) {
            maxSpaceCount = PICTURE_SPACE_MAX_1_VALUE;
        } else if (spaceInfo.getSpaceType().equals(PSpaceTypeEnum.SPACE_TYPE_2.getValue())) {
            maxSpaceCount = PICTURE_SPACE_MAX_2_VALUE;
        }
        if (StringUtils.isNull(maxSpaceCount)) {
            maxSpaceCount = 10;
        }
        //查询用户此类型空间创建了多少个
        long count = this.count(new LambdaQueryWrapper<SpaceInfo>().eq(SpaceInfo::getUserId, spaceInfo.getUserId()).eq(SpaceInfo::getSpaceType, spaceInfo.getSpaceType()));
        if (count >= maxSpaceCount) {
            throw new ServiceException("此类型空间创建了10个，不能再创建了！！！");
        }
        Date nowDate = DateUtils.getNowDate();
        spaceInfo.setCreateTime(nowDate);
        spaceInfo.setUpdateTime(nowDate);

        spaceInfo.setMaxCount(PICTURE_SPACE_MAX_COUNT_VALUE);
        spaceInfo.setMaxSize(PICTURE_SPACE_MAX_SIZE_VALUE);
        spaceInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        spaceInfo.setOssType(PSpaceOssTypeEnum.SPACE_OSS_TYPE_0.getValue());

        boolean save = this.save(spaceInfo);
        //如果该空间是团队空间
        if (PSpaceTypeEnum.SPACE_TYPE_1.getValue().equals(spaceInfo.getSpaceType())) {
            //为用户创建空间成员
            SpaceMemberInfo spaceMemberInfo = new SpaceMemberInfo();
            spaceMemberInfo.setSpaceId(spaceInfo.getSpaceId());
            spaceMemberInfo.setUserId(spaceInfo.getUserId());
            spaceMemberInfo.setRoleType(PSpaceRoleEnum.SPACE_ROLE_0.getValue());
            spaceMemberInfo.setInviterUserId(spaceInfo.getUserId());
            spaceMemberInfo.setJoinType(PSpaceJoinTypeEnum.SPACE_JOIN_TYPE_0.getValue());
            spaceMemberInfo.setCreateTime(nowDate);
            spaceMemberInfoService.save(spaceMemberInfo);
        }
        //异步更新封面文件日志
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.recordSpaceCoverFileInfoLog(spaceInfo));
        if (save) {
            //删除表格缓存
            deleteSpacePersonalTableCacheByUserId(spaceInfo.getUserId());
            deleteSpaceTeamTableCacheByUserId(spaceInfo.getUserId());
        }
        return save ? 1 : 0;
    }

    @Override
    public int userUpdateSpaceInfo(SpaceInfo spaceInfo) {
        //查询空间
        SpaceInfo old = this.getOne(new LambdaQueryWrapper<SpaceInfo>().eq(SpaceInfo::getSpaceId, spaceInfo.getSpaceId()));
        if (StringUtils.isNull(old)) {
            throw new ServiceException("空间不存在！！！");
        }
        if (!old.getUserId().equals(spaceInfo.getUserId())) {
            throw new ServiceException("空间不存在！！！");
        }
        spaceInfo.setUpdateTime(DateUtils.getNowDate());
        boolean b = this.updateById(spaceInfo);
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.updateSpaceCoverFileInfoLog(old, spaceInfo));
        if (b) {
            //删除表格缓存
            deleteSpacePersonalTableCacheByUserId(spaceInfo.getUserId());
            deleteSpaceTeamTableCacheByUserId(spaceInfo.getUserId());
        }
        return b ? 1 : 0;
    }

    @Override
    public TableDataInfo listSpaceInfoTable(UserSpaceInfoQuery userSpaceInfoQuery) {
        String jsonStr = JSON.toJSONString(userSpaceInfoQuery);
        //查询缓存是否存在
        String keyData = PICTURE_SPACE_PERSONAL_TABLE_DATA + userSpaceInfoQuery.getUserId() + COMMON_SEPARATOR_CACHE + jsonStr;
        //如果都存在直接返回
        if (redisCache.hasKey(keyData)) {
            return redisCache.getCacheObject(keyData);
        }
        //构造查询条件
        Page<SpaceInfo> spaceInfoPage = new Page<>();
        spaceInfoPage.setCurrent(userSpaceInfoQuery.getPageNum());
        spaceInfoPage.setSize(userSpaceInfoQuery.getPageSize());
        String beginCreateTime = ParamUtils.getSafeString(userSpaceInfoQuery, ParamUtils.BEGIN_CREATE_TIME);
        String endCreateTime = ParamUtils.getSafeString(userSpaceInfoQuery, ParamUtils.END_CREATE_TIME);
        //构造查询条件
        LambdaQueryWrapper<SpaceInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getIsDelete()), SpaceInfo::getIsDelete, userSpaceInfoQuery.getIsDelete())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getUserId()), SpaceInfo::getUserId, userSpaceInfoQuery.getUserId())
                .like(StringUtils.isNotEmpty(userSpaceInfoQuery.getSpaceName()), SpaceInfo::getSpaceName, userSpaceInfoQuery.getSpaceName())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getSpaceId()), SpaceInfo::getSpaceId, userSpaceInfoQuery.getSpaceId())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getOssType()), SpaceInfo::getOssType, userSpaceInfoQuery.getOssType())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getSpaceStatus()), SpaceInfo::getSpaceStatus, userSpaceInfoQuery.getSpaceStatus())
                .eq(StringUtils.isNotEmpty(userSpaceInfoQuery.getSpaceType()), SpaceInfo::getSpaceType, userSpaceInfoQuery.getSpaceType())
                .apply(
                        StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                        "create_time between {0} and {1}",
                        beginCreateTime,
                        endCreateTime
                );
        //构建排序
        if (StringUtils.isNotEmpty(userSpaceInfoQuery.getIsAsc()) && StringUtils.isNotEmpty(userSpaceInfoQuery.getOrderByColumn())
                && Arrays.asList("createTime", "totalSize", "totalCount", "lastUpdateTime").contains(userSpaceInfoQuery.getOrderByColumn())) {
            if (userSpaceInfoQuery.getOrderByColumn().equals("totalSize")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getTotalSize);
            }
            if (userSpaceInfoQuery.getOrderByColumn().equals("createTime")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getCreateTime);
            }
            if (userSpaceInfoQuery.getOrderByColumn().equals("totalSize")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getTotalSize);
            }
            if (userSpaceInfoQuery.getOrderByColumn().equals("totalCount")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getTotalCount);
            }
            if (userSpaceInfoQuery.getOrderByColumn().equals("lastUpdateTime")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInfoQuery.getIsAsc().equals("asc"), SpaceInfo::getLastUpdateTime);
            }
        } else {
            lambdaQueryWrapper
                    .orderBy(true, false, SpaceInfo::getCreateTime);
        }
        Page<SpaceInfo> page = this.page(spaceInfoPage, lambdaQueryWrapper);
        //如果为空直接返回
        List<SpaceInfo> records = page.getRecords();
        if (StringUtils.isEmpty(records)) {
            TableDataInfo tableDataInfo = new TableDataInfo();
            redisCache.setCacheObject(keyData, tableDataInfo, PICTURE_SPACE_PERSONAL_TABLE_DATE_EXPIRE_TIME, TimeUnit.SECONDS);
            return tableDataInfo;
        }
        //压缩图片
        //转换为vo并且转换地址
        List<UserPersonalSpaceInfoVo> personalSpaceInfoVos = page.getRecords().stream()
                .map(spaceInfo -> {
                    spaceInfo.setSpaceAvatar(ossConfig.builderUrl(spaceInfo.getSpaceAvatar()) + "?x-oss-process=image/resize,p_" + PICTURE_COVER_P_VALUE);
                    return UserPersonalSpaceInfoVo.objToVo(spaceInfo);
                }).toList();
        //存入缓存信息并返回
        TableDataInfo tableDataInfo = new TableDataInfo(personalSpaceInfoVos, Math.toIntExact(page.getTotal()));
        redisCache.setCacheObject(keyData, tableDataInfo, PICTURE_SPACE_PERSONAL_TABLE_DATE_EXPIRE_TIME, TimeUnit.SECONDS);
        return tableDataInfo;
    }

    /**
     * 删除空间表格缓存
     *
     * @param userId 用户编号
     * @return void
     * @author: YY
     * @method: deleteSpaceTableCacheByUserId
     * @date: 2025/6/28 17:28
     **/
    @Override
    public void deleteSpacePersonalTableCacheByUserId(String userId) {
        redisCache.deleteObjectsByPattern(PICTURE_SPACE_PERSONAL_TABLE_DATA + userId + "*");
    }

    @Override
    public TableDataInfo listTeamSpaceInfoTable(UserTeamSpaceInfoQuery userTeamSpaceInfoQuery) {
        String jsonStr = JSON.toJSONString(userTeamSpaceInfoQuery);
        //查询缓存是否存在
        String keyData = PICTURE_SPACE_TEAM_TABLE_DATA + userTeamSpaceInfoQuery.getUserId() + COMMON_SEPARATOR_CACHE + jsonStr;
        //如果都存在直接返回
        if (redisCache.hasKey(keyData)) {
            return redisCache.getCacheObject(keyData);
        }
        //构造查询条件
        Page<SpaceMemberInfo> spaceMemberInfoPage = new Page<>();
        spaceMemberInfoPage.setCurrent(userTeamSpaceInfoQuery.getPageNum());
        spaceMemberInfoPage.setSize(userTeamSpaceInfoQuery.getPageSize());
        //获取时间范围
        Map<String, Object> params = userTeamSpaceInfoQuery.getParams();
        // 提取 beginCreateTime 和 endCreateTime（安全获取）
        String beginCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("beginCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);

        String endCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("endCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);
        LambdaQueryWrapper<SpaceMemberInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(userTeamSpaceInfoQuery.getMemberId()), SpaceMemberInfo::getMemberId, userTeamSpaceInfoQuery.getMemberId())
                .eq(StringUtils.isNotEmpty(userTeamSpaceInfoQuery.getSpaceId()), SpaceMemberInfo::getSpaceId, userTeamSpaceInfoQuery.getSpaceId())
                .eq(StringUtils.isNotEmpty(userTeamSpaceInfoQuery.getUserId()), SpaceMemberInfo::getUserId, userTeamSpaceInfoQuery.getUserId())
                .eq(StringUtils.isNotEmpty(userTeamSpaceInfoQuery.getRoleType()), SpaceMemberInfo::getRoleType, userTeamSpaceInfoQuery.getRoleType())
                .eq(StringUtils.isNotEmpty(userTeamSpaceInfoQuery.getJoinType()), SpaceMemberInfo::getJoinType, userTeamSpaceInfoQuery.getJoinType())
                .apply(
                        StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                        "create_time between {0} and {1}",
                        beginCreateTime,
                        endCreateTime
                );
        //构建排序规则
        if (StringUtils.isNotEmpty(userTeamSpaceInfoQuery.getIsAsc()) && StringUtils.isNotEmpty(userTeamSpaceInfoQuery.getOrderByColumn())
                && Arrays.asList("createTime", "lastActiveTime").contains(userTeamSpaceInfoQuery.getOrderByColumn())) {
            if (userTeamSpaceInfoQuery.getOrderByColumn().equals("createTime")) {
                queryWrapper
                        .orderBy(true, userTeamSpaceInfoQuery.getIsAsc().equals("asc"), SpaceMemberInfo::getCreateTime);
            }
            if (userTeamSpaceInfoQuery.getOrderByColumn().equals("lastActiveTime")) {
                queryWrapper
                        .orderBy(true, userTeamSpaceInfoQuery.getIsAsc().equals("asc"), SpaceMemberInfo::getLastActiveTime);
            }
        } else {
            queryWrapper
                    .orderBy(true, false, SpaceMemberInfo::getCreateTime);
        }
        Page<SpaceMemberInfo> memberInfoPage = spaceMemberInfoService.page(spaceMemberInfoPage, queryWrapper);
        //如果没有数据返回vos
        List<SpaceMemberInfo> spaceMemberInfos = memberInfoPage.getRecords();
        if (StringUtils.isEmpty(spaceMemberInfos)) {
            //存入缓存
            TableDataInfo tableDataInfo = new TableDataInfo();
            redisCache.setCacheObject(keyData, tableDataInfo, PICTURE_SPACE_TEAM_TABLE_DATA_EXPIRE_TIME, TimeUnit.SECONDS);
            return tableDataInfo;
        }
        //获取到所有的空间ID，查询空间
        List<String> spaceIds = spaceMemberInfos.stream().map(SpaceMemberInfo::getSpaceId).toList();
        List<SpaceInfo> spaceInfos = this.list(new LambdaQueryWrapper<SpaceInfo>()
                .eq(SpaceInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .in(SpaceInfo::getSpaceId, spaceIds));
        //如果空间为空
        if (StringUtils.isEmpty(spaceMemberInfos)) {
            //存入缓存
            TableDataInfo tableDataInfo = new TableDataInfo();
            redisCache.setCacheObject(keyData, tableDataInfo, PICTURE_SPACE_TEAM_TABLE_DATA_EXPIRE_TIME, TimeUnit.SECONDS);
            return tableDataInfo;
        }
        //根据空间ID转换为map
        List<UserTeamSpaceInfoVo> vos = new ArrayList<>();
        Map<String, SpaceInfo> spaceInfoMap = spaceInfos.stream().collect(Collectors.toMap(SpaceInfo::getSpaceId, spaceInfo -> spaceInfo));
        vos = spaceMemberInfos.stream().map(memberInfo -> {
            SpaceInfo spaceInfo = spaceInfoMap.get(memberInfo.getSpaceId());
            spaceInfo.setSpaceAvatar(ossConfig.builderUrl(spaceInfo.getSpaceAvatar()) + "?x-oss-process=image/resize,p_" + PICTURE_COVER_P_VALUE);
            UserTeamSpaceInfoVo userTeamSpaceInfoVo = new UserTeamSpaceInfoVo(memberInfo, spaceInfo);
            userTeamSpaceInfoVo.setUserId(spaceInfo.getUserId());
            userTeamSpaceInfoVo.setCurrentMembers(spaceMemberInfoService.getSpaceMemberNumberCount(memberInfo.getSpaceId()));
            return userTeamSpaceInfoVo;
        }).toList();
        //存入缓存
        TableDataInfo tableDataInfo = new TableDataInfo(vos, (int) memberInfoPage.getTotal());
        redisCache.setCacheObject(keyData, tableDataInfo, PICTURE_SPACE_TEAM_TABLE_DATA_EXPIRE_TIME, TimeUnit.SECONDS);
        return tableDataInfo;
    }

    @Override
    public void deleteSpaceTeamTableCacheByUserId(String userId) {
        redisCache.deleteObjectsByPattern(PICTURE_SPACE_TEAM_TABLE_DATA + userId + "*");
        redisCache.deleteObjectsByPattern(PICTURE_SPACE_LIST + userId + "*");
    }

    @Override
    public TableDataInfo mySpace(SpaceInfoQuery spaceInfoQuery) {
        String jsonStr = JSON.toJSONString(spaceInfoQuery);
        //查询缓存是否存在
        String keyData = PICTURE_SPACE_LIST + spaceInfoQuery.getUserId() + COMMON_SEPARATOR_CACHE + jsonStr;
        //如果都存在直接返回
        if (redisCache.hasKey(keyData)) {
            return redisCache.getCacheObject(keyData);
        }
        //无需分页只需要拿到自己所有的空间即可
        // 查询用户自己的正常空间（个人或团队）
        List<SpaceInfo> spaceInfos = this.list(new LambdaQueryWrapper<SpaceInfo>()
                .eq(SpaceInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue())
                .and(wrapper -> wrapper
                        .eq(SpaceInfo::getSpaceType, PSpaceTypeEnum.SPACE_TYPE_0.getValue())
                        .or()
                        .eq(StringUtils.isNotEmpty(spaceInfoQuery.getUserId()), SpaceInfo::getUserId, spaceInfoQuery.getUserId())
                )
        );
        //查询用户加入的所有空间
        List<SpaceMemberInfo> spaceMemberInfos = spaceMemberInfoService.selectSpaceMemberInfoByUserId(spaceInfoQuery.getUserId());
        if (StringUtils.isNotEmpty(spaceMemberInfos)) {
            List<String> spaceIds = spaceMemberInfos.stream().map(SpaceMemberInfo::getSpaceId).toList();
            spaceInfos.addAll(this.list(new LambdaQueryWrapper<SpaceInfo>().eq(SpaceInfo::getIsDelete, CommonDeleteEnum.NORMAL.getValue()).in(SpaceInfo::getSpaceId, spaceIds)));
        }
        //根据空间ID去重,并根据空间类型排序
        List<SpaceInfo> list = spaceInfos.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(SpaceInfo::getSpaceId, spaceInfo -> spaceInfo, (existing, replacement) -> existing),
                        map -> new ArrayList<>(map.values())
                ))
                .stream()
                .sorted(Comparator.comparing(SpaceInfo::getSpaceType))
                .toList();

        List<UserSpaceInfoVo> listVo = UserSpaceInfoVo.objToVo(list);
        String dnsUrl = ossConfig.getDnsUrl();
        listVo.stream()
                .filter(vo -> StringUtils.isNotEmpty(vo.getSpaceAvatar()))
                .forEach(vo -> {
                    vo.setSpaceAvatar(dnsUrl + vo.getSpaceAvatar() + "?x-oss-process=image/resize,p_" + PICTURE_SPACE_AVATAR_P_VALUE);
                });

        TableDataInfo tableDataInfo = new TableDataInfo(listVo, listVo.size());
        redisCache.setCacheObject(keyData, tableDataInfo, PICTURE_SPACE_LIST_EXPIRE_TIME, TimeUnit.SECONDS);
        return tableDataInfo;
    }
}
