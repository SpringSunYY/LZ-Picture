package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.picture.model.domain.PictureApplyInfo;
import com.lz.picture.model.dto.pictureApplyInfo.PictureApplyInfoAdd;
import com.lz.picture.service.IPictureApplyInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图片申请信息Controller
 *
 * @author YY
 * @date 2025-06-17
 */
@RestController
@RequestMapping("/user/picture/pictureApplyInfo")
public class UserPictureApplyInfoController extends BaseUserInfoController {
    @Resource
    private IPictureApplyInfoService pictureApplyInfoService;

    /**
     * 新增图片申请信息
     */
    @PreAuthorize("@uss.hasPermi('picture:upload:apply')")
    @PostMapping
    public AjaxResult add(@RequestBody PictureApplyInfoAdd pictureApplyInfoAdd) {
        PictureApplyInfo pictureApplyInfo = PictureApplyInfoAdd.insertToObj(pictureApplyInfoAdd);
        pictureApplyInfo.setUserId(getUserId());
        return toAjax(pictureApplyInfoService.userInsertPictureApplyInfo(pictureApplyInfo));
    }
}
