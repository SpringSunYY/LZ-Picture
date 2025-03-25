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
import com.lz.ai.mapper.OfficialUsageLogInfoMapper;
import com.lz.ai.model.domain.OfficialUsageLogInfo;
import com.lz.ai.service.IOfficialUsageLogInfoService;
import com.lz.ai.model.dto.officialUsageLogInfo.OfficialUsageLogInfoQuery;
import com.lz.ai.model.vo.officialUsageLogInfo.OfficialUsageLogInfoVo;

/**
 * 官方AI操作日志Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class OfficialUsageLogInfoServiceImpl extends ServiceImpl<OfficialUsageLogInfoMapper, OfficialUsageLogInfo> implements IOfficialUsageLogInfoService
{
    @Resource
    private OfficialUsageLogInfoMapper officialUsageLogInfoMapper;

    //region mybatis代码
    /**
     * 查询官方AI操作日志
     *
     * @param logId 官方AI操作日志主键
     * @return 官方AI操作日志
     */
    @Override
    public OfficialUsageLogInfo selectOfficialUsageLogInfoByLogId(String logId)
    {
        return officialUsageLogInfoMapper.selectOfficialUsageLogInfoByLogId(logId);
    }

    /**
     * 查询官方AI操作日志列表
     *
     * @param officialUsageLogInfo 官方AI操作日志
     * @return 官方AI操作日志
     */
    @Override
    public List<OfficialUsageLogInfo> selectOfficialUsageLogInfoList(OfficialUsageLogInfo officialUsageLogInfo)
    {
        return officialUsageLogInfoMapper.selectOfficialUsageLogInfoList(officialUsageLogInfo);
    }

    /**
     * 新增官方AI操作日志
     *
     * @param officialUsageLogInfo 官方AI操作日志
     * @return 结果
     */
    @Override
    public int insertOfficialUsageLogInfo(OfficialUsageLogInfo officialUsageLogInfo)
    {
        officialUsageLogInfo.setCreateTime(DateUtils.getNowDate());
        return officialUsageLogInfoMapper.insertOfficialUsageLogInfo(officialUsageLogInfo);
    }

    /**
     * 修改官方AI操作日志
     *
     * @param officialUsageLogInfo 官方AI操作日志
     * @return 结果
     */
    @Override
    public int updateOfficialUsageLogInfo(OfficialUsageLogInfo officialUsageLogInfo)
    {
      officialUsageLogInfo.setUpdateTime(DateUtils.getNowDate());
        return officialUsageLogInfoMapper.updateOfficialUsageLogInfo(officialUsageLogInfo);
    }

    /**
     * 批量删除官方AI操作日志
     *
     * @param logIds 需要删除的官方AI操作日志主键
     * @return 结果
     */
    @Override
    public int deleteOfficialUsageLogInfoByLogIds(String[] logIds)
    {
        return officialUsageLogInfoMapper.deleteOfficialUsageLogInfoByLogIds(logIds);
    }

    /**
     * 删除官方AI操作日志信息
     *
     * @param logId 官方AI操作日志主键
     * @return 结果
     */
    @Override
    public int deleteOfficialUsageLogInfoByLogId(String logId)
    {
        return officialUsageLogInfoMapper.deleteOfficialUsageLogInfoByLogId(logId);
    }
    //endregion
    @Override
    public QueryWrapper<OfficialUsageLogInfo> getQueryWrapper(OfficialUsageLogInfoQuery officialUsageLogInfoQuery){
        QueryWrapper<OfficialUsageLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = officialUsageLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String logId = officialUsageLogInfoQuery.getLogId();
        queryWrapper.eq(StringUtils.isNotEmpty(logId) ,"log_id",logId);

    Long userId = officialUsageLogInfoQuery.getUserId();
        queryWrapper.eq( StringUtils.isNotNull(userId),"user_id",userId);

    String modelId = officialUsageLogInfoQuery.getModelId();
        queryWrapper.eq(StringUtils.isNotEmpty(modelId) ,"model_id",modelId);

    String operationType = officialUsageLogInfoQuery.getOperationType();
        queryWrapper.eq(StringUtils.isNotEmpty(operationType) ,"operation_type",operationType);

    String inputParams = officialUsageLogInfoQuery.getInputParams();
        queryWrapper.eq(StringUtils.isNotEmpty(inputParams) ,"input_params",inputParams);

    String outputResult = officialUsageLogInfoQuery.getOutputResult();
        queryWrapper.eq(StringUtils.isNotEmpty(outputResult) ,"output_result",outputResult);

    Date requestTime = officialUsageLogInfoQuery.getRequestTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginRequestTime"))&&StringUtils.isNotNull(params.get("endRequestTime")),"request_time",params.get("beginRequestTime"),params.get("endRequestTime"));

    String logStatus = officialUsageLogInfoQuery.getLogStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(logStatus) ,"log_status",logStatus);

    String aiStatusCode = officialUsageLogInfoQuery.getAiStatusCode();
        queryWrapper.eq(StringUtils.isNotEmpty(aiStatusCode) ,"ai_status_code",aiStatusCode);

    Date createTime = officialUsageLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date updateTime = officialUsageLogInfoQuery.getUpdateTime();
        queryWrapper.eq( StringUtils.isNotNull(updateTime),"update_time",updateTime);

    String isDelete = officialUsageLogInfoQuery.getIsDelete();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginIsDelete"))&&StringUtils.isNotNull(params.get("endIsDelete")),"is_delete",params.get("beginIsDelete"),params.get("endIsDelete"));

        return queryWrapper;
    }

    @Override
    public List<OfficialUsageLogInfoVo> convertVoList(List<OfficialUsageLogInfo> officialUsageLogInfoList) {
        if (StringUtils.isEmpty(officialUsageLogInfoList)) {
            return Collections.emptyList();
        }
        return officialUsageLogInfoList.stream().map(OfficialUsageLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
