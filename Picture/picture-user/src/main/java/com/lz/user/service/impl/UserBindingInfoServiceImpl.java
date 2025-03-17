package com.lz.user.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.user.mapper.UserBindingInfoMapper;
import com.lz.user.model.domain.UserBindingInfo;
import com.lz.user.service.IUserBindingInfoService;
import com.lz.user.model.dto.userBindingInfo.UserBindingInfoQuery;
import com.lz.user.model.vo.userBindingInfo.UserBindingInfoVo;

/**
 * 用户第三方账号绑定Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class UserBindingInfoServiceImpl extends ServiceImpl<UserBindingInfoMapper, UserBindingInfo> implements IUserBindingInfoService
{
    @Resource
    private UserBindingInfoMapper userBindingInfoMapper;

    //region mybatis代码
    /**
     * 查询用户第三方账号绑定
     *
     * @param bindingId 用户第三方账号绑定主键
     * @return 用户第三方账号绑定
     */
    @Override
    public UserBindingInfo selectUserBindingInfoByBindingId(String bindingId)
    {
        return userBindingInfoMapper.selectUserBindingInfoByBindingId(bindingId);
    }

    /**
     * 查询用户第三方账号绑定列表
     *
     * @param userBindingInfo 用户第三方账号绑定
     * @return 用户第三方账号绑定
     */
    @Override
    public List<UserBindingInfo> selectUserBindingInfoList(UserBindingInfo userBindingInfo)
    {
        return userBindingInfoMapper.selectUserBindingInfoList(userBindingInfo);
    }

    /**
     * 新增用户第三方账号绑定
     *
     * @param userBindingInfo 用户第三方账号绑定
     * @return 结果
     */
    @Override
    public int insertUserBindingInfo(UserBindingInfo userBindingInfo)
    {
        return userBindingInfoMapper.insertUserBindingInfo(userBindingInfo);
    }

    /**
     * 修改用户第三方账号绑定
     *
     * @param userBindingInfo 用户第三方账号绑定
     * @return 结果
     */
    @Override
    public int updateUserBindingInfo(UserBindingInfo userBindingInfo)
    {
        return userBindingInfoMapper.updateUserBindingInfo(userBindingInfo);
    }

    /**
     * 批量删除用户第三方账号绑定
     *
     * @param bindingIds 需要删除的用户第三方账号绑定主键
     * @return 结果
     */
    @Override
    public int deleteUserBindingInfoByBindingIds(String[] bindingIds)
    {
        return userBindingInfoMapper.deleteUserBindingInfoByBindingIds(bindingIds);
    }

    /**
     * 删除用户第三方账号绑定信息
     *
     * @param bindingId 用户第三方账号绑定主键
     * @return 结果
     */
    @Override
    public int deleteUserBindingInfoByBindingId(String bindingId)
    {
        return userBindingInfoMapper.deleteUserBindingInfoByBindingId(bindingId);
    }
    //endregion
    @Override
    public QueryWrapper<UserBindingInfo> getQueryWrapper(UserBindingInfoQuery userBindingInfoQuery){
        QueryWrapper<UserBindingInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userBindingInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String bindingId = userBindingInfoQuery.getBindingId();
        queryWrapper.eq(StringUtils.isNotEmpty(bindingId) ,"binding_id",bindingId);

    String userId = userBindingInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String bindingType = userBindingInfoQuery.getBindingType();
        queryWrapper.eq(StringUtils.isNotEmpty(bindingType) ,"binding_type",bindingType);

    Date bindingTime = userBindingInfoQuery.getBindingTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginBindingTime"))&&StringUtils.isNotNull(params.get("endBindingTime")),"binding_time",params.get("beginBindingTime"),params.get("endBindingTime"));

        return queryWrapper;
    }

    @Override
    public List<UserBindingInfoVo> convertVoList(List<UserBindingInfo> userBindingInfoList) {
        if (StringUtils.isEmpty(userBindingInfoList)) {
            return Collections.emptyList();
        }
        return userBindingInfoList.stream().map(UserBindingInfoVo::objToVo).collect(Collectors.toList());
    }

}
