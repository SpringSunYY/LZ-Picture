package com.lz.framework.web.service;

import com.lz.common.constant.CacheConstants;
import com.lz.common.constant.Constants;
import com.lz.common.constant.UserConstants;
import com.lz.common.constant.config.UserConfigConstants;
import com.lz.common.constant.redis.UserRedisConstants;
import com.lz.common.core.redis.RedisCache;
import com.lz.common.enums.ULoginStatus;
import com.lz.common.enums.ULoginType;
import com.lz.common.exception.ServiceException;
import com.lz.common.exception.user.CaptchaException;
import com.lz.common.exception.user.CaptchaExpireException;
import com.lz.common.exception.user.UserNotExistsException;
import com.lz.common.exception.user.UserPasswordNotMatchException;
import com.lz.common.utils.MessageUtils;
import com.lz.common.utils.StringUtils;
import com.lz.config.manager.sms.SmsTemplate;
import com.lz.config.manager.sms.model.SmsResponse;
import com.lz.framework.manager.AsyncManager;
import com.lz.framework.manager.factory.AsyncFactory;
import com.lz.framework.manager.factory.UserInfoLoginAsyncFactory;
import com.lz.framework.security.context.AuthenticationContextHolder;
import com.lz.common.core.domain.model.AuthUserInfo;
import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.userauth.service.IAuthUserInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.lz.common.constant.config.UserConfigConstants.SMS_LOGIN_CODE;
import static com.lz.framework.web.service.UserInfoTokenService.LOGIN_USER_KEY;


/**
 * 登录校验方法
 *
 * @author YY
 */
//TODO 登录记录
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

    @Resource
    private SmsTemplate smsTemplate;


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
//        if (!SecurityUtils.matchesPassword(password, authUserInfo.getPassword())) {
//            throw new ServiceException("用户名或密码错误");
//        }
        Set<String> userPermission = authUserInfoService.getUserPermission(authUserInfo);
        LoginUserInfo loginUserInfo = new LoginUserInfo(authUserInfo.getUserId(), authUserInfo, userPermission);
        AsyncManager.me().execute(UserInfoLoginAsyncFactory.userInfoLogin(username, authUserInfo.getUserId(), ULoginType.LOGIN_TYPE_0.getValue(), ULoginStatus.LOGIN_STATUS_0.getValue(), "登录成功"));
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

    /**
     * description: 获取短信验证码
     * author: YY
     * method: getSmsLoginCode
     * date: 2025/3/19 23:04
     * param:
     * param: phone 手机号码
     * param: countryCode 国家代码
     * param: captchaCode 图形验证码
     * param: captchaEnabled 是否开启验证码
     * param: uuid 唯一标识
     * return: java.lang.String
     **/
    public String getSmsCode(String phone, String countryCode, String captchaCode, Boolean captchaEnabled, String uuid) {
        validateCaptcha(captchaCode, captchaEnabled, uuid);
        //校验成功发送验证码
        //随机6位数验证码
        String code = StringUtils.generateCode();
        redisCache.setCacheObject(UserRedisConstants.USER_SMS_LOGIN_CODE + countryCode + ":" + phone, code, UserRedisConstants.USER_SMS_LOGIN_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        // TODO 发送真正验证码 先写死zh
        smsTemplate.sendCode(UserConfigConstants.SMS_LOGIN_CODE, code, phone, "zh");
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

    /**
     * description: 短信登录
     * author: YY
     * method: smsLogin
     * date: 2025/3/19 23:20
     * param:
     * param: phone
     * param: countryCode
     * param: smsCode
     * return: java.lang.String
     **/
    public String smsLogin(String countryCode, String phone, String smsCode) {
        //不能为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(countryCode) || StringUtils.isEmpty(smsCode)) {
            throw new ServiceException("号码、国家码、短信验证码不能为空");
        }
        System.out.println("countryCode = " + countryCode);
        System.out.println("phone = " + phone);
        //校验验证码
        checkSmsCode(UserRedisConstants.USER_SMS_LOGIN_CODE, countryCode, phone, smsCode);
        //根据国家号码编号和号码获取用户
        AuthUserInfo authUserInfo = authUserInfoService.selectUserInfoByPhone(phone, countryCode);
        if (StringUtils.isNull(authUserInfo)) {
            throw new ServiceException("号码未注册");
        }
        Set<String> userPermission = authUserInfoService.getUserPermission(authUserInfo);
        LoginUserInfo loginUserInfo = new LoginUserInfo(authUserInfo.getUserId(), authUserInfo, userPermission);
        AsyncManager.me().execute(UserInfoLoginAsyncFactory.userInfoLogin(authUserInfo.getUserName(), authUserInfo.getUserId(), ULoginType.LOGIN_TYPE_1.getValue(), ULoginStatus.LOGIN_STATUS_0.getValue(), "登录成功"));
        // 生成token
        return userTokenService.createToken(loginUserInfo);
    }

    /**
     * description: 获取注册验证码—
     * author: YY
     * method: getRegisterCode
     * date: 2025/3/19 23:20
     * param:
     * param: phone
     * param: countryCode
     * param: code
     * param: captchaEnabled
     * param: uuid
     * return: java.lang.String
     **/
    public String getRegisterCode(String phone, String countryCode, String code, boolean captchaEnabled, String uuid) {
        //校验验证码
        validateCaptcha(code, captchaEnabled, uuid);
        //校验成功发送验证码
        String registerCode = StringUtils.generateCode();
        redisCache.setCacheObject(UserRedisConstants.USER_SMS_REGISTER_CODE + countryCode + ":" + phone, registerCode, UserRedisConstants.USER_SMS_REGISTER_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        //发送短信验证码
        SmsResponse smsResponse = smsTemplate.sendCode(UserConfigConstants.SMS_REGISTER_CODE, registerCode, phone, "zh");
        System.out.println("smsResponse = " + smsResponse);
        return registerCode;
    }

    public void checkSmsCode(String key, String countryCode, String phone, String code) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(countryCode) || StringUtils.isEmpty(code)) {
            throw new ServiceException("参数异常");
        }
        String redisKey = key + countryCode + ":" + phone;
        System.out.println("redisKey = " + redisKey);
        String registerCode = redisCache.getCacheObject(redisKey);
        if (StringUtils.isEmpty(registerCode)) {
            throw new ServiceException("短信验证码已过期");
        }
        if (!code.equalsIgnoreCase(registerCode)) {
            throw new ServiceException("短信验证码不正确");
        }
        redisCache.deleteObject(redisKey);
    }

    public void logout(HttpServletRequest request) {
        userTokenService.delLoginUser(userTokenService.getToken(request));
    }

    public String getForgetPasswordCode(String phone, String countryCode, String code, boolean captchaEnabled, String uuid) {
        validateCaptcha(code, captchaEnabled, uuid);
        String registerCode = StringUtils.generateCode();
        redisCache.setCacheObject(UserRedisConstants.USER_SMS_FORGET_PASSWORD_CODE + countryCode + ":" + phone, registerCode, UserRedisConstants.USER_SMS_FORGET_PASSWORD_CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        smsTemplate.sendCode(UserConfigConstants.SMS_FORGET_PASSWORD_CODE, registerCode, phone, "zh");
        return registerCode;
    }
}