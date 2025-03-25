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
import com.lz.points.mapper.PointsRechargePackageInfoMapper;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.lz.points.service.IPointsRechargePackageInfoService;
import com.lz.points.model.dto.pointsRechargePackageInfo.PointsRechargePackageInfoQuery;
import com.lz.points.model.vo.pointsRechargePackageInfo.PointsRechargePackageInfoVo;

/**
 * 充值积分套餐Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class PointsRechargePackageInfoServiceImpl extends ServiceImpl<PointsRechargePackageInfoMapper, PointsRechargePackageInfo> implements IPointsRechargePackageInfoService
{
    @Resource
    private PointsRechargePackageInfoMapper pointsRechargePackageInfoMapper;

    //region mybatis代码
    /**
     * 查询充值积分套餐
     *
     * @param packageId 充值积分套餐主键
     * @return 充值积分套餐
     */
    @Override
    public PointsRechargePackageInfo selectPointsRechargePackageInfoByPackageId(String packageId)
    {
        return pointsRechargePackageInfoMapper.selectPointsRechargePackageInfoByPackageId(packageId);
    }

    /**
     * 查询充值积分套餐列表
     *
     * @param pointsRechargePackageInfo 充值积分套餐
     * @return 充值积分套餐
     */
    @Override
    public List<PointsRechargePackageInfo> selectPointsRechargePackageInfoList(PointsRechargePackageInfo pointsRechargePackageInfo)
    {
        return pointsRechargePackageInfoMapper.selectPointsRechargePackageInfoList(pointsRechargePackageInfo);
    }

    /**
     * 新增充值积分套餐
     *
     * @param pointsRechargePackageInfo 充值积分套餐
     * @return 结果
     */
    @Override
    public int insertPointsRechargePackageInfo(PointsRechargePackageInfo pointsRechargePackageInfo)
    {
        pointsRechargePackageInfo.setCreateTime(DateUtils.getNowDate());
        return pointsRechargePackageInfoMapper.insertPointsRechargePackageInfo(pointsRechargePackageInfo);
    }

    /**
     * 修改充值积分套餐
     *
     * @param pointsRechargePackageInfo 充值积分套餐
     * @return 结果
     */
    @Override
    public int updatePointsRechargePackageInfo(PointsRechargePackageInfo pointsRechargePackageInfo)
    {
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
    public int deletePointsRechargePackageInfoByPackageIds(String[] packageIds)
    {
        return pointsRechargePackageInfoMapper.deletePointsRechargePackageInfoByPackageIds(packageIds);
    }

    /**
     * 删除充值积分套餐信息
     *
     * @param packageId 充值积分套餐主键
     * @return 结果
     */
    @Override
    public int deletePointsRechargePackageInfoByPackageId(String packageId)
    {
        return pointsRechargePackageInfoMapper.deletePointsRechargePackageInfoByPackageId(packageId);
    }
    //endregion
    @Override
    public QueryWrapper<PointsRechargePackageInfo> getQueryWrapper(PointsRechargePackageInfoQuery pointsRechargePackageInfoQuery){
        QueryWrapper<PointsRechargePackageInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = pointsRechargePackageInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String packageId = pointsRechargePackageInfoQuery.getPackageId();
        queryWrapper.eq(StringUtils.isNotEmpty(packageId) ,"package_id",packageId);

    String packageName = pointsRechargePackageInfoQuery.getPackageName();
        queryWrapper.like(StringUtils.isNotEmpty(packageName) ,"package_name",packageName);

    String isLongTerm = pointsRechargePackageInfoQuery.getIsLongTerm();
        queryWrapper.eq(StringUtils.isNotEmpty(isLongTerm) ,"is_long_term",isLongTerm);

    Date startTime = pointsRechargePackageInfoQuery.getStartTime();
        queryWrapper.eq( StringUtils.isNotNull(startTime),"start_time",startTime);

    Date endTime = pointsRechargePackageInfoQuery.getEndTime();
        queryWrapper.eq( StringUtils.isNotNull(endTime),"end_time",endTime);

    String packageStatus = pointsRechargePackageInfoQuery.getPackageStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(packageStatus) ,"package_status",packageStatus);

    Date createTime = pointsRechargePackageInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date updateTime = pointsRechargePackageInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

        return queryWrapper;
    }

    @Override
    public List<PointsRechargePackageInfoVo> convertVoList(List<PointsRechargePackageInfo> pointsRechargePackageInfoList) {
        if (StringUtils.isEmpty(pointsRechargePackageInfoList)) {
            return Collections.emptyList();
        }
        return pointsRechargePackageInfoList.stream().map(PointsRechargePackageInfoVo::objToVo).collect(Collectors.toList());
    }

}
