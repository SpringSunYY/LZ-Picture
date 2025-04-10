package com.lz.framework.security.auth;

import com.lz.framework.manager.PasswordEncoderStrategy;
import com.lz.framework.manager.factory.PasswordEncoderFactory;
import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.common.core.domain.model.AuthUserInfo;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class MultiAuthProvider implements AuthenticationProvider {
    @Resource
    private PasswordEncoderFactory encoderFactory;

    @Resource(name = "userInfoDetailsService")
    private UserDetailsService userInfoDetailsService;

    @Override
    public Authentication authenticate(Authentication auth) {
        String username = auth.getName();
        String rawPassword = auth.getCredentials().toString();

        UserDetails user = userInfoDetailsService.loadUserByUsername(username);
        AuthUserInfo authUser = ((LoginUserInfo) user).getUser(); // 获取自定义用户对象

        // 根据用户配置选择加密策略
        PasswordEncoderStrategy strategy = encoderFactory.getStrategy(
                authUser.getSalt()
        );

        if (!strategy.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }

        return new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
