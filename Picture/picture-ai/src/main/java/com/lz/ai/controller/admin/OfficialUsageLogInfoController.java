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
import com.lz.ai.model.domain.OfficialUsageLogInfo;
import com.lz.ai.model.vo.officialUsageLogInfo.OfficialUsageLogInfoVo;
import com.lz.ai.model.dto.officialUsageLogInfo.OfficialUsageLogInfoQuery;
import com.lz.ai.model.dto.officialUsageLogInfo.OfficialUsageLogInfoInsert;
import com.lz.ai.model.dto.officialUsageLogInfo.OfficialUsageLogInfoEdit;
import com.lz.ai.service.IOfficialUsageLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 官方AI操作日志Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/ai/officialUsageLogInfo")
public class OfficialUsageLogInfoController extends BaseController
{
    @Resource
    private IOfficialUsageLogInfoService officialUsageLogInfoService;

    /**
     * 查询官方AI操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('ai:officialUsageLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(OfficialUsageLogInfoQuery officialUsageLogInfoQuery)
    {
        OfficialUsageLogInfo officialUsageLogInfo = OfficialUsageLogInfoQuery.queryToObj(officialUsageLogInfoQuery);
        startPage();
        List<OfficialUsageLogInfo> list = officialUsageLogInfoService.selectOfficialUsageLogInfoList(officialUsageLogInfo);
        List<OfficialUsageLogInfoVo> listVo= list.stream().map(OfficialUsageLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出官方AI操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('ai:officialUsageLogInfo:export')")
    @Log(title = "官方AI操作日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OfficialUsageLogInfoQuery officialUsageLogInfoQuery)
    {
        OfficialUsageLogInfo officialUsageLogInfo = OfficialUsageLogInfoQuery.queryToObj(officialUsageLogInfoQuery);
        List<OfficialUsageLogInfo> list = officialUsageLogInfoService.selectOfficialUsageLogInfoList(officialUsageLogInfo);
        ExcelUtil<OfficialUsageLogInfo> util = new ExcelUtil<OfficialUsageLogInfo>(OfficialUsageLogInfo.class);
        util.exportExcel(response, list, "官方AI操作日志数据");
    }

    /**
     * 获取官方AI操作日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:officialUsageLogInfo:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") String logId)
    {
        OfficialUsageLogInfo officialUsageLogInfo = officialUsageLogInfoService.selectOfficialUsageLogInfoByLogId(logId);
        return success(OfficialUsageLogInfoVo.objToVo(officialUsageLogInfo));
    }

    /**
     * 新增官方AI操作日志
     */
    @PreAuthorize("@ss.hasPermi('ai:officialUsageLogInfo:add')")
    @Log(title = "官方AI操作日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OfficialUsageLogInfoInsert officialUsageLogInfoInsert)
    {
        OfficialUsageLogInfo officialUsageLogInfo = OfficialUsageLogInfoInsert.insertToObj(officialUsageLogInfoInsert);
        return toAjax(officialUsageLogInfoService.insertOfficialUsageLogInfo(officialUsageLogInfo));
    }

    /**
     * 修改官方AI操作日志
     */
    @PreAuthorize("@ss.hasPermi('ai:officialUsageLogInfo:edit')")
    @Log(title = "官方AI操作日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OfficialUsageLogInfoEdit officialUsageLogInfoEdit)
    {
        OfficialUsageLogInfo officialUsageLogInfo = OfficialUsageLogInfoEdit.editToObj(officialUsageLogInfoEdit);
        return toAjax(officialUsageLogInfoService.updateOfficialUsageLogInfo(officialUsageLogInfo));
    }

    /**
     * 删除官方AI操作日志
     */
    @PreAuthorize("@ss.hasPermi('ai:officialUsageLogInfo:remove')")
    @Log(title = "官方AI操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable String[] logIds)
    {
        return toAjax(officialUsageLogInfoService.deleteOfficialUsageLogInfoByLogIds(logIds));
    }
}
