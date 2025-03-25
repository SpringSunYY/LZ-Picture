package com.lz.ai.controller.admin;

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
import com.lz.ai.model.domain.UserUsageLogInfo;
import com.lz.ai.model.vo.userUsageLogInfo.UserUsageLogInfoVo;
import com.lz.ai.model.dto.userUsageLogInfo.UserUsageLogInfoQuery;
import com.lz.ai.model.dto.userUsageLogInfo.UserUsageLogInfoInsert;
import com.lz.ai.model.dto.userUsageLogInfo.UserUsageLogInfoEdit;
import com.lz.ai.service.IUserUsageLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户AI使用记录Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/ai/userUsageLogInfo")
public class UserUsageLogInfoController extends BaseController
{
    @Resource
    private IUserUsageLogInfoService userUsageLogInfoService;

    /**
     * 查询用户AI使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:userUsageLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserUsageLogInfoQuery userUsageLogInfoQuery)
    {
        UserUsageLogInfo userUsageLogInfo = UserUsageLogInfoQuery.queryToObj(userUsageLogInfoQuery);
        startPage();
        List<UserUsageLogInfo> list = userUsageLogInfoService.selectUserUsageLogInfoList(userUsageLogInfo);
        List<UserUsageLogInfoVo> listVo= list.stream().map(UserUsageLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户AI使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:userUsageLogInfo:export')")
    @Log(title = "用户AI使用记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserUsageLogInfoQuery userUsageLogInfoQuery)
    {
        UserUsageLogInfo userUsageLogInfo = UserUsageLogInfoQuery.queryToObj(userUsageLogInfoQuery);
        List<UserUsageLogInfo> list = userUsageLogInfoService.selectUserUsageLogInfoList(userUsageLogInfo);
        ExcelUtil<UserUsageLogInfo> util = new ExcelUtil<UserUsageLogInfo>(UserUsageLogInfo.class);
        util.exportExcel(response, list, "用户AI使用记录数据");
    }

    /**
     * 获取用户AI使用记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:userUsageLogInfo:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") String logId)
    {
        UserUsageLogInfo userUsageLogInfo = userUsageLogInfoService.selectUserUsageLogInfoByLogId(logId);
        return success(UserUsageLogInfoVo.objToVo(userUsageLogInfo));
    }

    /**
     * 新增用户AI使用记录
     */
    @PreAuthorize("@ss.hasPermi('ai:userUsageLogInfo:add')")
    @Log(title = "用户AI使用记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserUsageLogInfoInsert userUsageLogInfoInsert)
    {
        UserUsageLogInfo userUsageLogInfo = UserUsageLogInfoInsert.insertToObj(userUsageLogInfoInsert);
        return toAjax(userUsageLogInfoService.insertUserUsageLogInfo(userUsageLogInfo));
    }

    /**
     * 修改用户AI使用记录
     */
    @PreAuthorize("@ss.hasPermi('ai:userUsageLogInfo:edit')")
    @Log(title = "用户AI使用记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserUsageLogInfoEdit userUsageLogInfoEdit)
    {
        UserUsageLogInfo userUsageLogInfo = UserUsageLogInfoEdit.editToObj(userUsageLogInfoEdit);
        return toAjax(userUsageLogInfoService.updateUserUsageLogInfo(userUsageLogInfo));
    }

    /**
     * 删除用户AI使用记录
     */
    @PreAuthorize("@ss.hasPermi('ai:userUsageLogInfo:remove')")
    @Log(title = "用户AI使用记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable String[] logIds)
    {
        return toAjax(userUsageLogInfoService.deleteUserUsageLogInfoByLogIds(logIds));
    }
}
