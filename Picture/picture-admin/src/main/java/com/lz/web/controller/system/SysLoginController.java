package com.lz.web.controller.system;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson2.JSON;
import com.lz.common.utils.MessageUtils;
import com.lz.common.utils.ServletUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.sign.RsaUtils;
import com.lz.framework.manager.AsyncManager;
import com.lz.framework.manager.factory.AsyncFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lz.common.constant.Constants;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.domain.entity.SysMenu;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.core.domain.model.LoginBody;
import com.lz.common.core.domain.model.LoginUser;
import com.lz.common.utils.SecurityUtils;
import com.lz.framework.web.service.SysLoginService;
import com.lz.framework.web.service.SysPermissionService;
import com.lz.framework.web.service.TokenService;
import com.lz.system.service.ISysMenuService;

import static com.lz.common.core.domain.AjaxResult.success;

/**
 * 登录验证
 *
 * @author YY
 */
@RestController
@RequestMapping("/admin")
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) throws Exception {
        AjaxResult ajax = success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), RsaUtils.decryptByPrivateKey(loginBody.getPassword()), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    @PostMapping("/logout")
    public AjaxResult logout(HttpServletRequest request, HttpServletResponse response) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(success(MessageUtils.message("user.logout.success"))));
        return success();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions)) {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return success(menuService.buildMenus(menus));
    }
}
