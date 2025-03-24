package com.lz.picture.controller;

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
import com.lz.picture.model.domain.UserActionLogInfo;
import com.lz.picture.model.vo.userActionLogInfo.UserActionLogInfoVo;
import com.lz.picture.model.dto.userActionLogInfo.UserActionLogInfoQuery;
import com.lz.picture.model.dto.userActionLogInfo.UserActionLogInfoInsert;
import com.lz.picture.model.dto.userActionLogInfo.UserActionLogInfoEdit;
import com.lz.picture.service.IUserActionLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户行为日志Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/userActionLogInfo")
public class UserActionLogInfoController extends BaseController
{
    @Resource
    private IUserActionLogInfoService userActionLogInfoService;

    /**
     * 查询用户行为日志列表
     */
    @PreAuthorize("@ss.hasPermi('picture:userActionLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserActionLogInfoQuery userActionLogInfoQuery)
    {
        UserActionLogInfo userActionLogInfo = UserActionLogInfoQuery.queryToObj(userActionLogInfoQuery);
        startPage();
        List<UserActionLogInfo> list = userActionLogInfoService.selectUserActionLogInfoList(userActionLogInfo);
        List<UserActionLogInfoVo> listVo= list.stream().map(UserActionLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户行为日志列表
     */
    @PreAuthorize("@ss.hasPermi('picture:userActionLogInfo:export')")
    @Log(title = "用户行为日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserActionLogInfoQuery userActionLogInfoQuery)
    {
        UserActionLogInfo userActionLogInfo = UserActionLogInfoQuery.queryToObj(userActionLogInfoQuery);
        List<UserActionLogInfo> list = userActionLogInfoService.selectUserActionLogInfoList(userActionLogInfo);
        ExcelUtil<UserActionLogInfo> util = new ExcelUtil<UserActionLogInfo>(UserActionLogInfo.class);
        util.exportExcel(response, list, "用户行为日志数据");
    }

    /**
     * 获取用户行为日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:userActionLogInfo:query')")
    @GetMapping(value = "/{actionId}")
    public AjaxResult getInfo(@PathVariable("actionId") String actionId)
    {
        UserActionLogInfo userActionLogInfo = userActionLogInfoService.selectUserActionLogInfoByActionId(actionId);
        return success(UserActionLogInfoVo.objToVo(userActionLogInfo));
    }

    /**
     * 新增用户行为日志
     */
    @PreAuthorize("@ss.hasPermi('picture:userActionLogInfo:add')")
    @Log(title = "用户行为日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserActionLogInfoInsert userActionLogInfoInsert)
    {
        UserActionLogInfo userActionLogInfo = UserActionLogInfoInsert.insertToObj(userActionLogInfoInsert);
        return toAjax(userActionLogInfoService.insertUserActionLogInfo(userActionLogInfo));
    }

    /**
     * 修改用户行为日志
     */
    @PreAuthorize("@ss.hasPermi('picture:userActionLogInfo:edit')")
    @Log(title = "用户行为日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserActionLogInfoEdit userActionLogInfoEdit)
    {
        UserActionLogInfo userActionLogInfo = UserActionLogInfoEdit.editToObj(userActionLogInfoEdit);
        return toAjax(userActionLogInfoService.updateUserActionLogInfo(userActionLogInfo));
    }

    /**
     * 删除用户行为日志
     */
    @PreAuthorize("@ss.hasPermi('picture:userActionLogInfo:remove')")
    @Log(title = "用户行为日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{actionIds}")
    public AjaxResult remove(@PathVariable String[] actionIds)
    {
        return toAjax(userActionLogInfoService.deleteUserActionLogInfoByActionIds(actionIds));
    }
}
