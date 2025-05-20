package com.lz.points.controller.user;

import com.alipay.api.AlipayApiException;
import com.lz.common.core.domain.AjaxResult;
import com.lz.points.model.dto.pay.PayRequest;
import com.lz.points.service.IPayService;
import com.lz.userauth.controller.BaseUserInfoController;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付
 *
 * @Project: Picture
 * @Author: YY
 * @CreateTime: 2025-05-15  09:38
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user/points/pay")
public class UserPayController extends BaseUserInfoController {
    @Resource
    private IPayService payService;

    /**
     * 支付宝支付
     */
    @PreAuthorize("@uss.hasPermi('points:payment')")
    @PostMapping("/alipay/web")
    public AjaxResult alipayWeb(@RequestBody PayRequest payRequest) {
        payRequest.setUserId(getUserId());
        return success(payService.alipayWeb(payRequest));
    }

    /**
     * 支付宝回调接口
     */
    @GetMapping("/alipay/callback")
    public void alipayCallback(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        System.out.println(parameterMap);

        HashMap<String, String> map = new HashMap<>();
        for (String s : parameterMap.keySet()) {
//            System.out.println(s + "--" + parameterMap.get(s)[0]);
            map.put(s, parameterMap.get(s)[0]);
        }
        String redirectUrl = payService.alipayCallback(map);
        response.sendRedirect(redirectUrl);
    }

    /**
     * 获取订单信息,数据库存储
     */
    @PreAuthorize("@uss.hasPermi('points:payment')")
    @GetMapping("/alipay/web/order/{outTradeNo}")
    public AjaxResult alipayWebOrder(@PathVariable("outTradeNo") String outTradeNo) {
        return success(payService.getAlipayWebOrder(outTradeNo, getUserId()));
    }

    /**
     *
     * @param outTradeNo
     * @return
     */
    @GetMapping("/alipay/web/{outTradeNo}")
    public AjaxResult alipayWeb(@PathVariable("outTradeNo") String outTradeNo) {
        return success(payService.getAlipayWeb(outTradeNo, getUserId()));
    }
}
