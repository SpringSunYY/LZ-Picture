package com.lz.points.service;

import java.util.List;
import com.lz.points.model.domain.PaymentMethodInfo;
import com.lz.points.model.vo.paymentMethodInfo.PaymentMethodInfoVo;
import com.lz.points.model.dto.paymentMethodInfo.PaymentMethodInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 支付方式Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IPaymentMethodInfoService extends IService<PaymentMethodInfo>
{
    //region mybatis代码
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
     * 批量删除支付方式
     *
     * @param methodIds 需要删除的支付方式主键集合
     * @return 结果
     */
    public int deletePaymentMethodInfoByMethodIds(String[] methodIds);

    /**
     * 删除支付方式信息
     *
     * @param methodId 支付方式主键
     * @return 结果
     */
    public int deletePaymentMethodInfoByMethodId(String methodId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param paymentMethodInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<PaymentMethodInfo> getQueryWrapper(PaymentMethodInfoQuery paymentMethodInfoQuery);

    /**
     * 转换vo
     *
     * @param paymentMethodInfoList PaymentMethodInfo集合
     * @return PaymentMethodInfoVO集合
     */
    List<PaymentMethodInfoVo> convertVoList(List<PaymentMethodInfo> paymentMethodInfoList);
}
