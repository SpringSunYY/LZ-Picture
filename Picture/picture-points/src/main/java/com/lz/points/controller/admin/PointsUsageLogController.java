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
import com.lz.points.model.domain.PointsUsageLog;
import com.lz.points.model.vo.pointsUsageLog.PointsUsageLogVo;
import com.lz.points.model.dto.pointsUsageLog.PointsUsageLogQuery;
import com.lz.points.model.dto.pointsUsageLog.PointsUsageLogInsert;
import com.lz.points.model.dto.pointsUsageLog.PointsUsageLogEdit;
import com.lz.points.service.IPointsUsageLogService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 积分使用记录Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/points/pointsUsageLog")
public class PointsUsageLogController extends BaseController
{
    @Resource
    private IPointsUsageLogService pointsUsageLogService;

    /**
     * 查询积分使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(PointsUsageLogQuery pointsUsageLogQuery)
    {
        PointsUsageLog pointsUsageLog = PointsUsageLogQuery.queryToObj(pointsUsageLogQuery);
        startPage();
        List<PointsUsageLog> list = pointsUsageLogService.selectPointsUsageLogList(pointsUsageLog);
        List<PointsUsageLogVo> listVo= list.stream().map(PointsUsageLogVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出积分使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLog:export')")
    @Log(title = "积分使用记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PointsUsageLogQuery pointsUsageLogQuery)
    {
        PointsUsageLog pointsUsageLog = PointsUsageLogQuery.queryToObj(pointsUsageLogQuery);
        List<PointsUsageLog> list = pointsUsageLogService.selectPointsUsageLogList(pointsUsageLog);
        ExcelUtil<PointsUsageLog> util = new ExcelUtil<PointsUsageLog>(PointsUsageLog.class);
        util.exportExcel(response, list, "积分使用记录数据");
    }

    /**
     * 获取积分使用记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLog:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") String logId)
    {
        PointsUsageLog pointsUsageLog = pointsUsageLogService.selectPointsUsageLogByLogId(logId);
        return success(PointsUsageLogVo.objToVo(pointsUsageLog));
    }

    /**
     * 新增积分使用记录
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLog:add')")
    @Log(title = "积分使用记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PointsUsageLogInsert pointsUsageLogInsert)
    {
        PointsUsageLog pointsUsageLog = PointsUsageLogInsert.insertToObj(pointsUsageLogInsert);
        return toAjax(pointsUsageLogService.insertPointsUsageLog(pointsUsageLog));
    }

    /**
     * 修改积分使用记录
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLog:edit')")
    @Log(title = "积分使用记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PointsUsageLogEdit pointsUsageLogEdit)
    {
        PointsUsageLog pointsUsageLog = PointsUsageLogEdit.editToObj(pointsUsageLogEdit);
        return toAjax(pointsUsageLogService.updatePointsUsageLog(pointsUsageLog));
    }

    /**
     * 删除积分使用记录
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLog:remove')")
    @Log(title = "积分使用记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable String[] logIds)
    {
        return toAjax(pointsUsageLogService.deletePointsUsageLogByLogIds(logIds));
    }
}
