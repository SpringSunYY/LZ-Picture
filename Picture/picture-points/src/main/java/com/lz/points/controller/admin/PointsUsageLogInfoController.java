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
import com.lz.points.model.domain.PointsUsageLogInfo;
import com.lz.points.model.vo.pointsUsageLogInfo.PointsUsageLogInfoVo;
import com.lz.points.model.dto.pointsUsageLogInfo.PointsUsageLogInfoQuery;
import com.lz.points.model.dto.pointsUsageLogInfo.PointsUsageLogInfoInsert;
import com.lz.points.model.dto.pointsUsageLogInfo.PointsUsageLogInfoEdit;
import com.lz.points.service.IPointsUsageLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 积分使用记录Controller
 *
 * @author YY
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/admin/points/pointsUsageLogInfo")
public class PointsUsageLogInfoController extends BaseController
{
    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;

    /**
     * 查询积分使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PointsUsageLogInfoQuery pointsUsageLogInfoQuery)
    {
        PointsUsageLogInfo pointsUsageLogInfo = PointsUsageLogInfoQuery.queryToObj(pointsUsageLogInfoQuery);
        startPage();
        List<PointsUsageLogInfo> list = pointsUsageLogInfoService.selectPointsUsageLogInfoList(pointsUsageLogInfo);
        List<PointsUsageLogInfoVo> listVo= list.stream().map(PointsUsageLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出积分使用记录列表
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLogInfo:export')")
    @Log(title = "积分使用记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PointsUsageLogInfoQuery pointsUsageLogInfoQuery)
    {
        PointsUsageLogInfo pointsUsageLogInfo = PointsUsageLogInfoQuery.queryToObj(pointsUsageLogInfoQuery);
        List<PointsUsageLogInfo> list = pointsUsageLogInfoService.selectPointsUsageLogInfoList(pointsUsageLogInfo);
        ExcelUtil<PointsUsageLogInfo> util = new ExcelUtil<PointsUsageLogInfo>(PointsUsageLogInfo.class);
        util.exportExcel(response, list, "积分使用记录数据");
    }

    /**
     * 获取积分使用记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLogInfo:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") String logId)
    {
        PointsUsageLogInfo pointsUsageLogInfo = pointsUsageLogInfoService.selectPointsUsageLogInfoByLogId(logId);
        return success(PointsUsageLogInfoVo.objToVo(pointsUsageLogInfo));
    }

    /**
     * 新增积分使用记录
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLogInfo:add')")
    @Log(title = "积分使用记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PointsUsageLogInfoInsert pointsUsageLogInfoInsert)
    {
        PointsUsageLogInfo pointsUsageLogInfo = PointsUsageLogInfoInsert.insertToObj(pointsUsageLogInfoInsert);
        return toAjax(pointsUsageLogInfoService.insertPointsUsageLogInfo(pointsUsageLogInfo));
    }

    /**
     * 修改积分使用记录
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLogInfo:edit')")
    @Log(title = "积分使用记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PointsUsageLogInfoEdit pointsUsageLogInfoEdit)
    {
        PointsUsageLogInfo pointsUsageLogInfo = PointsUsageLogInfoEdit.editToObj(pointsUsageLogInfoEdit);
        return toAjax(pointsUsageLogInfoService.updatePointsUsageLogInfo(pointsUsageLogInfo));
    }

    /**
     * 删除积分使用记录
     */
    @PreAuthorize("@ss.hasPermi('points:pointsUsageLogInfo:remove')")
    @Log(title = "积分使用记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable String[] logIds)
    {
        return toAjax(pointsUsageLogInfoService.deletePointsUsageLogInfoByLogIds(logIds));
    }
}
