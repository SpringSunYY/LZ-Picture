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
import com.lz.picture.model.domain.PictureCategoryInfo;
import com.lz.picture.model.vo.pictureCategoryInfo.PictureCategoryInfoVo;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoQuery;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoInsert;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoEdit;
import com.lz.picture.service.IPictureCategoryInfoService;
import com.lz.common.utils.poi.ExcelUtil;

/**
 * 图片分类信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureCategoryInfo")
public class PictureCategoryInfoController extends BaseController
{
    @Resource
    private IPictureCategoryInfoService pictureCategoryInfoService;

    /**
     * 查询图片分类信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCategoryInfo:list')")
    @GetMapping("/list")
        public AjaxResult list(PictureCategoryInfoQuery pictureCategoryInfoQuery)
        {
            PictureCategoryInfo pictureCategoryInfo = PictureCategoryInfoQuery.queryToObj(pictureCategoryInfoQuery);
            List<PictureCategoryInfo> list = pictureCategoryInfoService.selectPictureCategoryInfoList(pictureCategoryInfo);
            List<PictureCategoryInfoVo> listVo= list.stream().map(PictureCategoryInfoVo::objToVo).collect(Collectors.toList());
            return success(listVo);
        }

    /**
     * 导出图片分类信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCategoryInfo:export')")
    @Log(title = "图片分类信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureCategoryInfoQuery pictureCategoryInfoQuery)
    {
        PictureCategoryInfo pictureCategoryInfo = PictureCategoryInfoQuery.queryToObj(pictureCategoryInfoQuery);
        List<PictureCategoryInfo> list = pictureCategoryInfoService.selectPictureCategoryInfoList(pictureCategoryInfo);
        ExcelUtil<PictureCategoryInfo> util = new ExcelUtil<PictureCategoryInfo>(PictureCategoryInfo.class);
        util.exportExcel(response, list, "图片分类信息数据");
    }

    /**
     * 获取图片分类信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCategoryInfo:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") String categoryId)
    {
        PictureCategoryInfo pictureCategoryInfo = pictureCategoryInfoService.selectPictureCategoryInfoByCategoryId(categoryId);
        return success(PictureCategoryInfoVo.objToVo(pictureCategoryInfo));
    }

    /**
     * 新增图片分类信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCategoryInfo:add')")
    @Log(title = "图片分类信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureCategoryInfoInsert pictureCategoryInfoInsert)
    {
        PictureCategoryInfo pictureCategoryInfo = PictureCategoryInfoInsert.insertToObj(pictureCategoryInfoInsert);
        return toAjax(pictureCategoryInfoService.insertPictureCategoryInfo(pictureCategoryInfo));
    }

    /**
     * 修改图片分类信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCategoryInfo:edit')")
    @Log(title = "图片分类信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureCategoryInfoEdit pictureCategoryInfoEdit)
    {
        PictureCategoryInfo pictureCategoryInfo = PictureCategoryInfoEdit.editToObj(pictureCategoryInfoEdit);
        return toAjax(pictureCategoryInfoService.updatePictureCategoryInfo(pictureCategoryInfo));
    }

    /**
     * 删除图片分类信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCategoryInfo:remove')")
    @Log(title = "图片分类信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable String[] categoryIds)
    {
        return toAjax(pictureCategoryInfoService.deletePictureCategoryInfoByCategoryIds(categoryIds));
    }
}
