package com.lz.userauth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.userauth.mapper.AuthBannedPermissionInfoMapper;
import com.lz.userauth.model.domain.AuthBannedPermissionInfo;
import com.lz.userauth.service.IAuthBannedPermissionInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户封禁权限Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class AuthBannedPermissionInfoServiceImpl extends ServiceImpl<AuthBannedPermissionInfoMapper, AuthBannedPermissionInfo> implements IAuthBannedPermissionInfoService {
    @Resource
    private AuthBannedPermissionInfoMapper authBannedPermissionInfoMapper;


}
