package com.lz.points.controller.admin;

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
import com.lz.points.model.domain.RiskControlLogInfo;
import com.lz.points.model.vo.riskControlLogInfo.RiskControlLogInfoVo;
import com.lz.points.model.dto.riskControlLogInfo.RiskControlLogInfoQuery;
import com.lz.points.model.dto.riskControlLogInfo.RiskControlLogInfoInsert;
import com.lz.points.model.dto.riskControlLogInfo.RiskControlLogInfoEdit;
import com.lz.points.service.IRiskControlLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 风控日志Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/points/riskControlLogInfo")
public class RiskControlLogInfoController extends BaseController
{
    @Resource
    private IRiskControlLogInfoService riskControlLogInfoService;

    /**
     * 查询风控日志列表
     */
    @PreAuthorize("@ss.hasPermi('points:riskControlLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiskControlLogInfoQuery riskControlLogInfoQuery)
    {
        RiskControlLogInfo riskControlLogInfo = RiskControlLogInfoQuery.queryToObj(riskControlLogInfoQuery);
        startPage();
        List<RiskControlLogInfo> list = riskControlLogInfoService.selectRiskControlLogInfoList(riskControlLogInfo);
        List<RiskControlLogInfoVo> listVo= list.stream().map(RiskControlLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出风控日志列表
     */
    @PreAuthorize("@ss.hasPermi('points:riskControlLogInfo:export')")
    @Log(title = "风控日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiskControlLogInfoQuery riskControlLogInfoQuery)
    {
        RiskControlLogInfo riskControlLogInfo = RiskControlLogInfoQuery.queryToObj(riskControlLogInfoQuery);
        List<RiskControlLogInfo> list = riskControlLogInfoService.selectRiskControlLogInfoList(riskControlLogInfo);
        ExcelUtil<RiskControlLogInfo> util = new ExcelUtil<RiskControlLogInfo>(RiskControlLogInfo.class);
        util.exportExcel(response, list, "风控日志数据");
    }

    /**
     * 获取风控日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:riskControlLogInfo:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") String logId)
    {
        RiskControlLogInfo riskControlLogInfo = riskControlLogInfoService.selectRiskControlLogInfoByLogId(logId);
        return success(RiskControlLogInfoVo.objToVo(riskControlLogInfo));
    }

    /**
     * 新增风控日志
     */
    @PreAuthorize("@ss.hasPermi('points:riskControlLogInfo:add')")
    @Log(title = "风控日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RiskControlLogInfoInsert riskControlLogInfoInsert)
    {
        RiskControlLogInfo riskControlLogInfo = RiskControlLogInfoInsert.insertToObj(riskControlLogInfoInsert);
        return toAjax(riskControlLogInfoService.insertRiskControlLogInfo(riskControlLogInfo));
    }

    /**
     * 修改风控日志
     */
    @PreAuthorize("@ss.hasPermi('points:riskControlLogInfo:edit')")
    @Log(title = "风控日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RiskControlLogInfoEdit riskControlLogInfoEdit)
    {
        RiskControlLogInfo riskControlLogInfo = RiskControlLogInfoEdit.editToObj(riskControlLogInfoEdit);
        return toAjax(riskControlLogInfoService.updateRiskControlLogInfo(riskControlLogInfo));
    }

    /**
     * 删除风控日志
     */
    @PreAuthorize("@ss.hasPermi('points:riskControlLogInfo:remove')")
    @Log(title = "风控日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable String[] logIds)
    {
        return toAjax(riskControlLogInfoService.deleteRiskControlLogInfoByLogIds(logIds));
    }
}
