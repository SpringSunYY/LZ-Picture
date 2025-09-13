package com.lz.user.controller.admin;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.user.model.domain.UStatisticsInfo;
import com.lz.user.model.dto.statistics.UserInformStatisticsRequest;
import com.lz.user.model.dto.statistics.UserLoginStatisticsRequest;
import com.lz.user.model.dto.statistics.UserStatisticsRequest;
import com.lz.user.model.dto.uStatisticsInfo.UStatisticsInfoEdit;
import com.lz.user.model.dto.uStatisticsInfo.UStatisticsInfoInsert;
import com.lz.user.model.dto.uStatisticsInfo.UStatisticsInfoQuery;
import com.lz.user.model.vo.uStatisticsInfo.UStatisticsInfoVo;
import com.lz.user.service.IUStatisticsInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 统计信息Controller
 *
 * @author YY
 * @date 2025-09-09
 */
@RestController
@RequestMapping("/admin/user/uStatisticsInfo")
public class UStatisticsInfoController extends BaseController {
    @Resource
    private IUStatisticsInfoService uStatisticsInfoService;

    /**
     * 查询统计信息列表
     */
    @PreAuthorize("@ss.hasPermi('user:uStatisticsInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UStatisticsInfoQuery uStatisticsInfoQuery) {
        UStatisticsInfo uStatisticsInfo = UStatisticsInfoQuery.queryToObj(uStatisticsInfoQuery);
        startPage();
        List<UStatisticsInfo> list = uStatisticsInfoService.selectUStatisticsInfoList(uStatisticsInfo);
        List<UStatisticsInfoVo> listVo = list.stream().map(UStatisticsInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出统计信息列表
     */
    @PreAuthorize("@ss.hasPermi('user:uStatisticsInfo:export')")
    @Log(title = "统计信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UStatisticsInfoQuery uStatisticsInfoQuery) {
        UStatisticsInfo uStatisticsInfo = UStatisticsInfoQuery.queryToObj(uStatisticsInfoQuery);
        List<UStatisticsInfo> list = uStatisticsInfoService.selectUStatisticsInfoList(uStatisticsInfo);
        ExcelUtil<UStatisticsInfo> util = new ExcelUtil<UStatisticsInfo>(UStatisticsInfo.class);
        util.exportExcel(response, list, "统计信息数据");
    }

    /**
     * 获取统计信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('user:uStatisticsInfo:query')")
    @GetMapping(value = "/{statisticsId}")
    public AjaxResult getInfo(@PathVariable("statisticsId") String statisticsId) {
        UStatisticsInfo uStatisticsInfo = uStatisticsInfoService.selectUStatisticsInfoByStatisticsId(statisticsId);
        return success(UStatisticsInfoVo.objToVo(uStatisticsInfo));
    }

    /**
     * 新增统计信息
     */
    @PreAuthorize("@ss.hasPermi('user:uStatisticsInfo:add')")
    @Log(title = "统计信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UStatisticsInfoInsert uStatisticsInfoInsert) {
        UStatisticsInfo uStatisticsInfo = UStatisticsInfoInsert.insertToObj(uStatisticsInfoInsert);
        return toAjax(uStatisticsInfoService.insertUStatisticsInfo(uStatisticsInfo));
    }

    /**
     * 修改统计信息
     */
    @PreAuthorize("@ss.hasPermi('user:uStatisticsInfo:edit')")
    @Log(title = "统计信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UStatisticsInfoEdit uStatisticsInfoEdit) {
        UStatisticsInfo uStatisticsInfo = UStatisticsInfoEdit.editToObj(uStatisticsInfoEdit);
        return toAjax(uStatisticsInfoService.updateUStatisticsInfo(uStatisticsInfo));
    }

    /**
     * 删除统计信息
     */
    @PreAuthorize("@ss.hasPermi('user:uStatisticsInfo:remove')")
    @Log(title = "统计信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{statisticsIds}")
    public AjaxResult remove(@PathVariable String[] statisticsIds) {
        return toAjax(uStatisticsInfoService.deleteUStatisticsInfoByStatisticsIds(statisticsIds));
    }

    /**
     * 用户注册统计
     *
     * @param userStatisticsRequest 请求参数
     */
    @PreAuthorize("@ss.hasPermi('user:statistics')")
    @GetMapping("/user/register")
    public AjaxResult userRegisterStatistics(@Validated UserStatisticsRequest userStatisticsRequest) {
        return success(uStatisticsInfoService.userRegisterStatistics(userStatisticsRequest));
    }

    /**
     * 用户登录统计
     *
     * @param request 请求参数
     */
    @PreAuthorize("@ss.hasPermi('user:statistics')")
    @GetMapping("/user/login")
    public AjaxResult userRegisterStatistics(@Validated UserLoginStatisticsRequest request) {
        return success(uStatisticsInfoService.userLoginStatistics(request));
    }

    /**
     * 用户消息发送统计
     * @return
     */
    @PreAuthorize("@ss.hasPermi('user:statistics')")
    @GetMapping("/user/informType")
    public AjaxResult userInformTypeStatistics(@Validated UserStatisticsRequest request) {
        return success(uStatisticsInfoService.userInformTypeStatistics(request));
    }

    @PreAuthorize("@ss.hasPermi('user:statistics')")
    @GetMapping("/user/sex")
    public AjaxResult userSexStatistics() {
        return success(uStatisticsInfoService.userSexStatistics());
    }

    @PreAuthorize("@ss.hasPermi('user:statistics')")
    @GetMapping("/user/age")
    public AjaxResult userAgeStatistics() {
        return success(uStatisticsInfoService.userAgeStatistics());
    }

    /**
     * 用户消息发送统计
     * @return
     */
    @PreAuthorize("@ss.hasPermi('user:statistics')")
    @GetMapping("/user/inform")
    public AjaxResult userInformStatistics(@Validated UserInformStatisticsRequest request) {
        return success(uStatisticsInfoService.userInformStatistics(request));
    }

    /**
     * 用户总数
     */
    @PreAuthorize("@ss.hasPermi('user:statistics')")
    @GetMapping("/user/total")
    public AjaxResult userTotalStatistics() {
        return success(uStatisticsInfoService.userTotalStatistics());
    }
}
