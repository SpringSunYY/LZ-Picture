package com.lz.framework.web.service;

import java.util.Set;

import com.lz.config.service.IMenuInfoService;
import com.lz.userauth.model.domain.LoginUserInfo;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.lz.common.constant.Constants;
import com.lz.common.utils.StringUtils;
import com.lz.framework.security.context.PermissionContextHolder;

/**
 * RuoYi首创 自定义权限实现，ss取自SpringSecurity首字母
 *
 * @author YY
 */
@Service("uss")
public class UserPermissionService {

    @Resource
    private IMenuInfoService menuInfoService;

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        System.out.println("permission = " + permission);
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        LoginUserInfo loginUserInfo = UserInfoSecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUserInfo) || CollectionUtils.isEmpty(loginUserInfo.getPermissions())) {
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return hasPermissions(loginUserInfo.getPermissions(), permission);
    }

    /**
     * 校验接口是否关闭
     *
     * @return
     */
    private boolean checkMenu(String permission) {
        return menuInfoService.checkMenu(permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermi(String permission) {
        return hasPermi(permission) != true;
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以 PERMISSION_DELIMETER 为分隔符的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String permissions) {
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        LoginUserInfo loginUserInfo = UserInfoSecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUserInfo) || CollectionUtils.isEmpty(loginUserInfo.getPermissions())) {
            return false;
        }
        PermissionContextHolder.setContext(permissions);
        Set<String> authorities = loginUserInfo.getPermissions();
        for (String permission : permissions.split(Constants.PERMISSION_DELIMETER)) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        if (!checkMenu(permission)) {
            return false;
        }
        return permissions.contains(Constants.ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission)) ;
    }
}
