package com.lz.ai.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.ai.mapper.UserUsageLogInfoMapper;
import com.lz.ai.model.domain.UserUsageLogInfo;
import com.lz.ai.service.IUserUsageLogInfoService;
import com.lz.ai.model.dto.userUsageLogInfo.UserUsageLogInfoQuery;
import com.lz.ai.model.vo.userUsageLogInfo.UserUsageLogInfoVo;

/**
 * 用户AI使用记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class UserUsageLogInfoServiceImpl extends ServiceImpl<UserUsageLogInfoMapper, UserUsageLogInfo> implements IUserUsageLogInfoService
{
    @Resource
    private UserUsageLogInfoMapper userUsageLogInfoMapper;

    //region mybatis代码
    /**
     * 查询用户AI使用记录
     *
     * @param logId 用户AI使用记录主键
     * @return 用户AI使用记录
     */
    @Override
    public UserUsageLogInfo selectUserUsageLogInfoByLogId(String logId)
    {
        return userUsageLogInfoMapper.selectUserUsageLogInfoByLogId(logId);
    }

    /**
     * 查询用户AI使用记录列表
     *
     * @param userUsageLogInfo 用户AI使用记录
     * @return 用户AI使用记录
     */
    @Override
    public List<UserUsageLogInfo> selectUserUsageLogInfoList(UserUsageLogInfo userUsageLogInfo)
    {
        return userUsageLogInfoMapper.selectUserUsageLogInfoList(userUsageLogInfo);
    }

    /**
     * 新增用户AI使用记录
     *
     * @param userUsageLogInfo 用户AI使用记录
     * @return 结果
     */
    @Override
    public int insertUserUsageLogInfo(UserUsageLogInfo userUsageLogInfo)
    {
        userUsageLogInfo.setCreateTime(DateUtils.getNowDate());
        return userUsageLogInfoMapper.insertUserUsageLogInfo(userUsageLogInfo);
    }

    /**
     * 修改用户AI使用记录
     *
     * @param userUsageLogInfo 用户AI使用记录
     * @return 结果
     */
    @Override
    public int updateUserUsageLogInfo(UserUsageLogInfo userUsageLogInfo)
    {
      userUsageLogInfo.setUpdateTime(DateUtils.getNowDate());
        return userUsageLogInfoMapper.updateUserUsageLogInfo(userUsageLogInfo);
    }

    /**
     * 批量删除用户AI使用记录
     *
     * @param logIds 需要删除的用户AI使用记录主键
     * @return 结果
     */
    @Override
    public int deleteUserUsageLogInfoByLogIds(String[] logIds)
    {
        return userUsageLogInfoMapper.deleteUserUsageLogInfoByLogIds(logIds);
    }

    /**
     * 删除用户AI使用记录信息
     *
     * @param logId 用户AI使用记录主键
     * @return 结果
     */
    @Override
    public int deleteUserUsageLogInfoByLogId(String logId)
    {
        return userUsageLogInfoMapper.deleteUserUsageLogInfoByLogId(logId);
    }
    //endregion
    @Override
    public QueryWrapper<UserUsageLogInfo> getQueryWrapper(UserUsageLogInfoQuery userUsageLogInfoQuery){
        QueryWrapper<UserUsageLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = userUsageLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String logId = userUsageLogInfoQuery.getLogId();
        queryWrapper.eq(StringUtils.isNotEmpty(logId) ,"log_id",logId);

    String userId = userUsageLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String modelId = userUsageLogInfoQuery.getModelId();
        queryWrapper.eq(StringUtils.isNotEmpty(modelId) ,"model_id",modelId);

    Date requestTime = userUsageLogInfoQuery.getRequestTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginRequestTime"))&&StringUtils.isNotNull(params.get("endRequestTime")),"request_time",params.get("beginRequestTime"),params.get("endRequestTime"));

    String usageType = userUsageLogInfoQuery.getUsageType();
        queryWrapper.eq(StringUtils.isNotEmpty(usageType) ,"usage_type",usageType);

    String targetId = userUsageLogInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId) ,"target_id",targetId);

    String logStatus = userUsageLogInfoQuery.getLogStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(logStatus) ,"log_status",logStatus);

    String aiStatusCode = userUsageLogInfoQuery.getAiStatusCode();
        queryWrapper.eq(StringUtils.isNotEmpty(aiStatusCode) ,"ai_status_code",aiStatusCode);

    String ipAddr = userUsageLogInfoQuery.getIpAddr();
        queryWrapper.eq(StringUtils.isNotEmpty(ipAddr) ,"ip_addr",ipAddr);

    String deviceId = userUsageLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId) ,"device_id",deviceId);

    String browser = userUsageLogInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser) ,"browser",browser);

    String os = userUsageLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os) ,"os",os);

    String platform = userUsageLogInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform) ,"platform",platform);

    Date createTime = userUsageLogInfoQuery.getCreateTime();
        queryWrapper.eq( StringUtils.isNotNull(createTime),"create_time",createTime);

    Date updateTime = userUsageLogInfoQuery.getUpdateTime();
        queryWrapper.eq( StringUtils.isNotNull(updateTime),"update_time",updateTime);

    String isDelete = userUsageLogInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete) ,"is_delete",isDelete);

        return queryWrapper;
    }

    @Override
    public List<UserUsageLogInfoVo> convertVoList(List<UserUsageLogInfo> userUsageLogInfoList) {
        if (StringUtils.isEmpty(userUsageLogInfoList)) {
            return Collections.emptyList();
        }
        return userUsageLogInfoList.stream().map(UserUsageLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
