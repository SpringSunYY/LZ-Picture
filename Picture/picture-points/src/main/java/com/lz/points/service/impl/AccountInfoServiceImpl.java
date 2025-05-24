package com.lz.points.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.utils.StringUtils;

import java.math.BigDecimal;

import com.lz.common.utils.DateUtils;
import com.lz.points.model.dto.accountInfo.AccountPasswordUploadRequest;
import com.lz.userauth.model.domain.EncryptionPassword;
import com.lz.userauth.utils.PasswordUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.points.mapper.AccountInfoMapper;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.service.IAccountInfoService;
import com.lz.points.model.dto.accountInfo.AccountInfoQuery;
import com.lz.points.model.vo.accountInfo.AccountInfoVo;

import static com.lz.common.constant.redis.PointsRedisConstants.POINTS_ACCOUNT_PASSWORD_CHECK;

/**
 * 积分账户Service业务层处理
 *
 * @author YY
 * @date 2025-03-25
 */
@Service
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoMapper, AccountInfo> implements IAccountInfoService {
    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Resource
    private RedisCache redisCache;

    //region mybatis代码

    /**
     * 查询积分账户
     *
     * @param accountId 积分账户主键
     * @return 积分账户
     */
    @Override
    public AccountInfo selectAccountInfoByAccountId(String accountId) {
        return accountInfoMapper.selectAccountInfoByAccountId(accountId);
    }

    /**
     * 查询积分账户列表
     *
     * @param accountInfo 积分账户
     * @return 积分账户
     */
    @Override
    public List<AccountInfo> selectAccountInfoList(AccountInfo accountInfo) {
        return accountInfoMapper.selectAccountInfoList(accountInfo);
    }

    /**
     * 新增积分账户
     *
     * @param accountInfo 积分账户
     * @return 结果
     */
    @Override
    public int insertAccountInfo(AccountInfo accountInfo) {
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
    public int updateAccountInfo(AccountInfo accountInfo) {
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
    public int deleteAccountInfoByAccountIds(String[] accountIds) {
        return accountInfoMapper.deleteAccountInfoByAccountIds(accountIds);
    }

    /**
     * 删除积分账户信息
     *
     * @param accountId 积分账户主键
     * @return 结果
     */
    @Override
    public int deleteAccountInfoByAccountId(String accountId) {
        return accountInfoMapper.deleteAccountInfoByAccountId(accountId);
    }

    //endregion
    @Override
    public QueryWrapper<AccountInfo> getQueryWrapper(AccountInfoQuery accountInfoQuery) {
        QueryWrapper<AccountInfo> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = accountInfoQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        String accountId = accountInfoQuery.getAccountId();
        queryWrapper.eq(StringUtils.isNotEmpty(accountId), "account_id", accountId);

        String userId = accountInfoQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotEmpty(userId), "user_id", userId);

        Date createTime = accountInfoQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        Date updateTime = accountInfoQuery.getUpdateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginUpdateTime")) && StringUtils.isNotNull(params.get("endUpdateTime")), "update_time", params.get("beginUpdateTime"), params.get("endUpdateTime"));

        String isDelete = accountInfoQuery.getIsDelete();
        queryWrapper.eq(StringUtils.isNotEmpty(isDelete), "is_delete", isDelete);

        return queryWrapper;
    }

    @Override
    public List<AccountInfoVo> convertVoList(List<AccountInfo> accountInfoList) {
        if (StringUtils.isEmpty(accountInfoList)) {
            return Collections.emptyList();
        }
        return accountInfoList.stream().map(AccountInfoVo::objToVo).collect(Collectors.toList());
    }

    @Override
    public AccountInfo selectAccountInfoByUserId(String userId) {
        return this.getOne(new LambdaQueryWrapper<AccountInfo>().eq(AccountInfo::getUserId, userId));
    }

    @Override
    public int userUpdateAccountInfoPassword(AccountPasswordUploadRequest request) {
        //校验密码格式是否正确
        PasswordUtils.checkPasswordFormate(request.getPassword(), request.getConfirmPassword(), 6, 10);
        //首先查询是否有账号
        AccountInfo accountInfoDb = this.selectAccountInfoByUserId(request.getUserId());
        if (StringUtils.isNull(accountInfoDb)) {
            return 0;
        }
        //获取密码以及加密方式
        String password = accountInfoDb.getPassword();
        String salt = accountInfoDb.getSalt();
        if (!PasswordUtils.checkPassword(salt, request.getOldPassword(), password)) {
            return 0;
        }
        EncryptionPassword encrypted = PasswordUtils.encryptPassword(request.getPassword());
        accountInfoDb.setPassword(encrypted.getPassword());
        accountInfoDb.setSalt(encrypted.getSalt());
        return accountInfoMapper.updateById(accountInfoDb);
    }

    @Override
    public int auth(String userId) {
        //从缓存里面获取账号是否存在
        Boolean cache = redisCache.getCacheObject(POINTS_ACCOUNT_PASSWORD_CHECK + userId);
        return StringUtils.isNotNull(cache) && cache ? 1 : 0;
    }

    @Override
    public int verifyPassword(String userId, String password) {
        //首先查询是否有账号
        AccountInfo accountInfoDb = this.selectAccountInfoByUserId(userId);
        if (StringUtils.isNull(accountInfoDb)) {
            return 0;
        }
        //获取密码以及加密方式
        String salt = accountInfoDb.getSalt();
        System.out.println("salt = " + salt);
        System.out.println("accountInfoDb = " + accountInfoDb.getPassword());
        System.out.println("password = " + password);
        if (!PasswordUtils.checkPassword(salt, password, accountInfoDb.getPassword())) {
            return 0;
        } else {
            redisCache.setCacheObject(POINTS_ACCOUNT_PASSWORD_CHECK + userId, true, 60 * 60, TimeUnit.SECONDS);
            return 1;
        }
    }

}
