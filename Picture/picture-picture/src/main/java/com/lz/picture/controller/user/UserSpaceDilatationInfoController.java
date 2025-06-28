package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.picture.model.domain.SpaceDilatationInfo;
import com.lz.picture.model.dto.spaceDilatationInfo.SpaceDilatationInfoAdd;
import com.lz.picture.service.ISpaceDilatationInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 空间扩容信息Controller
 *
 * @author YY
 * @date 2025-06-28
 */
@RestController
@RequestMapping("/user/picture/spaceDilatationInfo")
public class UserSpaceDilatationInfoController extends BaseUserInfoController {
    @Resource
    private ISpaceDilatationInfoService spaceDilatationInfoService;


    /**
     * 新增空间扩容信息
     */
    @PreAuthorize("@uss.hasPermi('space:manage:dilatation')")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated SpaceDilatationInfoAdd spaceDilatationInfoAdd) {
        SpaceDilatationInfo spaceDilatationInfo = SpaceDilatationInfoAdd.insertToObj(spaceDilatationInfoAdd);
        spaceDilatationInfo.setUserId(getUserId());
        return toAjax(spaceDilatationInfoService.userInsertSpaceDilatationInfo(spaceDilatationInfo));
    }

    /**
     * 计算扩容
     */
    @PreAuthorize("@uss.hasPermi('space:manage:dilatation')")
    @GetMapping("/calculate")
    public AjaxResult calculateDilatationPointTotal(@Validated SpaceDilatationInfoAdd SpaceDilatationInfoAdd) {
        return success(spaceDilatationInfoService.calculateDilatationPointTotal(SpaceDilatationInfoAdd.getDilatationTotal(), SpaceDilatationInfoAdd.getDilatationType()));
    }
}
