package com.lz.user.service.impl;

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
import com.lz.user.mapper.UserFriendInfoMapper;
import com.lz.user.model.domain.UserFriendInfo;
import com.lz.user.service.IUserFriendInfoService;
import com.lz.user.model.dto.userFriendInfo.UserFriendInfoQuery;
import com.lz.user.model.vo.userFriendInfo.UserFriendInfoVo;

/**
 * 用户好友关系Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class UserFriendInfoServiceImpl extends ServiceImpl<UserFriendInfoMapper, UserFriendInfo> implements IUserFriendInfoService
{
    @Resource
    private UserFriendInfoMapper userFriendInfoMapper;

    //region mybatis代码
    /**
     * 查询用户好友关系
     *
     * @param relationId 用户好友关系主键
     * @return 用户好友关系
     */
    @Override
    public UserFriendInfo selectUserFriendInfoByRelationId(String relationId)
    {
        return userFriendInfoMapper.selectUserFriendInfoByRelationId(relationId);
    }

    /**
     * 查询用户好友关系列表
     *
     * @param userFriendInfo 用户好友关系
     * @return 用户好友关系
     */
    @Override
    public List<UserFriendInfo> selectUserFriendInfoList(UserFriendInfo userFriendInfo)
    {
        return userFriendInfoMapper.selectUserFriendInfoList(userFriendInfo);
    }

    /**
     * 新增用户好友关系
     *
     * @param userFriendInfo 用户好友关系
     * @return 结果
     */
    @Override
    public int insertUserFriendInfo(UserFriendInfo userFriendInfo)
    {
        userFriendInfo.setCreateTime(DateUtils.getNowDate());
        return userFriendInfoMapper.insertUserFriendInfo(userFriendInfo);
    }

    /**
     * 修改用户好友关系
     *
     * @param userFriendInfo 用户好友关系
     * @return 结果
     */
    @Override
    public int updateUserFriendInfo(UserFriendInfo userFriendInfo)
    {
        return userFriendInfoMapper.updateUserFriendInfo(userFriendInfo);
    }

    /**
     * 批量删除用户好友关系
     *
     * @param relationIds 需要删除的用户好友关系主键
     * @return 结果
     */
    @Override
    public int deleteUserFriendInfoByRelationIds(String[] relationIds)
    {
        return userFriendInfoMapper.deleteUserFriendInfoByRelationIds(relationIds);
    }

    /**
     * 删除用户好友关系信息
     *
     * @param relationId 用户好友关系主键
     * @return 结果
     */
    @Override
    public int deleteUserFriendInfoByRelationId(String relationId)
    {
        return userFriendInfoMapper.deleteUserFriendInfoByRelationId(relationId);
    }
    //endregion
    @Override
    public QueryWrapper<UserFriendInfo> getQueryWrapper(UserFriendInfoQuery userFriendInfoQuery){
        QueryWrapper<UserFriendInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userFriendInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String relationId = userFriendInfoQuery.getRelationId();
        queryWrapper.eq(StringUtils.isNotEmpty(relationId) ,"relation_id",relationId);

    String userId = userFriendInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String friendUserId = userFriendInfoQuery.getFriendUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(friendUserId) ,"friend_user_id",friendUserId);

    Date createTime = userFriendInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<UserFriendInfoVo> convertVoList(List<UserFriendInfo> userFriendInfoList) {
        if (StringUtils.isEmpty(userFriendInfoList)) {
            return Collections.emptyList();
        }
        return userFriendInfoList.stream().map(UserFriendInfoVo::objToVo).collect(Collectors.toList());
    }

}
