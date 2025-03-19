package com.lz.framework.web.service;

import cn.hutool.core.util.StrUtil;
import com.lz.common.constant.CacheConstants;
import com.lz.common.constant.Constants;
import com.lz.common.constant.UserConstants;
import com.lz.common.constant.redis.UserRedisConstants;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.exception.ServiceException;
import com.lz.common.exception.user.CaptchaException;
import com.lz.common.exception.user.CaptchaExpireException;
import com.lz.common.exception.user.UserNotExistsException;
import com.lz.common.exception.user.UserPasswordNotMatchException;
import com.lz.common.utils.MessageUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.framework.manager.AsyncManager;
import com.lz.framework.manager.factory.AsyncFactory;
import com.lz.framework.security.context.AuthenticationContextHolder;
import com.lz.userauth.model.domain.LoginUserInfo;
import com.lz.userauth.model.domain.AuthUserInfo;
import com.lz.userauth.model.sms.SmsLoginBody;
import com.lz.userauth.service.IAuthUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.lz.framework.web.service.UserInfoTokenService.LOGIN_USER_KEY;


/**
 * 登录校验方法
 *
 * @author YY
 */
@Component
public class UserInfoLoginService {
    // 是否允许账户多终端同时登录（true允许 false不允许）
    @Value("${token.soloLogin}")
    private boolean soloLogin;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private IAuthUserInfoService authUserInfoService;


    @Resource
    private UserInfoTokenService userTokenService;

    @Resource(name = "userInfoAuthenticationManager")
    private AuthenticationManager authenticationManager;


    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String userInfoLogin(String username, String password) {
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用userInfoDetailsService.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
            authentication.getPrincipal();
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUserInfo loginUser = (LoginUserInfo) authentication.getPrincipal();
//        recordLoginInfo(loginUser.getUserId());
        if (!soloLogin) {
            // 如果用户不允许多终端同时登录，清除缓存信息
            String userIdKey = LOGIN_USER_KEY + loginUser.getUser().getUserId();
            String userKey = redisCache.getCacheObject(userIdKey);
            if (StringUtils.isNotEmpty(userKey)) {
                redisCache.deleteObject(userIdKey);
                redisCache.deleteObject(userKey);
            }
        }
        AuthUserInfo authUserInfo = authUserInfoService.selectUserInfoByUserName(username);
        if (!SecurityUtils.matchesPassword(password, authUserInfo.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        Set<String> userPermission = authUserInfoService.getUserPermission(authUserInfo);
        LoginUserInfo loginUserInfo = new LoginUserInfo(authUserInfo.getUserId(), authUserInfo, userPermission);
        // 生成token
        return userTokenService.createToken(loginUserInfo);
    }

    /**
     * 登录前置校验
     *
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
    }

    public String getSmsLoginCode(SmsLoginBody smsLoginBody) {
        //校验验证码
        boolean captchaEnabled = smsLoginBody.isCaptchaEnabled();
        String uuid = smsLoginBody.getUuid();
        validateCaptcha(smsLoginBody, captchaEnabled, uuid);
        //校验成功发送验证码
        //随机6位数验证码
        String code = StringUtils.generateCode();
        redisCache.setCacheObject(UserRedisConstants.USER_SMS_LOGIN_CODE + smsLoginBody.getPhone(), code, UserRedisConstants.USER_SMS_LOGIN_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        // TODO 发送真正验证码
        return code;
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
    private void validateCaptcha(SmsLoginBody smsLoginBody, boolean captchaEnabled, String uuid) {
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (StringUtils.isEmpty(captcha)) {
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!smsLoginBody.getCode().equalsIgnoreCase(captcha)) {
                throw new CaptchaException();
            }
        }
    }
}