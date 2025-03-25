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
import com.lz.picture.model.domain.PictureCommentInfo;
import com.lz.picture.model.vo.pictureCommentInfo.PictureCommentInfoVo;
import com.lz.picture.model.dto.pictureCommentInfo.PictureCommentInfoQuery;
import com.lz.picture.model.dto.pictureCommentInfo.PictureCommentInfoInsert;
import com.lz.picture.model.dto.pictureCommentInfo.PictureCommentInfoEdit;
import com.lz.picture.service.IPictureCommentInfoService;
import com.lz.common.utils.poi.ExcelUtil;

/**
 * 图片评论Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureCommentInfo")
public class PictureCommentInfoController extends BaseController
{
    @Resource
    private IPictureCommentInfoService pictureCommentInfoService;

    /**
     * 查询图片评论列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentInfo:list')")
    @GetMapping("/list")
        public AjaxResult list(PictureCommentInfoQuery pictureCommentInfoQuery)
        {
            PictureCommentInfo pictureCommentInfo = PictureCommentInfoQuery.queryToObj(pictureCommentInfoQuery);
            List<PictureCommentInfo> list = pictureCommentInfoService.selectPictureCommentInfoList(pictureCommentInfo);
            List<PictureCommentInfoVo> listVo= list.stream().map(PictureCommentInfoVo::objToVo).collect(Collectors.toList());
            return success(listVo);
        }

    /**
     * 导出图片评论列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentInfo:export')")
    @Log(title = "图片评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureCommentInfoQuery pictureCommentInfoQuery)
    {
        PictureCommentInfo pictureCommentInfo = PictureCommentInfoQuery.queryToObj(pictureCommentInfoQuery);
        List<PictureCommentInfo> list = pictureCommentInfoService.selectPictureCommentInfoList(pictureCommentInfo);
        ExcelUtil<PictureCommentInfo> util = new ExcelUtil<PictureCommentInfo>(PictureCommentInfo.class);
        util.exportExcel(response, list, "图片评论数据");
    }

    /**
     * 获取图片评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentInfo:query')")
    @GetMapping(value = "/{commentId}")
    public AjaxResult getInfo(@PathVariable("commentId") String commentId)
    {
        PictureCommentInfo pictureCommentInfo = pictureCommentInfoService.selectPictureCommentInfoByCommentId(commentId);
        return success(PictureCommentInfoVo.objToVo(pictureCommentInfo));
    }

    /**
     * 新增图片评论
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentInfo:add')")
    @Log(title = "图片评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureCommentInfoInsert pictureCommentInfoInsert)
    {
        PictureCommentInfo pictureCommentInfo = PictureCommentInfoInsert.insertToObj(pictureCommentInfoInsert);
        return toAjax(pictureCommentInfoService.insertPictureCommentInfo(pictureCommentInfo));
    }

    /**
     * 修改图片评论
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentInfo:edit')")
    @Log(title = "图片评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureCommentInfoEdit pictureCommentInfoEdit)
    {
        PictureCommentInfo pictureCommentInfo = PictureCommentInfoEdit.editToObj(pictureCommentInfoEdit);
        return toAjax(pictureCommentInfoService.updatePictureCommentInfo(pictureCommentInfo));
    }

    /**
     * 删除图片评论
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentInfo:remove')")
    @Log(title = "图片评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/{commentIds}")
    public AjaxResult remove(@PathVariable String[] commentIds)
    {
        return toAjax(pictureCommentInfoService.deletePictureCommentInfoByCommentIds(commentIds));
    }
}
