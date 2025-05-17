package com.lz.points.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.lz.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.points.mapper.PointsRechargeInfoMapper;
import com.lz.points.model.domain.PointsRechargeInfo;
import com.lz.points.service.IPointsRechargeInfoService;
import com.lz.points.model.dto.pointsRechargeInfo.PointsRechargeInfoQuery;
import com.lz.points.model.vo.pointsRechargeInfo.PointsRechargeInfoVo;

/**
 * 积分充值记录Service业务层处理
 *
 * @author YY
 * @date 2025-05-17
 */
@Service
public class PointsRechargeInfoServiceImpl extends ServiceImpl<PointsRechargeInfoMapper, PointsRechargeInfo> implements IPointsRechargeInfoService {
    @Resource
    private PointsRechargeInfoMapper pointsRechargeInfoMapper;

    //region mybatis代码

    /**
     * 查询积分充值记录
     *
     * @param rechargeId 积分充值记录主键
     * @return 积分充值记录
     */
    @Override
    public PointsRechargeInfo selectPointsRechargeInfoByRechargeId(String rechargeId) {
        return pointsRechargeInfoMapper.selectPointsRechargeInfoByRechargeId(rechargeId);
    }

    /**
     * 查询积分充值记录列表
     *
     * @param pointsRechargeInfo 积分充值记录
     * @return 积分充值记录
     */
    @Override
    public List<PointsRechargeInfo> selectPointsRechargeInfoList(PointsRechargeInfo pointsRechargeInfo) {
        return pointsRechargeInfoMapper.selectPointsRechargeInfoList(pointsRechargeInfo);
    }

    /**
     * 新增积分充值记录
     *
     * @param pointsRechargeInfo 积分充值记录
     * @return 结果
     */
    @Override
    public int insertPointsRechargeInfo(PointsRechargeInfo pointsRechargeInfo) {
        pointsRechargeInfo.setCreateTime(DateUtils.getNowDate());
        return pointsRechargeInfoMapper.insertPointsRechargeInfo(pointsRechargeInfo);
    }

    /**
     * 修改积分充值记录
     *
     * @param pointsRechargeInfo 积分充值记录
     * @return 结果
     */
    @Override
    public int updatePointsRechargeInfo(PointsRechargeInfo pointsRechargeInfo) {
        pointsRechargeInfo.setUpdateTime(DateUtils.getNowDate());
        return pointsRechargeInfoMapper.updatePointsRechargeInfo(pointsRechargeInfo);
    }

    /**
     * 批量删除积分充值记录
     *
     * @param rechargeIds 需要删除的积分充值记录主键
     * @return 结果
     */
    @Override
    public int deletePointsRechargeInfoByRechargeIds(String[] rechargeIds) {
        return pointsRechargeInfoMapper.deletePointsRechargeInfoByRechargeIds(rechargeIds);
    }

    /**
     * 删除积分充值记录信息
     *
     * @param rechargeId 积分充值记录主键
     * @return 结果
     */
    @Override
    public int deletePointsRechargeInfoByRechargeId(String rechargeId) {
        return pointsRechargeInfoMapper.deletePointsRechargeInfoByRechargeId(rechargeId);
    }

    //endregion
    @Override
    public QueryWrapper<PointsRechargeInfo> getQueryWrapper(PointsRechargeInfoQuery pointsRechargeInfoQuery) {
        QueryWrapper<PointsRechargeInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pointsRechargeInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String rechargeId = pointsRechargeInfoQuery.getRechargeId();
        queryWrapper.eq(StringUtils.isNotEmpty(rechargeId), "recharge_id", rechargeId);

        String packageId = pointsRechargeInfoQuery.getPackageId();
        queryWrapper.eq(StringUtils.isNotEmpty(packageId), "package_id", packageId);

        String userId = pointsRechargeInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        String orderId = pointsRechargeInfoQuery.getOrderId();
        queryWrapper.eq(StringUtils.isNotEmpty(orderId), "order_id", orderId);

        Long totalCount = pointsRechargeInfoQuery.getTotalCount();
        queryWrapper.eq(StringUtils.isNotNull(totalCount), "total_count", totalCount);

        Long pointsCount = pointsRechargeInfoQuery.getPointsCount();
        queryWrapper.eq(StringUtils.isNotNull(pointsCount), "points_count", pointsCount);

        Long bonusCount = pointsRechargeInfoQuery.getBonusCount();
        queryWrapper.eq(StringUtils.isNotNull(bonusCount), "bonus_count", bonusCount);

        BigDecimal priceCount = pointsRechargeInfoQuery.getPriceCount();
        queryWrapper.eq(StringUtils.isNotNull(priceCount), "price_count", priceCount);

        Long rechargeCount = pointsRechargeInfoQuery.getRechargeCount();
        queryWrapper.eq(StringUtils.isNotNull(rechargeCount), "recharge_count", rechargeCount);

        String thirdParty = pointsRechargeInfoQuery.getThirdParty();
        queryWrapper.eq(StringUtils.isNotEmpty(thirdParty), "third_party", thirdParty);

        String thirdPartyOrder = pointsRechargeInfoQuery.getThirdPartyOrder();
        queryWrapper.eq(StringUtils.isNotEmpty(thirdPartyOrder), "third_party_order", thirdPartyOrder);

        String rechargeStatus = pointsRechargeInfoQuery.getRechargeStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(rechargeStatus), "recharge_status", rechargeStatus);

        Date createTime = pointsRechargeInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date arrivalTime = pointsRechargeInfoQuery.getArrivalTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginArrivalTime")) && StringUtils.isNotNull(params.get("endArrivalTime")), "arrival_time", params.get("beginArrivalTime"), params.get("endArrivalTime"));

        Date updateTime = pointsRechargeInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String deviceId = pointsRechargeInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId), "device_id", deviceId);

        String browser = pointsRechargeInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser), "browser", browser);

        String os = pointsRechargeInfoQuery.getOs();
        queryWrapper.like(StringUtils.isNotEmpty(os), "os", os);

        String platform = pointsRechargeInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform), "platform", platform);

        String ipAddr = pointsRechargeInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr), "ip_addr", ipAddr);

        String isDelete = pointsRechargeInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        return queryWrapper;
    }

    @Override
    public List<PointsRechargeInfoVo> convertVoList(List<PointsRechargeInfo> pointsRechargeInfoList) {
        if (StringUtils.isEmpty(pointsRechargeInfoList)) {
            return Collections.emptyList();
        }
        return pointsRechargeInfoList.stream().map(PointsRechargeInfoVo::objToVo).collect(Collectors.toList());
    }

}
