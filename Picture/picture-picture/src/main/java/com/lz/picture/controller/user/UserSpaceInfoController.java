package com.lz.picture.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.common.config.OssConfig;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.utils.StringUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoAdd;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoQuery;
import com.lz.picture.model.dto.spaceInfo.SpaceInfoUpdate;
import com.lz.picture.model.enums.PSpaceTypeEnum;
import com.lz.picture.model.vo.spaceInfo.UserSpaceInfoVo;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.lz.common.constant.config.UserConfigKeyConstants.PICTURE_SPACE_AVATAR_P;

/**
 * Project: Picture
 * Package: com.lz.picture.controller.user
 * Author: YY
 * CreateTime: 2025-03-30  21:49
 * Description: UserSpaceInfoController
 * Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/spaceInfo")
public class UserSpaceInfoController extends BaseUserInfoController {
    @Resource
    private ISpaceInfoService spaceInfoService;

    @Resource
    private IConfigInfoService configInfoService;

    @Resource
    private OssConfig ossConfig;

    @PreAuthorize("@uss.hasPermi('picture:space:add')")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated SpaceInfoAdd spaceInfoAdd) {
        SpaceInfo spaceInfo = SpaceInfoAdd.addToObj(spaceInfoAdd);
        String userId = getLoginUser().getUserId();
        spaceInfo.setUserId(userId);
        return toAjax(spaceInfoService.userInsertSpaceInfo(spaceInfo));
    }

    @PreAuthorize("@uss.hasPermi('picture:space:update')")
    @PutMapping
    public AjaxResult update(@RequestBody @Validated SpaceInfoUpdate spaceInfoUpdate) {
        SpaceInfo spaceInfo = SpaceInfoUpdate.updateToObj(spaceInfoUpdate);
        String userId = getLoginUser().getUserId();
        spaceInfo.setUserId(userId);
        return toAjax(spaceInfoService.userUpdateSpaceInfo(spaceInfo));
    }

    @PreAuthorize("@uss.hasPermi('picture:space')")
    @GetMapping("/mySpace")
    public TableDataInfo mySpace(SpaceInfoQuery spaceInfoQuery) {
        //无需分页只需要拿到自己所有的空间即可 TODO后续还有自己加入的空间
        spaceInfoQuery.setUserId(getLoginUser().getUserId());
        spaceInfoQuery.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        QueryWrapper<SpaceInfo> queryWrapper = spaceInfoService.getQueryWrapper(spaceInfoQuery);
        queryWrapper.or().eq("space_type", PSpaceTypeEnum.SPACE_TYPE_0.getValue());
        queryWrapper.orderByAsc("space_type");
        List<SpaceInfo> list = spaceInfoService.list(queryWrapper);
        //转为Vo
        List<UserSpaceInfoVo> listVo = list.stream().map(UserSpaceInfoVo::objToVo).collect(Collectors.toList());
        //压缩图片
        String inCache = configInfoService.getConfigInfoInCache(PICTURE_SPACE_AVATAR_P);
        String dnsUrl = ossConfig.getDnsUrl();
        listVo.stream()
                .filter(vo -> StringUtils.isNotEmpty(vo.getSpaceAvatar()))
                .forEach(vo -> {
                    vo.setSpaceAvatar(dnsUrl + vo.getSpaceAvatar() + "?x-oss-process=image/resize,p_" + inCache);
                });
        return getDataTable(list, listVo.size());
    }

    @PreAuthorize("@uss.hasPermi('picture:space')")
    @GetMapping("/{spaceId}")
    public AjaxResult getInfo(@PathVariable("spaceId") String spaceId) {
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(spaceId);
        return success(UserSpaceInfoVo.objToVo(spaceInfo));
    }
}
