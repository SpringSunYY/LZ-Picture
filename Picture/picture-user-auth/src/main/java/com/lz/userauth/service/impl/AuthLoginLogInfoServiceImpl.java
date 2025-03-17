package com.lz.userauth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.userauth.mapper.AuthLoginLogInfoMapper;
import com.lz.userauth.model.domain.AuthLoginLogInfo;
import com.lz.userauth.service.IAuthLoginLogInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 用户登录日志Service业务层处理
 *
 * @author YY
 * @date 2025-03-17
 */
@Service
public class AuthLoginLogInfoServiceImpl extends ServiceImpl<AuthLoginLogInfoMapper, AuthLoginLogInfo> implements IAuthLoginLogInfoService
{
    @Resource
    private AuthLoginLogInfoMapper authLoginLogInfoMapper;



}
