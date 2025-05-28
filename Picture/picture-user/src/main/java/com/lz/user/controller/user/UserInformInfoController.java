package com.lz.user.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.user.model.domain.InformInfo;
import com.lz.user.model.dto.informInfo.UserInformInfoQuery;
import com.lz.user.model.vo.informInfo.UserInformInfoVo;
import com.lz.user.service.IInformInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户信息
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-28  23:23
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user/user/informInfo")
public class UserInformInfoController extends BaseUserInfoController {
    @Resource
    private IInformInfoService informInfoService;

    /**
     * 查询用户通知信息列表
     */
    @PreAuthorize("@uss.hasPermi('inform')")
    @GetMapping("/list")
    public TableDataInfo list(UserInformInfoQuery query) {
        if (StringUtils.isNull(query.getPageSize())) {
            query.setPageSize(50);
        }
        if (query.getPageSize() > 50) {
            query.setPageSize(50);
        }
        query.setUserId(getUserId());
        Page<InformInfo> page = informInfoService.selectUserInformInfoList(query);
        List<UserInformInfoVo> userInformInfoVos = UserInformInfoVo.objToVo(page.getRecords());
        return getDataTable(userInformInfoVos, page.getTotal());
    }
}
