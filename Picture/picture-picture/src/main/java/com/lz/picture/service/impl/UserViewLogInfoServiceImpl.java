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
import com.lz.picture.mapper.UserViewLogInfoMapper;
import com.lz.picture.model.domain.UserViewLogInfo;
import com.lz.picture.service.IUserViewLogInfoService;
import com.lz.picture.model.dto.userViewLogInfo.UserViewLogInfoQuery;
import com.lz.picture.model.vo.userViewLogInfo.UserViewLogInfoVo;

/**
 * 用户浏览记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-24
 */
@Service
public class UserViewLogInfoServiceImpl extends ServiceImpl<UserViewLogInfoMapper, UserViewLogInfo> implements IUserViewLogInfoService
{
    @Resource
    private UserViewLogInfoMapper userViewLogInfoMapper;

    //region mybatis代码
    /**
     * 查询用户浏览记录
     *
     * @param viewId 用户浏览记录主键
     * @return 用户浏览记录
     */
    @Override
    public UserViewLogInfo selectUserViewLogInfoByViewId(String viewId)
    {
        return userViewLogInfoMapper.selectUserViewLogInfoByViewId(viewId);
    }

    /**
     * 查询用户浏览记录列表
     *
     * @param userViewLogInfo 用户浏览记录
     * @return 用户浏览记录
     */
    @Override
    public List<UserViewLogInfo> selectUserViewLogInfoList(UserViewLogInfo userViewLogInfo)
    {
        return userViewLogInfoMapper.selectUserViewLogInfoList(userViewLogInfo);
    }

    /**
     * 新增用户浏览记录
     *
     * @param userViewLogInfo 用户浏览记录
     * @return 结果
     */
    @Override
    public int insertUserViewLogInfo(UserViewLogInfo userViewLogInfo)
    {
        userViewLogInfo.setCreateTime(DateUtils.getNowDate());
        return userViewLogInfoMapper.insertUserViewLogInfo(userViewLogInfo);
    }

    /**
     * 修改用户浏览记录
     *
     * @param userViewLogInfo 用户浏览记录
     * @return 结果
     */
    @Override
    public int updateUserViewLogInfo(UserViewLogInfo userViewLogInfo)
    {
        return userViewLogInfoMapper.updateUserViewLogInfo(userViewLogInfo);
    }

    /**
     * 批量删除用户浏览记录
     *
     * @param viewIds 需要删除的用户浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteUserViewLogInfoByViewIds(String[] viewIds)
    {
        return userViewLogInfoMapper.deleteUserViewLogInfoByViewIds(viewIds);
    }

    /**
     * 删除用户浏览记录信息
     *
     * @param viewId 用户浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteUserViewLogInfoByViewId(String viewId)
    {
        return userViewLogInfoMapper.deleteUserViewLogInfoByViewId(viewId);
    }
    //endregion
    @Override
    public QueryWrapper<UserViewLogInfo> getQueryWrapper(UserViewLogInfoQuery userViewLogInfoQuery){
        QueryWrapper<UserViewLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userViewLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String viewId = userViewLogInfoQuery.getViewId();
        queryWrapper.eq(StringUtils.isNotEmpty(viewId) ,"view_id",viewId);

    String userId = userViewLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String targetType = userViewLogInfoQuery.getTargetType();
        queryWrapper.eq(StringUtils.isNotEmpty(targetType) ,"target_type",targetType);

    String targetId = userViewLogInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId) ,"target_id",targetId);

    String categoryId = userViewLogInfoQuery.getCategoryId();
        queryWrapper.eq(StringUtils.isNotEmpty(categoryId) ,"category_id",categoryId);

    String tags = userViewLogInfoQuery.getTags();
        queryWrapper.eq(StringUtils.isNotEmpty(tags) ,"tags",tags);

    Date createTime = userViewLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<UserViewLogInfoVo> convertVoList(List<UserViewLogInfo> userViewLogInfoList) {
        if (StringUtils.isEmpty(userViewLogInfoList)) {
            return Collections.emptyList();
        }
        return userViewLogInfoList.stream().map(UserViewLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
