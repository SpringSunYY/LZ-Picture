package com.lz.points.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.points.model.domain.PointsRechargeInfo;
import com.lz.points.model.domain.PointsUsageLogInfo;
import com.lz.points.model.dto.pointsUsageLogInfo.PointsUsageLogInfoEdit;
import com.lz.points.model.dto.pointsUsageLogInfo.PointsUsageLogInfoInsert;
import com.lz.points.model.dto.pointsUsageLogInfo.PointsUsageLogInfoQuery;
import com.lz.points.model.dto.pointsUsageLogInfo.UserPointsUsageLogInfoQuery;
import com.lz.points.model.vo.pointsRechargeInfo.UserPointsRechargeInfoVo;
import com.lz.points.model.vo.pointsUsageLogInfo.PointsUsageLogInfoVo;
import com.lz.points.model.vo.pointsUsageLogInfo.UserPointsUsageLogInfoVo;
import com.lz.points.service.IPointsUsageLogInfoService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户积分使用记录Controller
 *
 * @author YY
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/user/points/pointsUsageLogInfo")
public class UserPointsUsageLogInfoController extends BaseUserInfoController
{
    @Resource
    private IPointsUsageLogInfoService pointsUsageLogInfoService;

    /**
     * 查询积分使用记录列表
     */
    @PreAuthorize("@uss.hasPermi('points')")
    @GetMapping("/list")
    public TableDataInfo list(UserPointsUsageLogInfoQuery userPointsUsageLogInfoQuery)
    {
        if (StringUtils.isNull(userPointsUsageLogInfoQuery.getPageSize())) {
            userPointsUsageLogInfoQuery.setPageSize(50);
        }
        if (userPointsUsageLogInfoQuery.getPageSize() > 50) {
            userPointsUsageLogInfoQuery.setPageSize(50);
        }
        userPointsUsageLogInfoQuery.setUserId(getUserId());
        Page<PointsUsageLogInfo> page = pointsUsageLogInfoService.selectMyPointsUsageLogInfoList(userPointsUsageLogInfoQuery);
        List<UserPointsUsageLogInfoVo> userPointsUsageLogInfoVos = UserPointsUsageLogInfoVo.objToVo(page.getRecords());
        return getDataTable(userPointsUsageLogInfoVos, page.getTotal());
    }

}
