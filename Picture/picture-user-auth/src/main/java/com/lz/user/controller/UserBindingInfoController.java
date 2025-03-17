package com.lz.user.controller;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.enums.BusinessType;
import com.lz.user.model.domain.UserBindingInfo;
import com.lz.user.model.vo.userBindingInfo.UserBindingInfoVo;
import com.lz.user.model.dto.userBindingInfo.UserBindingInfoQuery;
import com.lz.user.model.dto.userBindingInfo.UserBindingInfoInsert;
import com.lz.user.model.dto.userBindingInfo.UserBindingInfoEdit;
import com.lz.user.service.IUserBindingInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户第三方账号绑定Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/user/auth/userBindingInfo")
public class UserBindingInfoController extends BaseController
{
    @Resource
    private IUserBindingInfoService userBindingInfoService;

    /**
     * 查询用户第三方账号绑定列表
     */
    @PreAuthorize("@ss.hasPermi('user:userBindingInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBindingInfoQuery userBindingInfoQuery)
    {
        UserBindingInfo userBindingInfo = UserBindingInfoQuery.queryToObj(userBindingInfoQuery);
        startPage();
        List<UserBindingInfo> list = userBindingInfoService.selectUserBindingInfoList(userBindingInfo);
        List<UserBindingInfoVo> listVo= list.stream().map(UserBindingInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户第三方账号绑定列表
     */
    @PreAuthorize("@ss.hasPermi('user:userBindingInfo:export')")
    @Log(title = "用户第三方账号绑定", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserBindingInfoQuery userBindingInfoQuery)
    {
        UserBindingInfo userBindingInfo = UserBindingInfoQuery.queryToObj(userBindingInfoQuery);
        List<UserBindingInfo> list = userBindingInfoService.selectUserBindingInfoList(userBindingInfo);
        ExcelUtil<UserBindingInfo> util = new ExcelUtil<UserBindingInfo>(UserBindingInfo.class);
        util.exportExcel(response, list, "用户第三方账号绑定数据");
    }

    /**
     * 获取用户第三方账号绑定详细信息
     */
    @PreAuthorize("@ss.hasPermi('user:userBindingInfo:query')")
    @GetMapping(value = "/{bindingId}")
    public AjaxResult getInfo(@PathVariable("bindingId") String bindingId)
    {
        UserBindingInfo userBindingInfo = userBindingInfoService.selectUserBindingInfoByBindingId(bindingId);
        return success(UserBindingInfoVo.objToVo(userBindingInfo));
    }

    /**
     * 新增用户第三方账号绑定
     */
    @PreAuthorize("@ss.hasPermi('user:userBindingInfo:add')")
    @Log(title = "用户第三方账号绑定", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserBindingInfoInsert userBindingInfoInsert)
    {
        UserBindingInfo userBindingInfo = UserBindingInfoInsert.insertToObj(userBindingInfoInsert);
        return toAjax(userBindingInfoService.insertUserBindingInfo(userBindingInfo));
    }

    /**
     * 修改用户第三方账号绑定
     */
    @PreAuthorize("@ss.hasPermi('user:userBindingInfo:edit')")
    @Log(title = "用户第三方账号绑定", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserBindingInfoEdit userBindingInfoEdit)
    {
        UserBindingInfo userBindingInfo = UserBindingInfoEdit.editToObj(userBindingInfoEdit);
        return toAjax(userBindingInfoService.updateUserBindingInfo(userBindingInfo));
    }

    /**
     * 删除用户第三方账号绑定
     */
    @PreAuthorize("@ss.hasPermi('user:userBindingInfo:remove')")
    @Log(title = "用户第三方账号绑定", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bindingIds}")
    public AjaxResult remove(@PathVariable String[] bindingIds)
    {
        return toAjax(userBindingInfoService.deleteUserBindingInfoByBindingIds(bindingIds));
    }
}
