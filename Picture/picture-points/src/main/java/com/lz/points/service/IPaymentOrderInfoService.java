package com.lz.points.service;

import java.util.Date;
import java.util.List;
import com.lz.points.model.domain.PaymentOrderInfo;
import com.lz.points.model.vo.paymentOrderInfo.PaymentOrderInfoVo;
import com.lz.points.model.dto.paymentOrderInfo.PaymentOrderInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 支付订单Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IPaymentOrderInfoService extends IService<PaymentOrderInfo>
{
    //region mybatis代码
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
     * 批量删除支付订单
     *
     * @param orderIds 需要删除的支付订单主键集合
     * @return 结果
     */
    public int deletePaymentOrderInfoByOrderIds(String[] orderIds);

    /**
     * 删除支付订单信息
     *
     * @param orderId 支付订单主键
     * @return 结果
     */
    public int deletePaymentOrderInfoByOrderId(String orderId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param paymentOrderInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PaymentOrderInfo> getQueryWrapper(PaymentOrderInfoQuery paymentOrderInfoQuery);

    /**
     * 转换vo
     *
     * @param paymentOrderInfoList PaymentOrderInfo集合
     * @return PaymentOrderInfoVO集合
     */
    List<PaymentOrderInfoVo> convertVoList(List<PaymentOrderInfo> paymentOrderInfoList);
}
