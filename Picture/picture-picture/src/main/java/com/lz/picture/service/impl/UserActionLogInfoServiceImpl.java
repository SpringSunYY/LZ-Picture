package com.lz.picture.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.picture.mapper.UserActionLogInfoMapper;
import com.lz.picture.model.domain.UserActionLogInfo;
import com.lz.picture.service.IUserActionLogInfoService;
import com.lz.picture.model.dto.userActionLogInfo.UserActionLogInfoQuery;
import com.lz.picture.model.vo.userActionLogInfo.UserActionLogInfoVo;

/**
 * 用户行为日志Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class UserActionLogInfoServiceImpl extends ServiceImpl<UserActionLogInfoMapper, UserActionLogInfo> implements IUserActionLogInfoService {
    @Resource
    private UserActionLogInfoMapper userActionLogInfoMapper;

    //region mybatis代码

    /**
     * 查询用户行为日志
     *
     * @param actionId 用户行为日志主键
     * @return 用户行为日志
     */
    @Override
    public UserActionLogInfo selectUserActionLogInfoByActionId(String actionId) {
        return userActionLogInfoMapper.selectUserActionLogInfoByActionId(actionId);
    }

    /**
     * 查询用户行为日志列表
     *
     * @param userActionLogInfo 用户行为日志
     * @return 用户行为日志
     */
    @Override
    public List<UserActionLogInfo> selectUserActionLogInfoList(UserActionLogInfo userActionLogInfo) {
        return userActionLogInfoMapper.selectUserActionLogInfoList(userActionLogInfo);
    }

    /**
     * 新增用户行为日志
     *
     * @param userActionLogInfo 用户行为日志
     * @return 结果
     */
    @Override
    public int insertUserActionLogInfo(UserActionLogInfo userActionLogInfo) {
        userActionLogInfo.setCreateTime(DateUtils.getNowDate());
        return userActionLogInfoMapper.insertUserActionLogInfo(userActionLogInfo);
    }

    /**
     * 修改用户行为日志
     *
     * @param userActionLogInfo 用户行为日志
     * @return 结果
     */
    @Override
    public int updateUserActionLogInfo(UserActionLogInfo userActionLogInfo) {
        return userActionLogInfoMapper.updateUserActionLogInfo(userActionLogInfo);
    }

    /**
     * 批量删除用户行为日志
     *
     * @param actionIds 需要删除的用户行为日志主键
     * @return 结果
     */
    @Override
    public int deleteUserActionLogInfoByActionIds(String[] actionIds) {
        return userActionLogInfoMapper.deleteUserActionLogInfoByActionIds(actionIds);
    }

    /**
     * 删除用户行为日志信息
     *
     * @param actionId 用户行为日志主键
     * @return 结果
     */
    @Override
    public int deleteUserActionLogInfoByActionId(String actionId) {
        return userActionLogInfoMapper.deleteUserActionLogInfoByActionId(actionId);
    }

    //endregion
    @Override
    public QueryWrapper<UserActionLogInfo> getQueryWrapper(UserActionLogInfoQuery userActionLogInfoQuery) {
        QueryWrapper<UserActionLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userActionLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String actionId = userActionLogInfoQuery.getActionId();
        queryWrapper.eq(StringUtils.isNotEmpty(actionId), "action_id", actionId);

        String searchId = userActionLogInfoQuery.getSearchId();
        queryWrapper.eq(StringUtils.isNotEmpty(searchId), "search_id", searchId);

        String userId = userActionLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String actionType = userActionLogInfoQuery.getActionType();
        queryWrapper.eq(StringUtils.isNotEmpty(actionType), "action_type", actionType);

        String targetType = userActionLogInfoQuery.getTargetType();
        queryWrapper.eq(StringUtils.isNotEmpty(targetType), "target_type", targetType);

        String targetId = userActionLogInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId), "target_id", targetId);

        Date createTime = userActionLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        String deviceId = userActionLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = userActionLogInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = userActionLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os), "os", os);

        String platform = userActionLogInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddress = userActionLogInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        return queryWrapper;
    }

    @Override
    public List<UserActionLogInfoVo> convertVoList(List<UserActionLogInfo> userActionLogInfoList) {
        if (StringUtils.isEmpty(userActionLogInfoList)) {
            return Collections.emptyList();
        }
        return userActionLogInfoList.stream().map(UserActionLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
