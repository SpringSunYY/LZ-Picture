package com.lz.picture.controller.user;

import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.dto.pictureRecommend.PictureRecommendRequest;
import com.lz.picture.model.vo.pictureInfo.UserRecommendPictureInfoVo;
import com.lz.picture.service.IPictureRecommendService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 图片推荐
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-06-09  22:02
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user/picture/pictureInfo")
public class UserPictureInfoRecommendController extends BaseUserInfoController {
    @Resource
    private IPictureRecommendService pictureRecommendService;

    @RequestMapping("/recommend")
    public TableDataInfo getPictureInfoRecommend(@Validated PictureRecommendRequest request) {
        if (StringUtils.isNull(request.getPageSize())) {
            request.setPageSize(50);
        }
        if (request.getPageSize() > 50) {
            request.setPageSize(50);
        }
        try {
            request.setUserId(getUserId());
        } catch (Exception e) {
            request.setUserId(null);
        }

        List<UserRecommendPictureInfoVo> pictureInfoRecommend = pictureRecommendService.getPictureInfoRecommend(request);
        return getDataTable(pictureInfoRecommend, 0);
    }
}
