package com.lz.points.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.utils.StringUtils;
import com.lz.points.model.domain.PointsRechargeInfo;
import com.lz.points.model.dto.pointsRechargeInfo.UserPointsRechargeInfoQuery;
import com.lz.points.model.vo.pointsRechargeInfo.UserPointsRechargeInfoVo;
import com.lz.points.model.vo.pointsRechargePackageInfo.UserPointsRechargePackageInfoVo;
import com.lz.points.service.IPointsRechargeInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 积分充值记录Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/user/points/pointsRechargeInfo")
public class UserPointsRechargeInfoController extends BaseUserInfoController {
    @Resource
    private IPointsRechargeInfoService pointsRechargeInfoService;

    /**
     * 查询积分充值记录列表
     */
    @PreAuthorize("@uss.hasPermi('points')")
    @GetMapping("/list")
    public TableDataInfo list(UserPointsRechargeInfoQuery userPointsRechargeInfoQuery) {
        if (StringUtils.isNull(userPointsRechargeInfoQuery.getPageSize())) {
            userPointsRechargeInfoQuery.setPageSize(50);
        }
        if (userPointsRechargeInfoQuery.getPageSize() > 50) {
            userPointsRechargeInfoQuery.setPageSize(50);
        }
        userPointsRechargeInfoQuery.setUserId(getUserId());
        Page<PointsRechargeInfo> page = pointsRechargeInfoService.selectMyPointsRechargeInfoList(userPointsRechargeInfoQuery);
        List<UserPointsRechargeInfoVo> userPointsRechargeInfoVos = UserPointsRechargeInfoVo.objToVo(page.getRecords());
        return getDataTable(userPointsRechargeInfoVos, page.getTotal());
    }
}
