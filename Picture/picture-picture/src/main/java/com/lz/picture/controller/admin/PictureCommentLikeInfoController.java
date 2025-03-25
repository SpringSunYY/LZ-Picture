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
import com.lz.picture.model.domain.PictureCommentLikeInfo;
import com.lz.picture.model.vo.pictureCommentLikeInfo.PictureCommentLikeInfoVo;
import com.lz.picture.model.dto.pictureCommentLikeInfo.PictureCommentLikeInfoQuery;
import com.lz.picture.model.dto.pictureCommentLikeInfo.PictureCommentLikeInfoInsert;
import com.lz.picture.model.dto.pictureCommentLikeInfo.PictureCommentLikeInfoEdit;
import com.lz.picture.service.IPictureCommentLikeInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 评论点赞记录Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/admin/picture/pictureCommentLikeInfo")
public class PictureCommentLikeInfoController extends BaseController
{
    @Resource
    private IPictureCommentLikeInfoService pictureCommentLikeInfoService;

    /**
     * 查询评论点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentLikeInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PictureCommentLikeInfoQuery pictureCommentLikeInfoQuery)
    {
        PictureCommentLikeInfo pictureCommentLikeInfo = PictureCommentLikeInfoQuery.queryToObj(pictureCommentLikeInfoQuery);
        startPage();
        List<PictureCommentLikeInfo> list = pictureCommentLikeInfoService.selectPictureCommentLikeInfoList(pictureCommentLikeInfo);
        List<PictureCommentLikeInfoVo> listVo= list.stream().map(PictureCommentLikeInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出评论点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentLikeInfo:export')")
    @Log(title = "评论点赞记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PictureCommentLikeInfoQuery pictureCommentLikeInfoQuery)
    {
        PictureCommentLikeInfo pictureCommentLikeInfo = PictureCommentLikeInfoQuery.queryToObj(pictureCommentLikeInfoQuery);
        List<PictureCommentLikeInfo> list = pictureCommentLikeInfoService.selectPictureCommentLikeInfoList(pictureCommentLikeInfo);
        ExcelUtil<PictureCommentLikeInfo> util = new ExcelUtil<PictureCommentLikeInfo>(PictureCommentLikeInfo.class);
        util.exportExcel(response, list, "评论点赞记录数据");
    }

    /**
     * 获取评论点赞记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentLikeInfo:query')")
    @GetMapping(value = "/{likeId}")
    public AjaxResult getInfo(@PathVariable("likeId") String likeId)
    {
        PictureCommentLikeInfo pictureCommentLikeInfo = pictureCommentLikeInfoService.selectPictureCommentLikeInfoByLikeId(likeId);
        return success(PictureCommentLikeInfoVo.objToVo(pictureCommentLikeInfo));
    }

    /**
     * 新增评论点赞记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentLikeInfo:add')")
    @Log(title = "评论点赞记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PictureCommentLikeInfoInsert pictureCommentLikeInfoInsert)
    {
        PictureCommentLikeInfo pictureCommentLikeInfo = PictureCommentLikeInfoInsert.insertToObj(pictureCommentLikeInfoInsert);
        return toAjax(pictureCommentLikeInfoService.insertPictureCommentLikeInfo(pictureCommentLikeInfo));
    }

    /**
     * 修改评论点赞记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentLikeInfo:edit')")
    @Log(title = "评论点赞记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PictureCommentLikeInfoEdit pictureCommentLikeInfoEdit)
    {
        PictureCommentLikeInfo pictureCommentLikeInfo = PictureCommentLikeInfoEdit.editToObj(pictureCommentLikeInfoEdit);
        return toAjax(pictureCommentLikeInfoService.updatePictureCommentLikeInfo(pictureCommentLikeInfo));
    }

    /**
     * 删除评论点赞记录
     */
    @PreAuthorize("@ss.hasPermi('picture:pictureCommentLikeInfo:remove')")
    @Log(title = "评论点赞记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{likeIds}")
    public AjaxResult remove(@PathVariable String[] likeIds)
    {
        return toAjax(pictureCommentLikeInfoService.deletePictureCommentLikeInfoByLikeIds(likeIds));
    }
}
