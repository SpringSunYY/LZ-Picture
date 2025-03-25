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
import com.lz.points.mapper.WithdrawalOrderInfoMapper;
import com.lz.points.model.domain.WithdrawalOrderInfo;
import com.lz.points.service.IWithdrawalOrderInfoService;
import com.lz.points.model.dto.withdrawalOrderInfo.WithdrawalOrderInfoQuery;
import com.lz.points.model.vo.withdrawalOrderInfo.WithdrawalOrderInfoVo;

/**
 * 用户提现记录Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class WithdrawalOrderInfoServiceImpl extends ServiceImpl<WithdrawalOrderInfoMapper, WithdrawalOrderInfo> implements IWithdrawalOrderInfoService
{
    @Resource
    private WithdrawalOrderInfoMapper withdrawalOrderInfoMapper;

    //region mybatis代码
    /**
     * 查询用户提现记录
     *
     * @param withdrawalId 用户提现记录主键
     * @return 用户提现记录
     */
    @Override
    public WithdrawalOrderInfo selectWithdrawalOrderInfoByWithdrawalId(String withdrawalId)
    {
        return withdrawalOrderInfoMapper.selectWithdrawalOrderInfoByWithdrawalId(withdrawalId);
    }

    /**
     * 查询用户提现记录列表
     *
     * @param withdrawalOrderInfo 用户提现记录
     * @return 用户提现记录
     */
    @Override
    public List<WithdrawalOrderInfo> selectWithdrawalOrderInfoList(WithdrawalOrderInfo withdrawalOrderInfo)
    {
        return withdrawalOrderInfoMapper.selectWithdrawalOrderInfoList(withdrawalOrderInfo);
    }

    /**
     * 新增用户提现记录
     *
     * @param withdrawalOrderInfo 用户提现记录
     * @return 结果
     */
    @Override
    public int insertWithdrawalOrderInfo(WithdrawalOrderInfo withdrawalOrderInfo)
    {
        withdrawalOrderInfo.setCreateTime(DateUtils.getNowDate());
        return withdrawalOrderInfoMapper.insertWithdrawalOrderInfo(withdrawalOrderInfo);
    }

    /**
     * 修改用户提现记录
     *
     * @param withdrawalOrderInfo 用户提现记录
     * @return 结果
     */
    @Override
    public int updateWithdrawalOrderInfo(WithdrawalOrderInfo withdrawalOrderInfo)
    {
      withdrawalOrderInfo.setUpdateTime(DateUtils.getNowDate());
        return withdrawalOrderInfoMapper.updateWithdrawalOrderInfo(withdrawalOrderInfo);
    }

    /**
     * 批量删除用户提现记录
     *
     * @param withdrawalIds 需要删除的用户提现记录主键
     * @return 结果
     */
    @Override
    public int deleteWithdrawalOrderInfoByWithdrawalIds(String[] withdrawalIds)
    {
        return withdrawalOrderInfoMapper.deleteWithdrawalOrderInfoByWithdrawalIds(withdrawalIds);
    }

    /**
     * 删除用户提现记录信息
     *
     * @param withdrawalId 用户提现记录主键
     * @return 结果
     */
    @Override
    public int deleteWithdrawalOrderInfoByWithdrawalId(String withdrawalId)
    {
        return withdrawalOrderInfoMapper.deleteWithdrawalOrderInfoByWithdrawalId(withdrawalId);
    }
    //endregion
    @Override
    public QueryWrapper<WithdrawalOrderInfo> getQueryWrapper(WithdrawalOrderInfoQuery withdrawalOrderInfoQuery){
        QueryWrapper<WithdrawalOrderInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = withdrawalOrderInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String withdrawalId = withdrawalOrderInfoQuery.getWithdrawalId();
        queryWrapper.eq(StringUtils.isNotEmpty(withdrawalId) ,"withdrawal_id",withdrawalId);

    String userId = withdrawalOrderInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    String withdrawalMethod = withdrawalOrderInfoQuery.getWithdrawalMethod();
        queryWrapper.eq(StringUtils.isNotEmpty(withdrawalMethod) ,"withdrawal_method",withdrawalMethod);

    String withdrawalAccount = withdrawalOrderInfoQuery.getWithdrawalAccount();
        queryWrapper.eq(StringUtils.isNotEmpty(withdrawalAccount) ,"withdrawal_account",withdrawalAccount);

    String withdrawalStatus = withdrawalOrderInfoQuery.getWithdrawalStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(withdrawalStatus) ,"withdrawal_status",withdrawalStatus);

    String withdrawalPlatformOrder = withdrawalOrderInfoQuery.getWithdrawalPlatformOrder();
        queryWrapper.eq(StringUtils.isNotEmpty(withdrawalPlatformOrder) ,"withdrawal_platform_order",withdrawalPlatformOrder);

    String transactionId = withdrawalOrderInfoQuery.getTransactionId();
        queryWrapper.eq(StringUtils.isNotEmpty(transactionId) ,"transaction_id",transactionId);

    String reviewStatus = withdrawalOrderInfoQuery.getReviewStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(reviewStatus) ,"review_status",reviewStatus);

    Date reviewTime = withdrawalOrderInfoQuery.getReviewTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginReviewTime"))&&StringUtils.isNotNull(params.get("endReviewTime")),"review_time",params.get("beginReviewTime"),params.get("endReviewTime"));

    Long reviewUserId = withdrawalOrderInfoQuery.getReviewUserId();
        queryWrapper.eq( StringUtils.isNotNull(reviewUserId),"review_user_id",reviewUserId);

    Date accomplishTime = withdrawalOrderInfoQuery.getAccomplishTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginAccomplishTime"))&&StringUtils.isNotNull(params.get("endAccomplishTime")),"accomplish_time",params.get("beginAccomplishTime"),params.get("endAccomplishTime"));

    String deviceId = withdrawalOrderInfoQuery.getDeviceId();
        queryWrapper.eq(StringUtils.isNotEmpty(deviceId) ,"device_id",deviceId);

    String browser = withdrawalOrderInfoQuery.getBrowser();
        queryWrapper.eq(StringUtils.isNotEmpty(browser) ,"browser",browser);

    String os = withdrawalOrderInfoQuery.getOs();
        queryWrapper.eq(StringUtils.isNotEmpty(os) ,"os",os);

    String platform = withdrawalOrderInfoQuery.getPlatform();
        queryWrapper.eq(StringUtils.isNotEmpty(platform) ,"platform",platform);

    String ipAddr = withdrawalOrderInfoQuery.getIpAddr();
        queryWrapper.like(StringUtils.isNotEmpty(ipAddr) ,"ip_addr",ipAddr);

    Date createTime = withdrawalOrderInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date updateTime = withdrawalOrderInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

    String isDelete = withdrawalOrderInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete) ,"is_delete",isDelete);

        return queryWrapper;
    }

    @Override
    public List<WithdrawalOrderInfoVo> convertVoList(List<WithdrawalOrderInfo> withdrawalOrderInfoList) {
        if (StringUtils.isEmpty(withdrawalOrderInfoList)) {
            return Collections.emptyList();
        }
        return withdrawalOrderInfoList.stream().map(WithdrawalOrderInfoVo::objToVo).collect(Collectors.toList());
    }

}
