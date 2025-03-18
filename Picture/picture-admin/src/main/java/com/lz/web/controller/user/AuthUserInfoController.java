package com.lz.web.controller.user;

import com.lz.common.constant.Constants;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.core.domain.model.LoginUser;
import com.lz.userauth.controller.BaseUserInfoController;
import com.lz.userauth.model.domain.AuthUserInfo;
import com.lz.userauth.model.domain.LoginUserInfo;
import com.lz.userauth.model.domain.UserInfoLoginBody;
import com.lz.framework.web.service.UserInfoLoginService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 用户信息Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/user")
public class AuthUserInfoController extends BaseUserInfoController {
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

    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        LoginUserInfo loginUser = getLoginUser();
        Set<String> permissions = loginUser.getPermissions();
        AuthUserInfo user = loginUser.getUser();
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("user", user);
        ajaxResult.put("permissions", permissions);
        return ajaxResult;
    }
}
