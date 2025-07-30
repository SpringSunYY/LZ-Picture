package com.lz.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomCacheEvict;
import com.lz.common.annotation.CustomCacheable;
import com.lz.common.constant.redis.PointsRedisConstants;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.points.mapper.PointsRechargePackageInfoMapper;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.lz.points.model.dto.pointsRechargePackageInfo.PointsRechargePackageInfoQuery;
import com.lz.points.model.enums.PoPackageIsLongTermEnum;
import com.lz.points.model.enums.PoPackageStatusEnum;
import com.lz.points.model.vo.pointsRechargePackageInfo.PointsRechargePackageInfoVo;
import com.lz.points.service.IPointsRechargePackageInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 充值积分套餐Service业务层处理
 *
 * @author YY
 * @date 2025-05-12
 */
@Service
public class PointsRechargePackageInfoServiceImpl extends ServiceImpl<PointsRechargePackageInfoMapper, PointsRechargePackageInfo> implements IPointsRechargePackageInfoService {
    @Resource
    private PointsRechargePackageInfoMapper pointsRechargePackageInfoMapper;

    //region mybatis代码

    /**
     * 查询充值积分套餐
     *
     * @param packageId 充值积分套餐主键
     * @return 充值积分套餐
     */
    @CustomCacheable(keyPrefix = PointsRedisConstants.POINTS_RECHARGE_PACKAGE_INFO_LIST, keyField = "packageId")
    @Override
    public PointsRechargePackageInfo selectPointsRechargePackageInfoByPackageId(String packageId) {
        return pointsRechargePackageInfoMapper.selectPointsRechargePackageInfoByPackageId(packageId);
    }

    /**
     * 查询充值积分套餐列表
     *
     * @param pointsRechargePackageInfo 充值积分套餐
     * @return 充值积分套餐
     */
    @Override
    public List<PointsRechargePackageInfo> selectPointsRechargePackageInfoList(PointsRechargePackageInfo pointsRechargePackageInfo) {
        return pointsRechargePackageInfoMapper.selectPointsRechargePackageInfoList(pointsRechargePackageInfo);
    }

    /**
     * 新增充值积分套餐
     *
     * @param pointsRechargePackageInfo 充值积分套餐
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = {PointsRedisConstants.POINTS_RECHARGE_PACKAGE_INFO_LIST})
    @Override
    public int insertPointsRechargePackageInfo(PointsRechargePackageInfo pointsRechargePackageInfo) {
        //如果不是长期套餐必须要设置套餐生效时间
        if (PoPackageIsLongTermEnum.PACKAGE_IS_LONG_TERM_1.getValue().equals(pointsRechargePackageInfo.getIsLongTerm())) {
            ThrowUtils.throwIf(StringUtils.isNull(pointsRechargePackageInfo.getStartTime()) || StringUtils.isNull(pointsRechargePackageInfo.getEndTime()), "套餐不是长期请设置套餐生效时间和结束时间!!!");
        }
        //查询是否已经拥有此套餐
        ThrowUtils.throwIf(StringUtils.isNotNull(selectPointRechargePackageInfoByPackageName(pointsRechargePackageInfo.getPackageName())), "已存在此套餐名称!!!");
        pointsRechargePackageInfo.setPackageId(IdUtils.snowflakeId().toString());
        pointsRechargePackageInfo.setCreateTime(DateUtils.getNowDate());
        return pointsRechargePackageInfoMapper.insertPointsRechargePackageInfo(pointsRechargePackageInfo);
    }

    /**
     * 修改充值积分套餐
     *
     * @param pointsRechargePackageInfo 充值积分套餐
     * @return 结果
     */
    @CustomCacheEvict(keyPrefixes = {PointsRedisConstants.POINTS_RECHARGE_PACKAGE_INFO_LIST})
    @Override
    public int updatePointsRechargePackageInfo(PointsRechargePackageInfo pointsRechargePackageInfo) {
        //如果不是长期套餐必须要设置套餐生效时间
        if (PoPackageIsLongTermEnum.PACKAGE_IS_LONG_TERM_1.getValue().equals(pointsRechargePackageInfo.getIsLongTerm())) {
            ThrowUtils.throwIf(StringUtils.isNull(pointsRechargePackageInfo.getStartTime()) || StringUtils.isNull(pointsRechargePackageInfo.getEndTime()), "套餐不是长期请设置套餐生效时间和结束时间!!!");
        }
        PointsRechargePackageInfo pointsRechargePackageInfoDb = selectPointRechargePackageInfoByPackageName(pointsRechargePackageInfo.getPackageName());
        ThrowUtils.throwIf(StringUtils.isNotNull(pointsRechargePackageInfoDb) && !pointsRechargePackageInfoDb.getPackageId().equals(pointsRechargePackageInfo.getPackageId()), "已存在此套餐名称!!!");
        pointsRechargePackageInfo.setUpdateTime(DateUtils.getNowDate());
        return pointsRechargePackageInfoMapper.updatePointsRechargePackageInfo(pointsRechargePackageInfo);
    }

