package com.lz.picture.controller;

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
import com.lz.picture.model.domain.SearchLogInfo;
import com.lz.picture.model.vo.searchLogInfo.SearchLogInfoVo;
import com.lz.picture.model.dto.searchLogInfo.SearchLogInfoQuery;
import com.lz.picture.model.dto.searchLogInfo.SearchLogInfoInsert;
import com.lz.picture.model.dto.searchLogInfo.SearchLogInfoEdit;
import com.lz.picture.service.ISearchLogInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户搜索记录Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/searchLogInfo")
public class SearchLogInfoController extends BaseController
{
    @Resource
    private ISearchLogInfoService searchLogInfoService;

    /**
     * 查询用户搜索记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:searchLogInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SearchLogInfoQuery searchLogInfoQuery)
    {
        SearchLogInfo searchLogInfo = SearchLogInfoQuery.queryToObj(searchLogInfoQuery);
        startPage();
        List<SearchLogInfo> list = searchLogInfoService.selectSearchLogInfoList(searchLogInfo);
        List<SearchLogInfoVo> listVo= list.stream().map(SearchLogInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户搜索记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:searchLogInfo:export')")
    @Log(title = "用户搜索记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SearchLogInfoQuery searchLogInfoQuery)
    {
        SearchLogInfo searchLogInfo = SearchLogInfoQuery.queryToObj(searchLogInfoQuery);
        List<SearchLogInfo> list = searchLogInfoService.selectSearchLogInfoList(searchLogInfo);
        ExcelUtil<SearchLogInfo> util = new ExcelUtil<SearchLogInfo>(SearchLogInfo.class);
        util.exportExcel(response, list, "用户搜索记录数据");
    }

    /**
     * 获取用户搜索记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:searchLogInfo:query')")
    @GetMapping(value = "/{searchId}")
    public AjaxResult getInfo(@PathVariable("searchId") String searchId)
    {
        SearchLogInfo searchLogInfo = searchLogInfoService.selectSearchLogInfoBySearchId(searchId);
        return success(SearchLogInfoVo.objToVo(searchLogInfo));
    }

    /**
     * 新增用户搜索记录
     */
    @PreAuthorize("@ss.hasPermi('picture:searchLogInfo:add')")
    @Log(title = "用户搜索记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SearchLogInfoInsert searchLogInfoInsert)
    {
        SearchLogInfo searchLogInfo = SearchLogInfoInsert.insertToObj(searchLogInfoInsert);
        return toAjax(searchLogInfoService.insertSearchLogInfo(searchLogInfo));
    }

    /**
     * 修改用户搜索记录
     */
    @PreAuthorize("@ss.hasPermi('picture:searchLogInfo:edit')")
    @Log(title = "用户搜索记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SearchLogInfoEdit searchLogInfoEdit)
    {
        SearchLogInfo searchLogInfo = SearchLogInfoEdit.editToObj(searchLogInfoEdit);
        return toAjax(searchLogInfoService.updateSearchLogInfo(searchLogInfo));
    }

    /**
     * 删除用户搜索记录
     */
    @PreAuthorize("@ss.hasPermi('picture:searchLogInfo:remove')")
    @Log(title = "用户搜索记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{searchIds}")
    public AjaxResult remove(@PathVariable String[] searchIds)
    {
        return toAjax(searchLogInfoService.deleteSearchLogInfoBySearchIds(searchIds));
    }
}
