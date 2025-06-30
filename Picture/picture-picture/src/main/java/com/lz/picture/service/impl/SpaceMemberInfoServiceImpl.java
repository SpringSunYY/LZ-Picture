package com.lz.picture.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.config.OssConfig;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.ParamUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.mapper.SpaceMemberInfoMapper;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.domain.SpaceMemberInfo;
import com.lz.picture.model.dto.spaceMemberInfo.SpaceMemberInfoQuery;
import com.lz.picture.model.dto.spaceMemberInfo.UserSpaceMemberInfoQuery;
import com.lz.picture.model.enums.PSpaceRoleEnum;
import com.lz.picture.model.vo.spaceMemberInfo.SpaceMemberInfoVo;
import com.lz.picture.model.vo.spaceMemberInfo.UserSpaceMemberInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.picture.service.ISpaceMemberInfoService;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.config.UserConfigKeyConstants.PICTURE_SPACE_AVATAR_P;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_SPACE_MEMBER_DATA;
import static com.lz.common.constant.redis.PictureRedisConstants.PICTURE_SPACE_MEMBER_DATA_EXPIRE_TIME;

/**
 * 空间成员信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class SpaceMemberInfoServiceImpl extends ServiceImpl<SpaceMemberInfoMapper, SpaceMemberInfo> implements ISpaceMemberInfoService {
    @Resource
    private SpaceMemberInfoMapper spaceMemberInfoMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private IConfigInfoService configInfoService;

    @Resource
    private OssConfig ossConfig;

    //region mybatis代码

    /**
     * 查询空间成员信息
     *
     * @param memberId 空间成员信息主键
     * @return 空间成员信息
     */
    @Override
    public SpaceMemberInfo selectSpaceMemberInfoByMemberId(String memberId) {
        return spaceMemberInfoMapper.selectSpaceMemberInfoByMemberId(memberId);
    }

    /**
     * 查询空间成员信息列表
     *
     * @param spaceMemberInfo 空间成员信息
     * @return 空间成员信息
     */
    @Override
    public List<SpaceMemberInfo> selectSpaceMemberInfoList(SpaceMemberInfo spaceMemberInfo) {
        return spaceMemberInfoMapper.selectSpaceMemberInfoList(spaceMemberInfo);
    }

    /**
     * 新增空间成员信息
     *
     * @param spaceMemberInfo 空间成员信息
     * @return 结果
     */
    @Override
    public int insertSpaceMemberInfo(SpaceMemberInfo spaceMemberInfo) {
        spaceMemberInfo.setCreateTime(DateUtils.getNowDate());
        return spaceMemberInfoMapper.insertSpaceMemberInfo(spaceMemberInfo);
    }

    /**
     * 修改空间成员信息
     *
     * @param spaceMemberInfo 空间成员信息
     * @return 结果
     */
    @Override
    public int updateSpaceMemberInfo(SpaceMemberInfo spaceMemberInfo) {
        spaceMemberInfo.setUpdateTime(DateUtils.getNowDate());
        return spaceMemberInfoMapper.updateSpaceMemberInfo(spaceMemberInfo);
    }

    /**
     * 批量删除空间成员信息
     *
     * @param memberIds 需要删除的空间成员信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceMemberInfoByMemberIds(String[] memberIds) {
        return spaceMemberInfoMapper.deleteSpaceMemberInfoByMemberIds(memberIds);
    }

    /**
     * 删除空间成员信息信息
     *
     * @param memberId 空间成员信息主键
     * @return 结果
     */
    @Override
    public int deleteSpaceMemberInfoByMemberId(String memberId) {
        return spaceMemberInfoMapper.deleteSpaceMemberInfoByMemberId(memberId);
    }

    //endregion
    @Override
    public QueryWrapper<SpaceMemberInfo> getQueryWrapper(SpaceMemberInfoQuery spaceMemberInfoQuery) {
        QueryWrapper<SpaceMemberInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = spaceMemberInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String memberId = spaceMemberInfoQuery.getMemberId();
        queryWrapper.eq(StringUtils.isNotEmpty(memberId), "member_id", memberId);

        String spaceId = spaceMemberInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String userId = spaceMemberInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String roleType = spaceMemberInfoQuery.getRoleType();
        queryWrapper.eq(StringUtils.isNotEmpty(roleType), "role_type", roleType);

        Date lastActiveTime = spaceMemberInfoQuery.getLastActiveTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginLastActiveTime")) && StringUtils.isNotNull(params.get("endLastActiveTime")), "last_active_time", params.get("beginLastActiveTime"), params.get("endLastActiveTime"));

        Date createTime = spaceMemberInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = spaceMemberInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String inviterUserId = spaceMemberInfoQuery.getInviterUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(inviterUserId), "inviter_user_id", inviterUserId);

        String joinType = spaceMemberInfoQuery.getJoinType();
        queryWrapper.eq(StringUtils.isNotEmpty(joinType), "join_type", joinType);

        return queryWrapper;
    }

    @Override
    public List<SpaceMemberInfoVo> convertVoList(List<SpaceMemberInfo> spaceMemberInfoList) {
        if (StringUtils.isEmpty(spaceMemberInfoList)) {
            return Collections.emptyList();
        }
        return spaceMemberInfoList.stream().map(SpaceMemberInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public Long getSpaceMemberNumberCount(String spaceId) {
        return this.count(new LambdaQueryWrapper<SpaceMemberInfo>()
                .eq(StringUtils.isNotEmpty(spaceId), SpaceMemberInfo::getSpaceId, spaceId));
    }

    public List<SpaceMemberInfo> listUserJoinSpace(String spaceId, String userId) {
        return this.list(new LambdaQueryWrapper<SpaceMemberInfo>()
                .eq(SpaceMemberInfo::getSpaceId, spaceId)
                .eq(SpaceMemberInfo::getUserId, userId));
    }

    @Override
    public TableDataInfo listUserSpaceMemberInfoList(UserSpaceMemberInfoQuery query) {
        String jsonStr = JSON.toJSONString(query);
        //查询缓存是否存在
        String keyData = PICTURE_SPACE_MEMBER_DATA + query.getSpaceId() + COMMON_SEPARATOR_CACHE +
                jsonStr;
        if (redisCache.hasKey(keyData)) {
            return redisCache.getCacheObject(keyData);
        }
        //先根据用户查询他是否再此空间，如果不在直接返回 空对象
        List<SpaceMemberInfo> spaceMemberInfoList = this.listUserJoinSpace(query.getSpaceId(), query.getUserId());
        if (StringUtils.isEmpty(spaceMemberInfoList)) {
            redisCache.setCacheObject(keyData, new TableDataInfo(), PICTURE_SPACE_MEMBER_DATA_EXPIRE_TIME, TimeUnit.SECONDS);
            return new TableDataInfo();
        }
        //构造查询条件
        Page<SpaceMemberInfo> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        String beginCreateTime = ParamUtils.getSafeString(query, ParamUtils.BEGIN_CREATE_TIME);
        String endCreateTime = ParamUtils.getSafeString(query, ParamUtils.END_CREATE_TIME);
        //构造查询条件
        LambdaQueryWrapper<SpaceMemberInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(StringUtils.isNotEmpty(query.getSpaceId()), SpaceMemberInfo::getSpaceId, query.getSpaceId())
                .like(StringUtils.isNotEmpty(query.getJoinType()), SpaceMemberInfo::getJoinType, query.getJoinType())
                .like(StringUtils.isNotEmpty(query.getRoleType()), SpaceMemberInfo::getRoleType, query.getRoleType())
                .apply(
                        StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                        "create_time between {0} and {1}",
                        beginCreateTime,
                        endCreateTime
                );
        //构造排序
        if (StringUtils.isNotEmpty(query.getIsAsc()) && StringUtils.isNotEmpty(query.getOrderByColumn())
                && Arrays.asList("createTime", "lastActiveTime").contains(query.getOrderByColumn())) {
            if (query.getOrderByColumn().equals("createTime")) {
                queryWrapper
                        .orderBy(true, query.getIsAsc().equals("asc"), SpaceMemberInfo::getCreateTime);
            }
            if (query.getOrderByColumn().equals("lastActiveTime")) {
                queryWrapper
                        .orderBy(true, query.getIsAsc().equals("asc"), SpaceMemberInfo::getLastActiveTime);
            }
        } else {
            queryWrapper
                    .orderBy(true, false, SpaceMemberInfo::getCreateTime);
        }
        Page<SpaceMemberInfo> memberInfoPage = this.page(page, queryWrapper);
        //如果为空直接返回
        List<SpaceMemberInfo> records = memberInfoPage.getRecords();
        if (StringUtils.isEmpty(records)) {
            redisCache.setCacheObject(keyData, new TableDataInfo(), PICTURE_SPACE_MEMBER_DATA_EXPIRE_TIME, TimeUnit.SECONDS);
            return new TableDataInfo();
        }
        //不为空查询用户信息 1、查询用户、2、查询邀请人
        List<String> userIds = records.stream().map(SpaceMemberInfo::getUserId).filter(StringUtils::isNotEmpty).toList();
        List<String> inviterUserIds = records.stream().map(SpaceMemberInfo::getInviterUserId).filter(StringUtils::isNotEmpty).toList();
        List<UserInfo> userInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>().in(UserInfo::getUserId, userIds));
        List<UserInfo> inviterUserInfoList = userInfoService.list(new LambdaQueryWrapper<UserInfo>().in(UserInfo::getUserId, inviterUserIds));
        //转换为map方便查询
        Map<String, UserInfo> userInfoMap = userInfoList.stream().filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUserName())).collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));
        Map<String, UserInfo> inviterUserInfoMap = inviterUserInfoList.stream().filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUserName())).collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));
        //查询空间
        SpaceInfo spaceInfo = spaceInfoService.selectNormalSpaceInfoBySpaceId(query.getSpaceId());
        //压缩图片
        String inCache = configInfoService.getConfigInfoInCache(PICTURE_SPACE_AVATAR_P);
        ArrayList<UserSpaceMemberInfoVo> userSpaceMemberInfoVos = new ArrayList<>();
        records.forEach(spaceMemberInfo -> {
            UserSpaceMemberInfoVo userSpaceMemberInfoVo = UserSpaceMemberInfoVo.objToVo(spaceMemberInfo);
            userSpaceMemberInfoVo.setSpaceName(spaceInfo.getSpaceName());
            userSpaceMemberInfoVo.setUserName(userInfoMap.get(spaceMemberInfo.getUserId()).getUserName());
            userSpaceMemberInfoVo.setAvatarUrl(ossConfig.builderUrl(userInfoMap.get(spaceMemberInfo.getUserId()).getAvatarUrl()) + "?x-oss-process=image/resize,p_" + inCache);
            userSpaceMemberInfoVo.setInviterUserName(inviterUserInfoMap.get(spaceMemberInfo.getInviterUserId()).getUserName());
            userSpaceMemberInfoVos.add(userSpaceMemberInfoVo);
        });
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setRows(userSpaceMemberInfoVos);
        tableDataInfo.setTotal(memberInfoPage.getTotal());
        redisCache.setCacheObject(keyData, tableDataInfo, PICTURE_SPACE_MEMBER_DATA_EXPIRE_TIME, TimeUnit.SECONDS);
        return tableDataInfo;
    }

    //删除空间成员缓存
    @Override
    public void deleteSpaceMemberCacheBySpaceId(String spaceId) {
        redisCache.deleteObjectsByPattern(PICTURE_SPACE_MEMBER_DATA + spaceId + "*");
    }

    @Override
    public int userDeleteSpaceMemberInfoByMemberId(String memberId) {
        //查询是否存在
        SpaceMemberInfo spaceMemberInfo = this.selectSpaceMemberInfoByMemberId(memberId);
        ThrowUtils.throwIf(StringUtils.isNull(spaceMemberInfo), "空间成员不存在！！！");
        //如果是创建者则不能删除
        ThrowUtils.throwIf(spaceMemberInfo.getRoleType().equals(PSpaceRoleEnum.SPACE_ROLE_0.getValue()), "创建者不能删除！！！");
        int i = spaceMemberInfoMapper.deleteSpaceMemberInfoByMemberId(memberId);
        if (i == 1) {
            Long spaceMemberNumberCount = this.getSpaceMemberNumberCount(spaceMemberInfo.getSpaceId());
            SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(spaceMemberInfo.getSpaceId());
            if (StringUtils.isNotNull(spaceInfo)) {
                spaceInfoService.updateById(spaceInfo);
                spaceInfo.setCurrentMembers(spaceMemberNumberCount);
            }
        }
        deleteSpaceMemberCacheBySpaceId(spaceMemberInfo.getSpaceId());
        return i;
    }
}
