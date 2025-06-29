package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.picture.mapper.SpaceMemberInfoMapper;
import com.lz.picture.model.domain.SpaceMemberInfo;
import com.lz.picture.model.dto.spaceMemberInfo.SpaceMemberInfoQuery;
import com.lz.picture.model.vo.spaceMemberInfo.SpaceMemberInfoVo;
import com.lz.picture.service.ISpaceMemberInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
}
