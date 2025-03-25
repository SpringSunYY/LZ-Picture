package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.PaymentOrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 支付订单Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface PaymentOrderInfoMapper extends BaseMapper<PaymentOrderInfo>
{
    /**
     * 查询支付订单
     *
     * @param orderId 支付订单主键
     * @return 支付订单
     */
    public PaymentOrderInfo selectPaymentOrderInfoByOrderId(String orderId);

    /**
     * 查询支付订单列表
     *
     * @param paymentOrderInfo 支付订单
     * @return 支付订单集合
     */
    public List<PaymentOrderInfo> selectPaymentOrderInfoList(PaymentOrderInfo paymentOrderInfo);

    /**
     * 新增支付订单
     *
     * @param paymentOrderInfo 支付订单
     * @return 结果
     */
    public int insertPaymentOrderInfo(PaymentOrderInfo paymentOrderInfo);

    /**
     * 修改支付订单
     *
     * @param paymentOrderInfo 支付订单
     * @return 结果
     */
    public int updatePaymentOrderInfo(PaymentOrderInfo paymentOrderInfo);

    /**
     * 删除支付订单
     *
     * @param orderId 支付订单主键
     * @return 结果
     */
    public int deletePaymentOrderInfoByOrderId(String orderId);

    /**
     * 批量删除支付订单
     *
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePaymentOrderInfoByOrderIds(String[] orderIds);
}
