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
import com.lz.points.model.domain.PaymentMethodInfo;
import com.lz.points.model.vo.paymentMethodInfo.PaymentMethodInfoVo;
import com.lz.points.model.dto.paymentMethodInfo.PaymentMethodInfoQuery;
import com.lz.points.model.dto.paymentMethodInfo.PaymentMethodInfoInsert;
import com.lz.points.model.dto.paymentMethodInfo.PaymentMethodInfoEdit;
import com.lz.points.service.IPaymentMethodInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 支付方式Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/points/paymentMethodInfo")
public class PaymentMethodInfoController extends BaseController
{
    @Resource
    private IPaymentMethodInfoService paymentMethodInfoService;

    /**
     * 查询支付方式列表
     */
    @PreAuthorize("@ss.hasPermi('points:paymentMethodInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaymentMethodInfoQuery paymentMethodInfoQuery)
    {
        PaymentMethodInfo paymentMethodInfo = PaymentMethodInfoQuery.queryToObj(paymentMethodInfoQuery);
        startPage();
        List<PaymentMethodInfo> list = paymentMethodInfoService.selectPaymentMethodInfoList(paymentMethodInfo);
        List<PaymentMethodInfoVo> listVo= list.stream().map(PaymentMethodInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出支付方式列表
     */
    @PreAuthorize("@ss.hasPermi('points:paymentMethodInfo:export')")
    @Log(title = "支付方式", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaymentMethodInfoQuery paymentMethodInfoQuery)
    {
        PaymentMethodInfo paymentMethodInfo = PaymentMethodInfoQuery.queryToObj(paymentMethodInfoQuery);
        List<PaymentMethodInfo> list = paymentMethodInfoService.selectPaymentMethodInfoList(paymentMethodInfo);
        ExcelUtil<PaymentMethodInfo> util = new ExcelUtil<PaymentMethodInfo>(PaymentMethodInfo.class);
        util.exportExcel(response, list, "支付方式数据");
    }

    /**
     * 获取支付方式详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:paymentMethodInfo:query')")
    @GetMapping(value = "/{methodId}")
    public AjaxResult getInfo(@PathVariable("methodId") String methodId)
    {
        PaymentMethodInfo paymentMethodInfo = paymentMethodInfoService.selectPaymentMethodInfoByMethodId(methodId);
        return success(PaymentMethodInfoVo.objToVo(paymentMethodInfo));
    }

    /**
     * 新增支付方式
     */
    @PreAuthorize("@ss.hasPermi('points:paymentMethodInfo:add')")
    @Log(title = "支付方式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaymentMethodInfoInsert paymentMethodInfoInsert)
    {
        PaymentMethodInfo paymentMethodInfo = PaymentMethodInfoInsert.insertToObj(paymentMethodInfoInsert);
        return toAjax(paymentMethodInfoService.insertPaymentMethodInfo(paymentMethodInfo));
    }

    /**
     * 修改支付方式
     */
    @PreAuthorize("@ss.hasPermi('points:paymentMethodInfo:edit')")
    @Log(title = "支付方式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaymentMethodInfoEdit paymentMethodInfoEdit)
    {
        PaymentMethodInfo paymentMethodInfo = PaymentMethodInfoEdit.editToObj(paymentMethodInfoEdit);
        return toAjax(paymentMethodInfoService.updatePaymentMethodInfo(paymentMethodInfo));
    }

    /**
     * 删除支付方式
     */
    @PreAuthorize("@ss.hasPermi('points:paymentMethodInfo:remove')")
    @Log(title = "支付方式", businessType = BusinessType.DELETE)
    @DeleteMapping("/{methodIds}")
    public AjaxResult remove(@PathVariable String[] methodIds)
    {
        return toAjax(paymentMethodInfoService.deletePaymentMethodInfoByMethodIds(methodIds));
    }
}
