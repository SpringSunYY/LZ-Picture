package com.lz.picture.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.StatisticsInfo;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoEdit;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoInsert;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoQuery;
import com.lz.picture.model.dto.statisticsInfo.StatisticsInfoRequest;
import com.lz.picture.model.vo.statisticsInfo.StatisticsInfoVo;
import com.lz.picture.service.IStatisticsInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计信息Controller
 *
 * @author YY
 * @date 2025-07-17
 */
@RestController
@RequestMapping("/admin/picture/statisticsInfo")
public class StatisticsInfoController extends BaseController {
    @Resource
    private IStatisticsInfoService statisticsInfoService;

    /**
     * 查询统计信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(StatisticsInfoQuery statisticsInfoQuery) {
        StatisticsInfo statisticsInfo = StatisticsInfoQuery.queryToObj(statisticsInfoQuery);
        startPage();
        List<StatisticsInfo> list = statisticsInfoService.selectStatisticsInfoList(statisticsInfo);
        List<StatisticsInfoVo> listVo = list.stream().map(StatisticsInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出统计信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:export')")
    @Log(title = "统计信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StatisticsInfoQuery statisticsInfoQuery) {
        StatisticsInfo statisticsInfo = StatisticsInfoQuery.queryToObj(statisticsInfoQuery);
        List<StatisticsInfo> list = statisticsInfoService.selectStatisticsInfoList(statisticsInfo);
        ExcelUtil<StatisticsInfo> util = new ExcelUtil<StatisticsInfo>(StatisticsInfo.class);
        util.exportExcel(response, list, "统计信息数据");
    }

    /**
     * 获取统计信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:query')")
    @GetMapping(value = "/{statisticsId}")
    public AjaxResult getInfo(@PathVariable("statisticsId") String statisticsId) {
        StatisticsInfo statisticsInfo = statisticsInfoService.selectStatisticsInfoByStatisticsId(statisticsId);
        return success(StatisticsInfoVo.objToVo(statisticsInfo));
    }

    /**
     * 新增统计信息
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:add')")
    @Log(title = "统计信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StatisticsInfoInsert statisticsInfoInsert) {
        StatisticsInfo statisticsInfo = StatisticsInfoInsert.insertToObj(statisticsInfoInsert);
        return toAjax(statisticsInfoService.insertStatisticsInfo(statisticsInfo));
    }

    /**
     * 修改统计信息
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:edit')")
    @Log(title = "统计信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StatisticsInfoEdit statisticsInfoEdit) {
        StatisticsInfo statisticsInfo = StatisticsInfoEdit.editToObj(statisticsInfoEdit);
        return toAjax(statisticsInfoService.updateStatisticsInfo(statisticsInfo));
    }

    /**
     * 删除统计信息
     */
    @PreAuthorize("@ss.hasPermi('picture:statisticsInfo:remove')")
    @Log(title = "统计信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{statisticsIds}")
    public AjaxResult remove(@PathVariable String[] statisticsIds) {
        return toAjax(statisticsInfoService.deleteStatisticsInfoByStatisticsIds(statisticsIds));
    }

    /**
     * 获取期数信息
     */
    @GetMapping("/stages")
    public AjaxResult stages(StatisticsInfoRequest request) {
        return success(statisticsInfoService.getStatisticsInfoStages(request));
    }
}
