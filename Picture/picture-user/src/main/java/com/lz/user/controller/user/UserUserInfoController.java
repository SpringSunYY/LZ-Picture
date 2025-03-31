package com.lz.user.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.config.model.domain.MenuInfo;
import com.lz.config.model.vo.menuInfo.MenuInfoUserVo;
import com.lz.user.service.IUserInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * Project: Picture
 * Package: com.lz.user.controller.user
 * Author: YY
 * CreateTime: 2025-03-31  22:11
 * Description: UserInfoController
 * Version: 1.0
 */
@RestController
@RequestMapping("/user/user/userInfo")
public class UserUserInfoController extends BaseUserInfoController {
    @Resource
    private IUserInfoService userInfoService;

    @GetMapping(value = "/menu")
    public AjaxResult getMenu() {
        Set<String> permissions = getLoginUser().getPermissions();
        List<MenuInfo> menu = userInfoService.getMenu(permissions);
        //转换为Vo
        List<MenuInfoUserVo> menuInfoList = menu.stream().map(MenuInfoUserVo::objToVo).toList();
        return AjaxResult.success(menuInfoList);
    }
}
