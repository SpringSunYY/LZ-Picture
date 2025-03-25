package com.lz.points.service.impl;

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
import com.lz.points.mapper.RiskControlLogInfoMapper;
import com.lz.points.model.domain.RiskControlLogInfo;
import com.lz.points.service.IRiskControlLogInfoService;
import com.lz.points.model.dto.riskControlLogInfo.RiskControlLogInfoQuery;
import com.lz.points.model.vo.riskControlLogInfo.RiskControlLogInfoVo;

/**
 * 风控日志Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class RiskControlLogInfoServiceImpl extends ServiceImpl<RiskControlLogInfoMapper, RiskControlLogInfo> implements IRiskControlLogInfoService
{
    @Resource
    private RiskControlLogInfoMapper riskControlLogInfoMapper;

    //region mybatis代码
    /**
     * 查询风控日志
     *
     * @param logId 风控日志主键
     * @return 风控日志
     */
    @Override
    public RiskControlLogInfo selectRiskControlLogInfoByLogId(String logId)
    {
        return riskControlLogInfoMapper.selectRiskControlLogInfoByLogId(logId);
    }

    /**
     * 查询风控日志列表
     *
     * @param riskControlLogInfo 风控日志
     * @return 风控日志
     */
    @Override
    public List<RiskControlLogInfo> selectRiskControlLogInfoList(RiskControlLogInfo riskControlLogInfo)
    {
        return riskControlLogInfoMapper.selectRiskControlLogInfoList(riskControlLogInfo);
    }

    /**
     * 新增风控日志
     *
     * @param riskControlLogInfo 风控日志
     * @return 结果
     */
    @Override
    public int insertRiskControlLogInfo(RiskControlLogInfo riskControlLogInfo)
    {
        riskControlLogInfo.setCreateTime(DateUtils.getNowDate());
        return riskControlLogInfoMapper.insertRiskControlLogInfo(riskControlLogInfo);
    }

    /**
     * 修改风控日志
     *
     * @param riskControlLogInfo 风控日志
     * @return 结果
     */
    @Override
    public int updateRiskControlLogInfo(RiskControlLogInfo riskControlLogInfo)
    {
        return riskControlLogInfoMapper.updateRiskControlLogInfo(riskControlLogInfo);
    }

    /**
     * 批量删除风控日志
     *
     * @param logIds 需要删除的风控日志主键
     * @return 结果
     */
    @Override
    public int deleteRiskControlLogInfoByLogIds(String[] logIds)
    {
        return riskControlLogInfoMapper.deleteRiskControlLogInfoByLogIds(logIds);
    }

    /**
     * 删除风控日志信息
     *
     * @param logId 风控日志主键
     * @return 结果
     */
    @Override
    public int deleteRiskControlLogInfoByLogId(String logId)
    {
        return riskControlLogInfoMapper.deleteRiskControlLogInfoByLogId(logId);
    }
    //endregion
    @Override
    public QueryWrapper<RiskControlLogInfo> getQueryWrapper(RiskControlLogInfoQuery riskControlLogInfoQuery){
        QueryWrapper<RiskControlLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = riskControlLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String logId = riskControlLogInfoQuery.getLogId();
        queryWrapper.eq(StringUtils.isNotEmpty(logId) ,"log_id",logId);

    String userId = riskControlLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String methodId = riskControlLogInfoQuery.getMethodId();
        queryWrapper.eq(StringUtils.isNotEmpty(methodId) ,"method_id",methodId);

    String riskType = riskControlLogInfoQuery.getRiskType();
        queryWrapper.eq(StringUtils.isNotEmpty(riskType) ,"risk_type",riskType);

    String riskLevel = riskControlLogInfoQuery.getRiskLevel();
        queryWrapper.eq(StringUtils.isNotEmpty(riskLevel) ,"risk_level",riskLevel);

    Date actionTime = riskControlLogInfoQuery.getActionTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginActionTime"))&&StringUtils.isNotNull(params.get("endActionTime")),"action_time",params.get("beginActionTime"),params.get("endActionTime"));

    Date createTime = riskControlLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    String deviceId = riskControlLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId) ,"device_id",deviceId);

    String browser = riskControlLogInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser) ,"browser",browser);

    String os = riskControlLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os) ,"os",os);

    String platform = riskControlLogInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform) ,"platform",platform);

    String ipAddr = riskControlLogInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr) ,"ip_addr",ipAddr);

        return queryWrapper;
    }

    @Override
    public List<RiskControlLogInfoVo> convertVoList(List<RiskControlLogInfo> riskControlLogInfoList) {
        if (StringUtils.isEmpty(riskControlLogInfoList)) {
            return Collections.emptyList();
        }
        return riskControlLogInfoList.stream().map(RiskControlLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
