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
import com.lz.picture.model.domain.PictureFavoriteInfo;
import com.lz.picture.model.vo.pictureFavoriteInfo.PictureFavoriteInfoVo;
import com.lz.picture.model.dto.pictureFavoriteInfo.PictureFavoriteInfoQuery;
import com.lz.picture.model.dto.pictureFavoriteInfo.PictureFavoriteInfoInsert;
import com.lz.picture.model.dto.pictureFavoriteInfo.PictureFavoriteInfoEdit;
import com.lz.picture.service.IPictureFavoriteInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户图片收藏Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureFavoriteInfo")
public class PictureFavoriteInfoController extends BaseController
{
    @Resource
    private IPictureFavoriteInfoService pictureFavoriteInfoService;

    /**
     * 查询用户图片收藏列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureFavoriteInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureFavoriteInfoQuery pictureFavoriteInfoQuery)
    {
        PictureFavoriteInfo pictureFavoriteInfo = PictureFavoriteInfoQuery.queryToObj(pictureFavoriteInfoQuery);
        startPage();
        List<PictureFavoriteInfo> list = pictureFavoriteInfoService.selectPictureFavoriteInfoList(pictureFavoriteInfo);
        List<PictureFavoriteInfoVo> listVo= list.stream().map(PictureFavoriteInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户图片收藏列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureFavoriteInfo:export')")
    @Log(title = "用户图片收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureFavoriteInfoQuery pictureFavoriteInfoQuery)
    {
        PictureFavoriteInfo pictureFavoriteInfo = PictureFavoriteInfoQuery.queryToObj(pictureFavoriteInfoQuery);
        List<PictureFavoriteInfo> list = pictureFavoriteInfoService.selectPictureFavoriteInfoList(pictureFavoriteInfo);
        ExcelUtil<PictureFavoriteInfo> util = new ExcelUtil<PictureFavoriteInfo>(PictureFavoriteInfo.class);
        util.exportExcel(response, list, "用户图片收藏数据");
    }

    /**
     * 获取用户图片收藏详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureFavoriteInfo:query')")
    @GetMapping(value = "/{favoriteId}")
    public AjaxResult getInfo(@PathVariable("favoriteId") String favoriteId)
    {
        PictureFavoriteInfo pictureFavoriteInfo = pictureFavoriteInfoService.selectPictureFavoriteInfoByFavoriteId(favoriteId);
        return success(PictureFavoriteInfoVo.objToVo(pictureFavoriteInfo));
    }

    /**
     * 新增用户图片收藏
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureFavoriteInfo:add')")
    @Log(title = "用户图片收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureFavoriteInfoInsert pictureFavoriteInfoInsert)
    {
        PictureFavoriteInfo pictureFavoriteInfo = PictureFavoriteInfoInsert.insertToObj(pictureFavoriteInfoInsert);
        return toAjax(pictureFavoriteInfoService.insertPictureFavoriteInfo(pictureFavoriteInfo));
    }

    /**
     * 修改用户图片收藏
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureFavoriteInfo:edit')")
    @Log(title = "用户图片收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureFavoriteInfoEdit pictureFavoriteInfoEdit)
    {
        PictureFavoriteInfo pictureFavoriteInfo = PictureFavoriteInfoEdit.editToObj(pictureFavoriteInfoEdit);
        return toAjax(pictureFavoriteInfoService.updatePictureFavoriteInfo(pictureFavoriteInfo));
    }

    /**
     * 删除用户图片收藏
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureFavoriteInfo:remove')")
    @Log(title = "用户图片收藏", businessType = BusinessType.DELETE)
    @DeleteMapping("/{favoriteIds}")
    public AjaxResult remove(@PathVariable String[] favoriteIds)
    {
        return toAjax(pictureFavoriteInfoService.deletePictureFavoriteInfoByFavoriteIds(favoriteIds));
    }
}
