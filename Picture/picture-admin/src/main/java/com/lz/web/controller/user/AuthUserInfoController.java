package com.lz.web.controller.user;

import com.lz.common.constant.Constants;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.framework.web.domain.UserInfoLoginBody;
import com.lz.framework.web.service.UserInfoLoginService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/user")
public class AuthUserInfoController extends BaseController {
    @Resource
    private UserInfoLoginService loginService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult customerLogin(@RequestBody UserInfoLoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.userInfoLogin(loginBody.getUsername(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

}
