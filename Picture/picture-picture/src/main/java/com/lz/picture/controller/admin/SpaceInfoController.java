package com.lz.picture.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.config.OssConfig;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoEdit;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoInsert;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoQuery;
import com.lz.picture.model.vo.spaceInfo.SpaceInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.system.service.ISysConfigService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.lz.common.constant.ConfigConstants.PICTURE_P;
import static com.lz.config.utils.ConfigInfoUtils.PICTURE_SPACE_AVATAR_P_VALUE;


/**
 * 空间信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/spaceInfo")
public class SpaceInfoController extends BaseController {
    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private ISysConfigService sysConfigService;

    /**
     * 查询空间信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpaceInfoQuery spaceInfoQuery) {
        SpaceInfo spaceInfo = SpaceInfoQuery.queryToObj(spaceInfoQuery);
        startPage();
        List<SpaceInfo> list = spaceInfoService.selectSpaceInfoList(spaceInfo);
        List<SpaceInfoVo> listVo = new ArrayList<>();
        String inCache = sysConfigService.selectConfigByKey(PICTURE_P);
        list.forEach(vo -> {
            vo.setSpaceAvatar(OssConfig.builderUrl(vo.getSpaceAvatar()) + "?x-oss-process=image/resize,p_" + inCache);
            SpaceInfoVo spaceInfoVo = SpaceInfoVo.objToVo(vo);
            listVo.add(spaceInfoVo);
        });
        TableDataInfo table = getDataTable(listVo);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出空间信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInfo:export')")
    @Log(title = "空间信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SpaceInfoQuery spaceInfoQuery) {
        SpaceInfo spaceInfo = SpaceInfoQuery.queryToObj(spaceInfoQuery);
        List<SpaceInfo> list = spaceInfoService.selectSpaceInfoList(spaceInfo);
        ExcelUtil<SpaceInfo> util = new ExcelUtil<SpaceInfo>(SpaceInfo.class);
        util.exportExcel(response, list, "空间信息数据");
    }

    /**
     * 获取空间信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInfo:query')")
    @GetMapping(value = "/{spaceId}")
    public AjaxResult getInfo(@PathVariable("spaceId") String spaceId) {
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(spaceId);
        return success(SpaceInfoVo.objToVo(spaceInfo));
    }

    /**
     * 新增空间信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInfo:add')")
    @Log(title = "空间信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpaceInfoInsert spaceInfoInsert) {
        SpaceInfo spaceInfo = SpaceInfoInsert.insertToObj(spaceInfoInsert);
        return toAjax(spaceInfoService.insertSpaceInfo(spaceInfo));
    }

    /**
     * 修改空间信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInfo:edit')")
    @Log(title = "空间信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpaceInfoEdit spaceInfoEdit) {
        SpaceInfo spaceInfo = SpaceInfoEdit.editToObj(spaceInfoEdit);
        return toAjax(spaceInfoService.updateSpaceInfo(spaceInfo));
    }

    /**
     * 删除空间信息
     */
    @PreAuthorize("@ss.hasPermi('picture:spaceInfo:remove')")
    @Log(title = "空间信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{spaceIds}")
    public AjaxResult remove(@PathVariable String[] spaceIds) {
        return toAjax(spaceInfoService.deleteSpaceInfoBySpaceIds(spaceIds));
    }
}
