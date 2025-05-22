package com.lz.points.controller.user;

import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.points.model.domain.PointsRechargePackageInfo;
import com.lz.points.model.dto.pointsRechargePackageInfo.UserPointsRechargePackageInfoQuery;
import com.lz.points.model.vo.pointsRechargePackageInfo.UserPointsRechargePackageInfoVo;
import com.lz.points.service.IPointsRechargePackageInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 充值积分套餐Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/user/points/pointsRechargePackageInfo")
public class UserPointsRechargePackageInfoController extends BaseUserInfoController {
    @Resource
    private IPointsRechargePackageInfoService pointsRechargePackageInfoService;

    /**
     * 查询充值积分套餐列表
     */
    @PreAuthorize("@uss.hasPermi('points')")
    @GetMapping("/list")
    public TableDataInfo list(UserPointsRechargePackageInfoQuery pointsRechargePackageInfoQuery) {
        PointsRechargePackageInfo pointsRechargePackageInfo = UserPointsRechargePackageInfoQuery.queryToObj(pointsRechargePackageInfoQuery);
        List<PointsRechargePackageInfo> list = pointsRechargePackageInfoService.userSelectPointsRechargePackageInfoList(pointsRechargePackageInfo);
        List<UserPointsRechargePackageInfoVo> userPointsRechargePackageInfoVos = UserPointsRechargePackageInfoVo.objToVo(list);
        return getDataTable(userPointsRechargePackageInfoVos, userPointsRechargePackageInfoVos.size());
    }

    /**
     * 查询充值积分套餐
     */
    @PreAuthorize("@uss.hasPermi('points')")
    @GetMapping("/{packageId}")
    public AjaxResult getPointsRechargePackageInfo(@PathVariable("packageId") String packageId) {
        return AjaxResult.success(pointsRechargePackageInfoService.selectPointsRechargePackageInfoByPackageId(packageId));
    }

}
