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
import com.lz.ai.model.domain.ModelParamsInfo;
import com.lz.ai.model.vo.modelParamsInfo.ModelParamsInfoVo;
import com.lz.ai.model.dto.modelParamsInfo.ModelParamsInfoQuery;
import com.lz.ai.model.dto.modelParamsInfo.ModelParamsInfoInsert;
import com.lz.ai.model.dto.modelParamsInfo.ModelParamsInfoEdit;
import com.lz.ai.service.IModelParamsInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * AI模型参数配置Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/ai/modelParamsInfo")
public class ModelParamsInfoController extends BaseController
{
    @Resource
    private IModelParamsInfoService modelParamsInfoService;

    /**
     * 查询AI模型参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('ai:modelParamsInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ModelParamsInfoQuery modelParamsInfoQuery)
    {
        ModelParamsInfo modelParamsInfo = ModelParamsInfoQuery.queryToObj(modelParamsInfoQuery);
        startPage();
        List<ModelParamsInfo> list = modelParamsInfoService.selectModelParamsInfoList(modelParamsInfo);
        List<ModelParamsInfoVo> listVo= list.stream().map(ModelParamsInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出AI模型参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('ai:modelParamsInfo:export')")
    @Log(title = "AI模型参数配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ModelParamsInfoQuery modelParamsInfoQuery)
    {
        ModelParamsInfo modelParamsInfo = ModelParamsInfoQuery.queryToObj(modelParamsInfoQuery);
        List<ModelParamsInfo> list = modelParamsInfoService.selectModelParamsInfoList(modelParamsInfo);
        ExcelUtil<ModelParamsInfo> util = new ExcelUtil<ModelParamsInfo>(ModelParamsInfo.class);
        util.exportExcel(response, list, "AI模型参数配置数据");
    }

    /**
     * 获取AI模型参数配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('ai:modelParamsInfo:query')")
    @GetMapping(value = "/{modelId}")
    public AjaxResult getInfo(@PathVariable("modelId") String modelId)
    {
        ModelParamsInfo modelParamsInfo = modelParamsInfoService.selectModelParamsInfoByModelId(modelId);
        return success(ModelParamsInfoVo.objToVo(modelParamsInfo));
    }

    /**
     * 新增AI模型参数配置
     */
    @PreAuthorize("@ss.hasPermi('ai:modelParamsInfo:add')")
    @Log(title = "AI模型参数配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ModelParamsInfoInsert modelParamsInfoInsert)
    {
        ModelParamsInfo modelParamsInfo = ModelParamsInfoInsert.insertToObj(modelParamsInfoInsert);
        return toAjax(modelParamsInfoService.insertModelParamsInfo(modelParamsInfo));
    }

    /**
     * 修改AI模型参数配置
     */
    @PreAuthorize("@ss.hasPermi('ai:modelParamsInfo:edit')")
    @Log(title = "AI模型参数配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ModelParamsInfoEdit modelParamsInfoEdit)
    {
        ModelParamsInfo modelParamsInfo = ModelParamsInfoEdit.editToObj(modelParamsInfoEdit);
        return toAjax(modelParamsInfoService.updateModelParamsInfo(modelParamsInfo));
    }

    /**
     * 删除AI模型参数配置
     */
    @PreAuthorize("@ss.hasPermi('ai:modelParamsInfo:remove')")
    @Log(title = "AI模型参数配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{modelIds}")
    public AjaxResult remove(@PathVariable String[] modelIds)
    {
        return toAjax(modelParamsInfoService.deleteModelParamsInfoByModelIds(modelIds));
    }
}
