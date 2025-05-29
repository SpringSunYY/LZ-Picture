package com.lz.points.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.dto.accountInfo.AccountInfoQuery;
import com.lz.points.model.dto.accountInfo.AccountPasswordUploadRequest;
import com.lz.points.model.vo.accountInfo.AccountInfoVo;

import java.util.List;

/**
 * 积分账户Service接口
 *
 * @author YY
 * @date 2025-03-25
 */
public interface IAccountInfoService extends IService<AccountInfo> {
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

    /**
     * 根据用户ID查询用户
     *
     * @param
     * @param userId 用户编号
     * @return AccountInfo
     * @author: YY
     * @method: selectAccountInfoByUserId
     * @date: 2025/5/18 21:12
     **/
    AccountInfo selectAccountInfoByUserId(String userId);

    /**
     * 用户更新账户密码
     *
     * @param accountPasswordUploadRequest 更新账号实体
     * @return int
     * @author: YY
     * @method: userUpdateAccountInfoPassword
     * @date: 2025/5/22 09:00
     **/
    int userUpdateAccountInfoPassword(AccountPasswordUploadRequest accountPasswordUploadRequest);

    /**
     * 校验用户是否近期输入密码
     *
     * @param userId
     * @return int
     * @author: YY
     * @method: auth
     * @date: 2025/5/24 14:45
     **/
    int getVerifyPassword(String userId);

    /**
     * 校验用户密码
     *
     * @param userId
     * @param password
     * @return int
     * @author: YY
     * @method: verifyPassword
     * @date: 2025/5/24 14:51
     **/
    int verifyPassword(String userId, String password);

    /**
     * 根据用户ID查询用户账户信息
     * 并存入缓存
     *
     * @param userId 用户编号
     * @return AccountInfo
     * @author: YY
     * @method: selectUserAccountInfoByUserId
     * @date: 2025/5/24 14:51
     **/
    AccountInfo selectUserAccountInfoByUserId(String userId);
}
