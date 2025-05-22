package com.lz.picture.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.domain.SpaceFolderInfo;
import com.lz.picture.model.domain.SpaceInfo;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoUserAdd;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoUserQuery;
import com.lz.picture.model.dto.spaceFolderInfo.SpaceFolderInfoUserUpdate;
import com.lz.picture.model.enums.PSpaceStatusEnum;
import com.lz.picture.model.vo.spaceFolderInfo.SpaceFolderInfoVo;
import com.lz.picture.service.ISpaceFolderInfoService;
import com.lz.picture.service.ISpaceInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Project: Picture
 * Package: com.lz.picture.controller.user
 * Author: YY
 * CreateTime: 2025-04-07  21:59
 * Description: UserSpaceFolderInfoController
 * Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/spaceFolderInfo")
public class UserSpaceFolderInfoController extends BaseUserInfoController {
    @Resource
    private ISpaceFolderInfoService spaceFolderInfoService;

    @Resource
    private ISpaceInfoService spaceInfoService;

    /**
     * 添加文件夹
     *
     * @param spaceFolderInfoUserAdd
     * @return
     */
    @PreAuthorize("@uss.hasPermi('picture:space:spaceFolder:add')")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated SpaceFolderInfoUserAdd spaceFolderInfoUserAdd) {
        SpaceFolderInfo spaceFolderInfo = SpaceFolderInfoUserAdd.addToObj(spaceFolderInfoUserAdd);
        String userId = getLoginUser().getUserId();
        spaceFolderInfo.setUserId(userId);
        return toAjax(spaceFolderInfoService.userInsertSpaceFolderInfo(spaceFolderInfo));
    }

    /**
     * 更新文件夹
     *
     * @param spaceFolderInfoUserUpdate
     * @return
     */
    @PreAuthorize("@uss.hasPermi('picture:space:spaceFolder:update')")
    @PutMapping
    public AjaxResult update(@RequestBody @Validated SpaceFolderInfoUserUpdate spaceFolderInfoUserUpdate) {
        SpaceFolderInfo spaceFolderInfo = SpaceFolderInfoUserUpdate.updateToObj(spaceFolderInfoUserUpdate);
        return toAjax(spaceFolderInfoService.userUpdateSpaceFolderInfo(spaceFolderInfo));
    }

    /**
     * 删除文件夹
     *
     * @param folderId
     * @return
     */
    @PreAuthorize("@uss.hasPermi('picture:space:spaceFolder:delete')")
    @DeleteMapping("/{folderId}")
    public AjaxResult delete(@PathVariable("folderId") String folderId) {
        return toAjax(spaceFolderInfoService.deleteSpaceFolderInfoByFolderId(folderId));
    }

    /**
     * 文件夹列表
     *
     * @param spaceFolderInfoQuery
     * @return
     */
    @PreAuthorize("@uss.hasPermi('picture:space:spaceFolder')")
    @GetMapping("/list")
    public TableDataInfo list(SpaceFolderInfoUserQuery spaceFolderInfoQuery) {
        SpaceFolderInfo spaceFolderInfo = SpaceFolderInfoUserQuery.queryToObj(spaceFolderInfoQuery);
//        spaceFolderInfoQuery.setUserId(getLoginUser().getUserId());
        //查询是否有这个空间
        SpaceInfo spaceInfo = spaceInfoService.selectSpaceInfoBySpaceId(spaceFolderInfo.getSpaceId());
        if (StringUtils.isNull(spaceInfo)
                || !spaceInfo.getIsDelete().equals(CommonDeleteEnum.NORMAL.getValue())) {
            throw new ServiceException("空间不存在，或者已被删除");
        }
        //如果空间为私有
        if (spaceInfo.getSpaceType().equals(PSpaceStatusEnum.SPACE_STATUS_1.getValue())
                && !spaceInfo.getUserId().equals(getLoginUser().getUserId())) {
            throw new ServiceException("您没有权限访问该空间");
        }
        List<SpaceFolderInfoVo> infoVos = spaceFolderInfoService.selectSpaceFolderInfoList(spaceFolderInfo)
                .stream()
                .sorted((a, b) -> Integer.compare(a.getSortOrder(), b.getSortOrder()))
                .map(SpaceFolderInfoVo::objToVo)
                .collect(Collectors.toList());
        return getDataTable(infoVos,infoVos.size());
    }

    /**
     * 获取文件夹
     *
     * @param folderId
     * @return
     */
    @PreAuthorize("@uss.hasPermi('picture:space:spaceFolder')")
    @GetMapping("/{folderId}")
    public AjaxResult getFolderListBySpaceId(@PathVariable("folderId") String folderId) {
        //查询是否有这个文件夹
        SpaceFolderInfo spaceFolderInfo = spaceFolderInfoService.selectSpaceFolderInfoByFolderId(folderId);
        if (StringUtils.isNull(spaceFolderInfo) || !spaceFolderInfo.getUserId().equals(getLoginUser().getUserId())) {
            throw new ServiceException("文件夹不存在");
        }
        return AjaxResult.success(SpaceFolderInfoVo.objToVo(spaceFolderInfo));
    }
}
