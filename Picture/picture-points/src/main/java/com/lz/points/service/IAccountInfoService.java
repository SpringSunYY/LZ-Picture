package com.lz.points.service;

import java.util.List;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.vo.accountInfo.AccountInfoVo;
import com.lz.points.model.dto.accountInfo.AccountInfoQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 积分账户Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IAccountInfoService extends IService<AccountInfo>
{
    //region mybatis代码
    /**
     * 查询积分账户
     *
     * @param accountId 积分账户主键
     * @return 积分账户
     */
    public AccountInfo selectAccountInfoByAccountId(String accountId);

    /**
     * 查询积分账户列表
     *
     * @param accountInfo 积分账户
     * @return 积分账户集合
     */
    public List<AccountInfo> selectAccountInfoList(AccountInfo accountInfo);

    /**
     * 新增积分账户
     *
     * @param accountInfo 积分账户
     * @return 结果
     */
    public int insertAccountInfo(AccountInfo accountInfo);

    /**
     * 修改积分账户
     *
     * @param accountInfo 积分账户
     * @return 结果
     */
    public int updateAccountInfo(AccountInfo accountInfo);

    /**
     * 批量删除积分账户
     *
     * @param accountIds 需要删除的积分账户主键集合
     * @return 结果
     */
    public int deleteAccountInfoByAccountIds(String[] accountIds);

    /**
     * 删除积分账户信息
     *
     * @param accountId 积分账户主键
     * @return 结果
     */
    public int deleteAccountInfoByAccountId(String accountId);
    //endregion
    /**
     * 获取查询条件
     *
     * @param accountInfoQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<AccountInfo> getQueryWrapper(AccountInfoQuery accountInfoQuery);

    /**
     * 转换vo
     *
     * @param accountInfoList AccountInfo集合
     * @return AccountInfoVO集合
     */
    List<AccountInfoVo> convertVoList(List<AccountInfo> accountInfoList);
}
