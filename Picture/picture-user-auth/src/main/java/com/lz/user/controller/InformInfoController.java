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
import com.lz.user.model.domain.InformInfo;
import com.lz.user.model.vo.informInfo.InformInfoVo;
import com.lz.user.model.dto.informInfo.InformInfoQuery;
import com.lz.user.model.dto.informInfo.InformInfoInsert;
import com.lz.user.model.dto.informInfo.InformInfoEdit;
import com.lz.user.service.IInformInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户通知记录Controller
 *
 * @author YY
 * @date 2025-03-17
 */
@RestController
@RequestMapping("/user/auth/informInfo")
public class InformInfoController extends BaseController
{
    @Resource
    private IInformInfoService informInfoService;

    /**
     * 查询用户通知记录列表
     */
    @PreAuthorize("@ss.hasPermi('user:informInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(InformInfoQuery informInfoQuery)
    {
        InformInfo informInfo = InformInfoQuery.queryToObj(informInfoQuery);
        startPage();
        List<InformInfo> list = informInfoService.selectInformInfoList(informInfo);
        List<InformInfoVo> listVo= list.stream().map(InformInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户通知记录列表
     */
    @PreAuthorize("@ss.hasPermi('user:informInfo:export')")
    @Log(title = "用户通知记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, InformInfoQuery informInfoQuery)
    {
        InformInfo informInfo = InformInfoQuery.queryToObj(informInfoQuery);
        List<InformInfo> list = informInfoService.selectInformInfoList(informInfo);
        ExcelUtil<InformInfo> util = new ExcelUtil<InformInfo>(InformInfo.class);
        util.exportExcel(response, list, "用户通知记录数据");
    }

    /**
     * 获取用户通知记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('user:informInfo:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") String recordId)
    {
        InformInfo informInfo = informInfoService.selectInformInfoByRecordId(recordId);
        return success(InformInfoVo.objToVo(informInfo));
    }

    /**
     * 新增用户通知记录
     */
    @PreAuthorize("@ss.hasPermi('user:informInfo:add')")
    @Log(title = "用户通知记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InformInfoInsert informInfoInsert)
    {
        InformInfo informInfo = InformInfoInsert.insertToObj(informInfoInsert);
        return toAjax(informInfoService.insertInformInfo(informInfo));
    }

    /**
     * 修改用户通知记录
     */
    @PreAuthorize("@ss.hasPermi('user:informInfo:edit')")
    @Log(title = "用户通知记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InformInfoEdit informInfoEdit)
    {
        InformInfo informInfo = InformInfoEdit.editToObj(informInfoEdit);
        return toAjax(informInfoService.updateInformInfo(informInfo));
    }

    /**
     * 删除用户通知记录
     */
    @PreAuthorize("@ss.hasPermi('user:informInfo:remove')")
    @Log(title = "用户通知记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable String[] recordIds)
    {
        return toAjax(informInfoService.deleteInformInfoByRecordIds(recordIds));
    }
}
