package com.lz.points.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.points.model.domain.PoStatisticsInfo;
import com.lz.points.model.dto.poStatisticsInfo.PoStatisticsInfoEdit;
import com.lz.points.model.dto.poStatisticsInfo.PoStatisticsInfoInsert;
import com.lz.points.model.dto.poStatisticsInfo.PoStatisticsInfoQuery;
import com.lz.points.model.dto.statistics.PaymentOrderStatisticsRequest;
import com.lz.points.model.dto.statistics.PointsUsageLogStatisticsRequest;
import com.lz.points.model.enums.PoPointsUsageLogTypeEnum;
import com.lz.points.model.enums.PoRechargeStatusEnum;
import com.lz.points.model.vo.poStatisticsInfo.PoStatisticsInfoVo;
import com.lz.points.service.IPoStatisticsInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计信息Controller
 *
 * @author YY
 * @date 2025-09-23
 */
@RestController
@RequestMapping("/admin/points/statisticsInfo")
public class PoStatisticsInfoController extends BaseController {
    @Resource
    private IPoStatisticsInfoService poStatisticsInfoService;

    /**
     * 查询统计信息列表
     */
    @PreAuthorize("@ss.hasPermi('points:statisticsInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PoStatisticsInfoQuery poStatisticsInfoQuery) {
        PoStatisticsInfo poStatisticsInfo = PoStatisticsInfoQuery.queryToObj(poStatisticsInfoQuery);
        startPage();
        List<PoStatisticsInfo> list = poStatisticsInfoService.selectPoStatisticsInfoList(poStatisticsInfo);
        List<PoStatisticsInfoVo> listVo = list.stream().map(PoStatisticsInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出统计信息列表
     */
    @PreAuthorize("@ss.hasPermi('points:statisticsInfo:export')")
    @Log(title = "统计信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PoStatisticsInfoQuery poStatisticsInfoQuery) {
        PoStatisticsInfo poStatisticsInfo = PoStatisticsInfoQuery.queryToObj(poStatisticsInfoQuery);
        List<PoStatisticsInfo> list = poStatisticsInfoService.selectPoStatisticsInfoList(poStatisticsInfo);
        ExcelUtil<PoStatisticsInfo> util = new ExcelUtil<PoStatisticsInfo>(PoStatisticsInfo.class);
        util.exportExcel(response, list, "统计信息数据");
    }

    /**
     * 获取统计信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:statisticsInfo:query')")
    @GetMapping(value = "/{statisticsId}")
    public AjaxResult getInfo(@PathVariable("statisticsId") String statisticsId) {
        PoStatisticsInfo poStatisticsInfo = poStatisticsInfoService.selectPoStatisticsInfoByStatisticsId(statisticsId);
        return success(PoStatisticsInfoVo.objToVo(poStatisticsInfo));
    }

    /**
     * 新增统计信息
     */
    @PreAuthorize("@ss.hasPermi('points:statisticsInfo:add')")
    @Log(title = "统计信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PoStatisticsInfoInsert poStatisticsInfoInsert) {
        PoStatisticsInfo poStatisticsInfo = PoStatisticsInfoInsert.insertToObj(poStatisticsInfoInsert);
        return toAjax(poStatisticsInfoService.insertPoStatisticsInfo(poStatisticsInfo));
    }

    /**
     * 修改统计信息
     */
    @PreAuthorize("@ss.hasPermi('points:statisticsInfo:edit')")
    @Log(title = "统计信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PoStatisticsInfoEdit poStatisticsInfoEdit) {
        PoStatisticsInfo poStatisticsInfo = PoStatisticsInfoEdit.editToObj(poStatisticsInfoEdit);
        return toAjax(poStatisticsInfoService.updatePoStatisticsInfo(poStatisticsInfo));
    }

    /**
     * 删除统计信息
     */
    @PreAuthorize("@ss.hasPermi('points:statisticsInfo:remove')")
    @Log(title = "统计信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{statisticsIds}")
    public AjaxResult remove(@PathVariable String[] statisticsIds) {
        return toAjax(poStatisticsInfoService.deletePoStatisticsInfoByStatisticsIds(statisticsIds));
    }

    /**
     * 积分消耗类型统计
     */
    @PreAuthorize("@ss.hasPermi('points:statistics')")
    @GetMapping("/points/usage/type")
    public AjaxResult pointsUsageTypeStatistics(@Validated PointsUsageLogStatisticsRequest request) {
        request.setLogType(PoPointsUsageLogTypeEnum.POINTS_USAGE_LOG_TYPE_1.getValue());
        return success(poStatisticsInfoService.pointsUsageTypeStatistics(request));
    }

    /**
     * 用户充值排行
     */
    @PreAuthorize("@ss.hasPermi('points:statistics')")
    @GetMapping("/points/order/rank")
    public AjaxResult pointsOrderRankStatistics(@Validated PaymentOrderStatisticsRequest request) {
        request.setPaymentStatus(PoRechargeStatusEnum.RECHARGE_STATUS_1.getValue());
        request.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        return success(poStatisticsInfoService.pointsOrderRankStatistics(request));
    }
}
