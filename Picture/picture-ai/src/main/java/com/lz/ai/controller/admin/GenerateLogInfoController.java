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
import com.lz.ai.model.domain.GenerateLogInfo;
import com.lz.ai.model.vo.generateLogInfo.GenerateLogInfoVo;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoQuery;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoInsert;
import com.lz.ai.model.dto.generateLogInfo.GenerateLogInfoEdit;
import com.lz.ai.service.IGenerateLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户生成记录Controller
 *
 * @author YY
 * @date 2025-08-08
 */
@RestController
@RequestMapping("/admin/ai/generateLogInfo")
public class GenerateLogInfoController extends BaseController
{
    @Resource
    private IGenerateLogInfoService generateLogInfoService;

    /**
     * 查询用户生成记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:generateLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(GenerateLogInfoQuery generateLogInfoQuery)
    {
        GenerateLogInfo generateLogInfo = GenerateLogInfoQuery.queryToObj(generateLogInfoQuery);
        startPage();
        List<GenerateLogInfo> list = generateLogInfoService.selectGenerateLogInfoList(generateLogInfo);
        List<GenerateLogInfoVo> listVo= list.stream().map(GenerateLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户生成记录列表
     */
    @PreAuthorize("@ss.hasPermi('ai:generateLogInfo:export')")
    @Log(title = "用户生成记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GenerateLogInfoQuery generateLogInfoQuery)
    {
        GenerateLogInfo generateLogInfo = GenerateLogInfoQuery.queryToObj(generateLogInfoQuery);
        List<GenerateLogInfo> list = generateLogInfoService.selectGenerateLogInfoList(generateLogInfo);
        ExcelUtil<GenerateLogInfo> util = new ExcelUtil<GenerateLogInfo>(GenerateLogInfo.class);
        util.exportExcel(response, list, "用户生成记录数据");
    }

    /**
     * 获取用户生成记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:generateLogInfo:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") String logId)
    {
        GenerateLogInfo generateLogInfo = generateLogInfoService.selectGenerateLogInfoByLogId(logId);
        return success(GenerateLogInfoVo.objToVo(generateLogInfo));
    }

    /**
     * 新增用户生成记录
     */
    @PreAuthorize("@ss.hasPermi('ai:generateLogInfo:add')")
    @Log(title = "用户生成记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GenerateLogInfoInsert generateLogInfoInsert)
    {
        GenerateLogInfo generateLogInfo = GenerateLogInfoInsert.insertToObj(generateLogInfoInsert);
        return toAjax(generateLogInfoService.insertGenerateLogInfo(generateLogInfo));
    }

    /**
     * 修改用户生成记录
     */
    @PreAuthorize("@ss.hasPermi('ai:generateLogInfo:edit')")
    @Log(title = "用户生成记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GenerateLogInfoEdit generateLogInfoEdit)
    {
        GenerateLogInfo generateLogInfo = GenerateLogInfoEdit.editToObj(generateLogInfoEdit);
        return toAjax(generateLogInfoService.updateGenerateLogInfo(generateLogInfo));
    }

    /**
     * 删除用户生成记录
     */
    @PreAuthorize("@ss.hasPermi('ai:generateLogInfo:remove')")
    @Log(title = "用户生成记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable String[] logIds)
    {
        return toAjax(generateLogInfoService.deleteGenerateLogInfoByLogIds(logIds));
    }
}
