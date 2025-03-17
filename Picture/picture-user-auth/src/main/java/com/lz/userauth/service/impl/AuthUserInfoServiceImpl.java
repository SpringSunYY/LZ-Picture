package com.lz.userauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.utils.StringUtils;
import com.lz.config.model.domain.PermissionInfo;
import com.lz.config.service.IPermissionInfoService;
import com.lz.userauth.mapper.AuthUserInfoMapper;
import com.lz.userauth.model.domain.AuthBannedPermissionInfo;
import com.lz.userauth.model.domain.AuthUserInfo;
import com.lz.userauth.model.enmus.UBannedPermissionStatusEnum;
import com.lz.userauth.service.IAuthBannedPermissionInfoService;
import com.lz.userauth.service.IAuthUserInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class AuthUserInfoServiceImpl extends ServiceImpl<AuthUserInfoMapper, AuthUserInfo> implements IAuthUserInfoService {
    @Resource
    private AuthUserInfoMapper authUserInfoMapper;

    @Resource
    private IPermissionInfoService permissionInfoService;
    ;

    @Resource
    private IAuthBannedPermissionInfoService bannedPermissionInfoService;




    @Override
    public AuthUserInfo selectUserInfoByUserName(String username) {
        return this.getOne(new LambdaQueryWrapper<AuthUserInfo>().eq(AuthUserInfo::getUserName, username));
    }

    @Override
    public Set<String> getUserPermission(AuthUserInfo user) {
        //获取所有的权限，用户默认拥有所有权限
        List<PermissionInfo> permissionInfos = permissionInfoService.selectPermissionInfoList(new PermissionInfo());
        //获取用户被封禁的权限
        AuthBannedPermissionInfo authBannedPermissionInfo = new AuthBannedPermissionInfo();
        authBannedPermissionInfo.setUserId(user.getUserId());
        authBannedPermissionInfo.setStatus(UBannedPermissionStatusEnum.BANNED_PERMISSION_STATUS_0.getValue());
        List<AuthBannedPermissionInfo> authBannedPermissionInfos = bannedPermissionInfoService.list(new LambdaQueryWrapper<AuthBannedPermissionInfo>()
                .eq(AuthBannedPermissionInfo::getUserId, authBannedPermissionInfo.getUserId())
                .eq(AuthBannedPermissionInfo::getStatus, authBannedPermissionInfo.getStatus()));
        //判断用户是否有封禁的权限，如果有则在所有权限里面删除
        if (StringUtils.isNotEmpty(authBannedPermissionInfos)) {
            for (AuthBannedPermissionInfo banned : authBannedPermissionInfos) {
                permissionInfos.removeIf(permissionInfo -> permissionInfo.getPermissionName().equals(banned.getPermissionName()));
            }
        }
        return permissionInfos.stream().map(PermissionInfo::getPermissionName).collect(Collectors.toSet());
    }

}
