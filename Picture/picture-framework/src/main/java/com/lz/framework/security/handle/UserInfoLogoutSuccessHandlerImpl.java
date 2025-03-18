package com.lz.framework.security.handle;

import com.alibaba.fastjson2.JSON;
import com.lz.common.constant.Constants;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.model.LoginUser;
import com.lz.common.utils.MessageUtils;
import com.lz.common.utils.ServletUtils;
import com.lz.common.utils.StringUtils;
import com.lz.framework.manager.AsyncManager;
import com.lz.framework.manager.factory.AsyncFactory;
import com.lz.framework.web.service.TokenService;
import com.lz.framework.web.service.UserInfoTokenService;
import com.lz.userauth.model.domain.LoginUserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 * 
 * @author YY
 */
@Configuration("userInfoLogoutSuccessHandler")
public class UserInfoLogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private UserInfoTokenService userInfoTokenService;

    /**
     * 退出处理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUserInfo loginUser = userInfoTokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            userInfoTokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
//            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}
