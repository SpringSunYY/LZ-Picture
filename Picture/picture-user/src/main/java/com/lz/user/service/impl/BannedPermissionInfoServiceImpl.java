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
import com.lz.user.mapper.BannedPermissionInfoMapper;
import com.lz.user.model.domain.BannedPermissionInfo;
import com.lz.user.service.IBannedPermissionInfoService;
import com.lz.user.model.dto.bannedPermissionInfo.BannedPermissionInfoQuery;
import com.lz.user.model.vo.bannedPermissionInfo.BannedPermissionInfoVo;

/**
 * 用户封禁权限Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class BannedPermissionInfoServiceImpl extends ServiceImpl<BannedPermissionInfoMapper, BannedPermissionInfo> implements IBannedPermissionInfoService
{
    @Resource
    private BannedPermissionInfoMapper bannedPermissionInfoMapper;

    //region mybatis代码
    /**
     * 查询用户封禁权限
     *
     * @param bannedId 用户封禁权限主键
     * @return 用户封禁权限
     */
    @Override
    public BannedPermissionInfo selectBannedPermissionInfoByBannedId(String bannedId)
    {
        return bannedPermissionInfoMapper.selectBannedPermissionInfoByBannedId(bannedId);
    }

    /**
     * 查询用户封禁权限列表
     *
     * @param bannedPermissionInfo 用户封禁权限
     * @return 用户封禁权限
     */
    @Override
    public List<BannedPermissionInfo> selectBannedPermissionInfoList(BannedPermissionInfo bannedPermissionInfo)
    {
        return bannedPermissionInfoMapper.selectBannedPermissionInfoList(bannedPermissionInfo);
    }

    /**
     * 新增用户封禁权限
     *
     * @param bannedPermissionInfo 用户封禁权限
     * @return 结果
     */
    @Override
    public int insertBannedPermissionInfo(BannedPermissionInfo bannedPermissionInfo)
    {
        return bannedPermissionInfoMapper.insertBannedPermissionInfo(bannedPermissionInfo);
    }

    /**
     * 修改用户封禁权限
     *
     * @param bannedPermissionInfo 用户封禁权限
     * @return 结果
     */
    @Override
    public int updateBannedPermissionInfo(BannedPermissionInfo bannedPermissionInfo)
    {
        return bannedPermissionInfoMapper.updateBannedPermissionInfo(bannedPermissionInfo);
    }

    /**
     * 批量删除用户封禁权限
     *
     * @param bannedIds 需要删除的用户封禁权限主键
     * @return 结果
     */
    @Override
    public int deleteBannedPermissionInfoByBannedIds(String[] bannedIds)
    {
        return bannedPermissionInfoMapper.deleteBannedPermissionInfoByBannedIds(bannedIds);
    }

    /**
     * 删除用户封禁权限信息
     *
     * @param bannedId 用户封禁权限主键
     * @return 结果
     */
    @Override
    public int deleteBannedPermissionInfoByBannedId(String bannedId)
    {
        return bannedPermissionInfoMapper.deleteBannedPermissionInfoByBannedId(bannedId);
    }
    //endregion
    @Override
    public QueryWrapper<BannedPermissionInfo> getQueryWrapper(BannedPermissionInfoQuery bannedPermissionInfoQuery){
        QueryWrapper<BannedPermissionInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = bannedPermissionInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String bannedId = bannedPermissionInfoQuery.getBannedId();
        queryWrapper.eq(StringUtils.isNotEmpty(bannedId) ,"banned_id",bannedId);

    String permissionName = bannedPermissionInfoQuery.getPermissionName();
        queryWrapper.like(StringUtils.isNotEmpty(permissionName) ,"permission_name",permissionName);

    String userId = bannedPermissionInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    Date startTime = bannedPermissionInfoQuery.getStartTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginStartTime"))&&StringUtils.isNotNull(params.get("endStartTime")),"start_time",params.get("beginStartTime"),params.get("endStartTime"));

    Date endTime = bannedPermissionInfoQuery.getEndTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginEndTime"))&&StringUtils.isNotNull(params.get("endEndTime")),"end_time",params.get("beginEndTime"),params.get("endEndTime"));

    String status = bannedPermissionInfoQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status) ,"status",status);

        return queryWrapper;
    }

    @Override
    public List<BannedPermissionInfoVo> convertVoList(List<BannedPermissionInfo> bannedPermissionInfoList) {
        if (StringUtils.isEmpty(bannedPermissionInfoList)) {
            return Collections.emptyList();
        }
        return bannedPermissionInfoList.stream().map(BannedPermissionInfoVo::objToVo).collect(Collectors.toList());
    }

}
