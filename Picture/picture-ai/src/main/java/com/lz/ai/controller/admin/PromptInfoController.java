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
import com.lz.ai.model.domain.PromptInfo;
import com.lz.ai.model.vo.promptInfo.PromptInfoVo;
import com.lz.ai.model.dto.promptInfo.PromptInfoQuery;
import com.lz.ai.model.dto.promptInfo.PromptInfoInsert;
import com.lz.ai.model.dto.promptInfo.PromptInfoEdit;
import com.lz.ai.service.IPromptInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 提示词信息Controller
 *
 * @author YY
 * @date 2025-08-08
 */
@RestController
@RequestMapping("/admin/ai/promptInfo")
public class PromptInfoController extends BaseController
{
    @Resource
    private IPromptInfoService promptInfoService;

    /**
     * 查询提示词信息列表
     */
    @PreAuthorize("@ss.hasPermi('ai:promptInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PromptInfoQuery promptInfoQuery)
    {
        PromptInfo promptInfo = PromptInfoQuery.queryToObj(promptInfoQuery);
        startPage();
        List<PromptInfo> list = promptInfoService.selectPromptInfoList(promptInfo);
        List<PromptInfoVo> listVo= list.stream().map(PromptInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出提示词信息列表
     */
    @PreAuthorize("@ss.hasPermi('ai:promptInfo:export')")
    @Log(title = "提示词信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PromptInfoQuery promptInfoQuery)
    {
        PromptInfo promptInfo = PromptInfoQuery.queryToObj(promptInfoQuery);
        List<PromptInfo> list = promptInfoService.selectPromptInfoList(promptInfo);
        ExcelUtil<PromptInfo> util = new ExcelUtil<PromptInfo>(PromptInfo.class);
        util.exportExcel(response, list, "提示词信息数据");
    }

    /**
     * 获取提示词信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:promptInfo:query')")
    @GetMapping(value = "/{infoId}")
    public AjaxResult getInfo(@PathVariable("infoId") String infoId)
    {
        PromptInfo promptInfo = promptInfoService.selectPromptInfoByInfoId(infoId);
        return success(PromptInfoVo.objToVo(promptInfo));
    }

    /**
     * 新增提示词信息
     */
    @PreAuthorize("@ss.hasPermi('ai:promptInfo:add')")
    @Log(title = "提示词信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PromptInfoInsert promptInfoInsert)
    {
        PromptInfo promptInfo = PromptInfoInsert.insertToObj(promptInfoInsert);
        return toAjax(promptInfoService.insertPromptInfo(promptInfo));
    }

    /**
     * 修改提示词信息
     */
    @PreAuthorize("@ss.hasPermi('ai:promptInfo:edit')")
    @Log(title = "提示词信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PromptInfoEdit promptInfoEdit)
    {
        PromptInfo promptInfo = PromptInfoEdit.editToObj(promptInfoEdit);
        return toAjax(promptInfoService.updatePromptInfo(promptInfo));
    }

    /**
     * 删除提示词信息
     */
    @PreAuthorize("@ss.hasPermi('ai:promptInfo:remove')")
    @Log(title = "提示词信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable String[] infoIds)
    {
        return toAjax(promptInfoService.deletePromptInfoByInfoIds(infoIds));
    }
}
