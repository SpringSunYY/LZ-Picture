package com.lz.points.service;

import java.util.List;
import com.lz.points.model.domain.WithdrawalOrderInfo;
import com.lz.points.model.vo.withdrawalOrderInfo.WithdrawalOrderInfoVo;
import com.lz.points.model.dto.withdrawalOrderInfo.WithdrawalOrderInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 用户提现记录Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IWithdrawalOrderInfoService extends IService<WithdrawalOrderInfo>
{
    //region mybatis代码
    /**
     * 查询用户提现记录
     *
     * @param withdrawalId 用户提现记录主键
     * @return 用户提现记录
     */
    public WithdrawalOrderInfo selectWithdrawalOrderInfoByWithdrawalId(String withdrawalId);

    /**
     * 查询用户提现记录列表
     *
     * @param withdrawalOrderInfo 用户提现记录
     * @return 用户提现记录集合
     */
    public List<WithdrawalOrderInfo> selectWithdrawalOrderInfoList(WithdrawalOrderInfo withdrawalOrderInfo);

    /**
     * 新增用户提现记录
     *
     * @param withdrawalOrderInfo 用户提现记录
     * @return 结果
     */
    public int insertWithdrawalOrderInfo(WithdrawalOrderInfo withdrawalOrderInfo);

    /**
     * 修改用户提现记录
     *
     * @param withdrawalOrderInfo 用户提现记录
     * @return 结果
     */
    public int updateWithdrawalOrderInfo(WithdrawalOrderInfo withdrawalOrderInfo);

    /**
     * 批量删除用户提现记录
     *
     * @param withdrawalIds 需要删除的用户提现记录主键集合
     * @return 结果
     */
    public int deleteWithdrawalOrderInfoByWithdrawalIds(String[] withdrawalIds);

    /**
     * 删除用户提现记录信息
     *
     * @param withdrawalId 用户提现记录主键
     * @return 结果
     */
    public int deleteWithdrawalOrderInfoByWithdrawalId(String withdrawalId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param withdrawalOrderInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<WithdrawalOrderInfo> getQueryWrapper(WithdrawalOrderInfoQuery withdrawalOrderInfoQuery);

    /**
     * 转换vo
     *
     * @param withdrawalOrderInfoList WithdrawalOrderInfo集合
     * @return WithdrawalOrderInfoVO集合
     */
    List<WithdrawalOrderInfoVo> convertVoList(List<WithdrawalOrderInfo> withdrawalOrderInfoList);
}
