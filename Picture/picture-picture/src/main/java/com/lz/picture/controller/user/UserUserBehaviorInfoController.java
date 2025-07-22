package com.lz.picture.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.config.OssConfig;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.picture.model.domain.UserBehaviorInfo;
import com.lz.picture.model.dto.userBehaviorInfo.MyUserBehaviorInfoQuery;
import com.lz.picture.model.dto.userBehaviorInfo.UserBehaviorInfoAdd;
import com.lz.picture.model.vo.userBehaviorInfo.MyUserBehaviorInfoVo;
import com.lz.picture.service.IUserBehaviorInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lz.config.utils.ConfigInfoUtils.PICTURE_COVER_P_VALUE;

/**
 * 用户行为Controller
 *
 * @author YY
 * @date 2025-04-12
 */
@RestController
@RequestMapping("/user/picture/userBehaviorInfo")
public class UserUserBehaviorInfoController extends BaseUserInfoController {
    @Resource
    private IUserBehaviorInfoService userBehaviorInfoService;

    /**
     * 新增用户行为
     */
    @PreAuthorize("@uss.hasPermi('picture:userBehaviorInfo:add')")
    @PostMapping
    public AjaxResult add(@RequestBody @Validated UserBehaviorInfoAdd userBehaviorInfoInsert) {
        UserBehaviorInfo userBehaviorInfo = UserBehaviorInfoAdd.addToObj(userBehaviorInfoInsert);
        userBehaviorInfo.setUserId(getUserId());
        return success(userBehaviorInfoService.userInsertUserBehaviorInfo(userBehaviorInfo));
    }

    /**
     * 查看用户行为
     */
    @PreAuthorize("@uss.hasPermi('picture:userBehaviorInfo')")
    @GetMapping("/list")
    public TableDataInfo list(MyUserBehaviorInfoQuery userBehaviorInfoQuery) {
        if (StringUtils.isNull(userBehaviorInfoQuery.getPageSize())) {
            userBehaviorInfoQuery.setPageSize(50);
        }
        if (userBehaviorInfoQuery.getPageSize() > 50) {
            userBehaviorInfoQuery.setPageSize(50);
        }
        userBehaviorInfoQuery.setUserId(getUserId());
        Page<UserBehaviorInfo> page = userBehaviorInfoService.selectMyUserBehaviorInfoList(userBehaviorInfoQuery);
        //压缩图片
        for (UserBehaviorInfo userBehaviorInfo : page.getRecords()) {
            if (StringUtils.isNotEmpty(userBehaviorInfo.getTargetCover())) {
                userBehaviorInfo.setTargetCover(OssConfig.builderUrl(userBehaviorInfo.getTargetCover()) + "?x-oss-process=image/resize,p_" + PICTURE_COVER_P_VALUE);
            }
        }
        List<MyUserBehaviorInfoVo> myUserBehaviorInfoVos = MyUserBehaviorInfoVo.objToVo(page.getRecords());
        return getDataTable(myUserBehaviorInfoVos, page.getTotal());
    }
}
