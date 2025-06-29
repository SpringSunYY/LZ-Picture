package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.constant.HttpStatus;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.picture.mapper.SpaceInvitationInfoMapper;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.domain.SpaceInvitationInfo;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoQuery;
import com.lz.picture.model.enums.PSpaceInvitationStatusEnum;
import com.lz.picture.model.enums.PSpaceRoleEnum;
import com.lz.picture.model.vo.spaceInvitationInfo.SpaceInvitationInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.picture.service.ISpaceInvitationInfoService;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 空间成员邀请记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class SpaceInvitationInfoServiceImpl extends ServiceImpl<SpaceInvitationInfoMapper, SpaceInvitationInfo> implements ISpaceInvitationInfoService {
    @Resource
    private SpaceInvitationInfoMapper spaceInvitationInfoMapper;

    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private IUserInfoService userInfoService;

    //region mybatis代码

    /**
     * 查询空间成员邀请记录
     *
     * @param invitationId 空间成员邀请记录主键
     * @return 空间成员邀请记录
     */
    @Override
    public SpaceInvitationInfo selectSpaceInvitationInfoByInvitationId(String invitationId) {
        return spaceInvitationInfoMapper.selectSpaceInvitationInfoByInvitationId(invitationId);
    }

    /**
     * 查询空间成员邀请记录列表
     *
     * @param spaceInvitationInfo 空间成员邀请记录
     * @return 空间成员邀请记录
     */
    @Override
    public List<SpaceInvitationInfo> selectSpaceInvitationInfoList(SpaceInvitationInfo spaceInvitationInfo) {
        return spaceInvitationInfoMapper.selectSpaceInvitationInfoList(spaceInvitationInfo);
    }

    /**
     * 新增空间成员邀请记录
     *
     * @param spaceInvitationInfo 空间成员邀请记录
     * @return 结果
     */
    @Override
    public int insertSpaceInvitationInfo(SpaceInvitationInfo spaceInvitationInfo) {
        spaceInvitationInfo.setCreateTime(DateUtils.getNowDate());
        return spaceInvitationInfoMapper.insertSpaceInvitationInfo(spaceInvitationInfo);
    }

    /**
     * 修改空间成员邀请记录
     *
     * @param spaceInvitationInfo 空间成员邀请记录
     * @return 结果
     */
    @Override
    public int updateSpaceInvitationInfo(SpaceInvitationInfo spaceInvitationInfo) {
        return spaceInvitationInfoMapper.updateSpaceInvitationInfo(spaceInvitationInfo);
    }

    /**
     * 批量删除空间成员邀请记录
     *
     * @param invitationIds 需要删除的空间成员邀请记录主键
     * @return 结果
     */
    @Override
    public int deleteSpaceInvitationInfoByInvitationIds(String[] invitationIds) {
        return spaceInvitationInfoMapper.deleteSpaceInvitationInfoByInvitationIds(invitationIds);
    }

    /**
     * 删除空间成员邀请记录信息
     *
     * @param invitationId 空间成员邀请记录主键
     * @return 结果
     */
    @Override
    public int deleteSpaceInvitationInfoByInvitationId(String invitationId) {
        return spaceInvitationInfoMapper.deleteSpaceInvitationInfoByInvitationId(invitationId);
    }

    //endregion
    @Override
    public QueryWrapper<SpaceInvitationInfo> getQueryWrapper(SpaceInvitationInfoQuery spaceInvitationInfoQuery) {
        QueryWrapper<SpaceInvitationInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = spaceInvitationInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String invitationId = spaceInvitationInfoQuery.getInvitationId();
        queryWrapper.eq(StringUtils.isNotEmpty(invitationId), "invitation_id", invitationId);

        String spaceId = spaceInvitationInfoQuery.getSpaceId();
        queryWrapper.eq(StringUtils.isNotEmpty(spaceId), "space_id", spaceId);

        String spaceName = spaceInvitationInfoQuery.getSpaceName();
        queryWrapper.like(StringUtils.isNotEmpty(spaceName), "space_name", spaceName);

        String roleType = spaceInvitationInfoQuery.getRoleType();
        queryWrapper.eq(StringUtils.isNotEmpty(roleType), "role_type", roleType);

        String invitationStatus = spaceInvitationInfoQuery.getInvitationStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(invitationStatus), "invitation_status", invitationStatus);

        String invitationUserId = spaceInvitationInfoQuery.getInvitationUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(invitationUserId), "invitation_user_id", invitationUserId);

        Date expireTime = spaceInvitationInfoQuery.getExpireTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginExpireTime")) && StringUtils.isNotNull(params.get("endExpireTime")), "expire_time", params.get("beginExpireTime"), params.get("endExpireTime"));

        Date createTime = spaceInvitationInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String userId = spaceInvitationInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        return queryWrapper;
    }

    @Override
    public List<SpaceInvitationInfoVo> convertVoList(List<SpaceInvitationInfo> spaceInvitationInfoList) {
        if (StringUtils.isEmpty(spaceInvitationInfoList)) {
            return Collections.emptyList();
        }
        return spaceInvitationInfoList.stream().map(SpaceInvitationInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public int userInsertSpaceInvitationInfo(SpaceInvitationInfo spaceInvitationInfo) {
        //判断角色是否是允许的状态
        String roleType = spaceInvitationInfo.getRoleType();
        ThrowUtils.throwIf(!roleType.equals(PSpaceRoleEnum.SPACE_ROLE_1.getValue())
                && !roleType.equals(PSpaceRoleEnum.SPACE_ROLE_2.getValue())
                && !roleType.equals(PSpaceRoleEnum.SPACE_ROLE_3.getValue()), HttpStatus.BAD_REQUEST, "角色类型错误");
        //查询空间是否是否存在
        SpaceInfo spaceInfo = spaceInfoService.selectNormalSpaceInfoByUserId(spaceInvitationInfo.getSpaceId());
        ThrowUtils.throwIf(StringUtils.isNull(spaceInfo) || !spaceInfo.getUserId().equals(spaceInvitationInfo.getInvitationUserId()), "空间不存在");
        UserInfo userInfo = userInfoService.selectUserByUserName(spaceInvitationInfo.getUserName());
        ThrowUtils.throwIf(StringUtils.isNull(userInfo), "用户不存在");

        spaceInvitationInfo.setUserId(userInfo.getUserId());
        spaceInvitationInfo.setSpaceName(spaceInfo.getSpaceName());
        spaceInvitationInfo.setSpaceAvatar(spaceInfo.getSpaceAvatar());
        spaceInvitationInfo.setInvitationStatus(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_0.getValue());
        spaceInvitationInfo.setCreateTime(DateUtils.getNowDate());
        spaceInvitationInfo.setExpireTime(DateUtils.addDays(DateUtils.getNowDate(), 7));
        spaceInvitationInfo.setInvitationId(IdUtils.snowflakeId().toString());
        return spaceInvitationInfoMapper.insertSpaceInvitationInfo(spaceInvitationInfo);
    }

}
