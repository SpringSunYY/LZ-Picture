package com.lz.picture.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.config.OssConfig;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.domain.UserViewLogInfo;
import com.lz.picture.model.dto.userViewLogInfo.MyUserViewLogInfoQuery;
import com.lz.picture.model.vo.userViewLogInfo.MyUserViewLogInfoVo;
import com.lz.picture.service.IUserViewLogInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.lz.config.utils.ConfigInfoUtils.PICTURE_COVER_P_VALUE;

/**
 * 用户浏览记录Controller
 *
 * @author YY
 * @date 2025-04-12
 */
@RestController
@RequestMapping("/user/picture/userViewLogInfo")
public class UserUserViewLogInfoController extends BaseUserInfoController {
    @Resource
    private IUserViewLogInfoService userViewLogInfoService;


    /**
     * 查询用户浏览记录列表
     */
    @PreAuthorize("@uss.hasPermi('picture:list')")
    @GetMapping("/list")
    public TableDataInfo list(MyUserViewLogInfoQuery myUserViewLogInfoQuery) {
        if (StringUtils.isNull(myUserViewLogInfoQuery.getPageSize())) {
            myUserViewLogInfoQuery.setPageSize(50);
        }
        if (myUserViewLogInfoQuery.getPageSize() > 50) {
            myUserViewLogInfoQuery.setPageSize(50);
        }
        myUserViewLogInfoQuery.setUserId(getUserId());
        Page<UserViewLogInfo> page = userViewLogInfoService.selectMyUserViewLogInfoList(myUserViewLogInfoQuery);
        //压缩图片
        for (UserViewLogInfo userViewLogInfo : page.getRecords()) {
            if (StringUtils.isNotEmpty(userViewLogInfo.getTargetCover())) {
                userViewLogInfo.setTargetCover(OssConfig.builderUrl(userViewLogInfo.getTargetCover()) + "?x-oss-process=image/resize,p_" + PICTURE_COVER_P_VALUE);
            }
        }
        List<MyUserViewLogInfoVo> myUserViewLogInfoVos = MyUserViewLogInfoVo.objToVo(page.getRecords());
        return getDataTable(myUserViewLogInfoVos, page.getTotal());
    }

}
