package com.lz.picture.controller.user;

import com.lz.common.core.page.TableDataInfo;
import com.lz.picture.model.domain.PictureCategoryInfo;
import com.lz.picture.model.dto.pictureCategoryInfo.PictureCategoryInfoQuery;
import com.lz.picture.model.vo.pictureCategoryInfo.UserPictureCategoryInfoVo;
import com.lz.picture.service.IPictureCategoryInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.lz.config.utils.ConfigInfoUtils.PICTURE_CATEGORY_AI_ID_VALUE;

/**
 * 图片分类信息Controller
 *
 * @author YY
 * @date 2025-03-24
 */
@RestController
@RequestMapping("/user/picture/pictureCategoryInfo")
public class UserPictureCategoryInfoController extends BaseUserInfoController {
    @Resource
    private IPictureCategoryInfoService pictureCategoryInfoService;

    /**
     * 查询图片分类信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PictureCategoryInfoQuery pictureCategoryInfoQuery) {
        PictureCategoryInfo pictureCategoryInfo = PictureCategoryInfoQuery.queryToObj(pictureCategoryInfoQuery);
        List<PictureCategoryInfo> list = pictureCategoryInfoService.userSelectPictureCategoryInfoList(pictureCategoryInfo);
        List<UserPictureCategoryInfoVo> listVo = list.stream().map(UserPictureCategoryInfoVo::objToVo).collect(Collectors.toList());
        return getDataTable(listVo, list.size());
    }

    @PreAuthorize("@uss.hasPermi('picture:pictureCategoryInfo')")
    @GetMapping("/list/ai")
    public TableDataInfo listAIChildren() {
        List<PictureCategoryInfo> list = pictureCategoryInfoService.findCategoryChildren(PICTURE_CATEGORY_AI_ID_VALUE);
        List<UserPictureCategoryInfoVo> listVo = list.stream().map(UserPictureCategoryInfoVo::objToVo).collect(Collectors.toList());
        return getDataTable(listVo, listVo.size());
    }
}
