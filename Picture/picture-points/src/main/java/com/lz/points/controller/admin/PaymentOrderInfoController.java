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
import com.lz.points.model.domain.PaymentOrderInfo;
import com.lz.points.model.vo.paymentOrderInfo.PaymentOrderInfoVo;
import com.lz.points.model.dto.paymentOrderInfo.PaymentOrderInfoQuery;
import com.lz.points.model.dto.paymentOrderInfo.PaymentOrderInfoInsert;
import com.lz.points.model.dto.paymentOrderInfo.PaymentOrderInfoEdit;
import com.lz.points.service.IPaymentOrderInfoService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 支付订单Controller
 *
 * @author YY
 * @date 2025-03-25
 */
@RestController
@RequestMapping("/admin/points/paymentOrderInfo")
public class PaymentOrderInfoController extends BaseController
{
    @Resource
    private IPaymentOrderInfoService paymentOrderInfoService;

    /**
     * 查询支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('points:paymentOrderInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaymentOrderInfoQuery paymentOrderInfoQuery)
    {
        PaymentOrderInfo paymentOrderInfo = PaymentOrderInfoQuery.queryToObj(paymentOrderInfoQuery);
        startPage();
        List<PaymentOrderInfo> list = paymentOrderInfoService.selectPaymentOrderInfoList(paymentOrderInfo);
        List<PaymentOrderInfoVo> listVo= list.stream().map(PaymentOrderInfoVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('points:paymentOrderInfo:export')")
    @Log(title = "支付订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaymentOrderInfoQuery paymentOrderInfoQuery)
    {
        PaymentOrderInfo paymentOrderInfo = PaymentOrderInfoQuery.queryToObj(paymentOrderInfoQuery);
        List<PaymentOrderInfo> list = paymentOrderInfoService.selectPaymentOrderInfoList(paymentOrderInfo);
        ExcelUtil<PaymentOrderInfo> util = new ExcelUtil<PaymentOrderInfo>(PaymentOrderInfo.class);
        util.exportExcel(response, list, "支付订单数据");
    }

    /**
     * 获取支付订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('points:paymentOrderInfo:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") String orderId)
    {
        PaymentOrderInfo paymentOrderInfo = paymentOrderInfoService.selectPaymentOrderInfoByOrderId(orderId);
        return success(PaymentOrderInfoVo.objToVo(paymentOrderInfo));
    }

    /**
     * 新增支付订单
     */
    @PreAuthorize("@ss.hasPermi('points:paymentOrderInfo:add')")
    @Log(title = "支付订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaymentOrderInfoInsert paymentOrderInfoInsert)
    {
        PaymentOrderInfo paymentOrderInfo = PaymentOrderInfoInsert.insertToObj(paymentOrderInfoInsert);
        return toAjax(paymentOrderInfoService.insertPaymentOrderInfo(paymentOrderInfo));
    }

    /**
     * 修改支付订单
     */
    @PreAuthorize("@ss.hasPermi('points:paymentOrderInfo:edit')")
    @Log(title = "支付订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaymentOrderInfoEdit paymentOrderInfoEdit)
    {
        PaymentOrderInfo paymentOrderInfo = PaymentOrderInfoEdit.editToObj(paymentOrderInfoEdit);
        return toAjax(paymentOrderInfoService.updatePaymentOrderInfo(paymentOrderInfo));
    }

    /**
     * 删除支付订单
     */
    @PreAuthorize("@ss.hasPermi('points:paymentOrderInfo:remove')")
    @Log(title = "支付订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable String[] orderIds)
    {
        return toAjax(paymentOrderInfoService.deletePaymentOrderInfoByOrderIds(orderIds));
    }
}
