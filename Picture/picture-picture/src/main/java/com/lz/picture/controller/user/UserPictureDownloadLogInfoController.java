package com.lz.picture.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.config.OssConfig;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.domain.PictureDownloadLogInfo;
import com.lz.picture.model.dto.pictureDownloadLogInfo.UserPictureDownloadLogInfoQuery;
import com.lz.picture.model.vo.pictureDownloadLogInfo.UserPictureDownloadLogInfoVo;
import com.lz.picture.service.IPictureDownloadLogInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.lz.config.utils.ConfigInfoUtils.PICTURE_COVER_P_VALUE;

/**
 * 图片下载记录Controller
 * 用户接口
 *
 * @author YY
 * @date 2025-05-24
 */
@RestController
@RequestMapping("/user/picture/pictureDownloadLogInfo")
public class UserPictureDownloadLogInfoController extends BaseUserInfoController {
    @Resource
    private IPictureDownloadLogInfoService pictureDownloadLogInfoService;

    /**
     * 查询图片下载记录列表
     */
    @PreAuthorize("@uss.hasPermi('picture:download')")
    @GetMapping("/list")
    public TableDataInfo list(UserPictureDownloadLogInfoQuery userPictureDownloadLogInfoQuery) {
        if (StringUtils.isNull(userPictureDownloadLogInfoQuery.getPageSize())) {
            userPictureDownloadLogInfoQuery.setPageSize(50);
        }
        if (userPictureDownloadLogInfoQuery.getPageSize() > 50) {
            userPictureDownloadLogInfoQuery.setPageSize(50);
        }
        userPictureDownloadLogInfoQuery.setUserId(getUserId());
        Page<PictureDownloadLogInfo> page = pictureDownloadLogInfoService.selectUserPictureDownloadLogInfoList(userPictureDownloadLogInfoQuery);
        //压缩图片
        page.getRecords().forEach(pictureDownloadLogInfo -> {
            if (StringUtils.isNotEmpty(pictureDownloadLogInfo.getThumbnailUrl())) {
                pictureDownloadLogInfo.setThumbnailUrl(OssConfig.builderPictureUrl(pictureDownloadLogInfo.getThumbnailUrl(), PICTURE_COVER_P_VALUE));
            }
        });
        List<UserPictureDownloadLogInfoVo> listVo = UserPictureDownloadLogInfoVo.objToVo(page.getRecords());
        return getDataTable(listVo, page.getTotal());
    }
}
