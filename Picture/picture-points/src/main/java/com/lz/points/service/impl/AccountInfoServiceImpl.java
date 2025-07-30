package com.lz.points.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.CustomSort;
import com.lz.common.constant.CacheConstants;
import com.lz.common.constant.HttpStatus;
import com.lz.common.constant.config.TemplateInfoKeyConstants;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.exception.user.CaptchaException;
import com.lz.common.exception.user.CaptchaExpireException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.config.manager.sms.SmsTemplate;
import com.lz.points.mapper.AccountInfoMapper;
import com.lz.points.model.domain.AccountInfo;
import com.lz.points.model.dto.accountInfo.AccountInfoQuery;
import com.lz.points.model.dto.accountInfo.AccountPasswordUploadRequest;
import com.lz.points.model.dto.accountInfo.ResetAccountPasswordBody;
import com.lz.points.model.enums.PoAccountStatusEnum;
import com.lz.points.model.vo.accountInfo.AccountInfoVo;
import com.lz.points.service.IAccountInfoService;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.service.IUserInfoService;
import com.lz.userauth.model.domain.EncryptionPassword;
import com.lz.userauth.utils.PasswordUtils;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.lz.common.constant.Constants.COMMON_SEPARATOR_CACHE;
import static com.lz.common.constant.config.LocaleConstants.ZH_CN;
import static com.lz.common.constant.redis.PointsRedisConstants.*;
import static com.lz.config.utils.ConfigInfoUtils.POINTS_ACCOUNT_VERIFY_PASSWORD_TIMEOUT_VALUE;

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

    @Resource
    private SmsTemplate smsTemplate;

    @Resource
    private IUserInfoService userInfoService;

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
    @CustomSort(sortFields = {"pointsEarned", "pointsUsed", "rechargeAmount", "pointsBalance",
            "createTime", "updateTime"},
            sortMappingFields = {"points_earned", "points_used", "recharge_amount", "points_balance",
                    "create_time", "update_time"})
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
        redisCache.deleteObject(POINTS_ACCOUNT_PASSWORD_CHECK + request.getUserId());
        return accountInfoMapper.updateById(accountInfoDb);
    }

    @Override
    public int getVerifyPassword(String userId) {
        //从缓存里面获取账号是否存在
        Boolean cache = redisCache.getCacheObject(POINTS_ACCOUNT_PASSWORD_CHECK + userId);
        return StringUtils.isNotNull(cache) && cache ? 1 : 0;
    }

    @Override
    public int verifyPassword(String userId, String password) {
        //首先查询是否有账号
        AccountInfo accountInfoDb = this.selectAccountInfoByUserId(userId);
        if (StringUtils.isNull(accountInfoDb)) {
            throw new ServiceException("您当前还没有账户，请先购买积分，平台会为您创建账户！！！", HttpStatus.NO_CONTENT);
        }
        ThrowUtils.throwIf(!accountInfoDb.getAccountStatus().equals(PoAccountStatusEnum.ACCOUNT_STATUS_0.getValue()), HttpStatus.UNAUTHORIZED, "账号异常，请联系管理员！！！");
        //获取密码以及加密方式
        String salt = accountInfoDb.getSalt();
        if (!PasswordUtils.checkPassword(salt, password, accountInfoDb.getPassword())) {
            return 0;
        } else {
            redisCache.setCacheObject(POINTS_ACCOUNT_PASSWORD_CHECK + userId, true, POINTS_ACCOUNT_VERIFY_PASSWORD_TIMEOUT_VALUE, TimeUnit.SECONDS);
            return 1;
        }
    }

    @Override
    public AccountInfo selectUserAccountInfoByUserId(String userId) {
        AccountInfo cacheObject = redisCache.getCacheObject(POINTS_ACCOUNT_INFO + userId);
        if (StringUtils.isNotNull(cacheObject)) {
            return cacheObject;
        }
        AccountInfo accountInfo = this.selectAccountInfoByUserId(userId);
        if (StringUtils.isNotNull(accountInfo)) {
            accountInfo.setPointsBalance(accountInfo.getPointsBalance());
            accountInfo.setPointsEarned(accountInfo.getPointsEarned());
            accountInfo.setPointsUsed(accountInfo.getPointsUsed());
            accountInfo.setRechargeAmount(accountInfo.getRechargeAmount());
        } else {
            accountInfo = new AccountInfo();
            accountInfo.setPointsBalance(0L);
            accountInfo.setPointsEarned(0L);
            accountInfo.setPointsUsed(0L);
            accountInfo.setRechargeAmount(new BigDecimal(BigInteger.ZERO));
        }
        redisCache.setCacheObject(POINTS_ACCOUNT_INFO + userId, accountInfo, POINTS_ACCOUNT_INFO_EXPIRE_TIME, TimeUnit.SECONDS);
        return accountInfo;
    }

    @Override
    public String getAccountPasswordCode(String phone, String countryCode, String code, boolean captchaEnabled, String uuid) {
        validateCaptcha(code, captchaEnabled, uuid);
        //校验用户手机号是否正确
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(UserInfoSecurityUtils.getUserId());
        if (!(userInfo.getPhone().equals(phone) && userInfo.getCountryCode().equals(countryCode))) {
            throw new ServiceException("手机号与用户手机号不一致");
        }
        String registerCode = StringUtils.generateCode();
        String key = POINTS_ACCOUNT_RESET_PASSWORD_CODE + countryCode + COMMON_SEPARATOR_CACHE + phone;
        smsTemplate.sendCode(key, TemplateInfoKeyConstants.SMS_ACCOUNT_RESET_PASSWORD_CODE, registerCode, phone, ZH_CN);
        redisCache.setCacheObject(key, registerCode, POINTS_ACCOUNT_RESET_PASSWORD_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        return registerCode;
    }

    @Override
    public void checkSmsCode(String key, String countryCode, String phone, String code) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(countryCode) || StringUtils.isEmpty(code)) {
            throw new ServiceException("参数异常");
        }
        String redisKey = key + countryCode + ":" + phone;
