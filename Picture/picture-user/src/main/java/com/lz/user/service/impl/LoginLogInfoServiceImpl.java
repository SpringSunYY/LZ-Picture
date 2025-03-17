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
import com.lz.user.mapper.LoginLogInfoMapper;
import com.lz.user.model.domain.LoginLogInfo;
import com.lz.user.service.ILoginLogInfoService;
import com.lz.user.model.dto.loginLogInfo.LoginLogInfoQuery;
import com.lz.user.model.vo.loginLogInfo.LoginLogInfoVo;

/**
 * 用户登录日志Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class LoginLogInfoServiceImpl extends ServiceImpl<LoginLogInfoMapper, LoginLogInfo> implements ILoginLogInfoService
{
    @Resource
    private LoginLogInfoMapper loginLogInfoMapper;

    //region mybatis代码
    /**
     * 查询用户登录日志
     *
     * @param infoId 用户登录日志主键
     * @return 用户登录日志
     */
    @Override
    public LoginLogInfo selectLoginLogInfoByInfoId(String infoId)
    {
        return loginLogInfoMapper.selectLoginLogInfoByInfoId(infoId);
    }

    /**
     * 查询用户登录日志列表
     *
     * @param loginLogInfo 用户登录日志
     * @return 用户登录日志
     */
    @Override
    public List<LoginLogInfo> selectLoginLogInfoList(LoginLogInfo loginLogInfo)
    {
        return loginLogInfoMapper.selectLoginLogInfoList(loginLogInfo);
    }

    /**
     * 新增用户登录日志
     *
     * @param loginLogInfo 用户登录日志
     * @return 结果
     */
    @Override
    public int insertLoginLogInfo(LoginLogInfo loginLogInfo)
    {
        return loginLogInfoMapper.insertLoginLogInfo(loginLogInfo);
    }

    /**
     * 修改用户登录日志
     *
     * @param loginLogInfo 用户登录日志
     * @return 结果
     */
    @Override
    public int updateLoginLogInfo(LoginLogInfo loginLogInfo)
    {
        return loginLogInfoMapper.updateLoginLogInfo(loginLogInfo);
    }

    /**
     * 批量删除用户登录日志
     *
     * @param infoIds 需要删除的用户登录日志主键
     * @return 结果
     */
    @Override
    public int deleteLoginLogInfoByInfoIds(String[] infoIds)
    {
        return loginLogInfoMapper.deleteLoginLogInfoByInfoIds(infoIds);
    }

    /**
     * 删除用户登录日志信息
     *
     * @param infoId 用户登录日志主键
     * @return 结果
     */
    @Override
    public int deleteLoginLogInfoByInfoId(String infoId)
    {
        return loginLogInfoMapper.deleteLoginLogInfoByInfoId(infoId);
    }
    //endregion
    @Override
    public QueryWrapper<LoginLogInfo> getQueryWrapper(LoginLogInfoQuery loginLogInfoQuery){
        QueryWrapper<LoginLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = loginLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String infoId = loginLogInfoQuery.getInfoId();
        queryWrapper.eq(StringUtils.isNotEmpty(infoId) ,"info_id",infoId);

    String userId = loginLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String userName = loginLogInfoQuery.getUserName();
        queryWrapper.like(StringUtils.isNotEmpty(userName) ,"user_name",userName);

    String loginType = loginLogInfoQuery.getLoginType();
        queryWrapper.eq(StringUtils.isNotEmpty(loginType) ,"login_type",loginType);

    String ipaddr = loginLogInfoQuery.getIpaddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipaddr) ,"ipaddr",ipaddr);

    String loginLocation = loginLogInfoQuery.getLoginLocation();
        queryWrapper.like(StringUtils.isNotEmpty(loginLocation) ,"login_location",loginLocation);

    String browser = loginLogInfoQuery.getBrowser();
        queryWrapper.like(StringUtils.isNotEmpty(browser) ,"browser",browser);

    String os = loginLogInfoQuery.getOs();
        queryWrapper.like(StringUtils.isNotEmpty(os) ,"os",os);

    String platform = loginLogInfoQuery.getPlatform();
        queryWrapper.like(StringUtils.isNotEmpty(platform) ,"platform",platform);

    String status = loginLogInfoQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status) ,"status",status);

    String errorCode = loginLogInfoQuery.getErrorCode();
        queryWrapper.eq(StringUtils.isNotEmpty(errorCode) ,"error_code",errorCode);

    String msg = loginLogInfoQuery.getMsg();
        queryWrapper.eq(StringUtils.isNotEmpty(msg) ,"msg",msg);

    Date loginTime = loginLogInfoQuery.getLoginTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginLoginTime"))&&StringUtils.isNotNull(params.get("endLoginTime")),"login_time",params.get("beginLoginTime"),params.get("endLoginTime"));

        return queryWrapper;
    }

    @Override
    public List<LoginLogInfoVo> convertVoList(List<LoginLogInfo> loginLogInfoList) {
        if (StringUtils.isEmpty(loginLogInfoList)) {
            return Collections.emptyList();
        }
        return loginLogInfoList.stream().map(LoginLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
