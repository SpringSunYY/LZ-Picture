package com.lz.picture.controller.user;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.picture.model.domain.PictureCategoryInfo;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoEdit;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoInsert;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoQuery;
import com.lz.picture.model.vo.pictureCategoryInfo.PictureCategoryInfoVo;
import com.lz.picture.model.vo.pictureCategoryInfo.UserPictureCategoryInfoVo;
import com.lz.picture.service.IPictureCategoryInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 图片分类信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/user/picture/pictureCategoryInfo")
public class UserPictureCategoryInfoController extends BaseController {
    @Resource
    private IPictureCategoryInfoService pictureCategoryInfoService;

    /**
     * 查询图片分类信息列表
     */
    @PreAuthorize("@uss.hasPermi('picture:pictureCategoryInfo')")
    @GetMapping("/list")
    public TableDataInfo list(PictureCategoryInfoQuery pictureCategoryInfoQuery) {
        PictureCategoryInfo pictureCategoryInfo = PictureCategoryInfoQuery.queryToObj(pictureCategoryInfoQuery);
        List<PictureCategoryInfo> list = pictureCategoryInfoService.selectPictureCategoryInfoList(pictureCategoryInfo);
        List<UserPictureCategoryInfoVo> listVo = list.stream().map(UserPictureCategoryInfoVo::objToVo).collect(Collectors.toList());
        return getDataTable(listVo);
    }
}