    /**
     * 批量删除充值积分套餐
     *
     * @param packageIds 需要删除的充值积分套餐主键
     * @return 结果
     */
    @Override
    public int deletePointsRechargePackageInfoByPackageIds(String[] packageIds) {
        return pointsRechargePackageInfoMapper.deletePointsRechargePackageInfoByPackageIds(packageIds);
    }

    /**
     * 删除充值积分套餐信息
     *
     * @param packageId 充值积分套餐主键
     * @return 结果
     */
    @Override
    public int deletePointsRechargePackageInfoByPackageId(String packageId) {
        return pointsRechargePackageInfoMapper.deletePointsRechargePackageInfoByPackageId(packageId);
    }

    //endregion
    @Override
    public QueryWrapper<PointsRechargePackageInfo> getQueryWrapper(PointsRechargePackageInfoQuery pointsRechargePackageInfoQuery) {
        QueryWrapper<PointsRechargePackageInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pointsRechargePackageInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String packageId = pointsRechargePackageInfoQuery.getPackageId();
        queryWrapper.eq(StringUtils.isNotEmpty(packageId), "package_id", packageId);

        String packageName = pointsRechargePackageInfoQuery.getPackageName();
        queryWrapper.like(StringUtils.isNotEmpty(packageName), "package_name", packageName);

        String isLongTerm = pointsRechargePackageInfoQuery.getIsLongTerm();
        queryWrapper.eq(StringUtils.isNotEmpty(isLongTerm), "is_long_term", isLongTerm);

        Date startTime = pointsRechargePackageInfoQuery.getStartTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginStartTime")) && StringUtils.isNotNull(params.get("endStartTime")), "start_time", params.get("beginStartTime"), params.get("endStartTime"));

        Date endTime = pointsRechargePackageInfoQuery.getEndTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginEndTime")) && StringUtils.isNotNull(params.get("endEndTime")), "end_time", params.get("beginEndTime"), params.get("endEndTime"));

        String packageStatus = pointsRechargePackageInfoQuery.getPackageStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(packageStatus), "package_status", packageStatus);

        Date createTime = pointsRechargePackageInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = pointsRechargePackageInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<PointsRechargePackageInfoVo> convertVoList(List<PointsRechargePackageInfo> pointsRechargePackageInfoList) {
        if (StringUtils.isEmpty(pointsRechargePackageInfoList)) {
            return Collections.emptyList();
        }
        return pointsRechargePackageInfoList.stream().map(PointsRechargePackageInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public PointsRechargePackageInfo selectPointRechargePackageInfoByPackageName(String packageName) {
        return this.getOne(new LambdaQueryWrapper<PointsRechargePackageInfo>()
                .eq(PointsRechargePackageInfo::getPackageName, packageName));
    }

    @CustomCacheable(keyPrefix = PointsRedisConstants.POINTS_RECHARGE_PACKAGE_INFO_LIST, useQueryParamsAsKey = true)
    @Override
    public List<PointsRechargePackageInfo> userSelectPointsRechargePackageInfoList(PointsRechargePackageInfo pointsRechargePackageInfo) {
        //指定查询未开始和正常的
        return this.list(new QueryWrapper<PointsRechargePackageInfo>()
                .in("package_status", PoPackageStatusEnum.PACKAGE_STATUS_0.getValue(), PoPackageStatusEnum.PACKAGE_STATUS_1.getValue())
                .eq(StringUtils.isNotEmpty(pointsRechargePackageInfo.getPackageName()), "package_name", pointsRechargePackageInfo.getPackageName())
                .eq(StringUtils.isNotEmpty(pointsRechargePackageInfo.getIsLongTerm()), "is_long_term", pointsRechargePackageInfo.getIsLongTerm())
                .orderBy(true, true, "sort_order"));
    }
}
