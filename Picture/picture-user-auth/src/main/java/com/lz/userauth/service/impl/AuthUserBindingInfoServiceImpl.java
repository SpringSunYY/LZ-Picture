package com.lz.userauth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.userauth.mapper.AuthUserBindingInfoMapper;
import com.lz.userauth.model.domain.AuthUserBindingInfo;
import com.lz.userauth.service.IAuthUserBindingInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户第三方账号绑定Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class AuthUserBindingInfoServiceImpl extends ServiceImpl<AuthUserBindingInfoMapper, AuthUserBindingInfo> implements IAuthUserBindingInfoService
{
    @Resource
    private AuthUserBindingInfoMapper authUserBindingInfoMapper;

}
