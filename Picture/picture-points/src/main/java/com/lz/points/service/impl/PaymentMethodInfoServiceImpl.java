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
import com.lz.points.mapper.PaymentMethodInfoMapper;
import com.lz.points.model.domain.PaymentMethodInfo;
import com.lz.points.service.IPaymentMethodInfoService;
import com.lz.points.model.dto.paymentMethodInfo.PaymentMethodInfoQuery;
import com.lz.points.model.vo.paymentMethodInfo.PaymentMethodInfoVo;

/**
 * 支付方式Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class PaymentMethodInfoServiceImpl extends ServiceImpl<PaymentMethodInfoMapper, PaymentMethodInfo> implements IPaymentMethodInfoService
{
    @Resource
    private PaymentMethodInfoMapper paymentMethodInfoMapper;

    //region mybatis代码
    /**
     * 查询支付方式
     *
     * @param methodId 支付方式主键
     * @return 支付方式
     */
    @Override
    public PaymentMethodInfo selectPaymentMethodInfoByMethodId(String methodId)
    {
        return paymentMethodInfoMapper.selectPaymentMethodInfoByMethodId(methodId);
    }

    /**
     * 查询支付方式列表
     *
     * @param paymentMethodInfo 支付方式
     * @return 支付方式
     */
    @Override
    public List<PaymentMethodInfo> selectPaymentMethodInfoList(PaymentMethodInfo paymentMethodInfo)
    {
        return paymentMethodInfoMapper.selectPaymentMethodInfoList(paymentMethodInfo);
    }

    /**
     * 新增支付方式
     *
     * @param paymentMethodInfo 支付方式
     * @return 结果
     */
    @Override
    public int insertPaymentMethodInfo(PaymentMethodInfo paymentMethodInfo)
    {
        paymentMethodInfo.setCreateTime(DateUtils.getNowDate());
        return paymentMethodInfoMapper.insertPaymentMethodInfo(paymentMethodInfo);
    }

    /**
     * 修改支付方式
     *
     * @param paymentMethodInfo 支付方式
     * @return 结果
     */
    @Override
    public int updatePaymentMethodInfo(PaymentMethodInfo paymentMethodInfo)
    {
      paymentMethodInfo.setUpdateTime(DateUtils.getNowDate());
        return paymentMethodInfoMapper.updatePaymentMethodInfo(paymentMethodInfo);
    }

    /**
     * 批量删除支付方式
     *
     * @param methodIds 需要删除的支付方式主键
     * @return 结果
     */
    @Override
    public int deletePaymentMethodInfoByMethodIds(String[] methodIds)
    {
        return paymentMethodInfoMapper.deletePaymentMethodInfoByMethodIds(methodIds);
    }

    /**
     * 删除支付方式信息
     *
     * @param methodId 支付方式主键
     * @return 结果
     */
    @Override
    public int deletePaymentMethodInfoByMethodId(String methodId)
    {
        return paymentMethodInfoMapper.deletePaymentMethodInfoByMethodId(methodId);
    }
    //endregion
    @Override
    public QueryWrapper<PaymentMethodInfo> getQueryWrapper(PaymentMethodInfoQuery paymentMethodInfoQuery){
        QueryWrapper<PaymentMethodInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = paymentMethodInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String methodId = paymentMethodInfoQuery.getMethodId();
        queryWrapper.eq(StringUtils.isNotEmpty(methodId) ,"method_id",methodId);

    String methodName = paymentMethodInfoQuery.getMethodName();
        queryWrapper.like(StringUtils.isNotEmpty(methodName) ,"method_name",methodName);

    String thirdParty = paymentMethodInfoQuery.getThirdParty();
        queryWrapper.eq(StringUtils.isNotEmpty(thirdParty) ,"third_party",thirdParty);

    String methodType = paymentMethodInfoQuery.getMethodType();
        queryWrapper.eq(StringUtils.isNotEmpty(methodType) ,"method_type",methodType);

    String apiUrl = paymentMethodInfoQuery.getApiUrl();
        queryWrapper.eq(StringUtils.isNotEmpty(apiUrl) ,"api_url",apiUrl);

    String merchantId = paymentMethodInfoQuery.getMerchantId();
        queryWrapper.eq(StringUtils.isNotEmpty(merchantId) ,"merchant_id",merchantId);

    String methodStatus = paymentMethodInfoQuery.getMethodStatus();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginMethodStatus"))&&StringUtils.isNotNull(params.get("endMethodStatus")),"method_status",params.get("beginMethodStatus"),params.get("endMethodStatus"));

    Date createTime = paymentMethodInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date updateTime = paymentMethodInfoQuery.getUpdateTime();
        queryWrapper.eq( StringUtils.isNotNull(updateTime),"update_time",updateTime);

        return queryWrapper;
    }

    @Override
    public List<PaymentMethodInfoVo> convertVoList(List<PaymentMethodInfo> paymentMethodInfoList) {
        if (StringUtils.isEmpty(paymentMethodInfoList)) {
            return Collections.emptyList();
        }
        return paymentMethodInfoList.stream().map(PaymentMethodInfoVo::objToVo).collect(Collectors.toList());
    }

}
