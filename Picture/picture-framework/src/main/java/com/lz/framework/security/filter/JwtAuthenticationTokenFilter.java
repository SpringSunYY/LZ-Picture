package com.lz.framework.security.filter;

import java.io.IOException;
import java.util.Collection;

import com.lz.common.core.domain.model.LoginUserInfo;
import com.lz.framework.web.service.UserInfoTokenService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.lz.common.core.domain.model.LoginUser;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.framework.web.service.TokenService;

import static io.lettuce.core.MigrateArgs.Builder.auth;

/**
 * token过滤器 验证token有效性
 *
 * @author YY
 */
@Slf4j
@Component("jwtAuthenticationTokenFilter")
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private TokenService tokenService;

    @Resource
    private UserInfoTokenService userInfoTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        logger.info("请求地址:" + request.getRequestURI());
        if (request.getRequestURI().startsWith("/user")) {
            log.info("前台接口请求:" + request.getRequestURI());
            LoginUserInfo loginUserInfo = userInfoTokenService.getLoginUser(request);
//            System.out.println("loginUserInfo = " + loginUserInfo);
            if (StringUtils.isNotNull(loginUserInfo) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
                userInfoTokenService.verifyToken(loginUserInfo);
                auth(loginUserInfo, loginUserInfo.getAuthorities(), request);
            }
        } else {
            logger.info("后台接口请求:" + request.getRequestURI());
            LoginUser loginUser = tokenService.getLoginUser(request);
            if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
                tokenService.verifyToken(loginUser);
                auth(loginUser, loginUser.getAuthorities(), request);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 排除登录端点和其他公共端点
        return request.getRequestURI().startsWith("/login") ||
                request.getRequestURI().startsWith("/user/auth/login");
    }

    /**
     * 验证token
     *
     * @param obj         登录信息
     * @param authorities 已授予的权限集合
     * @param request     网络请求
     */
    private void auth(Object obj, Collection<? extends GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(obj, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