//        System.out.println("redisKey = " + redisKey);
        String registerCode = redisCache.getCacheObject(redisKey);
        if (StringUtils.isEmpty(registerCode)) {
            throw new ServiceException("短信验证码已过期");
        }
        if (!code.equalsIgnoreCase(registerCode)) {
            throw new ServiceException("短信验证码不正确");
        }
        redisCache.deleteObject(redisKey);
    }

    /**
     * description: 校验验证码
     * author: YY
     * method: validateCaptcha
     * date: 2025/3/19 09:09
     * param:
     * param: smsLoginBody
     * param: captchaEnabled
     * param: uuid
     * return: void
     **/
    private void validateCaptcha(String code, boolean captchaEnabled, String uuid) {
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (StringUtils.isEmpty(captcha)) {
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha)) {
                throw new CaptchaException();
            }
        }
    }

    @Override
    public AccountInfo resetAccountPassword(ResetAccountPasswordBody resetAccountPasswordBody) {
        //查询是否有账户
        AccountInfo accountInfo = this.selectAccountInfoByUserId(UserInfoSecurityUtils.getUserId());
        if (StringUtils.isNull(accountInfo)) {
            //没有账户创建一个账户
            accountInfo = new AccountInfo();
            accountInfo.setPointsBalance(0L);
            accountInfo.setPointsEarned(0L);
            accountInfo.setPointsUsed(0L);
            accountInfo.setRechargeAmount(new BigDecimal(BigInteger.ZERO));
            accountInfo.setUserId(UserInfoSecurityUtils.getUserId());
            accountInfo.setAccountStatus(PoAccountStatusEnum.ACCOUNT_STATUS_0.getValue());
            accountInfo.setCreateTime(DateUtils.getNowDate());
            accountInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        }
        //校验用户手机号是否正确
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(UserInfoSecurityUtils.getUserId());
        if (!(userInfo.getPhone().equals(resetAccountPasswordBody.getPhone()) && userInfo.getCountryCode().equals(resetAccountPasswordBody.getCountryCode()))) {
            throw new ServiceException("手机号与用户手机号不一致");
        }
        String password = resetAccountPasswordBody.getPassword();
        //加密
        EncryptionPassword encryptionPassword = PasswordUtils.encryptPassword(password);
        accountInfo.setSalt(encryptionPassword.getSalt());
        accountInfo.setPassword(encryptionPassword.getPassword());
        this.saveOrUpdate(accountInfo);
        return accountInfo;
    }
}
