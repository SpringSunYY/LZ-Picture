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
import com.lz.user.model.domain.LoginLogInfo;
import com.lz.user.model.vo.loginLogInfo.LoginLogInfoVo;
import com.lz.user.model.dto.loginLogInfo.LoginLogInfoQuery;
import com.lz.user.model.dto.loginLogInfo.LoginLogInfoInsert;
import com.lz.user.model.dto.loginLogInfo.LoginLogInfoEdit;
import com.lz.user.service.ILoginLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户登录日志Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/user/loginLogInfo")
public class LoginLogInfoController extends BaseController
{
    @Resource
    private ILoginLogInfoService loginLogInfoService;

    /**
     * 查询用户登录日志列表
     */
    @PreAuthorize("@ss.hasPermi('user:loginLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoginLogInfoQuery loginLogInfoQuery)
    {
        LoginLogInfo loginLogInfo = LoginLogInfoQuery.queryToObj(loginLogInfoQuery);
        startPage();
        List<LoginLogInfo> list = loginLogInfoService.selectLoginLogInfoList(loginLogInfo);
        List<LoginLogInfoVo> listVo= list.stream().map(LoginLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户登录日志列表
     */
    @PreAuthorize("@ss.hasPermi('user:loginLogInfo:export')")
    @Log(title = "用户登录日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LoginLogInfoQuery loginLogInfoQuery)
    {
        LoginLogInfo loginLogInfo = LoginLogInfoQuery.queryToObj(loginLogInfoQuery);
        List<LoginLogInfo> list = loginLogInfoService.selectLoginLogInfoList(loginLogInfo);
        ExcelUtil<LoginLogInfo> util = new ExcelUtil<LoginLogInfo>(LoginLogInfo.class);
        util.exportExcel(response, list, "用户登录日志数据");
    }

    /**
     * 获取用户登录日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('user:loginLogInfo:query')")
    @GetMapping(value = "/{infoId}")
    public AjaxResult getInfo(@PathVariable("infoId") String infoId)
    {
        LoginLogInfo loginLogInfo = loginLogInfoService.selectLoginLogInfoByInfoId(infoId);
        return success(LoginLogInfoVo.objToVo(loginLogInfo));
    }

    /**
     * 新增用户登录日志
     */
    @PreAuthorize("@ss.hasPermi('user:loginLogInfo:add')")
    @Log(title = "用户登录日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LoginLogInfoInsert loginLogInfoInsert)
    {
        LoginLogInfo loginLogInfo = LoginLogInfoInsert.insertToObj(loginLogInfoInsert);
        return toAjax(loginLogInfoService.insertLoginLogInfo(loginLogInfo));
    }

    /**
     * 修改用户登录日志
     */
    @PreAuthorize("@ss.hasPermi('user:loginLogInfo:edit')")
    @Log(title = "用户登录日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LoginLogInfoEdit loginLogInfoEdit)
    {
        LoginLogInfo loginLogInfo = LoginLogInfoEdit.editToObj(loginLogInfoEdit);
        return toAjax(loginLogInfoService.updateLoginLogInfo(loginLogInfo));
    }

    /**
     * 删除用户登录日志
     */
    @PreAuthorize("@ss.hasPermi('user:loginLogInfo:remove')")
    @Log(title = "用户登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable String[] infoIds)
    {
        return toAjax(loginLogInfoService.deleteLoginLogInfoByInfoIds(infoIds));
    }
}
