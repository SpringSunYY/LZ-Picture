package com.lz.user.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.config.OssConfig;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.system.service.ISysConfigService;
import com.lz.user.model.domain.UserInfo;
import com.lz.user.model.dto.userInfo.UserInfoEdit;
import com.lz.user.model.dto.userInfo.UserInfoInsert;
import com.lz.user.model.dto.userInfo.UserInfoQuery;
import com.lz.user.model.vo.userInfo.UserInfoVo;
import com.lz.user.service.IUserInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.lz.common.constant.ConfigConstants.PICTURE_P;

/**
 * 用户信息Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/admin/user/userInfo")
public class UserInfoController extends BaseController {
    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private ISysConfigService sysConfigService;

    /**
     * 查询用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('user:userInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserInfoQuery userInfoQuery) {
        UserInfo userInfo = UserInfoQuery.queryToObj(userInfoQuery);
        startPage();
        List<UserInfo> list = userInfoService.selectUserInfoList(userInfo);
        List<UserInfoVo> listVo = new ArrayList<>();
        String inCache = sysConfigService.selectConfigByKey(PICTURE_P);
        for (UserInfo info : list) {
            UserInfoVo userInfoVo = UserInfoVo.objToVo(info);
            userInfoVo.setAvatarUrl(OssConfig.builderPictureUrl(info.getAvatarUrl(), inCache));
            listVo.add(userInfoVo);
        }
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户信息列表
     */
    @PreAuthorize("@ss.hasPermi('user:userInfo:export')")
    @Log(title = "用户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserInfoQuery userInfoQuery) {
        UserInfo userInfo = UserInfoQuery.queryToObj(userInfoQuery);
        List<UserInfo> list = userInfoService.selectUserInfoList(userInfo);
        ExcelUtil<UserInfo> util = new ExcelUtil<UserInfo>(UserInfo.class);
        util.exportExcel(response, list, "用户信息数据");
    }

    /**
     * 获取用户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('user:userInfo:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") String userId) {
        UserInfo userInfo = userInfoService.selectUserInfoByUserId(userId);
        return success(UserInfoVo.objToVo(userInfo));
    }

    /**
     * 新增用户信息
     */
    @PreAuthorize("@ss.hasPermi('user:userInfo:add')")
    @Log(title = "用户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserInfoInsert userInfoInsert) {
        UserInfo userInfo = UserInfoInsert.insertToObj(userInfoInsert);
        return toAjax(userInfoService.insertUserInfo(userInfo));
    }

    /**
     * 修改用户信息
     */
    @PreAuthorize("@ss.hasPermi('user:userInfo:edit')")
    @Log(title = "用户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserInfoEdit userInfoEdit) {
        UserInfo userInfo = UserInfoEdit.editToObj(userInfoEdit);
        return toAjax(userInfoService.updateUserInfo(userInfo));
    }

    /**
     * 删除用户信息
     */
    @PreAuthorize("@ss.hasPermi('user:userInfo:remove')")
    @Log(title = "用户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable String[] userIds) {
        return toAjax(userInfoService.deleteUserInfoByUserIds(userIds));
    }
}
