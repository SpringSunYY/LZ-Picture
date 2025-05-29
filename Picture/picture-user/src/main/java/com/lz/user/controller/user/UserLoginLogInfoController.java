package com.lz.user.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.user.model.domain.LoginLogInfo;
import com.lz.user.model.dto.loginLogInfo.UserLoginLogInfoQuery;
import com.lz.user.model.vo.loginLogInfo.MyLoginLogInfoVo;
import com.lz.user.model.vo.userInfo.MyUserInfoVo;
import com.lz.user.service.ILoginLogInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户登录日志Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/user/user/loginLogInfo")
public class UserLoginLogInfoController extends BaseUserInfoController {
    @Resource
    private ILoginLogInfoService loginLogInfoService;

    /**
     * 查询用户登录日志列表
     */
    @PreAuthorize("@uss.hasLogin()")
    @GetMapping("/list")
    public TableDataInfo list(UserLoginLogInfoQuery userLoginLogInfoQuery) {
        if (StringUtils.isNull(userLoginLogInfoQuery.getPageSize())) {
            userLoginLogInfoQuery.setPageSize(50);
        }
        if (userLoginLogInfoQuery.getPageSize() > 50) {
            userLoginLogInfoQuery.setPageSize(50);
        }
        userLoginLogInfoQuery.setUserId(getUserId());
        Page<LoginLogInfo> page = loginLogInfoService.selectUserLoginLogInfoList(userLoginLogInfoQuery);
        List<MyLoginLogInfoVo> myLoginLogInfoVos = MyLoginLogInfoVo.objToVo(page.getRecords());
        return getDataTable(myLoginLogInfoVos, page.getTotal());
    }
}
