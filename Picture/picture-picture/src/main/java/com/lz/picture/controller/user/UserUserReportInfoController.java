package com.lz.picture.controller.user;

import com.lz.common.annotation.Log;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.enums.BusinessType;
import com.lz.picture.model.domain.UserReportInfo;
import com.lz.picture.model.dto.userReportInfo.UserReportInfoAdd;
import com.lz.picture.service.IUserReportInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户举报信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/user/picture/userReportInfo")
public class UserUserReportInfoController extends BaseUserInfoController {
    @Resource
    private IUserReportInfoService userReportInfoService;

    /**
     * 新增用户举报信息
     */
    @PreAuthorize("@uss.hasLogin()")
    @Log(title = "用户举报信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody UserReportInfoAdd userReportInfoAdd) {
        UserReportInfo userReportInfo = UserReportInfoAdd.insertToObj(userReportInfoAdd);
        userReportInfo.setUserId(getUserId());
        return toAjax(userReportInfoService.userInsertUserReportInfo(userReportInfo));
    }
}
