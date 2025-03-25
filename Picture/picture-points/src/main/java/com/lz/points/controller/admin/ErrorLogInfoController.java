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
import com.lz.points.model.domain.ErrorLogInfo;
import com.lz.points.model.vo.errorLogInfo.ErrorLogInfoVo;
import com.lz.points.model.dto.errorLogInfo.ErrorLogInfoQuery;
import com.lz.points.model.dto.errorLogInfo.ErrorLogInfoInsert;
import com.lz.points.model.dto.errorLogInfo.ErrorLogInfoEdit;
import com.lz.points.service.IErrorLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 异常捕获Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/points/errorLogInfo")
public class ErrorLogInfoController extends BaseController
{
    @Resource
    private IErrorLogInfoService errorLogInfoService;

    /**
     * 查询异常捕获列表
     */
    @PreAuthorize("@ss.hasPermi('points:errorLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ErrorLogInfoQuery errorLogInfoQuery)
    {
        ErrorLogInfo errorLogInfo = ErrorLogInfoQuery.queryToObj(errorLogInfoQuery);
        startPage();
        List<ErrorLogInfo> list = errorLogInfoService.selectErrorLogInfoList(errorLogInfo);
        List<ErrorLogInfoVo> listVo= list.stream().map(ErrorLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出异常捕获列表
     */
    @PreAuthorize("@ss.hasPermi('points:errorLogInfo:export')")
    @Log(title = "异常捕获", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ErrorLogInfoQuery errorLogInfoQuery)
    {
        ErrorLogInfo errorLogInfo = ErrorLogInfoQuery.queryToObj(errorLogInfoQuery);
        List<ErrorLogInfo> list = errorLogInfoService.selectErrorLogInfoList(errorLogInfo);
        ExcelUtil<ErrorLogInfo> util = new ExcelUtil<ErrorLogInfo>(ErrorLogInfo.class);
        util.exportExcel(response, list, "异常捕获数据");
    }

    /**
     * 获取异常捕获详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:errorLogInfo:query')")
    @GetMapping(value = "/{errorId}")
    public AjaxResult getInfo(@PathVariable("errorId") String errorId)
    {
        ErrorLogInfo errorLogInfo = errorLogInfoService.selectErrorLogInfoByErrorId(errorId);
        return success(ErrorLogInfoVo.objToVo(errorLogInfo));
    }

    /**
     * 新增异常捕获
     */
    @PreAuthorize("@ss.hasPermi('points:errorLogInfo:add')")
    @Log(title = "异常捕获", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ErrorLogInfoInsert errorLogInfoInsert)
    {
        ErrorLogInfo errorLogInfo = ErrorLogInfoInsert.insertToObj(errorLogInfoInsert);
        return toAjax(errorLogInfoService.insertErrorLogInfo(errorLogInfo));
    }

    /**
     * 修改异常捕获
     */
    @PreAuthorize("@ss.hasPermi('points:errorLogInfo:edit')")
    @Log(title = "异常捕获", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ErrorLogInfoEdit errorLogInfoEdit)
    {
        ErrorLogInfo errorLogInfo = ErrorLogInfoEdit.editToObj(errorLogInfoEdit);
        return toAjax(errorLogInfoService.updateErrorLogInfo(errorLogInfo));
    }

    /**
     * 删除异常捕获
     */
    @PreAuthorize("@ss.hasPermi('points:errorLogInfo:remove')")
    @Log(title = "异常捕获", businessType = BusinessType.DELETE)
    @DeleteMapping("/{errorIds}")
    public AjaxResult remove(@PathVariable String[] errorIds)
    {
        return toAjax(errorLogInfoService.deleteErrorLogInfoByErrorIds(errorIds));
    }
}
