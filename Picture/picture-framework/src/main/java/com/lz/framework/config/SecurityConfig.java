package com.lz.framework.config;

import com.lz.framework.security.auth.MultiAuthProvider;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;
import com.lz.framework.config.properties.PermitAllUrlProperties;
import com.lz.framework.security.filter.JwtAuthenticationTokenFilter;
import com.lz.framework.security.handle.AuthenticationEntryPointImpl;
import com.lz.framework.security.handle.LogoutSuccessHandlerImpl;

import java.util.Collections;

/**
 * spring security配置
 *
 * @author ruoyi
 */
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig {
    /**
     * 自定义用户认证逻辑
     */
    @Resource
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Resource
    private MultiAuthProvider multiAuthProvider;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Resource
    private LogoutSuccessHandlerImpl logoutSuccessHandler;


    /**
     * token认证过滤器
     */
    @Autowired
    @Qualifier("jwtAuthenticationTokenFilter")
    private JwtAuthenticationTokenFilter authenticationTokenFilter;


    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * 允许匿名访问的地址
     */
    @Resource(name = "permitAllUrlProperties")
    private PermitAllUrlProperties permitAllUrlProperties;

    /**
     * 身份验证实现
     */
    @Bean(name = "authenticationManager")
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }


    @Bean(name = "userInfoAuthenticationManager")
    public AuthenticationManager userInfoAuthenticationManager() {
        return new ProviderManager(Collections.singletonList(multiAuthProvider));
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // CSRF禁用，因为不使用session
                .csrf(csrf -> csrf.disable())
                // 禁用HTTP响应标头
                .headers((headersCustomizer) -> {
                    headersCustomizer.cacheControl(cache -> cache.disable()).frameOptions(options -> options.sameOrigin());
                })
                // 认证失败处理类
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // 基于token，所以不需要session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 注解标记允许匿名访问的url
                .authorizeHttpRequests((requests) -> {
                    permitAllUrlProperties.getUrls().forEach(url -> requests.requestMatchers(url).permitAll());
                    // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                    requests.requestMatchers("/admin/login", "/admin/logout", "/admin/register", "/admin/captchaImage").permitAll()
                            .requestMatchers("/user/login", "user/captchaImage", "user/getSmsLoginCode",
                                    "user/smsLogin", "user/register", "user/getRegisterCode",
                                    "/user/getForgetPasswordCode", "/user/forgetPassword",
                                    "/user/picture/pictureInfo/list",
                                    "/user/picture/pictureInfo/search/recommend",
                                    "/user/picture/pictureInfo/recommend",
                                    "/user/picture/pictureInfo/search/suggestion",
                                    "/user/config/configInfo/key/**",
                                    "/user/points/pay/alipay/callback").permitAll()
                            // 静态资源，可匿名访问
                            .requestMatchers(HttpMethod.GET, "/", "/*.html", "/**.html", "/**.css", "/**.js", "/profile/**").permitAll()
                            .requestMatchers("/admin/swagger-ui.html", "/admin/v3/api-docs/**", "/admin/swagger-ui/**", "/admin/druid/**").permitAll()
                            // 除上面外的所有请求全部需要鉴权认证
                            .anyRequest().authenticated();
                })
                // 添加Logout filter
                .logout(logout -> logout.logoutUrl("/admin/logout").logoutSuccessHandler(logoutSuccessHandler))
//                .logout(logout -> logout.logoutUrl("/user/logout").logoutSuccessHandler(userInfoLogoutSuccessHandler))
                // 添加JWT filter
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加CORS filter
                .addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class)
                .addFilterBefore(corsFilter, LogoutFilter.class)
                .build();
    }

    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
