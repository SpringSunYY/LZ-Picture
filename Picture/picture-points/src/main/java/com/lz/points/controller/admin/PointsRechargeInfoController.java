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
import com.lz.points.model.domain.PointsRechargeInfo;
import com.lz.points.model.vo.pointsRechargeInfo.PointsRechargeInfoVo;
import com.lz.points.model.dto.pointsRechargeInfo.PointsRechargeInfoQuery;
import com.lz.points.model.dto.pointsRechargeInfo.PointsRechargeInfoInsert;
import com.lz.points.model.dto.pointsRechargeInfo.PointsRechargeInfoEdit;
import com.lz.points.service.IPointsRechargeInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 积分充值记录Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/points/pointsRechargeInfo")
public class PointsRechargeInfoController extends BaseController
{
    @Resource
    private IPointsRechargeInfoService pointsRechargeInfoService;

    /**
     * 查询积分充值记录列表
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargeInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PointsRechargeInfoQuery pointsRechargeInfoQuery)
    {
        PointsRechargeInfo pointsRechargeInfo = PointsRechargeInfoQuery.queryToObj(pointsRechargeInfoQuery);
        startPage();
        List<PointsRechargeInfo> list = pointsRechargeInfoService.selectPointsRechargeInfoList(pointsRechargeInfo);
        List<PointsRechargeInfoVo> listVo= list.stream().map(PointsRechargeInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出积分充值记录列表
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargeInfo:export')")
    @Log(title = "积分充值记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PointsRechargeInfoQuery pointsRechargeInfoQuery)
    {
        PointsRechargeInfo pointsRechargeInfo = PointsRechargeInfoQuery.queryToObj(pointsRechargeInfoQuery);
        List<PointsRechargeInfo> list = pointsRechargeInfoService.selectPointsRechargeInfoList(pointsRechargeInfo);
        ExcelUtil<PointsRechargeInfo> util = new ExcelUtil<PointsRechargeInfo>(PointsRechargeInfo.class);
        util.exportExcel(response, list, "积分充值记录数据");
    }

    /**
     * 获取积分充值记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargeInfo:query')")
    @GetMapping(value = "/{rechargeId}")
    public AjaxResult getInfo(@PathVariable("rechargeId") String rechargeId)
    {
        PointsRechargeInfo pointsRechargeInfo = pointsRechargeInfoService.selectPointsRechargeInfoByRechargeId(rechargeId);
        return success(PointsRechargeInfoVo.objToVo(pointsRechargeInfo));
    }

    /**
     * 新增积分充值记录
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargeInfo:add')")
    @Log(title = "积分充值记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PointsRechargeInfoInsert pointsRechargeInfoInsert)
    {
        PointsRechargeInfo pointsRechargeInfo = PointsRechargeInfoInsert.insertToObj(pointsRechargeInfoInsert);
        return toAjax(pointsRechargeInfoService.insertPointsRechargeInfo(pointsRechargeInfo));
    }

    /**
     * 修改积分充值记录
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargeInfo:edit')")
    @Log(title = "积分充值记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PointsRechargeInfoEdit pointsRechargeInfoEdit)
    {
        PointsRechargeInfo pointsRechargeInfo = PointsRechargeInfoEdit.editToObj(pointsRechargeInfoEdit);
        return toAjax(pointsRechargeInfoService.updatePointsRechargeInfo(pointsRechargeInfo));
    }

    /**
     * 删除积分充值记录
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargeInfo:remove')")
    @Log(title = "积分充值记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{rechargeIds}")
    public AjaxResult remove(@PathVariable String[] rechargeIds)
    {
        return toAjax(pointsRechargeInfoService.deletePointsRechargeInfoByRechargeIds(rechargeIds));
    }
}
