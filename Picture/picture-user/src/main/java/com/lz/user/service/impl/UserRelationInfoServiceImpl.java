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
import com.lz.user.mapper.UserRelationInfoMapper;
import com.lz.user.model.domain.UserRelationInfo;
import com.lz.user.service.IUserRelationInfoService;
import com.lz.user.model.dto.userRelationInfo.UserRelationInfoQuery;
import com.lz.user.model.vo.userRelationInfo.UserRelationInfoVo;

/**
 * 用户关系Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class UserRelationInfoServiceImpl extends ServiceImpl<UserRelationInfoMapper, UserRelationInfo> implements IUserRelationInfoService
{
    @Resource
    private UserRelationInfoMapper userRelationInfoMapper;

    //region mybatis代码
    /**
     * 查询用户关系
     *
     * @param relationId 用户关系主键
     * @return 用户关系
     */
    @Override
    public UserRelationInfo selectUserRelationInfoByRelationId(String relationId)
    {
        return userRelationInfoMapper.selectUserRelationInfoByRelationId(relationId);
    }

    /**
     * 查询用户关系列表
     *
     * @param userRelationInfo 用户关系
     * @return 用户关系
     */
    @Override
    public List<UserRelationInfo> selectUserRelationInfoList(UserRelationInfo userRelationInfo)
    {
        return userRelationInfoMapper.selectUserRelationInfoList(userRelationInfo);
    }

    /**
     * 新增用户关系
     *
     * @param userRelationInfo 用户关系
     * @return 结果
     */
    @Override
    public int insertUserRelationInfo(UserRelationInfo userRelationInfo)
    {
        userRelationInfo.setCreateTime(DateUtils.getNowDate());
        return userRelationInfoMapper.insertUserRelationInfo(userRelationInfo);
    }

    /**
     * 修改用户关系
     *
     * @param userRelationInfo 用户关系
     * @return 结果
     */
    @Override
    public int updateUserRelationInfo(UserRelationInfo userRelationInfo)
    {
        return userRelationInfoMapper.updateUserRelationInfo(userRelationInfo);
    }

    /**
     * 批量删除用户关系
     *
     * @param relationIds 需要删除的用户关系主键
     * @return 结果
     */
    @Override
    public int deleteUserRelationInfoByRelationIds(String[] relationIds)
    {
        return userRelationInfoMapper.deleteUserRelationInfoByRelationIds(relationIds);
    }

    /**
     * 删除用户关系信息
     *
     * @param relationId 用户关系主键
     * @return 结果
     */
    @Override
    public int deleteUserRelationInfoByRelationId(String relationId)
    {
        return userRelationInfoMapper.deleteUserRelationInfoByRelationId(relationId);
    }
    //endregion
    @Override
    public QueryWrapper<UserRelationInfo> getQueryWrapper(UserRelationInfoQuery userRelationInfoQuery){
        QueryWrapper<UserRelationInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userRelationInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String relationId = userRelationInfoQuery.getRelationId();
        queryWrapper.eq(StringUtils.isNotEmpty(relationId) ,"relation_id",relationId);

    String userId = userRelationInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String relationUserId = userRelationInfoQuery.getRelationUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(relationUserId) ,"relation_user_id",relationUserId);

    String relationType = userRelationInfoQuery.getRelationType();
        queryWrapper.eq(StringUtils.isNotEmpty(relationType) ,"relation_type",relationType);

    Date createTime = userRelationInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<UserRelationInfoVo> convertVoList(List<UserRelationInfo> userRelationInfoList) {
        if (StringUtils.isEmpty(userRelationInfoList)) {
            return Collections.emptyList();
        }
        return userRelationInfoList.stream().map(UserRelationInfoVo::objToVo).collect(Collectors.toList());
    }

}
