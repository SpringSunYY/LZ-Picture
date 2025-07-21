package com.lz.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.config.OssConfig;
import com.lz.common.constant.HttpStatus;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.ParamUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.model.enmus.CTemplateTypeEnum;
import com.lz.picture.mapper.SpaceInvitationInfoMapper;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.domain.SpaceInvitationInfo;
import com.lz.picture.model.domain.SpaceMemberInfo;
import com.lz.picture.model.dto.spaceInvitationInfo.SpaceInvitationInfoQuery;
import com.lz.picture.model.dto.spaceInvitationInfo.UserSpaceInvitationInfoQuery;
import com.lz.picture.model.enums.PSpaceInvitationStatusEnum;
import com.lz.picture.model.enums.PSpaceJoinTypeEnum;
import com.lz.picture.model.enums.PSpaceRoleEnum;
import com.lz.picture.model.vo.spaceInvitationInfo.SpaceInvitationInfoVo;
import com.lz.picture.model.vo.spaceInvitationInfo.UserSpaceInvitationInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.picture.service.ISpaceInvitationInfoService;
import com.lz.picture.service.ISpaceMemberInfoService;
import com.lz.user.manager.UserAsyncManager;
import com.lz.user.manager.factory.InformInfoAsyncFactory;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.enums.UInformTypeEnum;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.lz.common.constant.config.TemplateInfoKeyConstants.PICTURE_SPACE_INVITATION;
import static com.lz.common.constant.config.TemplateInfoKeyConstants.PICTURE_SPACE_INVITATION_SUCCESS;
import static com.lz.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;
import static com.lz.config.utils.ConfigInfoUtils.PICTURE_COVER_P_VALUE;

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
    private ISpaceMemberInfoService spaceMemberInfoService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private OssConfig ossConfig;

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
                && !roleType.equals(PSpaceRoleEnum.SPACE_ROLE_2.getValue()), HttpStatus.BAD_REQUEST, "角色类型错误");
        //查询空间是否是否存在
        SpaceInfo spaceInfo = spaceInfoService.selectNormalSpaceInfoBySpaceId(spaceInvitationInfo.getSpaceId());
        ThrowUtils.throwIf(StringUtils.isNull(spaceInfo) || !spaceInfo.getUserId().equals(spaceInvitationInfo.getInvitationUserId()), "空间不存在");
        UserInfo userInfo = userInfoService.selectUserByUserName(spaceInvitationInfo.getUserName());
        ThrowUtils.throwIf(StringUtils.isNull(userInfo), "用户不存在");

        spaceInvitationInfo.setUserId(userInfo.getUserId());
        spaceInvitationInfo.setSpaceName(spaceInfo.getSpaceName());
        spaceInvitationInfo.setSpaceAvatar(spaceInfo.getSpaceAvatar());
        spaceInvitationInfo.setInvitationStatus(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_0.getValue());
        spaceInvitationInfo.setCreateTime(DateUtils.getNowDate());
        spaceInvitationInfo.setExpireTime(DateUtils.addDays(DateUtils.getNowDate(), 7));
        String invitationId = IdUtils.snowflakeId().toString();
        spaceInvitationInfo.setInvitationId(invitationId);
        /*
        {
          "invitationId": "INV-20250626002",
          "spaceName": "前端开发协作空间",
          "roleType": "管理员",
          "createTime": "2025-06-26 21:10:00"
        }
         */
        HashMap<String, String> params = new HashMap();
        params.put("invitationId", invitationId);
        params.put("spaceName", spaceInfo.getSpaceName());
        Optional<PSpaceRoleEnum> enumByValue = PSpaceRoleEnum.getEnumByValue(spaceInvitationInfo.getRoleType());
        if (enumByValue.isPresent()) {
            params.put("roleType", enumByValue.get().getLabel());
        } else {
            params.put("roleType", "未知角色"); // 默认值
        }
        params.put("createTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, spaceInvitationInfo.getCreateTime()));
        UserAsyncManager.me().execute(InformInfoAsyncFactory.sendInform(spaceInvitationInfo.getUserId(),
                PICTURE_SPACE_INVITATION,
                null,
                CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue(),
                UInformTypeEnum.INFORM_TYPE_0.getValue(),
                params));
        return spaceInvitationInfoMapper.insertSpaceInvitationInfo(spaceInvitationInfo);
    }

    @Override
    public TableDataInfo listUserSpaceInvitationInfoTable(UserSpaceInvitationInfoQuery userSpaceInvitationInfoQuery) {
        //构造查询条件
        Page<SpaceInvitationInfo> page = new Page<>();
        page.setCurrent(userSpaceInvitationInfoQuery.getPageNum());
        page.setSize(userSpaceInvitationInfoQuery.getPageSize());
        String beginCreateTime = ParamUtils.getSafeString(userSpaceInvitationInfoQuery, ParamUtils.BEGIN_CREATE_TIME);
        String endCreateTime = ParamUtils.getSafeString(userSpaceInvitationInfoQuery, ParamUtils.END_CREATE_TIME);
        //构造查询条件
        LambdaQueryWrapper<SpaceInvitationInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StringUtils.isNotEmpty(userSpaceInvitationInfoQuery.getUserId()), SpaceInvitationInfo::getUserId, userSpaceInvitationInfoQuery.getUserId())
                .eq(StringUtils.isNotEmpty(userSpaceInvitationInfoQuery.getInvitationUserId()), SpaceInvitationInfo::getInvitationUserId, userSpaceInvitationInfoQuery.getInvitationUserId())
                .like(StringUtils.isNotEmpty(userSpaceInvitationInfoQuery.getSpaceName()), SpaceInvitationInfo::getSpaceName, userSpaceInvitationInfoQuery.getSpaceName())
                .eq(StringUtils.isNotEmpty(userSpaceInvitationInfoQuery.getInvitationStatus()), SpaceInvitationInfo::getInvitationStatus, userSpaceInvitationInfoQuery.getInvitationStatus())
                .eq(StringUtils.isNotEmpty(userSpaceInvitationInfoQuery.getRoleType()), SpaceInvitationInfo::getRoleType, userSpaceInvitationInfoQuery.getRoleType())
                .apply(
                        StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                        "create_time between {0} and {1}",
                        beginCreateTime,
                        endCreateTime
                );
        //构造排序
        if (StringUtils.isNotEmpty(userSpaceInvitationInfoQuery.getIsAsc()) && StringUtils.isNotEmpty(userSpaceInvitationInfoQuery.getOrderByColumn())
                && Arrays.asList("createTime", "expireTime").contains(userSpaceInvitationInfoQuery.getOrderByColumn())) {
            if (userSpaceInvitationInfoQuery.getOrderByColumn().equals("createTime")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInvitationInfoQuery.getIsAsc().equals("asc"), SpaceInvitationInfo::getCreateTime);
            } else if (userSpaceInvitationInfoQuery.getOrderByColumn().equals("expireTime")) {
                lambdaQueryWrapper
                        .orderBy(true, userSpaceInvitationInfoQuery.getIsAsc().equals("asc"), SpaceInvitationInfo::getExpireTime);
            }
        } else {
            lambdaQueryWrapper
                    .orderBy(true, false, SpaceInvitationInfo::getCreateTime);
        }
        Page<SpaceInvitationInfo> spaceInvitationInfoPage = this.page(page, lambdaQueryWrapper);
        //压缩图片
        page.getRecords().forEach(spaceInvitationInfo -> {
            spaceInvitationInfo.setSpaceAvatar(ossConfig.builderUrl(spaceInvitationInfo.getSpaceAvatar()) + "?x-oss-process=image/resize,p_" + PICTURE_COVER_P_VALUE);
        });
        return new TableDataInfo(UserSpaceInvitationInfoVo.objToVo(spaceInvitationInfoPage.getRecords()), (int) spaceInvitationInfoPage.getTotal());
    }

    @Override
    public int userActionSpaceInvitationInfo(SpaceInvitationInfo spaceInvitationInfo) {
        //此处接收同意拒绝
        ThrowUtils.throwIf(!spaceInvitationInfo.getInvitationStatus().equals(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_1.getValue())
                        && !spaceInvitationInfo.getInvitationStatus().equals(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_2.getValue()),
                HttpStatus.BAD_REQUEST,
                "操作参数有误！！！");
        Date nowDate = DateUtils.getNowDate();
        //首先查询是否存在
        SpaceInvitationInfo db = spaceInvitationInfoMapper.selectSpaceInvitationInfoByInvitationId(spaceInvitationInfo.getInvitationId());
        ThrowUtils.throwIf(StringUtils.isNull(db) || !db.getUserId().equals(spaceInvitationInfo.getUserId()), "邀请不存在");
        ThrowUtils.throwIf(!db.getInvitationStatus().equals(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_0.getValue()), "邀请已处理");
        if (db.getExpireTime().before(nowDate)) {
            spaceInvitationInfo.setInvitationStatus(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_3.getValue());
            spaceInvitationInfoMapper.updateSpaceInvitationInfo(spaceInvitationInfo);
            throw new ServiceException("邀请已过期，已为您给状态更新为过期！！！");
        }
        SpaceInfo spaceInfo = spaceInfoService.selectNormalSpaceInfoBySpaceId(db.getSpaceId());
        ThrowUtils.throwIf(StringUtils.isNull(spaceInfo), "空间不存在");
        //如果用户已经是此空间成员一律更新为已过期
        SpaceMemberInfo spaceMemberInfo = new SpaceMemberInfo();
        spaceMemberInfo.setSpaceId(db.getSpaceId());
        spaceMemberInfo.setUserId(db.getUserId());
        List<SpaceMemberInfo> spaceMemberInfos = spaceMemberInfoService.selectSpaceMemberInfoList(spaceMemberInfo);
        if (StringUtils.isNotEmpty(spaceMemberInfos)) {
            spaceInvitationInfo.setInvitationStatus(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_4.getValue());
            spaceInvitationInfoMapper.updateSpaceInvitationInfo(spaceInvitationInfo);
            throw new ServiceException("您已经是空间成员，已为您给状态更新为已取消！！！");
        }
        //如果是同意
        if (spaceInvitationInfo.getInvitationStatus().equals(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_1.getValue())) {
            //如果用户已经是此空间成员
            ThrowUtils.throwIf(spaceInfo.getCurrentMembers() >= spaceInfo.getMemberLimit(), "空间成员已满");
            Long spaceMemberNumberCount = spaceMemberInfoService.getSpaceMemberNumberCount(spaceInfo.getSpaceId());
            ThrowUtils.throwIf(spaceMemberNumberCount >= spaceInfo.getMemberLimit(), "空间成员已满");
            spaceInfo.setCurrentMembers(spaceMemberNumberCount + 1);
            //添加成员信息
            spaceMemberInfo.setRoleType(db.getRoleType());
            spaceMemberInfo.setInviterUserId(db.getInvitationUserId());
            spaceMemberInfo.setJoinType(PSpaceJoinTypeEnum.SPACE_JOIN_TYPE_1.getValue());
            spaceMemberInfo.setCreateTime(nowDate);
            Boolean execute = transactionTemplate.execute(res -> {
                this.updateById(spaceInvitationInfo);
                spaceMemberInfoService.save(spaceMemberInfo);
                return spaceInfoService.updateById(spaceInfo);
            });
            /*
                {
                 "userName":"YY",
                  "invitationId": "INV-20250626002",
                  "spaceName": "前端开发协作空间",
                  "roleType": "管理员",
                  "createTime": "2025-06-26 21:10:00",
                   "joinTime": "2025-06-26 21:10:00"
                }
            */
            HashMap<String, String> params = new HashMap();
            UserInfo userInfo = userInfoService.selectUserInfoByUserId(db.getUserId());
            params.put("userName", userInfo.getUserName());
            params.put("invitationId", spaceInvitationInfo.getInvitationId());
            params.put("spaceName", spaceInfo.getSpaceName());
            Optional<PSpaceRoleEnum> enumByValue = PSpaceRoleEnum.getEnumByValue(spaceInvitationInfo.getRoleType());
            if (enumByValue.isPresent()) {
                params.put("roleType", enumByValue.get().getLabel());
            } else {
                params.put("roleType", "未知角色"); // 默认值
            }
            params.put("createTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, db.getCreateTime()));
            params.put("joinTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS, nowDate));
            UserAsyncManager.me().execute(InformInfoAsyncFactory.sendInform(db.getInvitationUserId(),
                    PICTURE_SPACE_INVITATION_SUCCESS,
                    null,
                    CTemplateTypeEnum.TEMPLATE_TYPE_3.getValue(),
                    UInformTypeEnum.INFORM_TYPE_0.getValue(),
                    params));
            spaceInfoService.deleteSpaceTeamTableCacheByUserId(db.getInvitationUserId());
            spaceInfoService.deleteSpaceTeamTableCacheByUserId(db.getUserId());
            spaceMemberInfoService.deleteSpaceMemberCacheBySpaceId(db.getSpaceId());
            return StringUtils.isNotNull(execute) && execute ? 1 : 0;
        } else {
            return spaceInvitationInfoMapper.updateSpaceInvitationInfo(spaceInvitationInfo);
        }
    }

    @Override
    public int userCancelSpaceInvitationInfoByInvitationId(String invitationId, String userId) {
        //查询到对应的邀请信息
        SpaceInvitationInfo spaceInvitationInfo = spaceInvitationInfoMapper.selectSpaceInvitationInfoByInvitationId(invitationId);
        ThrowUtils.throwIf(StringUtils.isNull(spaceInvitationInfo), "邀请不存在");
        ThrowUtils.throwIf(!spaceInvitationInfo.getInvitationUserId().equals(userId), "您没有权限取消此邀请");
        ThrowUtils.throwIf(!spaceInvitationInfo.getInvitationStatus().equals(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_0.getValue()), "邀请已处理");
        spaceInvitationInfo.setInvitationStatus(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_4.getValue());
        return spaceInvitationInfoMapper.updateSpaceInvitationInfo(spaceInvitationInfo);
    }

    @Override
    public int userDeleteSpaceInvitationInfoByInvitationId(String invitationId, String userId) {
        //查询到对应的邀请信息
        SpaceInvitationInfo spaceInvitationInfo = spaceInvitationInfoMapper.selectSpaceInvitationInfoByInvitationId(invitationId);
        ThrowUtils.throwIf(StringUtils.isNull(spaceInvitationInfo), "邀请不存在");
        ThrowUtils.throwIf(!spaceInvitationInfo.getInvitationUserId().equals(userId) && !spaceInvitationInfo.getUserId().equals(userId), "您没有权限取消此邀请");
        ThrowUtils.throwIf(!spaceInvitationInfo.getInvitationStatus().equals(PSpaceInvitationStatusEnum.SPACE_INVITATION_STATUS_0.getValue()), "邀请已处理");
        return spaceInvitationInfoMapper.deleteSpaceInvitationInfoByInvitationId(invitationId);
    }

}
