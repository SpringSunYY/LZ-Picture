package com.lz.config.controller;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
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
import com.lz.config.model.domain.ConfigInfo;
import com.lz.config.model.vo.configInfo.ConfigInfoVo;
import com.lz.config.model.dto.configInfo.ConfigInfoQuery;
import com.lz.config.model.dto.configInfo.ConfigInfoInsert;
import com.lz.config.model.dto.configInfo.ConfigInfoEdit;
import com.lz.config.service.IConfigInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 配置信息Controller
 *
 * @author YY
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/config/configInfo")
public class ConfigInfoController extends BaseController
{
    @Resource
    private IConfigInfoService configInfoService;

/**
 * 查询配置信息列表
 */
@PreAuthorize("@ss.hasPermi('config:configInfo:list')")
@GetMapping("/list")
    public TableDataInfo list(ConfigInfoQuery configInfoQuery)
    {
        ConfigInfo configInfo = ConfigInfoQuery.queryToObj(configInfoQuery);
        startPage();
        List<ConfigInfo> list = configInfoService.selectConfigInfoList(configInfo);
        List<ConfigInfoVo> listVo= list.stream().map(ConfigInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出配置信息列表
     */
    @PreAuthorize("@ss.hasPermi('config:configInfo:export')")
    @Log(title = "配置信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ConfigInfoQuery configInfoQuery)
    {
        ConfigInfo configInfo = ConfigInfoQuery.queryToObj(configInfoQuery);
        List<ConfigInfo> list = configInfoService.selectConfigInfoList(configInfo);
        ExcelUtil<ConfigInfo> util = new ExcelUtil<ConfigInfo>(ConfigInfo.class);
        util.exportExcel(response, list, "配置信息数据");
    }

    /**
     * 获取配置信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:configInfo:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable("configId") Long configId)
    {
        ConfigInfo configInfo = configInfoService.selectConfigInfoByConfigId(configId);
        return success(ConfigInfoVo.objToVo(configInfo));
    }

    /**
     * 新增配置信息
     */
    @PreAuthorize("@ss.hasPermi('config:configInfo:add')")
    @Log(title = "配置信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Validated ConfigInfoInsert configInfoInsert)
    {
        ConfigInfo configInfo = ConfigInfoInsert.insertToObj(configInfoInsert);
        return toAjax(configInfoService.insertConfigInfo(configInfo));
    }

    /**
     * 修改配置信息
     */
    @PreAuthorize("@ss.hasPermi('config:configInfo:edit')")
    @Log(title = "配置信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConfigInfoEdit configInfoEdit)
    {
        ConfigInfo configInfo = ConfigInfoEdit.editToObj(configInfoEdit);
        return toAjax(configInfoService.updateConfigInfo(configInfo));
    }

    /**
     * 删除配置信息
     */
    @PreAuthorize("@ss.hasPermi('config:configInfo:remove')")
    @Log(title = "配置信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        return toAjax(configInfoService.deleteConfigInfoByConfigIds(configIds));
    }
}
