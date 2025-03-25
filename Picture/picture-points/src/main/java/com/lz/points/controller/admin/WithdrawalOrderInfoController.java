package com.lz.points.controller.admin;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.enums.BusinessType;
import com.lz.points.model.domain.WithdrawalOrderInfo;
import com.lz.points.model.vo.withdrawalOrderInfo.WithdrawalOrderInfoVo;
import com.lz.points.model.dto.withdrawalOrderInfo.WithdrawalOrderInfoQuery;
import com.lz.points.model.dto.withdrawalOrderInfo.WithdrawalOrderInfoInsert;
import com.lz.points.model.dto.withdrawalOrderInfo.WithdrawalOrderInfoEdit;
import com.lz.points.service.IWithdrawalOrderInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 用户提现记录Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/points/withdrawalOrderInfo")
public class WithdrawalOrderInfoController extends BaseController
{
    @Resource
    private IWithdrawalOrderInfoService withdrawalOrderInfoService;

    /**
     * 查询用户提现记录列表
     */
    @PreAuthorize("@ss.hasPermi('points:withdrawalOrderInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(WithdrawalOrderInfoQuery withdrawalOrderInfoQuery)
    {
        WithdrawalOrderInfo withdrawalOrderInfo = WithdrawalOrderInfoQuery.queryToObj(withdrawalOrderInfoQuery);
        startPage();
        List<WithdrawalOrderInfo> list = withdrawalOrderInfoService.selectWithdrawalOrderInfoList(withdrawalOrderInfo);
        List<WithdrawalOrderInfoVo> listVo= list.stream().map(WithdrawalOrderInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出用户提现记录列表
     */
    @PreAuthorize("@ss.hasPermi('points:withdrawalOrderInfo:export')")
    @Log(title = "用户提现记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WithdrawalOrderInfoQuery withdrawalOrderInfoQuery)
    {
        WithdrawalOrderInfo withdrawalOrderInfo = WithdrawalOrderInfoQuery.queryToObj(withdrawalOrderInfoQuery);
        List<WithdrawalOrderInfo> list = withdrawalOrderInfoService.selectWithdrawalOrderInfoList(withdrawalOrderInfo);
        ExcelUtil<WithdrawalOrderInfo> util = new ExcelUtil<WithdrawalOrderInfo>(WithdrawalOrderInfo.class);
        util.exportExcel(response, list, "用户提现记录数据");
    }

    /**
     * 获取用户提现记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:withdrawalOrderInfo:query')")
    @GetMapping(value = "/{withdrawalId}")
    public AjaxResult getInfo(@PathVariable("withdrawalId") String withdrawalId)
    {
        WithdrawalOrderInfo withdrawalOrderInfo = withdrawalOrderInfoService.selectWithdrawalOrderInfoByWithdrawalId(withdrawalId);
        return success(WithdrawalOrderInfoVo.objToVo(withdrawalOrderInfo));
    }

    /**
     * 新增用户提现记录
     */
    @PreAuthorize("@ss.hasPermi('points:withdrawalOrderInfo:add')")
    @Log(title = "用户提现记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WithdrawalOrderInfoInsert withdrawalOrderInfoInsert)
    {
        WithdrawalOrderInfo withdrawalOrderInfo = WithdrawalOrderInfoInsert.insertToObj(withdrawalOrderInfoInsert);
        return toAjax(withdrawalOrderInfoService.insertWithdrawalOrderInfo(withdrawalOrderInfo));
    }

    /**
     * 修改用户提现记录
     */
    @PreAuthorize("@ss.hasPermi('points:withdrawalOrderInfo:edit')")
    @Log(title = "用户提现记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WithdrawalOrderInfoEdit withdrawalOrderInfoEdit)
    {
        WithdrawalOrderInfo withdrawalOrderInfo = WithdrawalOrderInfoEdit.editToObj(withdrawalOrderInfoEdit);
        return toAjax(withdrawalOrderInfoService.updateWithdrawalOrderInfo(withdrawalOrderInfo));
    }

    /**
     * 删除用户提现记录
     */
    @PreAuthorize("@ss.hasPermi('points:withdrawalOrderInfo:remove')")
    @Log(title = "用户提现记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{withdrawalIds}")
    public AjaxResult remove(@PathVariable String[] withdrawalIds)
    {
        return toAjax(withdrawalOrderInfoService.deleteWithdrawalOrderInfoByWithdrawalIds(withdrawalIds));
    }
}
