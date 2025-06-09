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
import com.lz.picture.model.domain.PictureRecommendInfo;
import com.lz.picture.model.vo.pictureRecommendInfo.PictureRecommendInfoVo;
import com.lz.picture.model.dto.pictureRecommendInfo.PictureRecommendInfoQuery;
import com.lz.picture.model.dto.pictureRecommendInfo.PictureRecommendInfoInsert;
import com.lz.picture.model.dto.pictureRecommendInfo.PictureRecommendInfoEdit;
import com.lz.picture.service.IPictureRecommendInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户图片推荐模型Controller
 *
 * @author YY
 * @date 2025-06-10
 */
@RestController
@RequestMapping("/admin/picture/pictureRecommendInfo")
public class PictureRecommendInfoController extends BaseController
{
    @Resource
    private IPictureRecommendInfoService pictureRecommendInfoService;

    /**
     * 查询用户图片推荐模型列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureRecommendInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureRecommendInfoQuery pictureRecommendInfoQuery)
    {
        PictureRecommendInfo pictureRecommendInfo = PictureRecommendInfoQuery.queryToObj(pictureRecommendInfoQuery);
        startPage();
        List<PictureRecommendInfo> list = pictureRecommendInfoService.selectPictureRecommendInfoList(pictureRecommendInfo);
        List<PictureRecommendInfoVo> listVo= list.stream().map(PictureRecommendInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户图片推荐模型列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureRecommendInfo:export')")
    @Log(title = "用户图片推荐模型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureRecommendInfoQuery pictureRecommendInfoQuery)
    {
        PictureRecommendInfo pictureRecommendInfo = PictureRecommendInfoQuery.queryToObj(pictureRecommendInfoQuery);
        List<PictureRecommendInfo> list = pictureRecommendInfoService.selectPictureRecommendInfoList(pictureRecommendInfo);
        ExcelUtil<PictureRecommendInfo> util = new ExcelUtil<PictureRecommendInfo>(PictureRecommendInfo.class);
        util.exportExcel(response, list, "用户图片推荐模型数据");
    }

    /**
     * 获取用户图片推荐模型详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureRecommendInfo:query')")
    @GetMapping(value = "/{recommendId}")
    public AjaxResult getInfo(@PathVariable("recommendId") String recommendId)
    {
        PictureRecommendInfo pictureRecommendInfo = pictureRecommendInfoService.selectPictureRecommendInfoByRecommendId(recommendId);
        return success(PictureRecommendInfoVo.objToVo(pictureRecommendInfo));
    }

    /**
     * 新增用户图片推荐模型
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureRecommendInfo:add')")
    @Log(title = "用户图片推荐模型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureRecommendInfoInsert pictureRecommendInfoInsert)
    {
        PictureRecommendInfo pictureRecommendInfo = PictureRecommendInfoInsert.insertToObj(pictureRecommendInfoInsert);
        return toAjax(pictureRecommendInfoService.insertPictureRecommendInfo(pictureRecommendInfo));
    }

    /**
     * 修改用户图片推荐模型
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureRecommendInfo:edit')")
    @Log(title = "用户图片推荐模型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureRecommendInfoEdit pictureRecommendInfoEdit)
    {
        PictureRecommendInfo pictureRecommendInfo = PictureRecommendInfoEdit.editToObj(pictureRecommendInfoEdit);
        return toAjax(pictureRecommendInfoService.updatePictureRecommendInfo(pictureRecommendInfo));
    }

    /**
     * 删除用户图片推荐模型
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureRecommendInfo:remove')")
    @Log(title = "用户图片推荐模型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recommendIds}")
    public AjaxResult remove(@PathVariable String[] recommendIds)
    {
        return toAjax(pictureRecommendInfoService.deletePictureRecommendInfoByRecommendIds(recommendIds));
    }
}
