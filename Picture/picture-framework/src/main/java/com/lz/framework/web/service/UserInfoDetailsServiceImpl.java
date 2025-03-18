package com.lz.framework.web.service;

import com.lz.common.exception.ServiceException;
import com.lz.common.utils.MessageUtils;
import com.lz.common.utils.StringUtils;
import com.lz.userauth.model.domain.LoginUserInfo;
import com.lz.userauth.model.domain.AuthUserInfo;
import com.lz.userauth.model.enmus.UUserStatusEnum;
import com.lz.userauth.service.IAuthUserInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Slf4j
@Service("userInfoDetailsService")
public class UserInfoDetailsServiceImpl implements UserDetailsService {

    @Resource
    private IAuthUserInfoService authUserInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserInfo user = authUserInfoService.selectUserInfoByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException(StringUtils.format("登录用户：{}不存在哦", username));
        } else if (UUserStatusEnum.USER_STATUS_2.getValue().equals(user.getStatus())) {
            log.info("登录用户：{} 已被禁用.", username);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        }
        Set<String> userPermission = authUserInfoService.getUserPermission(user);
        return createLoginUser(user, userPermission);
    }

    public UserDetails createLoginUser(AuthUserInfo user, Set<String> userPermission) {
        return new LoginUserInfo(user.getUserId(), user, userPermission);
    }
}