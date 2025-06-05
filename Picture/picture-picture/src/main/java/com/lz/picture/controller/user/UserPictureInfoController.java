package com.lz.picture.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.annotation.SearchLog;
import com.lz.picture.annotation.UserViewLog;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureInfo.PictureInfoRecommendRequest;
import com.lz.picture.model.dto.pictureInfo.UserPictureInfoAdd;
import com.lz.picture.model.dto.pictureInfo.UserPictureInfoQuery;
import com.lz.picture.model.dto.pictureInfo.UserPictureInfoUpdate;
import com.lz.picture.model.enums.PPictureReviewStatusEnum;
import com.lz.picture.model.enums.PPictureStatusEnum;
import com.lz.picture.model.vo.pictureInfo.*;
import com.lz.picture.service.IPictureInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lz.common.constant.config.UserConfigKeyConstants.PICTURE_INDEX_P;

/**
 * Project: Picture
 * Package: com.lz.picture.controller.user
 * Author: YY
 * CreateTime: 2025-03-29  21:35
 * Description: UserPictureInfoController
 * Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/pictureInfo")
public class UserPictureInfoController extends BaseUserInfoController {

    @Resource
    private IPictureInfoService pictureInfoService;

    @Resource
    private IConfigInfoService configInfoService;


    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PostMapping()
    public AjaxResult add(@RequestBody @Validated UserPictureInfoAdd userPictureInfoAdd) {
        PictureInfo pictureInfo = UserPictureInfoAdd.addToObj(userPictureInfoAdd);
        pictureInfo.setUserId(getUserId());
        return success(pictureInfoService.userInsertPictureInfo(pictureInfo));
    }

    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PutMapping("/update")
    public AjaxResult update(@RequestBody @Validated UserPictureInfoUpdate userPictureInfoUpdate) {
        PictureInfo pictureInfo = UserPictureInfoUpdate.updateToObj(userPictureInfoUpdate);
        pictureInfo.setUserId(getUserId());
        return success(pictureInfoService.userUpdatePictureInfo(pictureInfo));
    }

    @UserViewLog(targetType = "0", score = 1)
    @PreAuthorize("@uss.hasPermi('picture:upload:detail')")
    @GetMapping("/{pictureId}")
    public AjaxResult getInfo(@PathVariable("pictureId") String pictureId) {
        UserPictureDetailInfoVo userPictureDetailInfoVo = pictureInfoService.userSelectPictureInfoByPictureId(pictureId, getUserId());
        return success(userPictureDetailInfoVo);
    }

    @PreAuthorize("@uss.hasPermi('picture:upload:detail')")
    @GetMapping("/my/{pictureId}")
    public AjaxResult getMyInfo(@PathVariable("pictureId") String pictureId) {
        UserPictureDetailInfoVo userPictureDetailInfoVo = pictureInfoService.userMySelectPictureInfoByPictureId(pictureId, getUserId());
        return success(userPictureDetailInfoVo);
    }

    @SearchLog(searchType = "0", referSource = "0")
    @GetMapping("/list")
    public TableDataInfo list(UserPictureInfoQuery userPictureInfoQuery) {
        if (StringUtils.isNull(userPictureInfoQuery.getPageSize())) {
            userPictureInfoQuery.setPageSize(50);
        }
        if (userPictureInfoQuery.getPageSize() > 50) {
            userPictureInfoQuery.setPageSize(50);
        }
        PictureInfo pictureInfo = UserPictureInfoQuery.queryToObj(userPictureInfoQuery);
        //限定审核通过 状态为正常
        pictureInfo.setReviewStatus(Long.parseLong(PPictureReviewStatusEnum.PICTURE_REVIEW_STATUS_1.getValue()));
        pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue());
        QueryWrapper<PictureInfo> queryWrapper = pictureInfoService.getQueryWrapper(pictureInfo);
        Page<PictureInfo> page = pictureInfoService.page(new Page<>(userPictureInfoQuery.getPageNum(), userPictureInfoQuery.getPageSize()), queryWrapper);
        List<PictureInfo> pictureInfoList = getPictureInfos(page);
        List<UserPictureInfoVo> userPictureInfoVos = UserPictureInfoVo.objToVo(pictureInfoList);
        //压缩图片
        String p = configInfoService.getConfigInfoInCache(PICTURE_INDEX_P);
        for (UserPictureInfoVo vo : userPictureInfoVos) {
            vo.setThumbnailUrl(vo.getThumbnailUrl() + "?x-oss-process=image/resize,p_" + p);
        }
        return getDataTable(userPictureInfoVos, page.getTotal());
    }

    @PreAuthorize("@uss.hasPermi('picture:list')")
    @GetMapping("/list/my")
    public TableDataInfo listMy(UserPictureInfoQuery userPictureInfoQuery) {
        PictureInfo pictureInfo = UserPictureInfoQuery.queryToObj(userPictureInfoQuery);
        pictureInfo.setUserId(getUserId());
        QueryWrapper<PictureInfo> queryWrapper = pictureInfoService.getQueryWrapper(pictureInfo);
        Page<PictureInfo> page = pictureInfoService.page(new Page<>(userPictureInfoQuery.getPageNum(), userPictureInfoQuery.getPageSize()), queryWrapper);
        List<PictureInfo> pictureInfoList = getPictureInfos(page);
        List<MyPictureInfoVo> userPictureInfoVos = MyPictureInfoVo.objToVo(pictureInfoList);
        //压缩图片
        String p = configInfoService.getConfigInfoInCache(PICTURE_INDEX_P);
        for (MyPictureInfoVo vo : userPictureInfoVos) {
            vo.setThumbnailUrl(vo.getThumbnailUrl() + "?x-oss-process=image/resize,p_" + p);
        }
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setRows(userPictureInfoVos);
        tableDataInfo.setTotal(page.getTotal());
        return tableDataInfo;
    }

    @GetMapping("/search/recommend")
    public TableDataInfo getSearchRecommend() {
        List<PictureInfoSearchRecommendVo> list = pictureInfoService.getSearchRecommend();
        return getDataTable(list, list.size());
    }

    @GetMapping("/search/suggestion")
    public TableDataInfo getSearchRecommend(@RequestParam(required = false) String name) {
        List<PictureInfoSearchSuggestionVo> list = pictureInfoService.getSearchSuggestion(name);
        return getDataTable(list, list.size());
    }

    @GetMapping("/detail/recommend")
    public TableDataInfo getPictureInfoDetailRecommend(PictureInfoRecommendRequest pictureInfoRecommendRequest) {
        pictureInfoRecommendRequest.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue());
        pictureInfoRecommendRequest.setReviewStatus(PPictureReviewStatusEnum.PICTURE_REVIEW_STATUS_1.getValue());
        List<UserPictureInfoVo> userPictureInfoVos = pictureInfoService.getPictureInfoDetailRecommend(pictureInfoRecommendRequest);
        //压缩图片
        String p = configInfoService.getConfigInfoInCache(PICTURE_INDEX_P);
        for (UserPictureInfoVo vo : userPictureInfoVos) {
            vo.setThumbnailUrl(vo.getThumbnailUrl() + "?x-oss-process=image/resize,p_" + p);
        }
        return getDataTable(userPictureInfoVos,userPictureInfoVos.size());
    }

    private List<PictureInfo> getPictureInfos(Page<PictureInfo> page) {
        List<PictureInfo> pictureInfoList = page.getRecords();
        for (PictureInfo info : pictureInfoList) {
            String pictureUrl = pictureInfoService.builderPictureUrl(info.getPictureUrl(), info.getDnsUrl());
            info.setPictureUrl(pictureUrl);
            String thumbnailUrl = pictureInfoService.builderPictureUrl(info.getThumbnailUrl(), info.getDnsUrl());
            info.setThumbnailUrl(thumbnailUrl);
        }
        return pictureInfoList;
    }
}
