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
import com.lz.points.mapper.ErrorLogInfoMapper;
import com.lz.points.model.domain.ErrorLogInfo;
import com.lz.points.service.IErrorLogInfoService;
import com.lz.points.model.dto.errorLogInfo.ErrorLogInfoQuery;
import com.lz.points.model.vo.errorLogInfo.ErrorLogInfoVo;

/**
 * 异常捕获Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class ErrorLogInfoServiceImpl extends ServiceImpl<ErrorLogInfoMapper, ErrorLogInfo> implements IErrorLogInfoService
{
    @Resource
    private ErrorLogInfoMapper errorLogInfoMapper;

    //region mybatis代码
    /**
     * 查询异常捕获
     *
     * @param errorId 异常捕获主键
     * @return 异常捕获
     */
    @Override
    public ErrorLogInfo selectErrorLogInfoByErrorId(String errorId)
    {
        return errorLogInfoMapper.selectErrorLogInfoByErrorId(errorId);
    }

    /**
     * 查询异常捕获列表
     *
     * @param errorLogInfo 异常捕获
     * @return 异常捕获
     */
    @Override
    public List<ErrorLogInfo> selectErrorLogInfoList(ErrorLogInfo errorLogInfo)
    {
        return errorLogInfoMapper.selectErrorLogInfoList(errorLogInfo);
    }

    /**
     * 新增异常捕获
     *
     * @param errorLogInfo 异常捕获
     * @return 结果
     */
    @Override
    public int insertErrorLogInfo(ErrorLogInfo errorLogInfo)
    {
        errorLogInfo.setCreateTime(DateUtils.getNowDate());
        return errorLogInfoMapper.insertErrorLogInfo(errorLogInfo);
    }

    /**
     * 修改异常捕获
     *
     * @param errorLogInfo 异常捕获
     * @return 结果
     */
    @Override
    public int updateErrorLogInfo(ErrorLogInfo errorLogInfo)
    {
        return errorLogInfoMapper.updateErrorLogInfo(errorLogInfo);
    }

    /**
     * 批量删除异常捕获
     *
     * @param errorIds 需要删除的异常捕获主键
     * @return 结果
     */
    @Override
    public int deleteErrorLogInfoByErrorIds(String[] errorIds)
    {
        return errorLogInfoMapper.deleteErrorLogInfoByErrorIds(errorIds);
    }

    /**
     * 删除异常捕获信息
     *
     * @param errorId 异常捕获主键
     * @return 结果
     */
    @Override
    public int deleteErrorLogInfoByErrorId(String errorId)
    {
        return errorLogInfoMapper.deleteErrorLogInfoByErrorId(errorId);
    }
    //endregion
    @Override
    public QueryWrapper<ErrorLogInfo> getQueryWrapper(ErrorLogInfoQuery errorLogInfoQuery){
        QueryWrapper<ErrorLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = errorLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String errorId = errorLogInfoQuery.getErrorId();
        queryWrapper.eq(StringUtils.isNotEmpty(errorId) ,"error_id",errorId);

    String userId = errorLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String methodId = errorLogInfoQuery.getMethodId();
        queryWrapper.eq(StringUtils.isNotEmpty(methodId) ,"method_id",methodId);

    String errorType = errorLogInfoQuery.getErrorType();
        queryWrapper.eq(StringUtils.isNotEmpty(errorType) ,"error_type",errorType);

    String relatedOrderId = errorLogInfoQuery.getRelatedOrderId();
        queryWrapper.eq(StringUtils.isNotEmpty(relatedOrderId) ,"related_order_id",relatedOrderId);

    Date createTime = errorLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    String deviceId = errorLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId) ,"device_id",deviceId);

    String browser = errorLogInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser) ,"browser",browser);

    String os = errorLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os) ,"os",os);

    String platform = errorLogInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform) ,"platform",platform);

    String ipAddr = errorLogInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr) ,"ip_addr",ipAddr);

    String resolveStatus = errorLogInfoQuery.getResolveStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(resolveStatus) ,"resolve_status",resolveStatus);

    Date resolveTime = errorLogInfoQuery.getResolveTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginResolveTime"))&&StringUtils.isNotNull(params.get("endResolveTime")),"resolve_time",params.get("beginResolveTime"),params.get("endResolveTime"));

        return queryWrapper;
    }

    @Override
    public List<ErrorLogInfoVo> convertVoList(List<ErrorLogInfo> errorLogInfoList) {
        if (StringUtils.isEmpty(errorLogInfoList)) {
            return Collections.emptyList();
        }
        return errorLogInfoList.stream().map(ErrorLogInfoVo::objToVo).collect(Collectors.toList());
    }

}
