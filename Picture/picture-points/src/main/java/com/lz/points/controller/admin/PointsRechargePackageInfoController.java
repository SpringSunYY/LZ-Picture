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
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.lz.points.model.vo.pointsRechargePackageInfo.PointsRechargePackageInfoVo;
import com.lz.points.model.dto.pointsRechargePackageInfo.PointsRechargePackageInfoQuery;
import com.lz.points.model.dto.pointsRechargePackageInfo.PointsRechargePackageInfoInsert;
import com.lz.points.model.dto.pointsRechargePackageInfo.PointsRechargePackageInfoEdit;
import com.lz.points.service.IPointsRechargePackageInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 充值积分套餐Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/points/pointsRechargePackageInfo")
public class PointsRechargePackageInfoController extends BaseController
{
    @Resource
    private IPointsRechargePackageInfoService pointsRechargePackageInfoService;

    /**
     * 查询充值积分套餐列表
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargePackageInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PointsRechargePackageInfoQuery pointsRechargePackageInfoQuery)
    {
        PointsRechargePackageInfo pointsRechargePackageInfo = PointsRechargePackageInfoQuery.queryToObj(pointsRechargePackageInfoQuery);
        startPage();
        List<PointsRechargePackageInfo> list = pointsRechargePackageInfoService.selectPointsRechargePackageInfoList(pointsRechargePackageInfo);
        List<PointsRechargePackageInfoVo> listVo= list.stream().map(PointsRechargePackageInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出充值积分套餐列表
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargePackageInfo:export')")
    @Log(title = "充值积分套餐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PointsRechargePackageInfoQuery pointsRechargePackageInfoQuery)
    {
        PointsRechargePackageInfo pointsRechargePackageInfo = PointsRechargePackageInfoQuery.queryToObj(pointsRechargePackageInfoQuery);
        List<PointsRechargePackageInfo> list = pointsRechargePackageInfoService.selectPointsRechargePackageInfoList(pointsRechargePackageInfo);
        ExcelUtil<PointsRechargePackageInfo> util = new ExcelUtil<PointsRechargePackageInfo>(PointsRechargePackageInfo.class);
        util.exportExcel(response, list, "充值积分套餐数据");
    }

    /**
     * 获取充值积分套餐详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargePackageInfo:query')")
    @GetMapping(value = "/{packageId}")
    public AjaxResult getInfo(@PathVariable("packageId") String packageId)
    {
        PointsRechargePackageInfo pointsRechargePackageInfo = pointsRechargePackageInfoService.selectPointsRechargePackageInfoByPackageId(packageId);
        return success(PointsRechargePackageInfoVo.objToVo(pointsRechargePackageInfo));
    }

    /**
     * 新增充值积分套餐
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargePackageInfo:add')")
    @Log(title = "充值积分套餐", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PointsRechargePackageInfoInsert pointsRechargePackageInfoInsert)
    {
        PointsRechargePackageInfo pointsRechargePackageInfo = PointsRechargePackageInfoInsert.insertToObj(pointsRechargePackageInfoInsert);
        return toAjax(pointsRechargePackageInfoService.insertPointsRechargePackageInfo(pointsRechargePackageInfo));
    }

    /**
     * 修改充值积分套餐
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargePackageInfo:edit')")
    @Log(title = "充值积分套餐", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PointsRechargePackageInfoEdit pointsRechargePackageInfoEdit)
    {
        PointsRechargePackageInfo pointsRechargePackageInfo = PointsRechargePackageInfoEdit.editToObj(pointsRechargePackageInfoEdit);
        return toAjax(pointsRechargePackageInfoService.updatePointsRechargePackageInfo(pointsRechargePackageInfo));
    }

    /**
     * 删除充值积分套餐
     */
    @PreAuthorize("@ss.hasPermi('points:pointsRechargePackageInfo:remove')")
    @Log(title = "充值积分套餐", businessType = BusinessType.DELETE)
    @DeleteMapping("/{packageIds}")
    public AjaxResult remove(@PathVariable String[] packageIds)
    {
        return toAjax(pointsRechargePackageInfoService.deletePointsRechargePackageInfoByPackageIds(packageIds));
    }
}
