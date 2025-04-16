package com.lz.picture.controller.user;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.exception.ServiceException;
import com.lz.common.manager.file.PictureUploadManager;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.file.FileUploadUtils;
import com.lz.common.utils.file.FileUtils;
import com.lz.config.service.IConfigInfoService;
import com.lz.picture.annotation.UserViewLog;
import com.lz.picture.model.domain.PictureInfo;
import com.lz.picture.model.dto.pictureInfo.UserPictureInfoAdd;
import com.lz.picture.model.dto.pictureInfo.UserPictureInfoQuery;
import com.lz.picture.model.enums.PPictureReviewStatus;
import com.lz.picture.model.enums.PPictureStatus;
import com.lz.picture.model.vo.pictureInfo.UserPictureDetailInfoVo;
import com.lz.picture.model.vo.pictureInfo.UserPictureInfoVo;
import com.lz.picture.service.IPictureInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.lz.common.constant.config.ConfigKeyConstants.PICTURE_INDEX_P;

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

    @UserViewLog(targetType = "0", score = 1)
    @PreAuthorize("@uss.hasPermi('picture:upload:detail')")
    @GetMapping("/{pictureId}")
    public AjaxResult getInfo(@PathVariable("pictureId") String pictureId) {
        UserPictureDetailInfoVo userPictureDetailInfoVo = pictureInfoService.userSelectPictureInfoByPictureId(pictureId, getUserId());
        return success(userPictureDetailInfoVo);
    }

    @GetMapping("/list")
    public TableDataInfo list(UserPictureInfoQuery userPictureInfoQuery) {
        if (StringUtils.isNull(userPictureInfoQuery.getPageSize())) {
            userPictureInfoQuery.setPageSize(50);
        }
        if (userPictureInfoQuery.getPageSize() > 50) {
            throw new ServiceException("请求参数异常！！！");
        }
        PictureInfo pictureInfo = UserPictureInfoQuery.queryToObj(userPictureInfoQuery);
        //限定审核通过 状态为正常
        pictureInfo.setReviewStatus(Long.parseLong(PPictureReviewStatus.PICTURE_REVIEW_STATUS_1.getValue()));
        pictureInfo.setPictureStatus(PPictureStatus.PICTURE_STATUS_0.getValue());
        QueryWrapper<PictureInfo> queryWrapper = pictureInfoService.getQueryWrapper(pictureInfo);
        Page<PictureInfo> page = pictureInfoService.page(new Page<>(userPictureInfoQuery.getPageNum(), userPictureInfoQuery.getPageSize()), queryWrapper);
        List<UserPictureInfoVo> userPictureInfoVos = UserPictureInfoVo.objToVo(page.getRecords());
        //压缩图片
        String p = configInfoService.getConfigInfoInCache(PICTURE_INDEX_P);
        for (UserPictureInfoVo vo : userPictureInfoVos) {
            vo.setThumbnailUrl(vo.getThumbnailUrl() + "?x-oss-process=image/resize,p_" + p);
        }
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setRows(userPictureInfoVos);
        tableDataInfo.setTotal(page.getTotal());
        return tableDataInfo;
    }
}
