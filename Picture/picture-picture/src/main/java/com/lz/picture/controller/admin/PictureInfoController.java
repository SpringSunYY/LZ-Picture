package com.lz.picture.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.config.OssConfig;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.PictureCategoryInfo;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoEdit;
import com.lz.picture.model.dto.pictureInfo.PictureInfoInsert;
import com.lz.picture.model.dto.pictureInfo.PictureInfoQuery;
import com.lz.picture.model.vo.pictureInfo.PictureInfoVo;
import com.lz.picture.service.IPictureCategoryInfoService;
import com.lz.picture.service.IPictureInfoService;
import com.lz.system.service.ISysConfigService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.lz.common.constant.ConfigConstants.PICTURE_P;

/**
 * 图片信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureInfo")
public class PictureInfoController extends BaseController {
    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private ISysConfigService sysConfigService;

    @Resource
    private IPictureCategoryInfoService pictureCategoryInfoService;

    /**
     * 查询图片信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureInfoQuery pictureInfoQuery) {
        PictureInfo pictureInfo = PictureInfoQuery.queryToObj(pictureInfoQuery);
        //查询到分类的所有子节点
        if (StringUtils.isNotEmpty(pictureInfo.getCategoryId())) {
            List<PictureCategoryInfo> categories = pictureCategoryInfoService.findCategoryChildren(pictureInfo.getCategoryId());
            pictureInfo.setCategoryId(null);
            pictureInfo.setCategoryIds(categories.stream().map(PictureCategoryInfo::getCategoryId).toArray(String[]::new));
        }
        startPage();
        List<PictureInfo> list = pictureInfoService.selectPictureInfoList(pictureInfo);
        List<PictureInfoVo> listVo = list.stream().map(PictureInfoVo::objToVo).collect(Collectors.toList());
        String inCache = sysConfigService.selectConfigByKey(PICTURE_P);
        listVo.forEach(item -> {
            item.setPictureUrl(OssConfig.builderPictureUrl(item.getPictureUrl(), inCache));
            item.setThumbnailUrl(OssConfig.builderPictureUrl(item.getThumbnailUrl(),inCache));
        });
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出图片信息列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:export')")
    @Log(title = "图片信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureInfoQuery pictureInfoQuery) {
        PictureInfo pictureInfo = PictureInfoQuery.queryToObj(pictureInfoQuery);
        //查询到分类的所有子节点
        if (StringUtils.isNotEmpty(pictureInfo.getCategoryId())) {
            List<PictureCategoryInfo> categories = pictureCategoryInfoService.findCategoryChildren(pictureInfo.getCategoryId());
            pictureInfo.setCategoryId(null);
            pictureInfo.setCategoryIds(categories.stream().map(PictureCategoryInfo::getCategoryId).toArray(String[]::new));
        }
        List<PictureInfo> list = pictureInfoService.selectPictureInfoList(pictureInfo);
        ExcelUtil<PictureInfo> util = new ExcelUtil<PictureInfo>(PictureInfo.class);
        util.exportExcel(response, list, "图片信息数据");
    }

    /**
     * 获取图片信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:query')")
    @GetMapping(value = "/{pictureId}")
    public AjaxResult getInfo(@PathVariable("pictureId") String pictureId) {
        PictureInfo pictureInfo = pictureInfoService.selectPictureInfoByPictureId(pictureId);
        return success(PictureInfoVo.objToVo(pictureInfo));
    }

    /**
     * 新增图片信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:add')")
    @Log(title = "图片信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureInfoInsert pictureInfoInsert) {
        PictureInfo pictureInfo = PictureInfoInsert.insertToObj(pictureInfoInsert);
        return toAjax(pictureInfoService.insertPictureInfo(pictureInfo));
    }

    /**
     * 修改图片信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:edit')")
    @Log(title = "图片信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureInfoEdit pictureInfoEdit) {
        PictureInfo pictureInfo = PictureInfoEdit.editToObj(pictureInfoEdit);
        return toAjax(pictureInfoService.updatePictureInfo(pictureInfo));
    }

    /**
     * 删除图片信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureInfo:remove')")
    @Log(title = "图片信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pictureIds}")
    public AjaxResult remove(@PathVariable String[] pictureIds) {
        return toAjax(pictureInfoService.deletePictureInfoByPictureIds(pictureIds));
    }
}
