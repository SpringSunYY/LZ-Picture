package com.lz.picture.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.config.OssConfig;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.SpaceDilatationInfo;
import com.lz.picture.model.dto.spaceDilatationInfo.SpaceDilatationInfoEdit;
import com.lz.picture.model.dto.spaceDilatationInfo.SpaceDilatationInfoInsert;
import com.lz.picture.model.dto.spaceDilatationInfo.SpaceDilatationInfoQuery;
import com.lz.picture.model.vo.spaceDilatationInfo.SpaceDilatationInfoVo;
import com.lz.picture.service.ISpaceDilatationInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.lz.config.utils.ConfigInfoUtils.PICTURE_COVER_P_VALUE;

/**
 * 空间扩容信息Controller
 *
 * @author YY
 * @date 2025-06-28
 */
@RestController
@RequestMapping("/admin/picture/spaceDilatationInfo")
public class SpaceDilatationInfoController extends BaseController {
    @Resource
    private ISpaceDilatationInfoService spaceDilatationInfoService;

    /**
     * 查询空间扩容信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceDilatationInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpaceDilatationInfoQuery spaceDilatationInfoQuery) {
        SpaceDilatationInfo spaceDilatationInfo = SpaceDilatationInfoQuery.queryToObj(spaceDilatationInfoQuery);
        startPage();
        List<SpaceDilatationInfo> list = spaceDilatationInfoService.selectSpaceDilatationInfoList(spaceDilatationInfo);
        List<SpaceDilatationInfoVo> listVo = new ArrayList<>();
        list.forEach(vo -> {
            vo.setThumbnailUrl(OssConfig.builderPictureUrl(vo.getThumbnailUrl(), PICTURE_COVER_P_VALUE));
            SpaceDilatationInfoVo obj = SpaceDilatationInfoVo.objToVo(vo);
            listVo.add(obj);
        });
        TableDataInfo table = getDataTable(listVo);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出空间扩容信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceDilatationInfo:export')")
    @Log(title = "空间扩容信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpaceDilatationInfoQuery spaceDilatationInfoQuery) {
        SpaceDilatationInfo spaceDilatationInfo = SpaceDilatationInfoQuery.queryToObj(spaceDilatationInfoQuery);
        List<SpaceDilatationInfo> list = spaceDilatationInfoService.selectSpaceDilatationInfoList(spaceDilatationInfo);
        ExcelUtil<SpaceDilatationInfo> util = new ExcelUtil<SpaceDilatationInfo>(SpaceDilatationInfo.class);
        util.exportExcel(response, list, "空间扩容信息数据");
    }

    /**
     * 获取空间扩容信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceDilatationInfo:query')")
    @GetMapping(value = "/{dilatationId}")
    public AjaxResult getInfo(@PathVariable("dilatationId") String dilatationId) {
        SpaceDilatationInfo spaceDilatationInfo = spaceDilatationInfoService.selectSpaceDilatationInfoByDilatationId(dilatationId);
        return success(SpaceDilatationInfoVo.objToVo(spaceDilatationInfo));
    }

    /**
     * 新增空间扩容信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceDilatationInfo:add')")
    @Log(title = "空间扩容信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpaceDilatationInfoInsert spaceDilatationInfoInsert) {
        SpaceDilatationInfo spaceDilatationInfo = SpaceDilatationInfoInsert.insertToObj(spaceDilatationInfoInsert);
        return toAjax(spaceDilatationInfoService.insertSpaceDilatationInfo(spaceDilatationInfo));
    }

    /**
     * 修改空间扩容信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceDilatationInfo:edit')")
    @Log(title = "空间扩容信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpaceDilatationInfoEdit spaceDilatationInfoEdit) {
        SpaceDilatationInfo spaceDilatationInfo = SpaceDilatationInfoEdit.editToObj(spaceDilatationInfoEdit);
        return toAjax(spaceDilatationInfoService.updateSpaceDilatationInfo(spaceDilatationInfo));
    }

    /**
     * 删除空间扩容信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceDilatationInfo:remove')")
    @Log(title = "空间扩容信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dilatationIds}")
    public AjaxResult remove(@PathVariable String[] dilatationIds) {
        return toAjax(spaceDilatationInfoService.deleteSpaceDilatationInfoByDilatationIds(dilatationIds));
    }
}
