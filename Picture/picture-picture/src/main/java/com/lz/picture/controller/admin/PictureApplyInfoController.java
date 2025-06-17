package com.lz.picture.controller.admin;

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
import com.lz.picture.model.domain.PictureApplyInfo;
import com.lz.picture.model.vo.pictureApplyInfo.PictureApplyInfoVo;
import com.lz.picture.model.dto.pictureApplyInfo.PictureApplyInfoQuery;
import com.lz.picture.model.dto.pictureApplyInfo.PictureApplyInfoInsert;
import com.lz.picture.model.dto.pictureApplyInfo.PictureApplyInfoEdit;
import com.lz.picture.service.IPictureApplyInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 图片申请信息Controller
 *
 * @author YY
 * @date 2025-06-17
 */
@RestController
@RequestMapping("/admin/picture/pictureApplyInfo")
public class PictureApplyInfoController extends BaseController
{
    @Resource
    private IPictureApplyInfoService pictureApplyInfoService;

    /**
     * 查询图片申请信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureApplyInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureApplyInfoQuery pictureApplyInfoQuery)
    {
        PictureApplyInfo pictureApplyInfo = PictureApplyInfoQuery.queryToObj(pictureApplyInfoQuery);
        startPage();
        List<PictureApplyInfo> list = pictureApplyInfoService.selectPictureApplyInfoList(pictureApplyInfo);
        List<PictureApplyInfoVo> listVo= list.stream().map(PictureApplyInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出图片申请信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureApplyInfo:export')")
    @Log(title = "图片申请信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureApplyInfoQuery pictureApplyInfoQuery)
    {
        PictureApplyInfo pictureApplyInfo = PictureApplyInfoQuery.queryToObj(pictureApplyInfoQuery);
        List<PictureApplyInfo> list = pictureApplyInfoService.selectPictureApplyInfoList(pictureApplyInfo);
        ExcelUtil<PictureApplyInfo> util = new ExcelUtil<PictureApplyInfo>(PictureApplyInfo.class);
        util.exportExcel(response, list, "图片申请信息数据");
    }

    /**
     * 获取图片申请信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureApplyInfo:query')")
    @GetMapping(value = "/{applyId}")
    public AjaxResult getInfo(@PathVariable("applyId") String applyId)
    {
        PictureApplyInfo pictureApplyInfo = pictureApplyInfoService.selectPictureApplyInfoByApplyId(applyId);
        return success(PictureApplyInfoVo.objToVo(pictureApplyInfo));
    }

    /**
     * 新增图片申请信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureApplyInfo:add')")
    @Log(title = "图片申请信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureApplyInfoInsert pictureApplyInfoInsert)
    {
        PictureApplyInfo pictureApplyInfo = PictureApplyInfoInsert.insertToObj(pictureApplyInfoInsert);
        return toAjax(pictureApplyInfoService.insertPictureApplyInfo(pictureApplyInfo));
    }

    /**
     * 修改图片申请信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureApplyInfo:edit')")
    @Log(title = "图片申请信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureApplyInfoEdit pictureApplyInfoEdit)
    {
        PictureApplyInfo pictureApplyInfo = PictureApplyInfoEdit.editToObj(pictureApplyInfoEdit);
        return toAjax(pictureApplyInfoService.updatePictureApplyInfo(pictureApplyInfo));
    }

    /**
     * 删除图片申请信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureApplyInfo:remove')")
    @Log(title = "图片申请信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applyIds}")
    public AjaxResult remove(@PathVariable String[] applyIds)
    {
        return toAjax(pictureApplyInfoService.deletePictureApplyInfoByApplyIds(applyIds));
    }
}
