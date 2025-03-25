package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.PaymentMethodInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 支付方式Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface PaymentMethodInfoMapper extends BaseMapper<PaymentMethodInfo>
{
    /**
     * 查询支付方式
     *
     * @param methodId 支付方式主键
     * @return 支付方式
     */
    public PaymentMethodInfo selectPaymentMethodInfoByMethodId(String methodId);

    /**
     * 查询支付方式列表
     *
     * @param paymentMethodInfo 支付方式
     * @return 支付方式集合
     */
    public List<PaymentMethodInfo> selectPaymentMethodInfoList(PaymentMethodInfo paymentMethodInfo);

    /**
     * 新增支付方式
     *
     * @param paymentMethodInfo 支付方式
     * @return 结果
     */
    public int insertPaymentMethodInfo(PaymentMethodInfo paymentMethodInfo);

    /**
     * 修改支付方式
     *
     * @param paymentMethodInfo 支付方式
     * @return 结果
     */
    public int updatePaymentMethodInfo(PaymentMethodInfo paymentMethodInfo);

    /**
     * 删除支付方式
     *
     * @param methodId 支付方式主键
     * @return 结果
     */
    public int deletePaymentMethodInfoByMethodId(String methodId);

    /**
     * 批量删除支付方式
     *
     * @param methodIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePaymentMethodInfoByMethodIds(String[] methodIds);
}
