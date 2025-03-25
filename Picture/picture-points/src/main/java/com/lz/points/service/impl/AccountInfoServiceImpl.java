package com.lz.points.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.lz.common.utils.StringUtils;
import java.math.BigDecimal;
import com.lz.common.utils.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.points.mapper.AccountInfoMapper;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.service.IAccountInfoService;
import com.lz.points.model.dto.accountInfo.AccountInfoQuery;
import com.lz.points.model.vo.accountInfo.AccountInfoVo;

/**
 * 积分账户Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoMapper, AccountInfo> implements IAccountInfoService
{
    @Resource
    private AccountInfoMapper accountInfoMapper;

    //region mybatis代码
    /**
     * 查询积分账户
     *
     * @param accountId 积分账户主键
     * @return 积分账户
     */
    @Override
    public AccountInfo selectAccountInfoByAccountId(String accountId)
    {
        return accountInfoMapper.selectAccountInfoByAccountId(accountId);
    }

    /**
     * 查询积分账户列表
     *
     * @param accountInfo 积分账户
     * @return 积分账户
     */
    @Override
    public List<AccountInfo> selectAccountInfoList(AccountInfo accountInfo)
    {
        return accountInfoMapper.selectAccountInfoList(accountInfo);
    }

    /**
     * 新增积分账户
     *
     * @param accountInfo 积分账户
     * @return 结果
     */
    @Override
    public int insertAccountInfo(AccountInfo accountInfo)
    {
        accountInfo.setCreateTime(DateUtils.getNowDate());
        return accountInfoMapper.insertAccountInfo(accountInfo);
    }

    /**
     * 修改积分账户
     *
     * @param accountInfo 积分账户
     * @return 结果
     */
    @Override
    public int updateAccountInfo(AccountInfo accountInfo)
    {
      accountInfo.setUpdateTime(DateUtils.getNowDate());
        return accountInfoMapper.updateAccountInfo(accountInfo);
    }

    /**
     * 批量删除积分账户
     *
     * @param accountIds 需要删除的积分账户主键
     * @return 结果
     */
    @Override
    public int deleteAccountInfoByAccountIds(String[] accountIds)
    {
        return accountInfoMapper.deleteAccountInfoByAccountIds(accountIds);
    }

    /**
     * 删除积分账户信息
     *
     * @param accountId 积分账户主键
     * @return 结果
     */
    @Override
    public int deleteAccountInfoByAccountId(String accountId)
    {
        return accountInfoMapper.deleteAccountInfoByAccountId(accountId);
    }
    //endregion
    @Override
    public QueryWrapper<AccountInfo> getQueryWrapper(AccountInfoQuery accountInfoQuery){
        QueryWrapper<AccountInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = accountInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
    String accountId = accountInfoQuery.getAccountId();
        queryWrapper.eq(StringUtils.isNotEmpty(accountId) ,"account_id",accountId);

    String userId = accountInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId) ,"user_id",userId);

    Date createTime = accountInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

    Date updateTime = accountInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime"))&&StringUtils.isNotNull(params.get("endUpdateTime")),"update_time",params.get("beginUpdateTime"),params.get("endUpdateTime"));

    String isDelete = accountInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete) ,"is_delete",isDelete);

        return queryWrapper;
    }

    @Override
    public List<AccountInfoVo> convertVoList(List<AccountInfo> accountInfoList) {
        if (StringUtils.isEmpty(accountInfoList)) {
            return Collections.emptyList();
        }
        return accountInfoList.stream().map(AccountInfoVo::objToVo).collect(Collectors.toList());
    }

}
