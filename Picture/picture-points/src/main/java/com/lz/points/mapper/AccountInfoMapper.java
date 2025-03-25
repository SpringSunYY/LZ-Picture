package com.lz.points.mapper;

import java.util.List;
import com.lz.points.model.domain.AccountInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 积分账户Mapper接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface AccountInfoMapper extends BaseMapper<AccountInfo>
{
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
     * 删除积分账户
     *
     * @param accountId 积分账户主键
     * @return 结果
     */
    public int deleteAccountInfoByAccountId(String accountId);

    /**
     * 批量删除积分账户
     *
     * @param accountIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccountInfoByAccountIds(String[] accountIds);
}
