package com.lz.points.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.DateUtils;
import com.lz.points.model.domain.PointsRechargeInfo;
import com.lz.points.model.dto.pointsUsageLogInfo.UserPointsUsageLogInfoQuery;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.points.mapper.PointsUsageLogInfoMapper;
import com.lz.points.model.domain.PointsUsageLogInfo;
import com.lz.points.service.IPointsUsageLogInfoService;
import com.lz.points.model.dto.pointsUsageLogInfo.PointsUsageLogInfoQuery;
import com.lz.points.model.vo.pointsUsageLogInfo.PointsUsageLogInfoVo;

/**
 * 积分使用记录Service业务层处理
 *
 * @author YY
 * @date 2025-05-23
 */
@Service
public class PointsUsageLogInfoServiceImpl extends ServiceImpl<PointsUsageLogInfoMapper, PointsUsageLogInfo> implements IPointsUsageLogInfoService {
    @Resource
    private PointsUsageLogInfoMapper pointsUsageLogInfoMapper;

    //region mybatis代码

    /**
     * 查询积分使用记录
     *
     * @param logId 积分使用记录主键
     * @return 积分使用记录
     */
    @Override
    public PointsUsageLogInfo selectPointsUsageLogInfoByLogId(String logId) {
        return pointsUsageLogInfoMapper.selectPointsUsageLogInfoByLogId(logId);
    }

    /**
     * 查询积分使用记录列表
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 积分使用记录
     */
    @Override
    public List<PointsUsageLogInfo> selectPointsUsageLogInfoList(PointsUsageLogInfo pointsUsageLogInfo) {
        return pointsUsageLogInfoMapper.selectPointsUsageLogInfoList(pointsUsageLogInfo);
    }

    /**
     * 新增积分使用记录
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 结果
     */
    @Override
    public int insertPointsUsageLogInfo(PointsUsageLogInfo pointsUsageLogInfo) {
        pointsUsageLogInfo.setCreateTime(DateUtils.getNowDate());
        return pointsUsageLogInfoMapper.insertPointsUsageLogInfo(pointsUsageLogInfo);
    }

    /**
     * 修改积分使用记录
     *
     * @param pointsUsageLogInfo 积分使用记录
     * @return 结果
     */
    @Override
    public int updatePointsUsageLogInfo(PointsUsageLogInfo pointsUsageLogInfo) {
        pointsUsageLogInfo.setUpdateTime(DateUtils.getNowDate());
        return pointsUsageLogInfoMapper.updatePointsUsageLogInfo(pointsUsageLogInfo);
    }

    /**
     * 批量删除积分使用记录
     *
     * @param logIds 需要删除的积分使用记录主键
     * @return 结果
     */
    @Override
    public int deletePointsUsageLogInfoByLogIds(String[] logIds) {
        return pointsUsageLogInfoMapper.deletePointsUsageLogInfoByLogIds(logIds);
    }

    /**
     * 删除积分使用记录信息
     *
     * @param logId 积分使用记录主键
     * @return 结果
     */
    @Override
    public int deletePointsUsageLogInfoByLogId(String logId) {
        return pointsUsageLogInfoMapper.deletePointsUsageLogInfoByLogId(logId);
    }

    //endregion
    @Override
    public QueryWrapper<PointsUsageLogInfo> getQueryWrapper(PointsUsageLogInfoQuery pointsUsageLogInfoQuery) {
        QueryWrapper<PointsUsageLogInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pointsUsageLogInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String logId = pointsUsageLogInfoQuery.getLogId();
        queryWrapper.eq(StringUtils.isNotEmpty(logId), "log_id", logId);

        String userId = pointsUsageLogInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String giveUserId = pointsUsageLogInfoQuery.getGiveUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(giveUserId), "give_user_id", giveUserId);

        String logType = pointsUsageLogInfoQuery.getLogType();
        queryWrapper.eq(StringUtils.isNotEmpty(logType), "log_type", logType);

        String usageType = pointsUsageLogInfoQuery.getUsageType();
        queryWrapper.eq(StringUtils.isNotEmpty(usageType), "usage_type", usageType);

        String targetId = pointsUsageLogInfoQuery.getTargetId();
        queryWrapper.eq(StringUtils.isNotEmpty(targetId), "target_id", targetId);

        String deviceId = pointsUsageLogInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = pointsUsageLogInfoQuery.getBrowser();
        queryWrapper.like(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = pointsUsageLogInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os), "os", os);

        String platform = pointsUsageLogInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddr = pointsUsageLogInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr), "ip_addr", ipAddr);

        String ipAddress = pointsUsageLogInfoQuery.getIpAddress();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddress), "ip_address", ipAddress);

        Date createTime = pointsUsageLogInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = pointsUsageLogInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String isDelete = pointsUsageLogInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        return queryWrapper;
    }

    @Override
    public List<PointsUsageLogInfoVo> convertVoList(List<PointsUsageLogInfo> pointsUsageLogInfoList) {
        if (StringUtils.isEmpty(pointsUsageLogInfoList)) {
            return Collections.emptyList();
        }
        return pointsUsageLogInfoList.stream().map(PointsUsageLogInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public Page<PointsUsageLogInfo> selectMyPointsUsageLogInfoList(UserPointsUsageLogInfoQuery userPointsUsageLogInfoQuery) {
        // 提取基础参数
        Integer pageNum = userPointsUsageLogInfoQuery.getPageNum();
        Integer pageSize = userPointsUsageLogInfoQuery.getPageSize();
        Map<String, Object> params = userPointsUsageLogInfoQuery.getParams();

        // 提取 beginCreateTime 和 endCreateTime（安全获取）
        String beginCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("beginCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);

        String endCreateTime = Optional.ofNullable(params)
                .map(p -> p.get("endCreateTime"))
                .map(Object::toString)
                .filter(StringUtils::isNotEmpty)
                .orElse(null);

        return this.page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<PointsUsageLogInfo>()
                        .eq(StringUtils.isNotEmpty(userPointsUsageLogInfoQuery.getUserId()), PointsUsageLogInfo::getUserId, userPointsUsageLogInfoQuery.getUserId())
                        .eq(StringUtils.isNotEmpty(userPointsUsageLogInfoQuery.getLogType()), PointsUsageLogInfo::getLogType, userPointsUsageLogInfoQuery.getLogType())
                        .eq(StringUtils.isNotEmpty(userPointsUsageLogInfoQuery.getUsageType()), PointsUsageLogInfo::getUsageType, userPointsUsageLogInfoQuery.getUsageType())
                        .apply(StringUtils.isNotEmpty(beginCreateTime) && StringUtils.isNotEmpty(endCreateTime),
                                "create_time between {0} and {1}",
                                beginCreateTime, endCreateTime)
                        .orderBy(StringUtils.isNotEmpty(userPointsUsageLogInfoQuery.getIsAsc()),
                                userPointsUsageLogInfoQuery.getIsAsc().equals("asc"),
                                PointsUsageLogInfo::getCreateTime)
        );
    }

}
