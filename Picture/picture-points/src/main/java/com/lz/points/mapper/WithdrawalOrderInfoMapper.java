package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.WithdrawalOrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户提现记录Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface WithdrawalOrderInfoMapper extends BaseMapper<WithdrawalOrderInfo>
{
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
     * 删除用户提现记录
     *
     * @param withdrawalId 用户提现记录主键
     * @return 结果
     */
    public int deleteWithdrawalOrderInfoByWithdrawalId(String withdrawalId);

    /**
     * 批量删除用户提现记录
     *
     * @param withdrawalIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWithdrawalOrderInfoByWithdrawalIds(String[] withdrawalIds);
}
