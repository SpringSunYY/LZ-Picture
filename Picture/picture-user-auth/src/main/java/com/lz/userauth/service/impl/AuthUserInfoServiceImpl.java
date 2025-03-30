package com.lz.userauth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.uuid.IdUtils;
import com.lz.config.model.domain.MenuInfo;
import com.lz.config.model.enmus.CMenuVisible;
import com.lz.config.service.IMenuInfoService;
import com.lz.userauth.mapper.AuthUserInfoMapper;
import com.lz.userauth.model.domain.AuthBannedPermissionInfo;
import com.lz.userauth.model.domain.AuthUserInfo;
import com.lz.userauth.model.domain.ForgetPasswordBody;
import com.lz.userauth.model.enmus.UBannedPermissionStatusEnum;
import com.lz.userauth.model.enmus.UUserStatusEnum;
import com.lz.userauth.model.domain.RegisterLoginBody;
import com.lz.userauth.service.IAuthBannedPermissionInfoService;
import com.lz.userauth.service.IAuthUserInfoService;
import com.lz.userauth.utils.UserInfoSecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
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
    private IMenuInfoService menuInfoService;
    ;

    @Resource
    private IAuthBannedPermissionInfoService bannedPermissionInfoService;


    @Override
    public AuthUserInfo selectUserInfoByUserName(String username) {
        return this.getOne(new LambdaQueryWrapper<AuthUserInfo>().eq(AuthUserInfo::getUserName, username));
    }

    @Override
    public Set<String> getUserPermission(AuthUserInfo user) {
        //获取所有的权限，用户默认拥有所有权限 查询显示的
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setVisible(CMenuVisible.MENU_VISIBLE_1.getValue());
        List<MenuInfo> menuInfos = menuInfoService.selectMenuInfoList(menuInfo);
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
                menuInfos.removeIf(permissionInfo -> permissionInfo.getPerms().equals(banned.getPermissionName()));
            }
        }
        return menuInfos.stream().map(MenuInfo::getPerms).collect(Collectors.toSet());
    }

    @Override
    public AuthUserInfo selectUserInfoByPhone(String phone, String countryCode) {
        return this.getOne(new LambdaQueryWrapper<AuthUserInfo>().eq(AuthUserInfo::getPhone, phone).eq(AuthUserInfo::getCountryCode, countryCode));
    }

    @Override
    public AuthUserInfo register(RegisterLoginBody registerLoginBody) {
        String countryCode = registerLoginBody.getCountryCode();
        String phone = registerLoginBody.getPhone();
        String password = registerLoginBody.getPassword();

        AuthUserInfo authUserInfo = new AuthUserInfo();
        authUserInfo.setPassword(password);
        //随机为用户设置加密方式 md5、bcrypt 随机数，如果是1，则使用bcrypt加密，否则使用md5加密
        if (new Random().nextInt(2) == 1) {
            authUserInfo.setPassword(UserInfoSecurityUtils.encodeEncryptPassword(password));
            authUserInfo.setSalt("bcrypt");
        } else {
            authUserInfo.setPassword(UserInfoSecurityUtils.encodeMd5Password(password));
            authUserInfo.setSalt("md5");
        }
        authUserInfo.setPhone(phone);
        authUserInfo.setCountryCode(countryCode);
        authUserInfo.setUserName("LZ-" + phone + "-" + RandomUtil.randomString(6));
        authUserInfo.setNickName("LZ-" + phone);
        authUserInfo.setStatus(UUserStatusEnum.USER_STATUS_0.getValue());
        authUserInfo.setCreateTime(DateUtils.getNowDate());
        authUserInfo.setUserId(IdUtils.fastSimpleUUID());
        authUserInfo.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        authUserInfoMapper.insert(authUserInfo);
        return authUserInfo;
    }

    @Override
    public AuthUserInfo forgetPassword(ForgetPasswordBody forgetPasswordBody) {
        //查询用户是否存在
        AuthUserInfo authUserInfo = this.selectUserInfoByPhone(forgetPasswordBody.getPhone(), forgetPasswordBody.getCountryCode());
        if (StringUtils.isNull(authUserInfo)) {
            throw new RuntimeException("请检查您的账号是否存在");
        }
        //判断用户是否被禁用
        if (authUserInfo.getStatus().equals(UUserStatusEnum.USER_STATUS_2.getValue())) {
            throw new RuntimeException("您的账号已被禁用，请联系管理员");
        }
        String password = forgetPasswordBody.getPassword();
        //随机为用户设置加密方式 md5、bcrypt 随机数，如果是1，则使用bcrypt加密，否则使用md5加密
        if (new Random().nextInt(2) == 1) {
            authUserInfo.setPassword(UserInfoSecurityUtils.encodeEncryptPassword(password));
            authUserInfo.setSalt("bcrypt");
        } else {
            authUserInfo.setPassword(UserInfoSecurityUtils.encodeMd5Password(password));
            authUserInfo.setSalt("md5");
        }
        authUserInfoMapper.updateById(authUserInfo);
        return authUserInfo;
    }

}
