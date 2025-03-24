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
import com.lz.picture.model.domain.PictureLikeInfo;
import com.lz.picture.model.vo.pictureLikeInfo.PictureLikeInfoVo;
import com.lz.picture.model.dto.pictureLikeInfo.PictureLikeInfoQuery;
import com.lz.picture.model.dto.pictureLikeInfo.PictureLikeInfoInsert;
import com.lz.picture.model.dto.pictureLikeInfo.PictureLikeInfoEdit;
import com.lz.picture.service.IPictureLikeInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 图片点赞记录Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureLikeInfo")
public class PictureLikeInfoController extends BaseController
{
    @Resource
    private IPictureLikeInfoService pictureLikeInfoService;

    /**
     * 查询图片点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureLikeInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureLikeInfoQuery pictureLikeInfoQuery)
    {
        PictureLikeInfo pictureLikeInfo = PictureLikeInfoQuery.queryToObj(pictureLikeInfoQuery);
        startPage();
        List<PictureLikeInfo> list = pictureLikeInfoService.selectPictureLikeInfoList(pictureLikeInfo);
        List<PictureLikeInfoVo> listVo= list.stream().map(PictureLikeInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出图片点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureLikeInfo:export')")
    @Log(title = "图片点赞记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureLikeInfoQuery pictureLikeInfoQuery)
    {
        PictureLikeInfo pictureLikeInfo = PictureLikeInfoQuery.queryToObj(pictureLikeInfoQuery);
        List<PictureLikeInfo> list = pictureLikeInfoService.selectPictureLikeInfoList(pictureLikeInfo);
        ExcelUtil<PictureLikeInfo> util = new ExcelUtil<PictureLikeInfo>(PictureLikeInfo.class);
        util.exportExcel(response, list, "图片点赞记录数据");
    }

    /**
     * 获取图片点赞记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureLikeInfo:query')")
    @GetMapping(value = "/{likeId}")
    public AjaxResult getInfo(@PathVariable("likeId") String likeId)
    {
        PictureLikeInfo pictureLikeInfo = pictureLikeInfoService.selectPictureLikeInfoByLikeId(likeId);
        return success(PictureLikeInfoVo.objToVo(pictureLikeInfo));
    }

    /**
     * 新增图片点赞记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureLikeInfo:add')")
    @Log(title = "图片点赞记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureLikeInfoInsert pictureLikeInfoInsert)
    {
        PictureLikeInfo pictureLikeInfo = PictureLikeInfoInsert.insertToObj(pictureLikeInfoInsert);
        return toAjax(pictureLikeInfoService.insertPictureLikeInfo(pictureLikeInfo));
    }

    /**
     * 修改图片点赞记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureLikeInfo:edit')")
    @Log(title = "图片点赞记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureLikeInfoEdit pictureLikeInfoEdit)
    {
        PictureLikeInfo pictureLikeInfo = PictureLikeInfoEdit.editToObj(pictureLikeInfoEdit);
        return toAjax(pictureLikeInfoService.updatePictureLikeInfo(pictureLikeInfo));
    }

    /**
     * 删除图片点赞记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureLikeInfo:remove')")
    @Log(title = "图片点赞记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{likeIds}")
    public AjaxResult remove(@PathVariable String[] likeIds)
    {
        return toAjax(pictureLikeInfoService.deletePictureLikeInfoByLikeIds(likeIds));
    }
}
