package com.lz.points.service.impl;

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
import com.lz.points.mapper.PointsUsageLogMapper;
import com.lz.points.model.domain.PointsUsageLog;
import com.lz.points.service.IPointsUsageLogService;
import com.lz.points.model.dto.pointsUsageLog.PointsUsageLogQuery;
import com.lz.points.model.vo.pointsUsageLog.PointsUsageLogVo;

/**
 * 积分使用记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class PointsUsageLogServiceImpl extends ServiceImpl<PointsUsageLogMapper, PointsUsageLog> implements IPointsUsageLogService
{
    @Resource
    private PointsUsageLogMapper pointsUsageLogMapper;

    //region mybatis代码
    /**
     * 查询积分使用记录
     *
     * @param logId 积分使用记录主键
     * @return 积分使用记录
     */
    @Override
    public PointsUsageLog selectPointsUsageLogByLogId(String logId)
    {
        return pointsUsageLogMapper.selectPointsUsageLogByLogId(logId);
    }

    /**
     * 查询积分使用记录列表
     *
     * @param pointsUsageLog 积分使用记录
     * @return 积分使用记录
     */
    @Override
    public List<PointsUsageLog> selectPointsUsageLogList(PointsUsageLog pointsUsageLog)
    {
        return pointsUsageLogMapper.selectPointsUsageLogList(pointsUsageLog);
    }

    /**
     * 新增积分使用记录
     *
     * @param pointsUsageLog 积分使用记录
     * @return 结果
     */
    @Override
    public int insertPointsUsageLog(PointsUsageLog pointsUsageLog)
    {
        pointsUsageLog.setCreateTime(DateUtils.getNowDate());
        return pointsUsageLogMapper.insertPointsUsageLog(pointsUsageLog);
    }

    /**
     * 修改积分使用记录
     *
     * @param pointsUsageLog 积分使用记录
     * @return 结果
     */
    @Override
    public int updatePointsUsageLog(PointsUsageLog pointsUsageLog)
    {
      pointsUsageLog.setUpdateTime(DateUtils.getNowDate());
        return pointsUsageLogMapper.updatePointsUsageLog(pointsUsageLog);
    }

    /**
     * 批量删除积分使用记录
     *
     * @param logIds 需要删除的积分使用记录主键
     * @return 结果
     */
    @Override
    public int deletePointsUsageLogByLogIds(String[] logIds)
    {
        return pointsUsageLogMapper.deletePointsUsageLogByLogIds(logIds);
    }

    /**
     * 删除积分使用记录信息
     *
     * @param logId 积分使用记录主键
     * @return 结果
     */
    @Override
    public int deletePointsUsageLogByLogId(String logId)
    {
        return pointsUsageLogMapper.deletePointsUsageLogByLogId(logId);
    }
    //endregion
    @Override
    public QueryWrapper<PointsUsageLog> getQueryWrapper(PointsUsageLogQuery pointsUsageLogQuery){
        QueryWrapper<PointsUsageLog> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pointsUsageLogQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String logId = pointsUsageLogQuery.getLogId();
        queryWrapper.eq(StringUtils.isNotEmpty(logId) ,"log_id",logId);

    String userId = pointsUsageLogQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String usageType = pointsUsageLogQuery.getUsageType();
        queryWrapper.eq(StringUtils.isNotEmpty(usageType) ,"usage_type",usageType);

    String targetId = pointsUsageLogQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId) ,"target_id",targetId);

    String deviceId = pointsUsageLogQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId) ,"device_id",deviceId);

    String browser = pointsUsageLogQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser) ,"browser",browser);

    String os = pointsUsageLogQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os) ,"os",os);

    String platform = pointsUsageLogQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform) ,"platform",platform);

    String ipAddr = pointsUsageLogQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr) ,"ip_addr",ipAddr);

    Date createTime = pointsUsageLogQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date updateTime = pointsUsageLogQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

    String isDelete = pointsUsageLogQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete) ,"is_delete",isDelete);

        return queryWrapper;
    }

    @Override
    public List<PointsUsageLogVo> convertVoList(List<PointsUsageLog> pointsUsageLogList) {
        if (StringUtils.isEmpty(pointsUsageLogList)) {
            return Collections.emptyList();
        }
        return pointsUsageLogList.stream().map(PointsUsageLogVo::objToVo).collect(Collectors.toList());
    }

}
