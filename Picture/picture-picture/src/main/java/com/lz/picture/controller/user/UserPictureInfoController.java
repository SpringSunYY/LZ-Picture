package com.lz.picture.controller.user;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.config.OssConfig;
import com.lz.common.constant.HttpStatus;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.CommonDeleteEnum;
import com.lz.common.exception.ServiceException;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.manager.file.model.FileResponse;
import com.lz.common.utils.StringUtils;
import com.lz.config.model.enmus.CFileLogOssTypeEnum;
import com.lz.config.model.enmus.CFileLogTypeEnum;
import com.lz.picture.annotation.SearchLog;
import com.lz.picture.annotation.UserViewLog;
import com.lz.picture.manager.PictureAsyncManager;
import com.lz.picture.manager.factory.PictureFileLogAsyncFactory;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureInfo.*;
import com.lz.picture.model.dto.pictureRecommend.PictureRecommendRequest;
import com.lz.picture.model.enums.PPictureStatusEnum;
import com.lz.picture.model.vo.pictureInfo.*;
import com.lz.picture.service.IPictureInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lz.common.constant.picture.PictureInfoConstants.PICTURE_HOT_TOTAL;
import static com.lz.config.utils.ConfigInfoUtils.PICTURE_INDEX_P_VALUE;


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
    private PictureUploadManager pictureUploadManager;

    @Resource
    private OssConfig ossConfig;


    /**
     * 新增图片
     */
    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PostMapping()
    public AjaxResult add(@RequestBody @Validated UserPictureInfoAdd userPictureInfoAdd) {
        PictureInfo pictureInfo = UserPictureInfoAdd.addToObj(userPictureInfoAdd);
        pictureInfo.setUserId(getUserId());
        return success(pictureInfoService.userInsertPictureInfo(pictureInfo));
    }

    /**
     * 上传图片 图片搜索
     */
    @PreAuthorize("@uss.hasPermi('picture:pictureSearchUpload')")
    @PostMapping("/upload/url")
    public AjaxResult uploadUrl(@RequestBody @Validated PictureUrlUpload pictureUrlUpload) {
        System.err.println(pictureUrlUpload);
        // 执行业务上传
        FileResponse fileResponse = pictureUploadManager.uploadUrl(pictureUrlUpload.getUrl(), "picture", getLoginUser());

        //异步执行存入文件日志
        String userId = getUserId();
//        System.err.println("picture = " + pictureFileResponse);
        PictureAsyncManager.me().execute(PictureFileLogAsyncFactory.recordFileLog(fileResponse,
                userId,
                CFileLogOssTypeEnum.OSS_TYPE_0.getValue(),
                CFileLogTypeEnum.LOG_TYPE_0.getValue()
        ));
        PictureInfo pictureInfo = new PictureInfo();
        pictureInfo.setPictureUrl(fileResponse.getUrl());
//        pictureInfo.setDnsUrl();
        pictureInfo.setName(pictureUrlUpload.getName());
        pictureInfo.setIntroduction(pictureUrlUpload.getIntroduction());
        pictureInfo.setCategoryId(pictureUrlUpload.getCategoryId());
        pictureInfo.setPicSize(fileResponse.getPicSize());
        pictureInfo.setPicWidth(fileResponse.getPicWidth());
        pictureInfo.setPicHeight(fileResponse.getPicHeight());
        pictureInfo.setPicScale(fileResponse.getPicScale());
        pictureInfo.setPicFormat(fileResponse.getPicFormat());
        pictureInfo.setUserId(userId);
        pictureInfo.setThumbnailUrl(fileResponse.getThumbnailUrl());
        pictureInfo.setSpaceId(pictureUrlUpload.getSpaceId());
        pictureInfo.setFolderId(pictureUrlUpload.getFolderId());
        PictureMoreInfo pictureMoreInfo = new PictureMoreInfo();
        pictureMoreInfo.setOriginUrl(pictureUrlUpload.getUrl());
        pictureInfo.setMoreInfo(JSON.toJSONString(pictureMoreInfo));
        pictureInfo.setTags(pictureUrlUpload.getTags());

        return success(pictureInfoService.userInsertPictureInfo(pictureInfo));
    }

    /**
     * 修改图片
     */
    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PutMapping("/update")
    public AjaxResult update(@RequestBody @Validated UserPictureInfoUpdate userPictureInfoUpdate) {
        PictureInfo pictureInfo = UserPictureInfoUpdate.updateToObj(userPictureInfoUpdate);
        pictureInfo.setUserId(getUserId());
        return success(pictureInfoService.userUpdatePictureInfo(pictureInfo));
    }

    /**
     * 修改名字
     */
    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @PutMapping("/update/name")
    public AjaxResult updateName(@RequestBody @Validated UserPictureInfoUpdateName userPictureInfoUpdateName) {
        PictureInfo pictureInfo = UserPictureInfoUpdateName.updateToObj(userPictureInfoUpdateName);
        pictureInfo.setUserId(getUserId());
        return success(pictureInfoService.userUpdatePictureInfoName(pictureInfo));
    }


    /**
     * 获取图片详细信息
     */
    @UserViewLog(targetType = "0")
    @PreAuthorize("@uss.hasPermi('picture:upload:detail')")
    @GetMapping("/{pictureId}")
    public AjaxResult getInfo(@PathVariable("pictureId") String pictureId) {
        UserPictureDetailInfoVo userPictureDetailInfoVo = pictureInfoService.userSelectPictureInfoByPictureId(pictureId, getUserId());
        return success(userPictureDetailInfoVo);
    }

    /**
     * 获取我的图片详细信息
     */
    @PreAuthorize("@uss.hasPermi('picture:upload:detail')")
    @GetMapping("/my/{pictureId}")
    public AjaxResult getMyInfo(@PathVariable("pictureId") String pictureId) {
        UserPictureDetailInfoVo userPictureDetailInfoVo = pictureInfoService.userMySelectPictureInfoByPictureId(pictureId, getUserId());
        return success(userPictureDetailInfoVo);
    }

    /**
     * 获取图片列表
     */
    @PreAuthorize("@uss.hasPermi('picture:picture:pictureTable')")
    @GetMapping("/list/my/table")
    public TableDataInfo listMyTable(UserPictureInfoQuery userPictureInfoQuery) {
        if (StringUtils.isNull(userPictureInfoQuery.getPageSize())) {
            userPictureInfoQuery.setPageSize(50);
        }
        if (userPictureInfoQuery.getPageSize() > 50) {
            userPictureInfoQuery.setPageSize(50);
        }
        userPictureInfoQuery.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        userPictureInfoQuery.setUserId(getUserId());
        return pictureInfoService.listPictureInfoTable(userPictureInfoQuery);
    }

    /**
     * 获取图片列表
     */
    @PreAuthorize("@uss.hasPermi('picture:picture:pictureTeamTable')")
    @GetMapping("/list/my/space/table")
    public TableDataInfo listMyPictureTable(UserPictureInfoQuery userPictureInfoQuery) {
        if (StringUtils.isNull(userPictureInfoQuery.getPageSize())) {
            userPictureInfoQuery.setPageSize(50);
        }
        if (userPictureInfoQuery.getPageSize() > 50) {
            userPictureInfoQuery.setPageSize(50);
        }
        userPictureInfoQuery.setIsDelete(CommonDeleteEnum.NORMAL.getValue());
        return pictureInfoService.listPictureInfoTeamTable(userPictureInfoQuery);
    }

    /**
     * 图片列表
     */
    @SearchLog(searchType = "0", referSource = "0")
    @GetMapping("/list")
    public TableDataInfo list(UserPictureInfoQuery userPictureInfoQuery) {
        if (StringUtils.isNull(userPictureInfoQuery.getPageSize())) {
            userPictureInfoQuery.setPageSize(50);
        }
        if (userPictureInfoQuery.getPageSize() > 50) {
            userPictureInfoQuery.setPageSize(50);
        }
//        PictureInfo pictureInfo = UserPictureInfoQuery.queryToObj(userPictureInfoQuery);
//        //限定审核通过 状态为正常
//        pictureInfo.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue());
//        QueryWrapper<PictureInfo> queryWrapper = pictureInfoService.getQueryWrapper(pictureInfo);
//        Page<PictureInfo> page = pictureInfoService.page(new Page<>(userPictureInfoQuery.getPageNum(), userPictureInfoQuery.getPageSize()), queryWrapper);
//        List<PictureInfo> pictureInfoList = getPictureInfos(page);
//        List<UserPictureInfoVo> userPictureInfoVos = UserPictureInfoVo.objToVo(pictureInfoList);
//        //压缩图片
//        for (UserPictureInfoVo vo : userPictureInfoVos) {
//            vo.setThumbnailUrl(vo.getThumbnailUrl() + "?x-oss-process=image/resize,p_" + PICTURE_SPACE_AVATAR_P_VALUE);
//        }
        if (StringUtils.isEmpty(userPictureInfoQuery.getName())) {
            throw new ServiceException("搜索内容不能为空", HttpStatus.BAD_REQUEST);
        }
        PictureRecommendRequest pictureRecommendRequest = new PictureRecommendRequest();
        pictureRecommendRequest.setName(userPictureInfoQuery.getName());
        pictureRecommendRequest.setCurrentPage(userPictureInfoQuery.getPageNum());
        pictureRecommendRequest.setPageSize(userPictureInfoQuery.getPageSize());
        List<UserRecommendPictureInfoVo> recommentHotPictureInfoList = pictureInfoService.getRecommentHotPictureInfoList(pictureRecommendRequest);
        return getDataTable(recommentHotPictureInfoList, recommentHotPictureInfoList.size());
    }

    /**
     * list我的
     */
    @PreAuthorize("@uss.hasPermi('picture:list')")
    @GetMapping("/list/my")
    public TableDataInfo listMy(UserPictureInfoQuery userPictureInfoQuery) {
        if (StringUtils.isNull(userPictureInfoQuery.getPageSize())) {
            userPictureInfoQuery.setPageSize(50);
        }
        if (userPictureInfoQuery.getPageSize() > 50) {
            userPictureInfoQuery.setPageSize(50);
        }
        return pictureInfoService.listMy(userPictureInfoQuery);
    }

    /**
     * 图片搜索推荐
     */
    @GetMapping("/search/recommend")
    public TableDataInfo getSearchRecommend() {
        List<PictureInfoSearchRecommendVo> list = pictureInfoService.getSearchRecommend();
        return getDataTable(list, list.size());
    }

    /**
     * 图片搜索建议
     */
    @GetMapping("/search/suggestion")
    public TableDataInfo getSearchRecommend(@RequestParam(required = false) String name) {
        List<PictureInfoSearchSuggestionVo> list = pictureInfoService.getSearchSuggestion(name);
        return getDataTable(list, list.size());
    }

    /**
     * 图片详情推荐
     */
    @GetMapping("/detail/recommend")
    public TableDataInfo getPictureInfoDetailRecommend(PictureInfoDetailRecommendRequest pictureInfoDetailRecommendRequest) {
        pictureInfoDetailRecommendRequest.setPictureStatus(PPictureStatusEnum.PICTURE_STATUS_0.getValue());
        List<UserPictureInfoVo> userPictureInfoVos = pictureInfoService.getPictureInfoDetailRecommend(pictureInfoDetailRecommendRequest);
        //压缩图片
        for (UserPictureInfoVo vo : userPictureInfoVos) {
            vo.setThumbnailUrl(vo.getThumbnailUrl() + "?x-oss-process=image/resize,p_" + PICTURE_INDEX_P_VALUE);
        }
        return getDataTable(userPictureInfoVos, userPictureInfoVos.size());
    }

    /**
     * 删除图片
     */
    @PreAuthorize("@uss.hasPermi('picture:upload')")
    @DeleteMapping("/{pictureIds}")
    public AjaxResult remove(@PathVariable String[] pictureIds) {
        return toAjax(pictureInfoService.userDeletePictureInfoByIds(pictureIds));
    }

    @PreAuthorize("@uss.hasPermi('picture:hot')")
    @GetMapping("/hot")
    public TableDataInfo getPictureInfoHot(PictureInfoHotRequest pictureInfoHotRequest) {
        if (StringUtils.isEmpty(pictureInfoHotRequest.getType())) {
            pictureInfoHotRequest.setType(PICTURE_HOT_TOTAL);
        }
        return pictureInfoService.getPictureInfoHot(pictureInfoHotRequest);
    }
}
